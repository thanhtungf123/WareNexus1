<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    int rentalOrderId = (request.getAttribute("rentalOrderId") != null)
        ? (Integer) request.getAttribute("rentalOrderId")
        : 0;
    String status = (String) request.getAttribute("status");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Payment Result - WareNexus</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="css/style.css" />
    <style>
            .btn-back { border: none; color: white; border-radius: 0.5rem;
                  padding: 0.75rem 2rem; font-weight: 600; transition: all 0.3s ease;
                  text-decoration: none; display: inline-flex; align-items: center; gap: 0.5rem;
                  background: #6b7280;
            }
            .btn-back:hover {
                background: #333f57;
                transform: translateY(-2px); box-shadow: 0 4px 12px rgb(0 0 0 / 0.15); color: white;
            }
    </style>
</head>
<body>
<jsp:include page="header.jsp"/>

<div class="container my-5">
    <div class="bg-white p-5 shadow rounded text-center">
        <h3 class="text-success mb-3">Payment Result</h3>
        <p class="mb-4">Rental Order ID: <strong><%= rentalOrderId %></strong></p>

        <% if ("PAID".equalsIgnoreCase(status)) { %>
            <div class="alert alert-success">Payment successful! Please proceed to contract signing.</div>
            <a href="confirmPassword.jsp?rentalOrderId=<%= rentalOrderId %>" class="btn btn-success px-4">Sign Contract</a>
        <% } else { %>
            <div class="alert alert-danger">Payment failed or was canceled.</div>
            <a href="userhome.jsp" class="btn btn-secondary px-4">Return to Homepage</a>
        <% } %>
        <div class="d-flex gap-3 flex-wrap">
            <button onclick="goBack()" class="btn-back">Quay lại</button>
        </div>
    </div>
</div>

<jsp:include page="footer.jsp"/>
<script>
    function goBack() {
        window.history.back();
    }
</script>
</body>
</html>
