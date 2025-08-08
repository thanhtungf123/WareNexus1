<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.warenexus.model.Account" %>

<%
    Account user = (Account) session.getAttribute("acc");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Rental Order Detail - WareNexus</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="css/style.css" />
    <style>
        body {
            background-color: #f4f6f8;
            font-family: 'Segoe UI', sans-serif;
        }
        .detail-container {
            max-width: 900px;
            margin: 60px auto;
            background: #fff;
            border-radius: 12px;
            box-shadow: 0 6px 16px rgba(0,0,0,0.1);
            padding: 40px;
        }
        .detail-container h3 {
            color: #1976d2;
            margin-bottom: 30px;
            text-align: center;
        }
        .info-label {
            font-weight: bold;
            color: #333;
        }
        .info-value {
            color: #555;
        }
        .info-row {
            margin-bottom: 16px;
        }
        .btn-back {
            margin-top: 30px;
            padding: 10px 25px;
            font-weight: bold;
        }
        .alert-warning {
            font-size: 0.95rem;
            margin-top: 12px;
        }
        .warehouse-img {
            width: 100%;
            max-height: 400px;
            object-fit: cover;
            margin-bottom: 20px;
            border-radius: 10px;
        }
    </style>
</head>
<body>

<jsp:include page="header.jsp"/>

<div class="detail-container">
    <h3>📋 Rental Order Detail</h3>

    <!-- Warehouse Image -->
    <c:if test="${not empty warehouse.imageUrl}">
        <img src="image?id=${warehouse.imageUrl}" alt="Warehouse Image" class="warehouse-img" />
    </c:if>
    <c:if test="${empty warehouse.imageUrl}">
        <img src="images/default-warehouse.jpg" alt="No Image Available" class="warehouse-img" />
    </c:if>

    <!-- Rental Info -->
    <div class="info-row">
        <span class="info-label">Rental Order ID:</span>
        <span class="info-value">${rental.rentalOrderID}</span>
    </div>
    <div class="info-row">
        <span class="info-label">Warehouse:</span>
        <span class="info-value">${warehouse.name}</span>
    </div>
    <div class="info-row">
        <span class="info-label">Location:</span>
        <span class="info-value">${warehouse.address}, ${warehouse.ward}, ${warehouse.district}</span>
    </div>
    <div class="info-row">
        <span class="info-label">Start Date:</span>
        <span class="info-value">${rental.startDate}</span>
    </div>
    <div class="info-row">
        <span class="info-label">End Date:</span>
        <span class="info-value">${rental.endDate}</span>
    </div>
    <div class="info-row">
        <span class="info-label">Deposit:</span>
        <span class="info-value">
            <fmt:formatNumber value="${rental.deposit}" type="currency" currencySymbol="₫"/>
        </span>
    </div>
    <div class="info-row">
        <span class="info-label">Total Price:</span>
        <span class="info-value">
            <fmt:formatNumber value="${rental.totalPrice}" type="currency" currencySymbol="₫"/>
        </span>
    </div>

    <!-- ✅ Total Price + Payment Section -->
    <div class="info-row">
        <c:choose>
            <c:when test="${isFinalPaid}">
                <span class="info-value text-success">✅ Đã thanh toán toàn bộ</span>
            </c:when>
            <c:otherwise>
                <span class="info-value text-danger">
                    <fmt:formatNumber value="${rental.totalPrice}" type="currency" currencySymbol="₫"/>
                </span>

                <!-- 🔔 Cảnh báo 14 ngày đầu sau ngày bắt đầu -->
                <c:if test="${daysUntilFinalDue >= 0}">
                    <div class="alert alert-warning">
                        💡 <strong>Lưu ý:</strong> Bạn cần thanh toán toàn bộ số tiền thuê trong vòng <strong>14 ngày</strong> kể từ ngày bắt đầu thuê.
                        <br/>⏰ Còn <strong>${daysUntilFinalDue}</strong> ngày để thanh toán.
                    </div>
                </c:if>

                <!-- 👉 Pay Final Button -->
                <div class="mt-2">
                    <form action="final-payment" method="post">
                        <<input type="hidden" name="rentalOrderId" value="${rental.rentalOrderID}" />
                        <input type="hidden" name="amount" value="${rental.totalPrice}" />
                        <button class="btn btn-primary">💳 Thanh toán số tiền còn lại</button>
                    </form>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- 🟡 Rental Status -->
    <div class="info-row">
        <span class="info-label">Rental Status:</span>
        <span class="info-value">${rental.status}</span>
    </div>

    <div class="info-row">
        <span class="info-label">Created At:</span>
        <span class="info-value">${rental.createdAt}</span>
    </div>

    <div class="text-center">
        <a href="rental-history" class="btn btn-secondary btn-back">🔙 Back to History</a>
    </div>
</div>

<jsp:include page="footer.jsp"/>
</body>
</html>
