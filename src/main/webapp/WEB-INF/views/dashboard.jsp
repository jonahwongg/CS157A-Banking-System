<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Banking Application Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css">
</head>
<body>
<div class="page-shell" data-active-tab="${activeTab}">
    <header class="hero">
        <div>
            <h1>Banking Application Dashboard</h1>
            <p class="hero-copy">Manage customers, accounts, transaction history, and loan processing.</p>
            <div class="session-bar">
                <span>Signed in as ${authUser.fullName} (${authUser.roleName})</span>
                <form action="${pageContext.request.contextPath}/logout" method="post">
                    <button type="submit" class="secondary session-button">Logout</button>
                </form>
            </div>
        </div>
        <div class="hero-stat-grid">
            <div class="stat-card">
                <span class="stat-label">Customers</span>
                <strong>${customers.size()}</strong>
            </div>
            <div class="stat-card">
                <span class="stat-label">Accounts</span>
                <strong>${accounts.size()}</strong>
            </div>
            <div class="stat-card">
                <span class="stat-label">Transactions</span>
                <strong>${transactions.size()}</strong>
            </div>
            <div class="stat-card">
                <span class="stat-label">Loans</span>
                <strong>${loans.size()}</strong>
            </div>
        </div>
    </header>

    <c:if test="${not empty param.message}">
        <div class="flash ${param.messageType == 'success' ? 'flash-success' : 'flash-error'}">
            ${param.message}
        </div>
    </c:if>

    <nav class="tab-nav" aria-label="Dashboard sections">
        <button type="button" class="tab-button is-active" data-tab-target="customers-tab">Customers</button>
        <button type="button" class="tab-button" data-tab-target="accounts-tab">Accounts</button>
        <button type="button" class="tab-button" data-tab-target="transactions-tab">Transaction Log</button>
        <button type="button" class="tab-button" data-tab-target="loans-tab">Loans</button>
    </nav>

    <main class="dashboard-grid">
        <section class="panel tab-panel is-active" id="customers-tab">
            <div class="panel-heading">
                <h2>Customers</h2>
                <p>Create, edit, and delete customer profiles.</p>
            </div>
            <form action="${pageContext.request.contextPath}/dashboard" method="get" class="search-form">
                <input type="text" name="customerQuery" value="${customerQuery}" placeholder="Search customers by ID, name, email, or phone">
                <input type="hidden" name="accountQuery" value="${accountQuery}">
                <input type="hidden" name="transactionQuery" value="${transactionQuery}">
                <input type="hidden" name="activeTab" value="customers-tab">
                <button type="submit">Search</button>
                <a href="${pageContext.request.contextPath}/dashboard?accountQuery=${accountQuery}&transactionQuery=${transactionQuery}&activeTab=customers-tab" class="text-link">Clear</a>
            </form>
            <form action="${pageContext.request.contextPath}/customers" method="post" class="form-grid">
                <input type="hidden" name="action" value="create">
                <input type="text" name="firstName" placeholder="First name" required>
                <input type="text" name="lastName" placeholder="Last name" required>
                <input type="email" name="email" placeholder="Email" required>
                <input type="text" name="phoneNumber" placeholder="Phone number (123-4567)" pattern="\d{3}-\d{4}" title="Phone number must use the format 123-4567" required>
                <button type="submit">Add Customer</button>
            </form>
            <div class="table-wrap">
                <table>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Phone</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="customer" items="${customers}">
                        <tr>
                            <td>${customer.customerId}</td>
                            <td>${customer.firstName} ${customer.lastName}</td>
                            <td>${customer.email}</td>
                            <td>${customer.phoneNumber}</td>
                            <td>
                                <div class="action-stack">
                                    <form action="${pageContext.request.contextPath}/customers" method="post" class="inline-form">
                                        <input type="hidden" name="action" value="update">
                                        <input type="hidden" name="customerId" value="${customer.customerId}">
                                        <input type="text" name="firstName" value="${customer.firstName}" required>
                                        <input type="text" name="lastName" value="${customer.lastName}" required>
                                        <input type="email" name="email" value="${customer.email}" required>
                                        <input type="text" name="phoneNumber" value="${customer.phoneNumber}" pattern="\d{3}-\d{4}" title="Phone number must use the format 123-4567" required>
                                        <button type="submit" class="secondary">Update</button>
                                    </form>
                                    <form action="${pageContext.request.contextPath}/customers" method="post">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="customerId" value="${customer.customerId}">
                                        <button type="submit" class="danger">Delete</button>
                                    </form>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </section>

        <section class="panel tab-panel" id="accounts-tab">
            <div class="panel-heading">
                <h2>Accounts</h2>
                <p>Open or remove accounts and adjust account details.</p>
            </div>
            <form action="${pageContext.request.contextPath}/dashboard" method="get" class="search-form">
                <input type="hidden" name="customerQuery" value="${customerQuery}">
                <input type="text" name="accountQuery" value="${accountQuery}" placeholder="Search accounts by ID, number, customer, type, or status">
                <input type="hidden" name="transactionQuery" value="${transactionQuery}">
                <input type="hidden" name="activeTab" value="accounts-tab">
                <button type="submit">Search</button>
                <a href="${pageContext.request.contextPath}/dashboard?customerQuery=${customerQuery}&transactionQuery=${transactionQuery}&activeTab=accounts-tab" class="text-link">Clear</a>
            </form>
            <form action="${pageContext.request.contextPath}/accounts" method="post" class="form-grid">
                <input type="hidden" name="action" value="create">
                <select name="customerId" required>
                    <option value="">Customer</option>
                    <c:forEach var="customer" items="${customerOptions}">
                        <option value="${customer.customerId}">${customer.firstName} ${customer.lastName}</option>
                    </c:forEach>
                </select>
                <input type="text" name="accountNumber" placeholder="Account number (CHK-00001)" pattern="(CHK|SVG|SYS|BUS)-\d{5}" title="Account number must use CHK, SVG, SYS, or BUS followed by a 5 digit number" required>
                <select name="accountType" required>
                    <option value="">Type</option>
                    <option value="CHECKING">Checking</option>
                    <option value="SAVINGS">Savings</option>
                    <option value="BUSINESS">Business</option>
                </select>
                <input type="number" step="0.01" min="0" name="balance" placeholder="Opening balance" required>
                <select name="status" required>
                    <option value="ACTIVE">Active</option>
                    <option value="FROZEN">Frozen</option>
                    <option value="CLOSED">Closed</option>
                </select>
                <button type="submit">Create Account</button>
            </form>
            <div class="table-wrap">
                <table>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Customer</th>
                        <th>Number</th>
                        <th>Type</th>
                        <th>Balance</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="account" items="${accounts}">
                        <tr>
                            <td>${account.accountId}</td>
                            <td>${account.customerName}</td>
                            <td>${account.accountNumber}</td>
                            <td>${account.accountType}</td>
                            <td>$${account.balance}</td>
                            <td>${account.status}</td>
                            <td>
                                <div class="action-stack">
                                    <form action="${pageContext.request.contextPath}/accounts" method="post" class="inline-form account-form">
                                        <input type="hidden" name="action" value="update">
                                        <input type="hidden" name="accountId" value="${account.accountId}">
                                        <select name="customerId" required>
                                            <c:forEach var="customer" items="${customerOptions}">
                                                <option value="${customer.customerId}" ${customer.customerId == account.customerId ? 'selected' : ''}>
                                                        ${customer.firstName} ${customer.lastName}
                                                </option>
                                            </c:forEach>
                                        </select>
                                        <input type="text" name="accountNumber" value="${account.accountNumber}" pattern="(CHK|SVG|SYS|BUS)-\d{5}" title="Account number must use CHK, SVG, SYS, or BUS followed by a 5 digit number" required>
                                        <select name="accountType" required>
                                            <option value="CHECKING" ${account.accountType == 'CHECKING' ? 'selected' : ''}>Checking</option>
                                            <option value="SAVINGS" ${account.accountType == 'SAVINGS' ? 'selected' : ''}>Savings</option>
                                            <option value="BUSINESS" ${account.accountType == 'BUSINESS' ? 'selected' : ''}>Business</option>
                                        </select>
                                        <input type="number" step="0.01" min="0" name="balance" value="${account.balance}" required>
                                        <select name="status" required>
                                            <option value="ACTIVE" ${account.status == 'ACTIVE' ? 'selected' : ''}>Active</option>
                                            <option value="FROZEN" ${account.status == 'FROZEN' ? 'selected' : ''}>Frozen</option>
                                            <option value="CLOSED" ${account.status == 'CLOSED' ? 'selected' : ''}>Closed</option>
                                        </select>
                                        <button type="submit" class="secondary">Update</button>
                                    </form>
                                    <form action="${pageContext.request.contextPath}/accounts" method="post">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="accountId" value="${account.accountId}">
                                        <button type="submit" class="danger">Delete</button>
                                    </form>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </section>

        <section class="panel">
            <div class="panel-heading">
                <h2>Banking Operations</h2>
                <p>Deposit, withdraw, and transfer funds. Each action writes directly to the transaction log.</p>
            </div>
            <div class="operations-grid">
                <form action="${pageContext.request.contextPath}/banking" method="post" class="operation-card">
                    <h3>Deposit</h3>
                    <input type="hidden" name="action" value="deposit">
                    <select name="accountId" required>
                        <option value="">Select account</option>
                        <c:forEach var="account" items="${accounts}">
                            <option value="${account.accountId}">${account.accountNumber} (${account.customerName})</option>
                        </c:forEach>
                    </select>
                    <input type="number" step="0.01" min="0.01" name="amount" placeholder="Amount" required>
                    <input type="text" name="description" placeholder="Description" required>
                    <button type="submit">Run Deposit</button>
                </form>
                <form action="${pageContext.request.contextPath}/banking" method="post" class="operation-card">
                    <h3>Withdrawal</h3>
                    <input type="hidden" name="action" value="withdraw">
                    <select name="accountId" required>
                        <option value="">Select account</option>
                        <c:forEach var="account" items="${accounts}">
                            <option value="${account.accountId}">${account.accountNumber} (${account.customerName})</option>
                        </c:forEach>
                    </select>
                    <input type="number" step="0.01" min="0.01" name="amount" placeholder="Amount" required>
                    <input type="text" name="description" placeholder="Description" required>
                    <button type="submit">Run Withdrawal</button>
                </form>
                <form action="${pageContext.request.contextPath}/banking" method="post" class="operation-card">
                    <h3>Transfer</h3>
                    <input type="hidden" name="action" value="transfer">
                    <select name="fromAccountId" required>
                        <option value="">From account</option>
                        <c:forEach var="account" items="${accounts}">
                            <option value="${account.accountId}">${account.accountNumber} (${account.customerName})</option>
                        </c:forEach>
                    </select>
                    <select name="toAccountId" required>
                        <option value="">To account</option>
                        <c:forEach var="account" items="${accounts}">
                            <option value="${account.accountId}">${account.accountNumber} (${account.customerName})</option>
                        </c:forEach>
                    </select>
                    <input type="number" step="0.01" min="0.01" name="amount" placeholder="Amount" required>
                    <input type="text" name="description" placeholder="Description" required>
                    <button type="submit">Run Transfer</button>
                </form>
            </div>
        </section>

        <section class="panel tab-panel" id="transactions-tab">
            <div class="panel-heading">
                <h2>Transaction Log</h2>
                <p>View all deposits, withdrawals, and transfers.</p>
            </div>
            <form action="${pageContext.request.contextPath}/dashboard" method="get" class="search-form">
                <input type="hidden" name="customerQuery" value="${customerQuery}">
                <input type="hidden" name="accountQuery" value="${accountQuery}">
                <input type="text" name="transactionQuery" value="${transactionQuery}" placeholder="Search transactions by ID, type, description, account, or amount">
                <input type="hidden" name="activeTab" value="transactions-tab">
                <button type="submit">Search</button>
                <a href="${pageContext.request.contextPath}/dashboard?customerQuery=${customerQuery}&accountQuery=${accountQuery}&activeTab=transactions-tab" class="text-link">Clear</a>
            </form>
            <div class="table-wrap">
                <table>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Type</th>
                        <th>From</th>
                        <th>To</th>
                        <th>Amount</th>
                        <th>Description</th>
                        <th>Timestamp</th>
                        <th>Delete</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="transaction" items="${transactions}">
                        <tr>
                            <td>${transaction.transactionId}</td>
                            <td>${transaction.transactionType}</td>
                            <td>${empty transaction.fromAccountNumber ? '-' : transaction.fromAccountNumber}</td>
                            <td>${empty transaction.toAccountNumber ? '-' : transaction.toAccountNumber}</td>
                            <td>$${transaction.amount}</td>
                            <td>${transaction.description}</td>
                            <td>${transaction.transactionTime}</td>
                            <td>
                                <form action="${pageContext.request.contextPath}/transactions" method="post">
                                    <input type="hidden" name="transactionId" value="${transaction.transactionId}">
                                    <button type="submit" class="danger">Delete</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </section>

        <section class="panel tab-panel" id="loans-tab">
            <div class="panel-heading">
                <h2>Loans</h2>
                <p>Create loan applications, process status changes, and manage repayments manually.</p>
            </div>
            <form action="${pageContext.request.contextPath}/loans" method="post" class="form-grid">
                <input type="hidden" name="action" value="create">
                <select name="customerId" required>
                    <option value="">Customer</option>
                    <c:forEach var="customer" items="${customerOptions}">
                        <option value="${customer.customerId}">${customer.firstName} ${customer.lastName}</option>
                    </c:forEach>
                </select>
                <input type="number" step="0.01" min="0.01" name="principalAmount" placeholder="Principal amount" required>
                <input type="number" step="0.01" min="0.01" name="interestRate" placeholder="Interest rate" required>
                <input type="number" min="1" name="termMonths" placeholder="Term (months)" required>
                <select name="status" required>
                    <option value="PENDING">Pending</option>
                    <option value="APPROVED">Approved</option>
                    <option value="REJECTED">Rejected</option>
                    <option value="CLOSED">Closed</option>
                </select>
                <input type="date" name="applicationDate" required>
                <button type="submit">Create Loan</button>
            </form>
            <div class="table-wrap">
                <table>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Customer</th>
                        <th>Principal</th>
                        <th>Rate</th>
                        <th>Term</th>
                        <th>Status</th>
                        <th>Application Date</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="loan" items="${loans}">
                        <tr>
                            <td>${loan.loanId}</td>
                            <td>${loan.customerName}</td>
                            <td>$${loan.principalAmount}</td>
                            <td>${loan.interestRate}%</td>
                            <td>${loan.termMonths}</td>
                            <td>${loan.status}</td>
                            <td>${loan.applicationDate}</td>
                            <td>
                                <div class="action-stack">
                                    <form action="${pageContext.request.contextPath}/loans" method="post" class="inline-form loan-form">
                                        <input type="hidden" name="action" value="update">
                                        <input type="hidden" name="loanId" value="${loan.loanId}">
                                        <select name="customerId" required>
                                            <c:forEach var="customer" items="${customerOptions}">
                                                <option value="${customer.customerId}" ${customer.customerId == loan.customerId ? 'selected' : ''}>
                                                        ${customer.firstName} ${customer.lastName}
                                                </option>
                                            </c:forEach>
                                        </select>
                                        <input type="number" step="0.01" min="0.01" name="principalAmount" value="${loan.principalAmount}" required>
                                        <input type="number" step="0.01" min="0.01" name="interestRate" value="${loan.interestRate}" required>
                                        <input type="number" min="1" name="termMonths" value="${loan.termMonths}" required>
                                        <select name="status" required>
                                            <option value="PENDING" ${loan.status == 'PENDING' ? 'selected' : ''}>Pending</option>
                                            <option value="APPROVED" ${loan.status == 'APPROVED' ? 'selected' : ''}>Approved</option>
                                            <option value="REJECTED" ${loan.status == 'REJECTED' ? 'selected' : ''}>Rejected</option>
                                            <option value="CLOSED" ${loan.status == 'CLOSED' ? 'selected' : ''}>Closed</option>
                                        </select>
                                        <input type="date" name="applicationDate" value="${loan.applicationDate}" required>
                                        <button type="submit" class="secondary">Update</button>
                                    </form>
                                    <form action="${pageContext.request.contextPath}/loans" method="post">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="loanId" value="${loan.loanId}">
                                        <button type="submit" class="danger">Delete</button>
                                    </form>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </section>
    </main>
</div>
<script src="${pageContext.request.contextPath}/assets/js/app.js"></script>
</body>
</html>
