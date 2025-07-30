<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Verify OTP</title>
    <style>
        /* Styling for OTP input */
    </style>
</head>
<body>
    <div class="otp-container">
        <h2>Verify Your OTP</h2>
        <p>Please enter the OTP sent to your email.</p>

        <form method="post" action="verifyOTP" onsubmit="return validateOTP()">
            <div class="otp-input">
                <input type="text" id="otp1" maxlength="1" required>
                <input type="text" id="otp2" maxlength="1" required>
                <input type="text" id="otp3" maxlength="1" required>
                <input type="text" id="otp4" maxlength="1" required>
                <input type="text" id="otp5" maxlength="1" required>
                <input type="text" id="otp6" maxlength="1" required>
            </div>
            <button type="submit" class="btn">Verify OTP</button>
        </form>

        <p id="errorMessage" style="color: red; display: none;">Invalid OTP. Please try again.</p>
    </div>

    <script>
        function validateOTP() {
            const otp = document.getElementById("otp1").value + document.getElementById("otp2").value +
                        document.getElementById("otp3").value + document.getElementById("otp4").value +
                        document.getElementById("otp5").value + document.getElementById("otp6").value;

            const otpStored = "<%= session.getAttribute('otp') %>"; // Get OTP from session

            if (otp !== otpStored) {
                document.getElementById("errorMessage").style.display = "block"; // Show error message if OTP doesn't match
                return false;
            }

            return true;
        }
    </script>
</body>
</html>
