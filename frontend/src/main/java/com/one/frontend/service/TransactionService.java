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

        // 不需要再将中文设置回枚举
        // 通过 getFriendlyTransactionType() 在前端返回用户友好的字符串
        transactions.forEach(transaction -> {
            // 使用友好的类型（中文），返回给前端时处理为中文字符串
            System.out.println(transaction.getFriendlyTransactionType());
        });

        return transactions;
    }

}
