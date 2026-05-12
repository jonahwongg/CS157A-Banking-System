package com.bank.web;

import com.bank.model.Customer;
import com.bank.service.CustomerService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/customers")
public class CustomerServlet extends BaseServlet {
    private final CustomerService customerService = new CustomerService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        try {
            if ("create".equals(action)) {
                customerService.createCustomer(buildCustomer(request, false));
                redirectWithMessage(request, response, "success", "Customer created successfully.");
            } else if ("update".equals(action)) {
                customerService.updateCustomer(buildCustomer(request, true));
                redirectWithMessage(request, response, "success", "Customer updated successfully.");
            } else if ("delete".equals(action)) {
                customerService.deleteCustomer(Integer.parseInt(request.getParameter("customerId")));
                redirectWithMessage(request, response, "success", "Customer deleted successfully.");
            } else {
                redirectWithMessage(request, response, "error", "Unknown customer action.");
            }
        } catch (SQLException | IllegalArgumentException ex) {
            redirectWithMessage(request, response, "error", ex.getMessage());
        }
    }

    private Customer buildCustomer(HttpServletRequest request, boolean includeId) {
        Customer customer = new Customer();
        if (includeId) {
            customer.setCustomerId(Integer.parseInt(request.getParameter("customerId")));
        }
        customer.setFirstName(request.getParameter("firstName"));
        customer.setLastName(request.getParameter("lastName"));
        customer.setEmail(request.getParameter("email"));
        customer.setPhoneNumber(request.getParameter("phoneNumber"));
        return customer;
    }
}
