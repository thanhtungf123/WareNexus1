<%@ page contentType="text/html; charset=UTF-8" %>
<%
    int rentalOrderId = Integer.parseInt(request.getParameter("rentalOrderId"));
    String errorMessage = (String) request.getAttribute("errorMessage");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Confirm Password</title>

    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="css/style.css" />

    <style>
        body {
            font-family: 'Poppins', sans-serif;
            background-color: #f7f9fc;
        }

        /* Card styling */
        .card {
            border-radius: 12px;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
        }

        /* Card header */
        .card-header {
            background-color: #004d99; /* Dark blue background for title */
            color: white;
            border-radius: 12px 12px 0 0;
            padding: 20px;
        }

        .card-body {
            background-color: white;
            padding: 30px;
            border-radius: 0 0 12px 12px;
        }

        /* Button style */
        .btn-primary {
            background-color: #007bff; /* Lighter blue */
            border-color: #007bff;
            font-size: 14px;
            padding: 8px 16px;
            width: 100%;
            border-radius: 8px;
        }

        .btn-primary:hover {
            background-color: #0056b3; /* Darker blue on hover */
            border-color: #0056b3;
        }

        /* Input fields */
        .form-control {
            border-radius: 8px;
            border: 1px solid #ccc;
            font-size: 14px;
            padding: 12px;
        }

        .form-group label {
            font-weight: 500;
            font-size: 14px;
        }

        /* Error message */
        .alert-danger {
            background-color: #f8d7da;
            border-color: #f5c6cb;
            color: #721c24;
            border-radius: 8px;
            padding: 10px;
            margin-bottom: 20px;
        }

        .btn-back {
            border: none; color: white; border-radius: 0.5rem;
            padding: 0.75rem 2rem; font-weight: 600; transition: all 0.3s ease;
            text-decoration: none; display: inline-flex; align-items: center; gap: 0.5rem;
            background: #6b7280;
        }
        .btn-back:hover {
            background: #333f57;
            transform: translateY(-2px); box-shadow: 0 4px 12px rgb(0 0 0 / 0.15); color: white;
        }

        /* Center the form vertically */
        .container {
            justify-content: center;
            align-items: center;
        }
    </style>
</head>
<body>

    <jsp:include page="header.jsp"/> <!-- Include common header -->

    <div class="container my-5">
        <div class="row justify-content-center">
            <div class="col-md-4">
                <div class="card shadow-sm">
                    <div class="card-header text-center">
                        <h4>Confirm Your Password</h4>
                    </div>
                    <div class="card-body">
                        <h6 class="text-center mb-4">Please enter your password to proceed with signing the contract</h6>

                        <% if (errorMessage != null) { %>
                            <div class="alert alert-danger">
                                <%= errorMessage %>
                            </div>
                        <% } %>

                        <form action="verifyPassword" method="post">
                            <div class="mb-3">
                                <label for="password" class="form-label">Password:</label>
                                <input type="password" name="password" id="password" class="form-control" required />
                                <input type="hidden" name="rentalOrderId" value="<%= rentalOrderId %>" />
                            </div>
                            <button type="submit" class="btn btn-primary">Verify Password</button>
                        </form>
                    </div>
                </div>
            </div>
            <div class="d-flex gap-3 flex-wrap">
                <button onclick="goBack()" class="btn-back">Quay lại</button>
            </div>
        </div>
    </div>

    <jsp:include page="footer.jsp"/> <!-- Include common footer -->

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function goBack() {
            window.history.back();
        }
    </script>
</body>
</html>
