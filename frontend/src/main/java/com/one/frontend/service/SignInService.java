package com.one.frontend.service;

import com.one.frontend.model.DailySignInRecord;
import com.one.frontend.model.SignIn;
import com.one.frontend.repository.DailySignInRepository;
import com.one.frontend.repository.SignInMapper;
import com.one.frontend.response.SignInRes;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SignInService {

    private final SignInMapper signInMapper;
    private final DailySignInRepository dailySignInRepository;

    public List<SignIn> getAllSignIns() {
        return signInMapper.findAll();
    }

    public SignInRes spinWheel(Long userId) throws Exception {
        List<SignInRes> signIns = signInMapper.findAllByRes();
        double randomValue = Math.random(); // 0.0 到 1.0 之间的隨機數

        double cumulativeProbability = 0.0;
        for (SignInRes signIn : signIns) {
            cumulativeProbability += signIn.getProbability();
            if (randomValue <= cumulativeProbability) {

                DailySignInRecord todayRecord = dailySignInRepository.getRecordByUserIdAndDate(userId, LocalDate.now());
                
                if (todayRecord == null) {
                    DailySignInRecord newRecord = DailySignInRecord.builder()
                            .userId(userId)
                            .signInDate(LocalDate.now())
                            .rewardPoints(signIn.getSliverPrice())
                            .build();

                    dailySignInRepository.insertSignInRecord(newRecord);
                } else {
                    throw new Exception("已經簽到");
                }

                return signIn;
            }
        }

        return null;
    }
}
