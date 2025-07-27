<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    int rentalOrderId = (request.getAttribute("rentalOrderId") != null)
            ? (Integer) request.getAttribute("rentalOrderId")
            : 0;
    String status = (String) request.getAttribute("status");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Payment Result - WareNexus</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="css/style.css" />
    <style>
        body {
            background-color: #f4f6f8;
            font-family: 'Segoe UI', sans-serif;
        }
        .result-box {
            max-width: 600px;
            margin: 60px auto;
            background: #fff;
            border-radius: 12px;
            box-shadow: 0 6px 16px rgba(0,0,0,0.1);
            padding: 40px;
            text-align: center;
        }
        .result-box h3 {
            color: #2e7d32;
            margin-bottom: 20px;
        }
        .btn-action {
            padding: 12px 24px;
            font-size: 16px;
            border-radius: 6px;
            text-decoration: none;
            transition: 0.3s ease;
        }
        .btn-action:hover {
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
        .btn-success {
            background-color: #2e7d32;
            color: white;
        }
        .btn-success:hover {
            background-color: #1b5e20;
        }
        .btn-secondary {
            background-color: #eeeeee;
            color: #333;
        }
        .btn-secondary:hover {
            background-color: #e0e0e0;
        }
    </style>
</head>
<body>
<jsp:include page="header.jsp"/>

<div class="result-box">
    <h3>💳 Payment Result</h3>
    <p>Rental Order ID: <strong><%= rentalOrderId %></strong></p>

    <% if ("PAID".equalsIgnoreCase(status)) { %>
    <div class="alert alert-success mt-4">Your payment was successful! Please proceed to sign the contract.</div>
    <a href="confirmPassword.jsp?rentalOrderId=<%= rentalOrderId %>" class="btn btn-action btn-success mt-3">Sign Contract</a>
    <div class="mt-4">
        <button onclick="goBack()" class="btn btn-action btn-back">🔙 Go Back</button>
    </div>
    <% } else { %>
    <div class="alert alert-danger mt-4">Payment failed or was canceled.</div>
    <a href="userhome.jsp" class="btn btn-action btn-back">🔙 Return to Homepage</a>
    <% } %>


</div>

<jsp:include page="footer.jsp"/>
<script>
    function goBack() {
        window.history.back();
    }
</script>
</body>
</html>
