<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đã xảy ra lỗi</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .error-container {
            max-width: 600px;
            margin: 80px auto;
            padding: 40px;
            background-color: #fff;
            border-radius: 12px;
            box-shadow: 0 0 20px rgba(0,0,0,0.1);
            text-align: center;
        }
        .error-icon {
            font-size: 60px;
            color: #dc3545;
        }
    </style>
</head>
<body>
<<<<<<< HEAD
    <jsp:include page="header.jsp"/>
=======
>>>>>>> 65f61a6 (rental history)
<div class="error-container">
    <div class="error-icon">⚠️</div>
    <h2 class="text-danger mt-3">Đã xảy ra lỗi</h2>
    <p class="lead text-muted">
        <c:out value="${error != null ? error : 'Có lỗi xảy ra trong quá trình xử lý. Vui lòng thử lại sau.'}" />
    </p>
    <a href="index.jsp" class="btn btn-outline-primary mt-4">← Quay lại trang chủ</a>
</div>
<<<<<<< HEAD
<jsp:include page="footer.jsp"/>
=======

>>>>>>> 65f61a6 (rental history)
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
v