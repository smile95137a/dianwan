package com.one.frontend.service;

import com.one.frontend.model.Cart;
import com.one.frontend.model.Role;
import com.one.frontend.model.User;
import com.one.frontend.repository.CartRepository;
import com.one.frontend.repository.RoleRepository;
import com.one.frontend.repository.UserRepository;
import com.one.frontend.request.UserReq;
import com.one.frontend.response.UserRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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

	public UserRes getUserById(String userUid) {
		return userRepository.getUserById(userUid);
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
			String encryptedPassword = passwordEncoder.encode(userDto.getPassword());

			User user = new User();
			user.setUserUid(UUID.randomUUID().toString());
			user.setUsername(userDto.getUsername());
			user.setPassword(encryptedPassword);
			user.setEmail(userDto.getEmail());
			user.setAddress(userDto.getAddress());
			user.setPhoneNumber(userDto.getPhoneNumber());
			user.setCreatedAt(LocalDateTime.now());
			user.setRoleId(memberRole.getId());
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

	public UserRes updateUser(UserReq userReq, String userUid) {
		try {
			UserRes user = userRepository.getUserById(userUid);

			if (userReq.getPassword() != null && !userReq.getPassword().isEmpty()) {
				String encryptedPassword = passwordEncoder.encode(userReq.getPassword());
				user.setPassword(encryptedPassword);
			}

			user.setId(userReq.getUserId());
			user.setUsername(userReq.getUsername());
			user.setNickName(userReq.getNickName());
			user.setEmail(userReq.getEmail());
			user.setPhoneNumber(userReq.getPhoneNumber());
			user.setAddress(userReq.getAddress());
			user.setUpdatedAt(LocalDateTime.now());

			userRepository.update(user);

			return userRepository.getUserById(userUid);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}