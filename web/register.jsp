<%-- 
    Document   : signup
    Created on : Jun 18, 2025, 4:38:56 PM
    Author     : HP Victus
--%>

<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sign Up - WareNexus</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
            background: linear-gradient(135deg, #eff6ff 0%, #dbeafe 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 2rem;
        }

        .signup-container {
            background: white;
            border-radius: 1rem;
            box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
            overflow: hidden;
            max-width: 500px;
            width: 100%;
        }

        .signup-header {
            background: linear-gradient(135deg, #2563eb, #1d4ed8);
            color: white;
            padding: 2rem;
            text-align: center;
        }

        .logo {
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 0.5rem;
            font-size: 1.5rem;
            font-weight: bold;
            margin-bottom: 1rem;
        }

        .logo img {
            width: 32px;
            height: 32px;
        }

        .signup-header h1 {
            font-size: 1.8rem;
            font-weight: 700;
            margin-bottom: 0.5rem;
        }

        .signup-header p {
            opacity: 0.9;
            font-size: 0.95rem;
        }

        .signup-form {
            padding: 2rem;
        }

        .form-group {
            margin-bottom: 1.5rem;
        }

        .form-group label {
            display: block;
            margin-bottom: 0.5rem;
            font-weight: 600;
            color: #374151;
            font-size: 0.9rem;
        }

        .form-group input,
        .form-group select {
            width: 100%;
            padding: 0.75rem;
            border: 2px solid #e5e7eb;
            border-radius: 0.5rem;
            font-size: 1rem;
            transition: all 0.3s;
            background: white;
        }

        .form-group input:focus,
        .form-group select:focus {
            outline: none;
            border-color: #2563eb;
            box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.1);
        }

        .form-group input::placeholder {
            color: #9ca3af;
        }

        .radio-group {
            display: flex;
            gap: 1.5rem;
            margin-top: 0.5rem;
        }

        .radio-option {
            display: flex;
            align-items: center;
            gap: 0.5rem;
        }

        .radio-option input[type="radio"] {
            width: auto;
            margin: 0;
        }

        .radio-option label {
            margin: 0;
            font-weight: 500;
            cursor: pointer;
        }

        .company-field {
            display: none;
            margin-top: 1rem;
            padding: 1rem;
            background: #f8fafc;
            border-radius: 0.5rem;
            border: 1px solid #e2e8f0;
        }

        .company-field.show {
            display: block;
            animation: slideDown 0.3s ease-out;
        }

        @keyframes slideDown {
            from {
                opacity: 0;
                transform: translateY(-10px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        .btn {
            width: 100%;
            padding: 0.875rem;
            background: linear-gradient(135deg, #2563eb, #1d4ed8);
            color: white;
            border: none;
            border-radius: 0.5rem;
            font-size: 1rem;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s;
            margin-top: 1rem;
        }

        .btn:hover {
            background: linear-gradient(135deg, #1d4ed8, #1e40af);
            transform: translateY(-1px);
            box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1);
        }

        .btn:active {
            transform: translateY(0);
        }

        .back-link {
            text-align: center;
            margin-top: 1.5rem;
        }

        .back-link a {
            color: #2563eb;
            text-decoration: none;
            font-weight: 500;
            font-size: 0.9rem;
        }

        .back-link a:hover {
            text-decoration: underline;
        }

        .required {
            color: #ef4444;
        }

        .success-message {
            display: none;
            background: #f0fdf4;
            border: 1px solid #bbf7d0;
            color: #166534;
            padding: 1rem;
            border-radius: 0.5rem;
            margin-bottom: 1rem;
            text-align: center;
        }

        .success-message.show {
            display: block;
            animation: slideDown 0.3s ease-out;
        }

        .otp-section {
            display: none;
            background: #fefce8;
            border: 1px solid #fde047;
            padding: 1.5rem;
            border-radius: 0.5rem;
            margin-top: 1rem;
            text-align: center;
        }

        .otp-section.show {
            display: block;
            animation: slideDown 0.3s ease-out;
        }

        .otp-section h3 {
            color: #a16207;
            margin-bottom: 0.5rem;
        }

        .otp-section p {
            color: #a16207;
            font-size: 0.9rem;
            margin-bottom: 1rem;
        }

        .otp-input {
            display: flex;
            gap: 0.5rem;
            justify-content: center;
            margin-bottom: 1rem;
        }

        .otp-input input {
            width: 3rem !important;
            height: 3rem;
            text-align: center;
            font-size: 1.2rem;
            font-weight: bold;
        }
        #otpForm {
            display: none;
            margin-top: 1.5rem;
            padding: 1rem;
            background: #f9fbfc;
            border: 1px solid #e0e6ed;
            border-radius: 6px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.05);
        }

        #otpForm .form-group {
            margin-bottom: 1rem;
        }

        #otpForm label {
            display: block;
            margin-bottom: 0.5rem;
            font-weight: 600;
            color: #333;
        }

        #otpForm input[type="text"] {
            width: 100%;
            padding: 0.5rem 0.75rem;
            border: 1px solid #ccc;
            border-radius: 4px;
            font-size: 1rem;
        }

        #otpForm button[type="submit"] {
            width: 100%;
            padding: 0.6rem 1rem;
            background-color: #28a745;
            color: white;
            font-weight: bold;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        #otpForm button[type="submit"]:hover {
            background-color: #218838;
        }


        @media (max-width: 640px) {
            .signup-container {
                margin: 1rem;
            }

            .signup-form {
                padding: 1.5rem;
            }

            .radio-group {
                flex-direction: column;
                gap: 1rem;
            }

            .otp-input input {
                width: 2.5rem !important;
                height: 2.5rem;
            }
        }
    </style>
