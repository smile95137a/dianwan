package com.one.frontend.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.one.frontend.model.Cart;
import com.one.frontend.model.Role;
import com.one.frontend.model.User;
import com.one.frontend.repository.CartRepository;
import com.one.frontend.repository.RoleRepository;
import com.one.frontend.repository.UserRepository;
import com.one.frontend.request.UserReq;
import com.one.frontend.response.UserRes;
import com.one.frontend.util.RandomUtils;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private RoleRepository roleRepository;

	private final String MEMBER = "未驗證會員";

	public List<User> getAllUser() {
		return userRepository.getAllUser();
	}

	public UserRes getUserById(Long userId) {
		return userRepository.getUserById(userId);
	}

	public String deleteUser(Integer userId) {
		try {
			userRepository.deleteUser(userId);
			return "1";
		} catch (Exception e) {
			return "0";
		}
	}

	public int getUserCountByRoleId(int roleId) {
		return userRepository.countByRoleId(roleId);
	}

	public UserRes registerUser(UserReq userDto) throws Exception {
		try {
			User check = userRepository.getUserByUserName(userDto.getUsername());
			if (check != null) {
				throw new Exception("帳號已存在");
			}
			Role memberRole = roleRepository.findByName(this.MEMBER);

			System.out.println(memberRole);
			String encryptedPassword = passwordEncoder.encode(userDto.getPassword());

			User user = User.builder().userUid(RandomUtils.genRandom(32)).username(userDto.getUsername())
					.password(encryptedPassword).nickname(userDto.getNickname()).email(userDto.getEmail())
					.phoneNumber(userDto.getPhoneNumber()).city(userDto.getCity()).area(userDto.getArea())
					.address(userDto.getAddress()).addressName(userDto.getAddressName()).lineId(userDto.getLineId())
					.createdAt(LocalDateTime.now()).roleId(memberRole.getId()).balance(BigDecimal.ZERO)
					.bonus(BigDecimal.ZERO).sliverCoin(BigDecimal.ZERO).status("ACTIVE").drawCount(0L).provider("local")
					.invoiceInfoEmail(userDto.getEmail())
					.build();
			userRepository.createUser(user);

			UserRes userRes = new UserRes();
			userRes.setUsername(user.getUsername());
			userRes.setEmail(user.getEmail());
			userRes.setPhoneNumber(user.getPhoneNumber());
			userRes.setAddress(user.getAddress());

			User userCart = userRepository.getUserByUserName(userDto.getUsername());
			Cart cart = new Cart();
			cart.setUserId(userCart.getId());
			cart.setCreatedAt(LocalDateTime.now());
			cartRepository.addCart(cart);

			return userRes;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Transactional
	public boolean updateUser(UserReq req, Long userId) throws Exception {
		try {
			User user = userRepository.getById(userId);

			user.setNickname(req.getNickname());
			user.setAddressName(req.getAddressName());
			user.setCity(req.getCity());
			user.setArea(req.getArea());
			user.setAddress(req.getAddress());
			user.setLineId(req.getLineId());
			user.setPhoneNumber(req.getPhoneNumber());
			user.setUpdatedAt(LocalDateTime.now());

			userRepository.update(user);
			return true;
		} catch (Exception e) {
			throw new Exception("Failed to update user with ID: " + userId, e);
		}
	}
	
	@Transactional
	public boolean updateUserInvoice(UserReq req, Long userId) throws Exception {
		try {
			User user = userRepository.getById(userId);
			user.setInvoiceInfo(req.getInvoiceInfo());
			user.setInvoiceInfoEmail(req.getInvoiceInfoEmail());
			user.setUpdatedAt(LocalDateTime.now());

			userRepository.update(user);
			return true;
		} catch (Exception e) {
			throw new Exception("Failed to update user with ID: " + userId, e);
		}
	}

}