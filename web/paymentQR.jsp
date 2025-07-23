<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.warenexus.model.Account" %>
<%
    // Kiểm tra xem người dùng đã đăng nhập chưa
    Account user = (Account) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Pay Deposit - WareNexus</title>
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
    <div class="bg-white shadow p-4 rounded text-center">
        <!-- Chỉnh sửa tiêu đề thành link -->
        <h3 class="text-primary mb-4">Click here to proceed with the payment</h3>

        <c:choose>
            <c:when test="${not empty paymentLink}">
                <!-- Hiển thị link thanh toán nếu có -->
                <a href="${paymentLink}" target="_blank" class="btn btn-outline-primary mt-3">Payment</a>
            </c:when>
            <c:otherwise>
                <!-- Hiển thị thông báo nếu không có link thanh toán -->
                <div class="alert alert-warning mt-3">Payment is currently unavailable</div>
            </c:otherwise>
        </c:choose>
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
