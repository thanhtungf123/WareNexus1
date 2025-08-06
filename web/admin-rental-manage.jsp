<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.*" %>
<%@ page import="com.warenexus.model.*" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin - Rental Management</title>
    <link rel="stylesheet" href="css/style.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<jsp:include page="header.jsp"/>

<div class="container my-5">
    <h2 class="text-primary mb-4 text-center">Rental Order Management</h2>

    <a href="admin-dashboard.jsp" class="btn btn-outline-secondary mb-3">&larr; Back</a>

    <div class="table-responsive">
        <table class="table table-bordered table-hover text-center align-middle">
            <thead class="table-primary">
                <tr>
                    <th>#</th>
                    <th>Customer Name</th>
                    <th>Warehouse ID</th>
                    <th>Start Date</th>
                    <th>End Date</th>
                    <th>Deposit</th>
                    <th>Total Price</th>
                    <th>Final Payment</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${empty orders}">
                        <tr>
                            <td colspan="10" class="text-center">No ongoing rentals found.</td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="order" items="${orders}" varStatus="loop">
                            <tr>
                                <td>${loop.index + 1}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${customers[order.rentalOrderID] != null}">
                                            ${customers[order.rentalOrderID].fullName}
                                        </c:when>
                                        <c:otherwise>
                                            Unknown
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>${order.warehouseID}</td>
                                <td>
                                    <fmt:formatDate value="${order.startDate}" pattern="dd/MM/yyyy"/>
                                </td>
                                <td>
                                    <fmt:formatDate value="${order.endDate}" pattern="dd/MM/yyyy"/>
                                </td>
                                <td>
                                    <fmt:formatNumber value="${order.deposit}" type="currency" currencySymbol="₫"/>
                                </td>
                                <td>
                                    <fmt:formatNumber value="${order.totalPrice}" type="currency" currencySymbol="₫"/>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${finalPayments[order.rentalOrderID]}">
                                            ✅ Paid
                                        </c:when>
                                        <c:otherwise>
                                            ❌ Not Yet
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    ${order.status}
                                </td>
                                <td>
                                    <!-- Bạn có thể thêm nút xem chi tiết hoặc hủy đơn -->
                                    <a href="rental-detail?rentalOrderId=${order.rentalOrderID}" class="btn btn-sm btn-outline-info">View</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>
</div>

<jsp:include page="footer.jsp"/>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
