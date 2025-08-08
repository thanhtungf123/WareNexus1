<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.warenexus.model.Account" %>

<%
    Account user = (Account) session.getAttribute("user");
    if (user == null || user.getRoleId() != 1) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quản lý đơn thuê đã duyệt</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet"/>
    <link rel="stylesheet" href="css/style.css"/>
    <style>
        body {
            background-color: #f8f9fa;
        }
        .card-title {
            font-size: 1.25rem;
        }
        .badge {
            font-size: 0.85em;
            padding: 0.4em 0.6em;
        }
        .btn-circle {
            border-radius: 50%;
            width: 36px;
            height: 36px;
            padding: 6px 0;
            text-align: center;
        }
    </style>
</head>
<body>

<jsp:include page="header.jsp"/>

<div class="container mt-4 mb-5">
    <h2 class="text-center text-primary fw-bold mb-4">📋 Danh sách đơn thuê đã được duyệt</h2>
    
    <c:if test="${not empty sessionScope.success}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <i class="bi bi-check-circle-fill"></i> ${sessionScope.success}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <c:remove var="success" scope="session"/>
    </c:if>

    <c:if test="${not empty sessionScope.error}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="bi bi-exclamation-triangle-fill"></i> ${sessionScope.error}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <c:remove var="error" scope="session"/>
    </c:if>
    
    <c:if test="${not empty sessionScope.success}">
    <div class="alert alert-success alert-dismissible fade show" role="alert">
        ${sessionScope.success}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>
    <c:remove var="success" scope="session"/>
    </c:if>

    <c:if test="${not empty sessionScope.error}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            ${sessionScope.error}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <c:remove var="error" scope="session"/>
    </c:if>

    <c:if test="${not empty orders}">
        <div class="row g-4">
            <c:forEach var="o" items="${orders}">
                <div class="col-12 col-md-6 col-lg-4">
                    <div class="card shadow-sm h-100">
                        <img src="image?id=${o.imageId}" class="card-img-top" style="height: 200px; object-fit: cover;" alt="Warehouse Image"/>
                        <div class="card-body">
                            <h5 class="card-title text-primary fw-bold">${o.warehouseName}</h5>
                            <p class="mb-1"><i class="bi bi-geo-alt-fill text-danger"></i>
                                ${o.warehouseAddress}
                            </p>
                            <p class="mb-1"><strong>Khách hàng:</strong> ${o.customerName}</p>
                            <p class="mb-1"><strong>Email:</strong> ${o.customerEmail}</p>
                            <p class="mb-1"><strong>Thời gian:</strong> 
                                <fmt:formatDate value="${o.startDate}" pattern="dd/MM/yyyy"/> → 
                                <fmt:formatDate value="${o.endDate}" pattern="dd/MM/yyyy"/>
                            </p>
                            <p class="mb-1"><strong>Đặt cọc:</strong> 
                                <fmt:formatNumber value="${o.deposit}" type="currency" currencySymbol="₫"/>
                            </p>
                            <p class="mb-1"><strong>Tổng thanh toán:</strong> 
                                <fmt:formatNumber value="${o.totalPrice}" type="currency" currencySymbol="₫"/>
                            </p>

                            <!-- Trạng thái thanh toán -->
                            <p class="mt-2 mb-1">
                                <span class="badge bg-${o.depositPaid ? 'success' : 'secondary'}">
                                    ${o.depositPaid ? '✅ Đã đặt cọc' : '❌ Chưa đặt cọc'}
                                </span>
                                <span class="badge bg-${o.finalPaid ? 'success' : 'secondary'}">
                                    ${o.finalPaid ? '✅ Đã thanh toán' : '❌ Chưa thanh toán'}
                                </span>
                            </p>

                            <!-- Thời gian thuê còn lại -->
                            <p class="mb-1">
                                <c:choose>
                                    <c:when test="${o.daysUntilEndDate < 0}">
                                        <span class="badge bg-danger">⏰ Quá hạn thuê ${-o.daysUntilEndDate} ngày</span>
                                    </c:when>
                                    <c:when test="${o.daysUntilEndDate <= 14}">
                                        <span class="badge bg-warning text-dark">⚠️ Còn ${o.daysUntilEndDate} ngày thuê</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-info text-dark">🕓 Còn ${o.daysUntilEndDate} ngày thuê</span>
                                    </c:otherwise>
                                </c:choose>
                            </p>

                            <!-- Hạn thanh toán tổng -->
                            <p class="mb-3">
                                <c:if test="${!o.finalPaid}">
                                    <c:choose>
                                        <c:when test="${o.daysUntilFinalPaymentDue < 0}">
                                            <span class="badge bg-danger">⏰ Quá hạn thanh toán ${-o.daysUntilFinalPaymentDue} ngày</span>
                                        </c:when>
                                        <c:when test="${o.daysUntilFinalPaymentDue == 0}">
                                            <span class="badge bg-warning text-dark">⚠️ Thanh toán hôm nay</span>
                                        </c:when>
                                        <c:when test="${o.daysUntilFinalPaymentDue <= 14}">
                                            <span class="badge bg-warning text-dark">⚠️ Còn ${o.daysUntilFinalPaymentDue} ngày để thanh toán</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-info text-dark">💰 Còn ${o.daysUntilFinalPaymentDue} ngày</span>
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                            </p>

                            <!-- Nút hành động -->
                            <div class="d-flex justify-content-between">
                                <form action="admin/send-reminder" method="post">
                                    <input type="hidden" name="id" value="${o.rentalOrderID}" />
                                    <button type="submit" class="btn btn-outline-warning btn-circle"
                                            title="Gửi nhắc nhở"
                                            onclick="return confirm('Bạn có chắc chắn muốn gửi email nhắc nhở khách hàng?')">
                                        <i class="bi bi-envelope-fill"></i>
                                    </button>
                                </form>
                                <form action="cancel-rental" method="post"
                                      onsubmit="return confirm('Bạn có chắc muốn hủy đơn thuê này?');">
                                    <input type="hidden" name="rentalOrderId" value="${o.rentalOrderID}" />
                                    <button type="submit" class="btn btn-outline-danger btn-circle" title="Hủy đơn">
                                        <i class="bi bi-x-circle-fill"></i>
                                    </button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </c:if>

    <c:if test="${empty orders}">
        <div class="alert alert-warning text-center mt-4">
            <i class="bi bi-exclamation-triangle-fill"></i> Không có đơn thuê nào được duyệt.
        </div>
    </c:if>
</div>

<jsp:include page="footer.jsp"/>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
