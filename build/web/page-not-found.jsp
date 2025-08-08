<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>404 - Không tìm thấy trang</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f2f4f8;
            display: flex;
            align-items: center;
            justify-content: center;
            height: 100vh;
        }
        .error-box {
            text-align: center;
            background-color: #fff;
            padding: 50px;
            border-radius: 12px;
            box-shadow: 0 0 25px rgba(0,0,0,0.1);
        }
        .error-code {
            font-size: 100px;
            font-weight: bold;
            color: #dc3545;
        }
        .error-message {
            font-size: 24px;
            margin-bottom: 20px;
            color: #6c757d;
        }
    </style>
</head>
<body>
<div class="error-box">
    <div class="error-code">404</div>
    <div class="error-message">Oops! Không tìm thấy trang bạn yêu cầu.</div>
    <a href="index.jsp" class="btn btn-outline-primary btn-lg">← Quay lại trang chủ</a>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
