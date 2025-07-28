<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.warenexus.model.Account" %>
<%
    Account user = (Account) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Awaiting Approval - WareNexus</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="css/style.css" />
    <style>
        body {
            background-color: #f4f6f8;
            font-family: 'Segoe UI', sans-serif;
        }
        .confirm-box {
            max-width: 600px;
            margin: 60px auto;
            background: #fff;
            border-radius: 12px;
            box-shadow: 0 6px 16px rgba(0,0,0,0.1);
            padding: 40px;
            text-align: center;
        }
        .confirm-box h3 {
            color: #1976d2;
            margin-bottom: 20px;
        }
        .alert-message {
            font-size: 16px;
            color: #333;
            margin-bottom: 24px;
        }
        .btn-confirm {
            padding: 12px 24px;
            font-size: 16px;
            border-radius: 6px;
            text-decoration: none;
            transition: 0.3s ease;
        }
        .btn-confirm:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(0,0,0,0.15);
        }
        .btn-back {
            background-color: #6b7280;
            color: white;
        }
        .btn-back:hover {
            background-color: #333f57;
        }
        .btn-primary {
            background-color: #1976d2;
            color: white;
        }
        .btn-primary:hover {
            background-color: #1565c0;
        }
    </style>
</head>
<body>
<jsp:include page="header.jsp"/>

<div class="confirm-box">
    <h3>⏳ Rental Order Awaiting Approval</h3>
    <p class="alert-message">
        Your rental request has not yet been approved by the administrator.<br/>
        Please wait for confirmation before proceeding with the deposit payment.
    </p>

    <div class="mt-4">
            <button onclick="goBack()" class="btn btn-payment btn-back">🔙 Go Home</button>
        </div>
</div>

<jsp:include page="footer.jsp"/>
<script>
    function goBack() {
        window.location.href = "userhome";
    }
</script>
</body>
</html>
