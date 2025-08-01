<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.warenexus.model.Account" %>
<%
    Account user = (Account) session.getAttribute("acc");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String status = request.getParameter("status");
    String rentalOrderId = request.getParameter("rentalOrderId");
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <title>Kết quả thanh toán</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="css/style.css" />
    <style>
        body {
            background-color: #f4f6f8;
            font-family: 'Segoe UI', sans-serif;
        }
        .result-box {
            max-width: 600px;
            margin: 80px auto;
            background: #fff;
            border-radius: 12px;
            box-shadow: 0 6px 16px rgba(0,0,0,0.1);
            padding: 40px;
            text-align: center;
        }
        .result-box h3 {
            color: #1976d2;
            margin-bottom: 20px;
        }
        .success-icon {
            font-size: 50px;
            color: #2e7d32;
        }
        .fail-icon {
            font-size: 50px;
            color: #d32f2f;
        }
        .btn-back {
            margin-top: 30px;
            background-color: #1976d2;
            color: white;
            border: none;
            padding: 12px 24px;
            border-radius: 6px;
            font-size: 16px;
            transition: 0.3s ease;
        }
        .btn-back:hover {
            background-color: #1565c0;
        }
    </style>
</head>
<body>
<jsp:include page="header.jsp"/>

<div class="result-box">
    <c:choose>
        <c:when test="${param.status eq 'PAID'}">
            <div class="success-icon">✅</div>
            <h3>Thanh toán thành công!</h3>
            <p>Đơn thuê <strong>#<%= rentalOrderId %></strong> đã được thanh toán tiền tổng.</p>
        </c:when>
        <c:otherwise>
            <div class="fail-icon">❌</div>
            <h3>Thanh toán thất bại hoặc bị hủy</h3>
            <p>Vui lòng thử lại hoặc liên hệ với quản trị viên.</p>
        </c:otherwise>
    </c:choose>

    <a href="rental-history" class="btn-back">🔙 Quay lại lịch sử thuê</a>
</div>

<jsp:include page="footer.jsp"/>
</body>
</html>
