package com.bank.service;

import com.bank.dao.CustomerDao;
import com.bank.model.Customer;

import java.sql.SQLException;
import java.util.List;
import java.util.regex.Pattern;

public class CustomerService {
    private static final int SYSTEM_CUSTOMER_ID = 1;
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{3}-\\d{4}$");

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
        validateCustomer(customer);
        customerDao.create(customer);
    }

    public void updateCustomer(Customer customer) throws SQLException {
        validateCustomer(customer);
        customerDao.update(customer);
    }

    public void deleteCustomer(int customerId) throws SQLException {
        if (customerId == SYSTEM_CUSTOMER_ID) {
            throw new IllegalArgumentException("The system customer cannot be deleted.");
        }
        customerDao.delete(customerId);
    }

    private void validateCustomer(Customer customer) {
        if (customer.getPhoneNumber() == null || !PHONE_PATTERN.matcher(customer.getPhoneNumber().trim()).matches()) {
            throw new IllegalArgumentException("Phone number must use the format ###-####.");
        }
    }
}
