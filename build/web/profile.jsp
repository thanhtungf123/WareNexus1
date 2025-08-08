<%@ page import="com.warenexus.model.Account" %>
<%@ page import="com.warenexus.model.Customer" %>
<%@ page import="com.warenexus.dao.CustomerDAO" %>
<%@ page import="java.util.List" %>
<%
    Customer customer = (Customer) request.getAttribute("customer");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>View Profile - WareNexus</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="css/style.css" rel="stylesheet">
</head>
<body>

<jsp:include page="header.jsp" />

<div class="container mt-5 mb-5">
    <div class="card shadow-sm">
        <div class="card-header bg-primary text-white d-flex align-items-center justify-content-between">
            <h4 class="mb-0"><i class="bi bi-person-circle me-2"></i>Your Profile</h4>
            <a href="javascript:history.back()" class="btn btn-outline-light btn-sm">
                <i class="bi bi-arrow-left-circle"></i> Back
            </a>
        </div>

        <div class="card-body">
            <div class="row mb-4">
                <div class="col-md-3 text-center">
                    <img src="https://cdn-icons-png.flaticon.com/512/6275/6275760.png" alt="Avatar" class="rounded-circle shadow-sm" style="width: 100px; height: 100px;">
                </div>
                <div class="col-md-9">
                    <table class="table table-borderless">
                        <tr>
                            <th class="text-muted">Full Name</th>
                            <td><%= customer.getFullName() %></td>
                        </tr>
                        <tr>
                            <th class="text-muted">Email</th>
                            <td><%= customer.getEmail() %></td>
                        </tr>
                        <tr>
                            <th class="text-muted">Phone</th>
                            <td><%= customer.getPhone() != null ? customer.getPhone() : "Not provided" %></td>
                        </tr>
                        <tr>
                            <th class="text-muted">Status</th>
                            <td>
                <span class="badge bg-<%= customer.isActive() ? "success" : "secondary" %>">
                  <%= customer.isActive() ? "Active" : "Inactive" %>
                </span>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>

        <div class="card-footer text-end">
            <button class="btn btn-warning me-2" data-bs-toggle="modal" data-bs-target="#editProfileModal">
                <i class="bi bi-pencil-square"></i> Edit Profile
            </button>
            <a href="change-password.jsp" class="btn btn-outline-primary">
                <i class="bi bi-key"></i> Change Password
            </a>
        </div>
    </div>
    <div class="modal fade" id="editProfileModal" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <form action="update-profile" method="post" class="modal-content">
                <div class="modal-header bg-warning text-white">
                    <h5 class="modal-title"><i class="bi bi-pencil-square me-2"></i>Edit Profile</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>

                <div class="modal-body px-4 py-3">
                    <input type="hidden" name="accountId" value="<%= customer.getAccountId() %>">
                    <div class="mb-3">
                        <label class="form-label">Full Name</label>
                        <input name="fullName" value="<%= customer.getFullName() %>" class="form-control" required minlength="2" maxlength="50">
                        <div class="invalid-feedback">Full name must be between 2 and 50 characters.</div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Phone</label>
                        <input name="phone" class="form-control" value="<%= customer.getPhone() != null ? customer.getPhone() : "" %>" required>
                        <div class="invalid-feedback">Phone must be 10 or 11 digits.</div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Email</label>
                        <input name="email" type="email" value="<%= customer.getEmail() != null ? customer.getEmail() : "" %>" class="form-control" required>
                        <div class="invalid-feedback">Please enter a valid email address.</div>
                    </div>


                </div>

                <div class="modal-footer bg-light">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-success">
                        <i class="bi bi-check-circle"></i> Save Changes
                    </button>
                </div>
            </form>
        </div>
    </div>

</div>


<jsp:include page="footer.jsp" />
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
</body>
<script>
    document.addEventListener("DOMContentLoaded", function () {
        const form = document.querySelector("#editProfileModal form");

        form.addEventListener("submit", function (e) {
            let isValid = true;

            // Full Name
            const fullName = form.fullName;
            const fullNameVal = fullName.value.trim();
            if (fullNameVal.length < 2 || fullNameVal.length > 50) {
                fullName.classList.add("is-invalid");
                isValid = false;
            } else {
                fullName.classList.remove("is-invalid");
            }

            // Email
            const email = form.email;
            const emailVal = email.value.trim();
            const emailRegex = /^[\w.-]+@[\w.-]+\.\w{2,}$/;
            if (!emailRegex.test(emailVal)) {
                email.classList.add("is-invalid");
                isValid = false;
            } else {
                email.classList.remove("is-invalid");
            }

            // Phone
            const phone = form.phone;
            const phoneVal = phone.value.trim();
            if (phoneVal && !/^\d{10,11}$/.test(phoneVal)) {
                phone.classList.add("is-invalid");
                isValid = false;
            } else {
                phone.classList.remove("is-invalid");
            }

            if (!isValid) {
                e.preventDefault();
            }
        });

        // Xóa lỗi khi người dùng nhập lại
        ["fullName", "email", "phone"].forEach(name => {
            const input = form[name];
            input.addEventListener("input", () => {
                input.classList.remove("is-invalid");
            });
        });
    });
</script>


</html>
