package com.one.frontend.service;

import com.one.frontend.model.CartItem;
import com.one.frontend.repository.CartRepository;
import com.one.frontend.repository.UserRepository;
import com.one.frontend.response.CartItemRes;
import com.one.frontend.response.UserRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    public List<CartItemRes> getCatItem(String userUid) {
        return cartRepository.getCatItem(userUid);
    }

    public Long getCartIdByUserId(String userUid) {
        Long cartId = cartRepository.getCartIdByUserId(userUid);
        return cartId;
    }

    public List<CartItem> findByUserUidAndIsPayFalse(String userUid) {
        UserRes res = userRepository.getUserById(userUid);
        return cartRepository.findByUserUidAndIsPayFalse(res.getId());
    }
}
