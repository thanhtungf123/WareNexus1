<%@ page import="com.warenexus.model.Account" %>
<%@ page session="true" %>
<%
    Account user = (Account) session.getAttribute("user");
    if (user == null || user.getRoleId() != 1) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard - WareNexus</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">

    <link href="css/style.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .dashboard-header {
            background: linear-gradient(135deg, #2563eb, #1d4ed8);
            color: white;
            padding: 2rem;
            border-radius: 0.5rem;
            text-align: center;
            margin-bottom: 2rem;
        }
        .card-hover:hover {
            transform: translateY(-4px);
            transition: 0.3s ease;
            box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
        }

        .icon-circle {
            width: 60px;
            height: 60px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
        }
    </style>
</head>
<body>

<jsp:include page="header.jsp" />

<div class="container mt-4 mb-5">
    <!-- Header -->
    <div class="dashboard-header text-white text-center p-4 rounded mb-4" style="background: linear-gradient(135deg, #2563eb, #1d4ed8);">
        <h2 class="fw-bold">Welcome, Admin <%= user.getEmail() %></h2>
        <p class="mb-0">You are accessing the administrative panel of WareNexus</p>
    </div>

    <!-- Dashboard Cards -->
    <div class="row g-4">
        <!-- Rental Orders -->
        <div class="col-md-6 col-lg-4">
            <a href="admin-approve-list.jsp" class="text-decoration-none">
                <div class="card card-hover border-0 shadow-sm h-100 text-center p-4">
                    <div class="icon-circle bg-primary text-white mb-3 mx-auto">
                        <i class="bi bi-journal-text fs-3"></i>
                    </div>
                    <h5 class="fw-bold text-dark">Rental Orders</h5>
                    <p class="text-muted">View and manage customer rental orders.</p>
                </div>
            </a>
        </div>

        <!-- Payment Confirmation -->
        <div class="col-md-6 col-lg-4">
            <a href="comingsoon.jsp" class="text-decoration-none">
                <div class="card card-hover border-0 shadow-sm h-100 text-center p-4">
                    <div class="icon-circle bg-success text-white mb-3 mx-auto">
                        <i class="bi bi-cash-coin fs-3"></i>
                    </div>
                    <h5 class="fw-bold text-dark">Payment Confirmation</h5>
                    <p class="text-muted">Verify and confirm deposit or rental payments.</p>
                </div>
            </a>
        </div>

        <!-- Contract Approvals -->
        <div class="col-md-6 col-lg-4">
            <a href="comingsoon.jsp" class="text-decoration-none">
                <div class="card card-hover border-0 shadow-sm h-100 text-center p-4">
                    <div class="icon-circle bg-warning text-white mb-3 mx-auto">
                        <i class="bi bi-file-earmark-text fs-3"></i>
                    </div>
                    <h5 class="fw-bold text-dark">Contract Approvals</h5>
                    <p class="text-muted">Review and approve digital contracts.</p>
                </div>
            </a>
        </div>

        <!-- Customers -->
        <div class="col-md-6 col-lg-4">
            <a href="admin-customers.jsp" class="text-decoration-none">
                <div class="card card-hover border-0 shadow-sm h-100 text-center p-4">
                    <div class="icon-circle bg-info text-white mb-3 mx-auto">
                        <i class="bi bi-person-lines-fill fs-3"></i>
                    </div>
                    <h5 class="fw-bold text-dark">Customers</h5>
                    <p class="text-muted">View and manage registered customers.</p>
                </div>
            </a>
        </div>

        <!-- Warehouses -->
        <div class="col-md-6 col-lg-4">
            <a href="admin-warehouse" class="text-decoration-none">
                <div class="card card-hover border-0 shadow-sm h-100 text-center p-4">
                    <div class="icon-circle bg-danger text-white mb-3 mx-auto">
                        <i class="bi bi-building fs-3"></i>
                    </div>
                    <h5 class="fw-bold text-dark">Warehouses</h5>
                    <p class="text-muted">Manage warehouse listings, availability, and status.</p>
                </div>
            </a>
        </div>

        <!-- Manage Rentals -->
            <div class="col-md-6 col-lg-4">
                <a href="admin-rental-manage.jsp" class="text-decoration-none">
                    <div class="card card-hover border-0 shadow-sm h-100 text-center p-4">
                        <div class="icon-circle bg-secondary text-white mb-3 mx-auto">
                            <i class="bi bi-box-seam fs-3"></i>
                        </div>
                        <h5 class="fw-bold text-dark">Manage Rentals</h5>
                        <p class="text-muted">Monitor active rentals, send reminders, or cancel overdue ones.</p>
                    </div>
                </a>
            </div>
    </div>

</div>

<jsp:include page="footer.jsp"/>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
