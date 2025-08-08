<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.warenexus.model.Account" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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
    <title>Rental History - WareNexus</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="css/style.css" />
    <style>
        body {
            background-color: #f4f6f8;
            font-family: 'Segoe UI', sans-serif;
        }
        .history-container {
            max-width: 1000px;
            margin: 60px auto;
            background: #fff;
            border-radius: 12px;
            box-shadow: 0 6px 16px rgba(0,0,0,0.1);
            padding: 40px;
        }
        .history-container h3 {
            color: #1976d2;
            margin-bottom: 30px;
            text-align: center;
        }
        .warning {
            color: #e65100;
            font-weight: bold;
        }
        .table th {
            background-color: #1976d2;
            color: white;
        }
        .btn-pay {
            padding: 6px 12px;
            font-size: 14px;
        }
    </style>
</head>
<body>
<jsp:include page="header.jsp"/>

<div class="container mt-5 mb-5">
    <h2 class="text-primary text-center mb-4">📦 Rental History</h2>

    <c:choose>
        <c:when test="${empty orders}">
            <div class="alert alert-info text-center">You have no approved rental orders at this time.</div>
        </c:when>
        <c:otherwise>
            <table class="table table-bordered text-center align-middle shadow">
                <thead class="table-primary">
                    <tr>
                        <th>#</th>
                        <th>Warehouse</th>
                        <th>Start Date</th>
                        <th>End Date</th>
                        <th>Deposit</th>
                        <th>Total Price</th>
                        <th>Deposit payment</th>
                        <th>Final Payment</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="order" items="${orders}" varStatus="loop">
                        <tr>
                            <td>${loop.index + 1}</td>
                            <td>${warehouseMap[order.rentalOrderID].name}</td>
                            <td><fmt:formatDate value="${order.startDate}" pattern="yyyy-MM-dd"/></td>
                            <td><fmt:formatDate value="${order.endDate}" pattern="yyyy-MM-dd"/></td>
                            <td><fmt:formatNumber value="${order.deposit}" type="currency" currencySymbol="₫"/></td>
                            <td><fmt:formatNumber value="${order.totalPrice}" type="currency" currencySymbol="₫"/></td>                           
                            <td>
                                <c:choose>
                                    <c:when test="${order.depositPaid}">
                                        <span class="text-success">Đã thanh toán cọc</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-danger">Chưa thanh toán cọc</span><br/>
                                        <small class="text-muted">Kho chưa được thuê</small>
                                    </c:otherwise>
                                </c:choose>
                            </td>

                            <td>
                                <c:choose>
                                    <c:when test="${isFinalPaidMap[order.rentalOrderID]}">
                                        <span class="text-success fw-bold">Paid</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-danger fw-bold">Unpaid</span><br/>
                                        <c:if test="${daysLeftMap[order.rentalOrderID] <= 14}">
                                            <small class="text-warning">⚠ ${daysLeftMap[order.rentalOrderID]} day(s) left</small>
                                        </c:if>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <a href="rental-detail?rentalOrderId=${order.rentalOrderID}" class="btn btn-outline-info btn-sm mb-1">Details</a>
                                <c:if test="${!isFinalPaidMap[order.rentalOrderID]}">
                                    <form action="final-payment" method="post" class="d-inline">
                                        <input type="hidden" name="rentalOrderId" value="${order.rentalOrderID}" />
                                        <input type="hidden" name="amount" value="${order.totalPrice}" />
                                        <button type="submit" class="btn btn-primary btn-sm">Pay Now</button>
                                    </form>
                                </c:if>
                                <!-- Contract buttons -->
                                <c:choose>
                                    <%-- File present on server --%>
                                    <c:when test="${contractMap[order.rentalOrderID] != null}">
                                        <a class="btn btn-outline-primary btn-sm mb-1"
                                           href="view-contract?orderId=${order.rentalOrderID}" target="_blank">View&nbsp;PDF</a>
                                        <a class="btn btn-outline-success btn-sm mb-1"
                                           href="download-contract-email?orderId=${order.rentalOrderID}">Download</a>
                                    </c:when>

                                    <%-- File missing – offer re-mail --%>
                                    <c:otherwise>
                                        <a class="btn btn-outline-warning btn-sm mb-1"
                                           href="download-contract-email?orderId=${order.rentalOrderID}">
                                           Email&nbsp;me&nbsp;PDF
                                        </a>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>

    <div class="text-center mt-4">
        <a href="userhome" class="btn btn-secondary">🔙 Back to Home</a>
    </div>
</div>

<jsp:include page="footer.jsp"/>
</body>
</html>
