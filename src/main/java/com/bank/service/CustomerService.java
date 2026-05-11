package com.bank.service;

import com.bank.dao.CustomerDao;
import com.bank.model.Customer;

import java.sql.SQLException;
import java.util.List;

public class CustomerService {
    private static final int SYSTEM_CUSTOMER_ID = 1;

    private final CustomerDao customerDao = new CustomerDao();

    public List<Customer> getAllCustomers() throws SQLException {
        return customerDao.findAll();
    }

    public List<Customer> searchCustomers(String query) throws SQLException {
        if (query == null || query.isBlank()) {
            return customerDao.findAll();
        }
        return customerDao.search(query);
    }

    public void createCustomer(Customer customer) throws SQLException {
        customerDao.create(customer);
    }

    public void updateCustomer(Customer customer) throws SQLException {
        customerDao.update(customer);
    }

    public void deleteCustomer(int customerId) throws SQLException {
        if (customerId == SYSTEM_CUSTOMER_ID) {
            throw new IllegalArgumentException("The system customer cannot be deleted.");
        }
        customerDao.delete(customerId);
    }
}
