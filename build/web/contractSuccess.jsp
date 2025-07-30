<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hợp Đồng Thành Công</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="css/style.css" />
</head>
<body>
    <!-- Include header -->
    <jsp:include page="header.jsp" />

    <!-- Nội dung thông báo thành công -->
    <div class="container my-5">
        <!-- Hiển thị thông báo thành công -->
        <div class="alert alert-success text-center">
            <h3>Hợp đồng đã được ký thành công!</h3>
        </div>

        <!-- Nút quay về trang chủ -->
        <div class="text-center mt-3">
            <a href="userhome" class="btn btn-primary">Quay về trang chủ</a>
        </div>
    </div>

    <!-- Include footer -->
    <jsp:include page="footer.jsp" />
</body>
</html>
