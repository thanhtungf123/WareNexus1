<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Warehouse Reviews</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="container mt-5">
    <h3 class="mb-4">Customer Reviews</h3>
    <c:forEach var="review" items="${reviews}">
        <div class="card mb-3">
            <div class="card-body">
                <strong>Rating:</strong> ${review.rating}/5<br/>
                <strong>Comment:</strong> ${review.comment}<br/>
                <small class="text-muted">Reviewed on: ${review.reviewDate}</small>
            </div>
        </div>
    </c:forEach>
</div>
</body>
</html>
