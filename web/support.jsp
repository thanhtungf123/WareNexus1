<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Support Tickets</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="container mt-5">
    <h3 class="mb-4">Support Tickets</h3>
    <form method="post" action="support">
        <input type="hidden" name="rentalOrderID" value="${rentalOrderID}">
        <div class="mb-3">
            <label>Issue Title</label>
            <input type="text" class="form-control" name="title" required>
        </div>
        <div class="mb-3">
            <label>Issue Description</label>
            <textarea class="form-control" name="description" required></textarea>
        </div>
        <button type="submit" class="btn btn-primary">Submit Ticket</button>
    </form>

    <hr/>
    <h5>Previous Tickets</h5>
    <c:forEach var="t" items="${tickets}">
        <div class="card my-2">
            <div class="card-body">
                <strong>${t.issueTitle}</strong><br/>
                ${t.issueDescription}<br/>
                Status: <span class="badge bg-info">${t.status}</span><br/>
                Created: ${t.createdAt}
            </div>
        </div>
    </c:forEach>
</div>
</body>
</html>
