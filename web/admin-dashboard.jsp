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
            transition: 0.3s;
            box-shadow: 0 8px 20px rgba(0,0,0,0.1);
        }
    </style>
</head>
<body>

<jsp:include page="header.jsp" />

<div class="container mt-4 mb-5">
    <div class="dashboard-header">
        <h2>Welcome, Admin <%= user.getEmail() %></h2>
        <p>You are accessing the administrative panel of WareNexus</p>
    </div>

    <div class="row g-4">
        <!-- Rental Orders -->
        <div class="col-md-6 col-lg-4">
            <a href="admin-approve-list.jsp" class="text-decoration-none">
                <div class="card card-hover text-center p-4">
                    <h4>Rental Orders</h4>
                    <p class="text-muted">View and manage customer rental orders.</p>
                </div>
            </a>
        </div>

        <!-- Payment Confirmation -->
        <div class="col-md-6 col-lg-4">
            <a href="admin-payment-list.jsp" class="text-decoration-none">
                <div class="card card-hover text-center p-4">
                    <h4>Payment Confirmation</h4>
                    <p class="text-muted">Verify and confirm deposit or rental payments.</p>
                </div>
            </a>
        </div>

        <!-- Contract Approvals -->
        <div class="col-md-6 col-lg-4">
            <a href="admin-contracts.jsp" class="text-decoration-none">
                <div class="card card-hover text-center p-4">
                    <h4>Contract Approvals</h4>
                    <p class="text-muted">Review and approve digital contracts.</p>
                </div>
            </a>
        </div>

        <!-- Customer Management -->
        <div class="col-md-6 col-lg-4">
            <a href="admin-customers.jsp" class="text-decoration-none">
                <div class="card card-hover text-center p-4">
                    <h4>Customers</h4>
                    <p class="text-muted">View and manage registered customers.</p>
                </div>
            </a>
        </div>

        <!-- Warehouse Management -->
        <div class="col-md-6 col-lg-4">
            <a href="admin-warehouse" class="text-decoration-none">
                <div class="card card-hover text-center p-4">
                    <h4>Warehouses</h4>
                    <p class="text-muted">Manage warehouse listings, availability, and status.</p>
                </div>
            </a>
        </div>

        <!-- Support Tickets -->
        <div class="col-md-6 col-lg-4">
            <a href="admin-support.jsp" class="text-decoration-none">
                <div class="card card-hover text-center p-4">
                    <h4>Support Tickets</h4>
                    <p class="text-muted">Track and resolve user issues or complaints.</p>
                </div>
            </a>
        </div>
    </div>
</div>
<jsp:include page="footer.jsp"/>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
