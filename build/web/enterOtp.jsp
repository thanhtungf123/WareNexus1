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
    <title>Enter OTP - WareNexus</title>

    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="css/style.css" />
    <style>
        body {
            font-family: 'Poppins', sans-serif;
            background-color: #f7f9fc;
        }

        .card {
            border-radius: 12px;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
        }

        .card-header {
            background-color: #0056b3;
            color: white;
            border-radius: 12px 12px 0 0;
        }

        .card-body {
            background-color: white;
            padding: 30px;
            border-radius: 0 0 12px 12px;
        }

        .btn-primary {
            background-color: #007bff;
            border-color: #007bff;
            font-size: 14px;
            padding: 8px 16px;
            width: 100%;
            border-radius: 8px;
        }

        .btn-primary:hover {
            background-color: #0056b3;
            border-color: #0056b3;
        }

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

        .container {
            justify-content: center;
            align-items: center;
        }
    </style>
</head>
<body>

<jsp:include page="header.jsp"/>

<div class="container my-5">
    <div class="row justify-content-center">
        <div class="col-md-4">
            <div class="card">
                <div class="card-header text-center">
                    <h5>Enter OTP</h5>
                </div>
                <div class="card-body">
                    <div class="text-center mb-3">
                        <span id="countdown" class="fw-bold text-danger">10:00</span>
                    </div>
                    <h6 class="text-center mb-4">Please enter the OTP sent to your email to proceed with contract signing</h6>

                    <% if (errorMessage != null) { %>
                    <div class="alert alert-danger">
                        <%= errorMessage %>
                    </div>
                    <% } %>

                    <form action="verifyOTP" method="POST">
                        <div class="mb-3">
                            <label for="otp" class="form-label">OTP:</label>
                            <input type="text" name="otp" id="otp" class="form-control" required />
                            <input type="hidden" name="rentalOrderId" value="<%= rentalOrderId %>" />
                        </div>
                        <button type="submit" class="btn btn-primary">Verify OTP</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <div class="d-flex gap-3 flex-wrap justify-content-center mt-4">
        <button onclick="goBack()" class="btn-back">🔙 Go Back</button>
    </div>
</div>

<jsp:include page="footer.jsp"/>
</body>
<script>
    function goBack() {
        window.history.back();
    }
    window.onload = function () {
        let timeLeft = 600; // 10 phút = 600 giây
        const countdownElement = document.getElementById('countdown');

        if (!countdownElement) {
            console.error("Countdown element not found.");
            return;
        }

        // Hiển thị thời gian ban đầu
        countdownElement.textContent = "10:00";

        const timer = setInterval(() => {
            if (timeLeft >= 0) {
                const minutes = Math.floor(timeLeft / 60);
                const seconds = timeLeft % 60;
                countdownElement.textContent = minutes + ":" + seconds.toString().padStart(2, '0');
                timeLeft--;
            } else {
                clearInterval(timer);
                countdownElement.textContent = "Expired";
                const submitBtn = document.querySelector("form button[type='submit']");
                if (submitBtn) submitBtn.disabled = true;
                alert("OTP has expired. Please request a new one.");
                window.history.back(); // 🔙 Quay lại trang
            }
        }, 1000);
    };
</script>

</html>
