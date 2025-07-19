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
<html>
<head>
    <title>Pay Deposit - WareNexus</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

<jsp:include page="header.jsp"/>

<div class="container my-5">
    <div class="bg-white shadow p-4 rounded text-center">
        <h3 class="text-primary mb-4">Scan the QR Code to Pay Deposit</h3>

        <c:choose>
            <c:when test="${not empty qrUrl}">
                <img src="${qrUrl}" alt="QR Code PayOS" class="img-fluid mb-4" style="max-width: 280px;">
            </c:when>
            <c:otherwise>
                <div class="alert alert-danger">Unable to display QR code. Please try again.</div>
            </c:otherwise>
        </c:choose>

        <p class="mb-4">Once the payment is completed, click the button below to proceed to contract signing.</p>

        <form action="payos-return" method="get">
            <input type="hidden" name="rentalOrderId" value="${rentalOrderId}" />
            <input type="hidden" name="status" value="PAID" />
            <button type="submit" class="btn btn-success px-4">I Have Paid</button>
        </form>
    </div>
</div>

<jsp:include page="footer.jsp"/>

</body>
</html>