</head>
<body>
<div class="signup-container">
    <div class="signup-header">
        <div class="logo">
            <img src="${pageContext.request.contextPath}/images/warehouse.png" alt="WareNexus Logo">
            WareNexus
        </div>
        <h1>Create Your Account</h1>
        <p>Join thousands of businesses using our warehouse solutions</p>
    </div>

    <div class="signup-form">
        <c:if test="${not empty error}">
            <div class="error-message" style="color:red;text-align:center;margin-bottom:1rem;">${error}</div>
        </c:if>

        <form method="post" action="register" onsubmit="showOtpForm();">
            <div class="form-group">
                <label for="email">Email Address <span class="required">*</span></label>
                <input type="email" id="email" name="email" value="${email}" required>
            </div>

            <div class="form-group">
                <label for="password">Password <span class="required">*</span></label>
                <input type="password" id="password" name="password" value="${password}" required>
            </div>

            <div class="form-group">
                <label for="fullName">Full Name <span class="required">*</span></label>
                <input type="text" id="fullName" name="fullName" value="${fullName}" required>
            </div>

            <div class="form-group">
                <label for="phone">Phone Number</label>
                <input type="tel" id="phone" name="phone" value="${phone}">
            </div>

            <div class="form-group">
                <label>Account Type <span class="required">*</span></label>
                <div class="radio-group">
                    <div class="radio-option">
                        <input type="radio" id="individual" name="type" value="individual" checked>
                        <label for="individual">Individual</label>
                    </div>
                    <div class="radio-option">
                        <input type="radio" id="company" name="type" value="company">
                        <label for="company">Company</label>
                    </div>
                </div>
            </div>

            <div id="individualFields">
                <div class="form-group">
                    <label>Address</label>
                    <input type="text" name="address">
                </div>
                <div class="form-group">
                    <label>Citizen ID</label>
                    <input type="text" name="citizenId">
                </div>
                <div class="form-group">
                    <label>Tax Code</label>
                    <input type="text" name="taxCode">
                </div>
                <div class="form-group">
                    <label>Date of Birth</label>
                    <input type="date" name="dob">
                </div>
                <div class="form-group">
                    <label>ID Issue Date</label>
                    <input type="date" name="issueDate">
                </div>
                <div class="form-group">
                    <label>ID Issue Place</label>
                    <input type="text" name="issuePlace">
                </div>
            </div>

            <div id="companyFields" style="display:none">
                <div class="form-group">
                    <label>Company Name</label>
                    <input type="text" name="companyName">
                </div>
                <div class="form-group">
                    <label>Company Address</label>
                    <input type="text" name="companyAddress">
                </div>
                <div class="form-group">
                    <label>Company Tax Code</label>
                    <input type="text" name="companyTax">
                </div>
                <div class="form-group">
                    <label>Legal Representative Name</label>
                    <input type="text" name="repName">
                </div>
                <div class="form-group">
                    <label>Legal Rep. ID Number</label>
                    <input type="text" name="repId">
                </div>
                <div class="form-group">
                    <label>Contact Phone</label>
                    <input type="text" name="repPhone">
                </div>
                <div class="form-group">
                    <label>Contact Email</label>
                    <input type="email" name="repEmail">
                </div>
            </div>

            <button type="submit" class="btn">Create Account</button>
        </form>

        <!-- Form nhập OTP -->
        <form id="otpForm" method="post" action="verify-otp" style="display:none; margin-top:2rem;">
            <div class="form-group">
                <label>Nhập mã OTP đã gửi vào email</label>
                <input type="text" name="otp" required />
            </div>
            <button type="submit">Xác nhận OTP</button>
        </form>

        <div class="back-link">
            <a href="warehouse.jsp">← Back to Homepage</a>
        </div>
    </div>
</div>

<script>
    const individualRadio = document.getElementById('individual');
    const companyRadio = document.getElementById('company');
    const companyFields = document.getElementById('companyFields');
    const individualFields = document.getElementById('individualFields');

    function toggleFields() {
        if (companyRadio.checked) {
            companyFields.style.display = 'block';
            individualFields.style.display = 'none';
        } else {
            companyFields.style.display = 'none';
            individualFields.style.display = 'block';
        }
    }

    individualRadio.addEventListener('change', toggleFields);
    companyRadio.addEventListener('change', toggleFields);

    window.onload = function() {
            const showOtp = '${showOtpForm}';
            if (showOtp === 'true') {
                document.getElementById("otpForm").style.display = "block";
            }
        };
</script>
</body>
</html>