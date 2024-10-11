package com.one.frontend.service;

import com.one.frontend.model.UserTransaction;
import com.one.frontend.repository.UserTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private UserTransactionRepository transactionRepository;

    public List<UserTransaction> getTransactions(Long userId, Date startDate, Date endDate) {
        if (startDate != null && endDate != null) {
            return transactionRepository.findTransactionsByUserIdAndDateRange(userId, startDate, endDate);
        } else {
            return transactionRepository.findAllTransactionsByUserId(userId);
        }
    }
}
