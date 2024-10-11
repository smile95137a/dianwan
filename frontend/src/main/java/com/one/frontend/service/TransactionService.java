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
        List<UserTransaction> transactions;

        if (startDate != null && endDate != null) {
            transactions = transactionRepository.findTransactionsByUserIdAndDateRange(userId, startDate, endDate);
        } else {
            transactions = transactionRepository.findAllTransactionsByUserId(userId);
        }

        // 更新交易类型为用户友好的字符串
        transactions.forEach(transaction -> {
            transaction.setTransactionType(UserTransaction.TransactionType.valueOf(transaction.getFriendlyTransactionType()));
        });

        return transactions;
    }

}
