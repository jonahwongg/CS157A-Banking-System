<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Banking Application Login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css">
</head>
<body>
<div class="login-shell">
    <section class="login-panel">
        <div class="login-copy">
            <h1>Secure Banking Access</h1>
            <p>Sign in to manage customers, accounts, transaction history, and loan processing.</p>
        </div>

        <c:if test="${not empty param.message}">
            <div class="flash flash-success">${param.message}</div>
        </c:if>
        <c:if test="${not empty loginError}">
            <div class="flash flash-error">${loginError}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/login" method="post" class="login-form">
            <input type="text" name="username" placeholder="Username" required>
            <input type="password" name="password" placeholder="Password" required>
            <button type="submit">Sign In</button>
        </form>

        <div class="demo-credentials">
            <strong>Demo login</strong>
            <span>Username: <code>admin</code></span>
            <span>Password: <code>password123</code></span>
        </div>
    </section>
</div>
</body>
</html>
