package com.bank.service;

import com.bank.dao.TransactionDao;
import com.bank.model.BankTransaction;

import java.sql.SQLException;
import java.util.List;

public class TransactionService {
    private final TransactionDao transactionDao = new TransactionDao();

    public List<BankTransaction> getAllTransactions() throws SQLException {
        return transactionDao.findAll();
    }

    public List<BankTransaction> searchTransactions(String query) throws SQLException {
        if (query == null || query.isBlank()) {
            return transactionDao.findAll();
        }
        return transactionDao.search(query);
    }

    public void deleteTransaction(int transactionId) throws SQLException {
        transactionDao.delete(transactionId);
    }
}
