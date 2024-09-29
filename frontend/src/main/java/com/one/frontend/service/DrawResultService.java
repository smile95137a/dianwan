package com.one.frontend.service;

import com.one.frontend.dto.OrderDetailDto;
import com.one.frontend.eenum.OrderStatus;
import com.one.frontend.eenum.PrizeCategory;
import com.one.frontend.eenum.ProductStatus;
import com.one.frontend.eenum.ProductType;
import com.one.frontend.model.*;
import com.one.frontend.repository.*;
import com.one.frontend.response.DrawResponse;
import com.one.frontend.response.ProductDetailRes;
import com.one.frontend.response.ProductRes;
import com.one.frontend.response.UserRes;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DrawResultService {

	private final DrawRepository drawRepository;
	private final UserRepository userRepository;
	private final OrderRepository orderRepository;
	private final OrderDetailRepository orderDetailRepository;
	private final ProductRepository productRepository;
	private final ProductDetailRepository productDetailRepository;
	private final PrizeNumberMapper prizeNumberMapper;
	private final OrderService orderService;
	private final PrizeCartItemRepository prizeCartItemRepository;
	private final PrizeCartRepository prizeCartRepository;


	@Autowired
	private SimpMessagingTemplate messagingTemplate;


	// 本地锁，用于锁定抽奖资源
	private final ConcurrentHashMap<Long, Lock> productLockMap = new ConcurrentHashMap<>();
	// 存储用户的锁
	private final Map<Long, Lock> userLockMap = new ConcurrentHashMap<>();
	// 存储产品的上次抽奖时间和抽奖用户
	private final Map<Long, DrawProtection> productDrawProtectionMap = new ConcurrentHashMap<>();

	private static class DrawProtection {
		LocalDateTime lastDrawTime;
		Long userId;

		DrawProtection(LocalDateTime lastDrawTime, Long userId) {
			this.lastDrawTime = lastDrawTime;
			this.userId = userId;
		}
	}

	private Lock getLockForUser(Long userId) {
		return userLockMap.computeIfAbsent(userId, k -> new ReentrantLock());
	}

	// 获取抽奖保护时间，根据抽奖次数计算保护期时间
	private long getDrawProtectionTime(int drawCount) {
		long protectionTime = 300 + (30 * (drawCount - 1)); // 初始保护时间300秒，每多抽一次加30秒
		return Math.min(protectionTime, 600); // 最大保护时间为600秒
	}

	// 抽奖操作，处理锁机制和保护期
	public LocalDateTime handleDrawForLock(Long userId, Long productId, List<String> prizeNumbers) throws Exception {
		Lock lock = getLockForUser(userId); // 获取用户锁
		if (lock.tryLock()) { // 尝试获取锁
			try {
				LocalDateTime now = LocalDateTime.now(); // 当前时间
				DrawProtection protection = productDrawProtectionMap.get(productId); // 获取该产品的保护信息
				long protectionTime = getDrawProtectionTime(prizeNumbers.size()); // 根据抽奖次数计算保护时间

				// 如果有保护信息，计算是否在保护期内
				if (protection != null) {
					long secondsSinceLastDraw = Duration.between(protection.lastDrawTime, now).getSeconds();
					System.out.println("Protection time: " + protectionTime + " seconds");
					System.out.println("Seconds since last draw: " + secondsSinceLastDraw + " seconds");

					// 如果在保护期内，且不是同一个用户，则抛出异常
					if (secondsSinceLastDraw < protectionTime && !Objects.equals(userId, protection.userId)) {
						throw new Exception("抽獎保護期內，其他用戶不能抽獎。剩餘時間：" + (protectionTime - secondsSinceLastDraw) + "秒");
					}
				}

				// 更新抽奖保护信息，设置新的保护时间
				productDrawProtectionMap.put(productId, new DrawProtection(now, userId));
				System.out.println("Updated DrawProtection for productId: " + productId + ", userId: " + userId);

				// 返回保护期的结束时间
				LocalDateTime endTimes = now.plusSeconds(protectionTime);
				return endTimes;
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception("抽獎發生錯誤: " + e.getMessage());
			} finally {
				lock.unlock(); // 释放锁
			}
		} else {
			throw new Exception("目前有其他用戶正在抽獎，請稍後。");
		}
	}






	public List<DrawResult> handleDraw(Long userId, Long productId) throws Exception {
		Random random = new Random();
		List<Long> number = prizeNumberMapper.getNumbers(productId);
		int randomIndex = random.nextInt(number.size());
		String prizeNumber = number.get(randomIndex).toString();
		List<String> prizeNumbers = new ArrayList<>();
		prizeNumbers.add(prizeNumber);
        return handleDraw2(userId , productId , prizeNumbers , "1");
	}

	public DrawResponse getAllPrizes(Long productId, Long userId) {
		// 1. 获取产品详细信息
		List<ProductDetail> productDetails = productDetailRepository.getAllProductDetailsByProductId(productId);
		List<PrizeNumber> allPrizeNumbers = new ArrayList<>();

		// 2. 获取奖品编号
		for (ProductDetail productDetail : productDetails) {
			List<PrizeNumber> prizeNumbers = prizeNumberMapper
					.getAllPrizeNumbersByProductDetailId(Long.valueOf(productDetail.getProductDetailId()));
			for (PrizeNumber prize : prizeNumbers) {
				if (!prize.getIsDrawn()) {
					prize.setLevel(null);  // 只显示未抽中的奖品
				}
			}
			allPrizeNumbers.addAll(prizeNumbers);
		}

		// 3. 对 allPrizeNumbers 按照 number 排序
		allPrizeNumbers.sort(Comparator.comparing(PrizeNumber::getNumber)); // 假设 PrizeNumber 有 getNumber() 方法

		// 4. 获取当前时间
		LocalDateTime now = LocalDateTime.now();

		// 5. 获取当前产品的抽奖保护状态
		DrawProtection protection = productDrawProtectionMap.get(productId);
		LocalDateTime endTimes = null; // 初始化 endTimes 为 null

		// 6. 如果有保护状态且与当前用户无关，计算保护时间
		if (protection != null) {
			long secondsSinceLastDraw = Duration.between(protection.lastDrawTime, now).getSeconds();
			int drawCount = (int) allPrizeNumbers.stream().filter(PrizeNumber::getIsDrawn).count();
			long protectionTime = getDrawProtectionTime(drawCount);

			// 判断是否在保护期内
			if (secondsSinceLastDraw < protectionTime) {
				endTimes = protection.lastDrawTime.plusSeconds(protectionTime);
			}
		}

		allPrizeNumbers.sort(Comparator.comparing(prize -> {
			try {
				return Long.parseLong(prize.getNumber()); // 假设 number 是可以转换为 Long 类型的字符串
			} catch (NumberFormatException e) {
				// 如果 number 无法转换为 Long，则返回一个非常大的值，避免影响排序
				return Long.MAX_VALUE;
			}
		}));

		// 7. 返回结果，包括排序后的奖品列表和保护结束时间
		return new DrawResponse(allPrizeNumbers, endTimes);
	}



	public List<DrawResult> handleDraw2(Long userId, Long productId, List<String> prizeNumbers , String payMethod) throws Exception {
		try {
			// 验证用戶
			Long prizeCart = prizeCartRepository.getCartIdByUserId(userId);
			if (prizeCart == null) {
				throw new Exception("沒有賞品盒不能抽獎，請聯繫客服人員");
			}

			// 获取产品详细列表
			List<ProductDetailRes> productDetails = productDetailRepository.getProductDetailByProductId(productId);
			if (productDetails.isEmpty()) {
				throw new Exception("該產品沒有可用的獎品");
			}

			// 获取选中的奖品编号
			List<PrizeNumber> selectedPrizeNumbers = prizeNumberMapper.getPrizeNumbersByProductIdAndNumbers(productId, prizeNumbers);
			if (selectedPrizeNumbers.size() < prizeNumbers.size()) {
				throw new Exception("部分指定的獎品編號不存在");
			}

			// 为每个选择的编号随机抽奖（按概率）
			List<PrizeNumber> drawnPrizeNumbers = drawPrizesForNumbersWithStock(selectedPrizeNumbers, productDetails);
			// 获取商品价格
			ProductRes product = productRepository.getProductById(productId);


			// 扣除会员内储值金或红利
			PrizeCategory category = product.getPrizeCategory();
			BigDecimal balance = new BigDecimal(userRepository.getBalance(userId));
			if (category == PrizeCategory.BONUS) {
				BigDecimal amount = product.getBonusPrice();
				// 计算抽奖总金额
				BigDecimal totalAmount = amount.multiply(BigDecimal.valueOf(prizeNumbers.size()));
				BigDecimal bonusPoints = new BigDecimal(userRepository.getBonusPoints(userId));
				if (bonusPoints.compareTo(totalAmount) >= 0) {
					BigDecimal newBonusPoints = bonusPoints.subtract(totalAmount);
					userRepository.deductUserBonusPoints(userId, newBonusPoints);
				} else {
					throw new Exception("红利不足，請加值");
				}
			} else if("1".equals(payMethod)){ //1是金幣抽獎
				BigDecimal amount = product.getPrice();
				// 计算抽奖总金额
				BigDecimal totalAmount = amount.multiply(BigDecimal.valueOf(prizeNumbers.size()));
				if (balance.compareTo(totalAmount) >= 0) {
					BigDecimal newBalance = balance.subtract(totalAmount);
					userRepository.deductUserBalance(userId, newBalance);
				} else {
					throw new Exception("餘額不足，請加值");
				}
			} else if("2".equals(payMethod)){
				BigDecimal amount = product.getSliverPrice();
				BigDecimal totalAmount = amount.multiply(BigDecimal.valueOf(prizeNumbers.size()));
				BigDecimal sliver = new BigDecimal(String.valueOf(userRepository.getSliver(userId)));
				if (sliver.compareTo(totalAmount) >= 0) {
					BigDecimal newBalance = balance.subtract(totalAmount);
					userRepository.deductUserSliver(userId, newBalance);
				} else {
					throw new Exception("銀幣不足");
				}
			}
			LocalDateTime endTimes = null;
			if(product.getProductType() == ProductType.PRIZE){
				endTimes = handleDrawForLock(userId ,productId , prizeNumbers);
			}
			List<DrawResult> drawResults = new ArrayList<>();
			int remainingDrawCount = prizeNumbers.size();
			UserRes user = userRepository.getUserById(userId);
			// 处理每个抽中的奖品
			for (PrizeNumber selectedPrizeNumber : drawnPrizeNumbers) {
				prizeNumberMapper.markPrizeNumberAsDrawn(selectedPrizeNumber.getPrizeNumberId(), selectedPrizeNumber.getProductId(), selectedPrizeNumber.getProductDetailId());

				// 更新奖品数量
				ProductDetailRes selectedPrizeDetail = productDetailRepository
						.getProductDetailById(selectedPrizeNumber.getProductDetailId());
				// 记录抽奖结果
				DrawResult drawResult = new DrawResult();
				drawResult.setUserId(userId);
				drawResult.setProductId(productId);
				drawResult.setProductDetailId(selectedPrizeDetail.getProductDetailId().longValue());
				drawResult.setDrawTime(LocalDateTime.now());
				if (category == PrizeCategory.BONUS) {
					BigDecimal amount = product.getBonusPrice();
					drawResult.setAmount(amount);
				}else if("1".equals(payMethod)){
					BigDecimal amount = product.getPrice();
					drawResult.setAmount(amount);
				}else if("2".equals(payMethod)){
					BigDecimal amount = product.getSliverPrice();
					drawResult.setAmount(amount);
				}

				drawResult.setDrawCount(1);
				drawResult.setRemainingDrawCount(remainingDrawCount);
				drawResult.setPrizeNumber(selectedPrizeNumber.getNumber());
				drawResult.setStatus("ACTIVE");
				drawResult.setTotalDrawCount((long) prizeNumbers.size());
				drawResult.setCreateDate(LocalDateTime.now());
				drawResult.setUpdateDate(LocalDateTime.now());
				drawResult.setImageUrls(selectedPrizeDetail.getImageUrls().get(0));
				drawResult.setProductName(selectedPrizeDetail.getProductName());
				drawResults.add(drawResult);


				//告知抽獎結果跑馬燈
				GachaMessage message = new GachaMessage();
				message.setNickName(user.getNickname());
				message.setName(selectedPrizeDetail.getProductName());
				message.setProductDetail(selectedPrizeDetail);
				message.setCreatedDate(LocalDateTime.now());

				messagingTemplate.convertAndSend("/topic/lottery", message);
				remainingDrawCount--;



			}

			// 批量插入抽奖结果
			drawRepository.insertBatch(drawResults);

			//商品加入賞品盒
			Long prizeCartId = prizeCartRepository.getCartIdByUserId(userId);
			List<PrizeCartItem> cartItemList = new ArrayList<>();
			if(!(prizeCartId == null)){
				for (PrizeNumber selectedPrizeNumber : selectedPrizeNumbers) {
					PrizeCartItem prizeCartItem = new PrizeCartItem();
					ProductDetailRes selectedPrizeDetail = productDetailRepository
							.getProductDetailById(selectedPrizeNumber.getProductDetailId());
					ProductRes pro = productRepository.getProductById(productId);
					ProductType productType = pro.getProductType();
					if (productType.equals(ProductType.GACHA) || productType.equals(ProductType.BLIND_BOX)) {
						prizeCartItem.setSliverPrice(BigDecimal.ZERO);
					} else {
						prizeCartItem.setSliverPrice(selectedPrizeDetail.getSliverPrice());
					}
					prizeCartItem.setCartId(prizeCartId);
					prizeCartItem.setSize(selectedPrizeDetail.getSize());

					prizeCartItem.setProductDetailId(selectedPrizeDetail.getProductDetailId());
					prizeCartItem.setIsSelected(true);
					cartItemList.add(prizeCartItem);
				}
			}else{
				throw new Exception("賞品盒不存在");
			}


			prizeCartItemRepository.insertBatch(cartItemList);


			// 如果抽完数量变1 1代表剩SP赏的时候要送SP赏
			List<PrizeCartItem> cartItemArr = new ArrayList<>();
			Long prizeCartId2 = prizeCartRepository.getCartIdByUserId(userId);
			List<ProductDetailRes> productDetail = productDetailRepository.getProductDetailByProductId(productId);
			ProductDetailRes spPrize = productDetailRepository.getProductDetailSpPrizeByProductId(productId);
			int totalQue = 0;

// 计算总数量
			for (ProductDetailRes res : productDetail) {
				totalQue += res.getQuantity();
			}

// 检查总数量是否为1
			if (totalQue == 1 && spPrize != null) {
				PrizeCartItem cartItem = new PrizeCartItem();
				cartItem.setCartId(prizeCartId2);
				cartItem.setSize(spPrize.getSize());
				cartItem.setQuantity(1);
				cartItem.setSliverPrice(spPrize.getSliverPrice());
				cartItem.setProductDetailId(spPrize.getProductDetailId());
				cartItem.setIsSelected(true);
				cartItemArr.add(cartItem);
			}

// 将新创建的 cartItem 添加到 cartItemList 中
			if (!cartItemArr.isEmpty()) {
				cartItemList.addAll(cartItemArr); // 将 cartItemArr 添加到 cartItemList
			}

			if (!cartItemList.isEmpty()) {
				prizeCartItemRepository.insertBatch(cartItemList);
			} else {
				System.out.println("cartItemList is empty, skipping insertBatch.");
			}

// 扣除SP赏的数量
			if(spPrize != null){
				if (spPrize.getQuantity() > 0) { // 检查数量是否大于0
					spPrize.setQuantity(spPrize.getQuantity() - 1);
					productDetailRepository.updateProductDetailQuantity(spPrize);
					productRepository.updateStatus(productId);
				} else {
					System.out.println("SP prize quantity is already 0, skipping update.");
				}
			}



			// 处理用户抽奖次数和红利

			Long drawCount = user.getDrawCount();
			if (drawCount < 3L) {
				userRepository.addDrawCount(userId);
			} else {
				userRepository.updateBonus(userId);
			}

			ProductRes res = productRepository.getProductById(productId);
			if(res.getStockQuantity() <= 0){
				res.setStatus(ProductStatus.UNAVAILABLE);
				productRepository.updateProductQuantity(res);
			}


			return drawResults;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("抽奖过程中出现错误: " + e.getMessage());
		}
	}

	private List<PrizeNumber> drawPrizesForNumbersWithStock(List<PrizeNumber> selectedPrizeNumbers,
															List<ProductDetailRes> productDetails) {
		List<PrizeNumber> drawnPrizeNumbers = new ArrayList<>();

		for (PrizeNumber selectedPrizeNumber : selectedPrizeNumbers) {
			PrizeNumber drawnPrizeNumber = drawPrizeForNumber(selectedPrizeNumber, productDetails);
			if (drawnPrizeNumber != null) {
				drawnPrizeNumbers.add(drawnPrizeNumber);
			} else {
				// 处理所有奖品都抽完的情况，可以抛出异常或返回空列表
				throw new RuntimeException("所有奖品都已抽完");
			}
		}

		return drawnPrizeNumbers;
	}

	public PrizeNumber drawPrizeForNumber(PrizeNumber selectedPrizeNumber, List<ProductDetailRes> productDetails) {
		Random random = new Random();
		List<ProductDetail> toUpdateProductDetails = new ArrayList<>();
		List<PrizeNumber> toUpdatePrizeNumbers = new ArrayList<>();

		while (true) {
			List<ProductDetailRes> availableProductDetails = productDetails.stream()
					.filter(detail -> detail.getQuantity() > 0)
					.collect(Collectors.toList());

			if (availableProductDetails.isEmpty()) {
				return null; // 所有奖品都已抽完
			}

			double totalProbability = availableProductDetails.stream()
					.mapToDouble(ProductDetailRes::getProbability)
					.sum();

			availableProductDetails.sort((o1, o2) -> Double.compare(o2.getProbability(), o1.getProbability()));

			double randomNumber = random.nextDouble();
			double cumulativeProbability = 0.0;

			for (ProductDetailRes detail : availableProductDetails) {
				double normalizedProbability = detail.getProbability() / totalProbability;
				cumulativeProbability += normalizedProbability;

				if (randomNumber <= cumulativeProbability) {
					// 检查库存
					if (detail.getQuantity() > 0) {
						// 更新 PrizeNumber
						selectedPrizeNumber.setLevel(detail.getGrade());
						selectedPrizeNumber.setIsDrawn(true);
						selectedPrizeNumber.setProductDetailId(detail.getProductDetailId());

						// 更新库存
						int newQuantity = detail.getQuantity() - 1;
						if (newQuantity >= 0) {
							detail.setQuantity(newQuantity);
							toUpdateProductDetails.add(new ProductDetail(detail.getProductDetailId(), newQuantity, detail.getDrawnNumbers()));

							// 使用原始的 selectedPrizeNumber 对象
							toUpdatePrizeNumbers.add(selectedPrizeNumber);
							break;
						}
					}
				}
			}

			// 批量更新数据
			if (!toUpdatePrizeNumbers.isEmpty()) {
				// 批量更新 PrizeNumber
				prizeNumberMapper.updatePrizeNumberBatch(toUpdatePrizeNumbers, selectedPrizeNumber.getProductId());

				// 批量更新 ProductDetail
				if (!toUpdateProductDetails.isEmpty()) {
					productDetailRepository.updateProductDetailQuantityAndDrawnNumbersBatch(toUpdateProductDetails);
				}

				return selectedPrizeNumber;
			}
		}
	}









	public DrawResult handleDrawRandom(Long userId, Long productId) throws Exception {


		// 获取未抽走的奖品编号
		List<ProductDetailRes> product = productDetailRepository.getProductDetailByProductId(productId);
		if (product.isEmpty()) {
			throw new Exception("獎品已被抽完");
		}

		Random random = new Random();

// 定義每種獎品的概率
		double grandPrizeProbability = 0.05; // 大獎的概率為5%
		double consolationPrizeProbability = 0.95; // 安慰獎的概率為95%

		// 扣除會員內儲值金
		BigDecimal totalAmount = BigDecimal.ZERO;
		ProductRes product2 = productRepository.getProductById(productId);
		BigDecimal amount = product2.getPrice();
		PrizeCategory category = product2.getPrizeCategory();
		Integer balanceInt = userRepository.getBalance(userId);
		BigDecimal balance = new BigDecimal(balanceInt);
		if (category == PrizeCategory.BONUS) {
			Integer bonusPointsInt = userRepository.getBonusPoints(userId);
			BigDecimal bonusPoints = new BigDecimal(bonusPointsInt);
			if (bonusPoints.compareTo(totalAmount) >= 0) {
				BigDecimal newBonusPoints = bonusPoints.subtract(totalAmount);
				userRepository.deductUserBonusPoints(userId, newBonusPoints);
			} else {
				throw new Exception("紅利不足");
			}
		} else {
			if (balance.compareTo(totalAmount) >= 0) {
				BigDecimal newBalance = balance.subtract(totalAmount);
				userRepository.deductUserBalance(userId, newBalance);
			} else {
				throw new Exception("餘額不足，請加值");
			}
		}

		// 根據隨機概率決定獎品
		ProductDetailRes selectedPrizeDetail;
		if (random.nextDouble() < grandPrizeProbability) {
			// 使用戶贏得大獎
					selectedPrizeDetail = getGrandPrizeDetail(productId);
		} else {
			// 使用戶贏得安慰獎
			selectedPrizeDetail = getConsolationPrizeDetail(productId);
		}

// 檢查選中的獎品是否仍可用
		if (selectedPrizeDetail.getQuantity() <= 0) {
			throw new Exception("選中的獎品已經被抽完");
		}

// 更新獎品數量
		selectedPrizeDetail.setQuantity(selectedPrizeDetail.getQuantity() - 1);
		productDetailRepository.updateProductDetailQuantity(selectedPrizeDetail);
		// Record draw result
		DrawResult drawResult = new DrawResult();
		drawResult.setUserId(Long.valueOf(userId));
		drawResult.setProductDetailId(Long.valueOf(selectedPrizeDetail.getProductDetailId()));
		drawResult.setDrawTime(LocalDateTime.now());
		drawResult.setDrawCount(1);
		drawResult.setCreateDate(LocalDateTime.now());
		drawRepository.insertDrawResult(drawResult);

		// Record order and order details
		// 生成订单
		var orderNumber = orderService.genOrderNumber();
		
		var orderEntity = Order.builder()
		.userId(userId)
		.orderNumber(orderNumber)
		.createdAt(LocalDateTime.now())
		.resultStatus(OrderStatus.PREPARING_SHIPMENT)
		.totalAmount(amount)
		.build();
		orderRepository.insertOrder(orderEntity);

		// 获取订单ID
		Long orderId = orderRepository.getOrderIdByOrderNumber(orderNumber);
		List<OrderDetailDto> orderDetailDtoList = new ArrayList<>();

		// 生成订单明细
		OrderDetailDto orderDetail = new OrderDetailDto();
		orderDetail.setOrderId(orderId);
		orderDetail.setProductId(productId);
		orderDetail.setProductDetailId(selectedPrizeDetail.getProductDetailId());
		orderDetail.setProductDetailName(selectedPrizeDetail.getProductName());
		orderDetail.setQuantity(1);
		orderDetail.setUnitPrice(amount);
		orderDetail.setResultStatus(OrderStatus.PREPARING_SHIPMENT);

		orderDetailRepository.insertOrderDetailOne(orderDetail);

		return drawResult;
	}

	// 獲取大獎詳細信息
	public ProductDetailRes getGrandPrizeDetail(Long productId) {
		return productDetailRepository.findFirstByProductIdAndPrizeType(productId, "GRAND");
	}

	// 獲取安慰獎詳細信息
	public ProductDetailRes getConsolationPrizeDetail(Long productId) {
		return productDetailRepository.findFirstByProductIdAndPrizeType(productId, "CONSOLATION");
	}

}
