package com.one.frontend.service;

import com.one.frontend.model.SignIn;
import com.one.frontend.repository.SignInMapper;
import com.one.frontend.response.SignInRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SignInService {

    @Autowired
    private SignInMapper signInMapper;

    public List<SignIn> getAllSignIns() {
        return signInMapper.findAll();
    }

    public SignInRes spinWheel() {
        List<SignInRes> signIns = signInMapper.findAllByRes();
        double randomValue = Math.random(); // 0.0 到 1.0 之间的隨機數

        double cumulativeProbability = 0.0;
        for (SignInRes signIn : signIns) {
            cumulativeProbability += signIn.getProbability();
            if (randomValue <= cumulativeProbability) {
                return signIn;
            }
        }

        return null;
    }

}
