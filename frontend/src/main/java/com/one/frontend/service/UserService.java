package com.one.frontend.service;

import com.one.frontend.model.Cart;
import com.one.frontend.model.User;
import com.one.frontend.repository.CartRepository;
import com.one.frontend.repository.UserRepository;
import com.one.frontend.request.UserReq;
import com.one.frontend.response.UserRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private CartRepository cartRepository;

	public List<User> getAllUser() {
		return userRepository.getAllUser();
	}

	public UserRes getUserById(Integer userId) {
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

			String encryptedPassword = passwordEncoder.encode(userDto.getPassword());

			User user = new User();
			user.setUsername(userDto.getUsername());
			user.setPassword(encryptedPassword);
			user.setEmail(userDto.getEmail());
			user.setAddress(userDto.getAddress());
			user.setPhoneNumber(userDto.getPhoneNumber());
			user.setCreatedAt(LocalDateTime.now());
			user.setRoleId(2L); // 註冊即是正式會員
			user.setBalance(BigDecimal.ZERO);
			user.setBonus(BigDecimal.ZERO);
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

	public UserRes updateUser(UserReq userReq, Integer id) {
		try {
			// 获取当前用户信息
			UserRes user = userRepository.getUserById(id);

			// 检查密码是否为空
			if (userReq.getPassword() != null && !userReq.getPassword().isEmpty()) {
				String encryptedPassword = passwordEncoder.encode(userReq.getPassword());
				user.setPassword(encryptedPassword);
			}

			// 更新其他用户信息
			user.setId(userReq.getUserId());
			user.setUsername(userReq.getUsername());
			user.setNickName(userReq.getNickName());
			user.setEmail(userReq.getEmail());
			user.setPhoneNumber(userReq.getPhoneNumber());
			user.setAddress(userReq.getAddress());
			user.setUpdatedAt(LocalDateTime.now());

			// 更新数据库中的用户信息
			userRepository.update(user);

			// 返回更新后的用户信息
			return userRepository.getUserById(id);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}