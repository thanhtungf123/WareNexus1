<%@ page import="java.util.List" %>
<%@ page import="com.warenexus.model.Customer" %>
<%@ page import="com.warenexus.dao.CustomerDAO" %>
<%@ page import="com.warenexus.model.Account" %>
<%
    Account user = (Account) session.getAttribute("user");
    if (user == null || user.getRoleId() != 1) {
        response.sendRedirect("login.jsp");
        return;
    }

    CustomerDAO customerDAO = new CustomerDAO();
    List<Customer> customers = customerDAO.getAllCustomers();
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Admin - Customer Management</title>
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
        .table th, .table td {
            vertical-align: middle;
        }
        .btn-delete {
            color: white;
            background-color: #dc3545;
            border: none;
            padding: 5px 12px;
            border-radius: 5px;
            transition: 0.3s;
        }
        .btn-delete:hover {
            background-color: #bb2d3b;
        }
    </style>
</head>
<body>

<jsp:include page="header.jsp" />

<div class="container mt-4 mb-5">
    <div class="dashboard-header">
        <h2>Customer Management</h2>
        <p>Admin Panel - View and manage registered customers</p>
    </div>
    <!-- Back Button -->
    <div class="mb-3">
        <button class="btn btn-outline-secondary" onclick="history.back()">
            <i class="bi bi-arrow-left-circle me-1"></i> Back
        </button>
    </div>
    <div class="card shadow-sm">
        <div class="card-body">
            <table class="table table-bordered table-hover table-striped align-middle mb-0">
                <thead class="table-primary text-center">
                    <tr>
                        <th>#</th>
                        <th>Full Name</th>
                        <th>Email</th>
                        <th>Phone</th>
                        <th>Status</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (Customer c : customers) { %>
                        <tr>
                            <td class="text-center"><%= c.getAccountId() %></td>
                            <td><%= c.getFullName() %></td>
                            <td><%= c.getEmail() %></td>
                            <td><%= c.getPhone() %></td>
                            <td class="text-center">
                                <span class="badge bg-<%= c.isActive() ? "success" : "secondary" %>">
                                    <%= c.isActive() ? "Active" : "Inactive" %>
                                </span>
                            </td>
                            <td class="text-center">
                                <button type="button" class="btn btn-sm btn-danger" data-bs-toggle="modal" data-bs-target="#deleteModal_<%= c.getAccountId() %>">
                                    <i class="bi bi-trash"></i> Delete
                                </button>
                            </td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
    <% for (Customer c : customers) { %>
    <!-- Modal: Confirm Delete -->
    <div class="modal fade" id="deleteModal_<%= c.getAccountId() %>" tabindex="-1" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header bg-danger text-white">
                    <h5 class="modal-title"><i class="bi bi-exclamation-triangle me-2"></i>Confirm Deletion</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    Are you sure you want to delete customer <strong><%= c.getFullName() %></strong>?
                </div>
                <div class="modal-footer">
                    <form method="post" action="admin-delete-customer">
                        <input type="hidden" name="accountId" value="<%= c.getAccountId() %>">
                        <button type="submit" class="btn btn-danger">
                            <i class="bi bi-trash"></i> Yes, Delete
                        </button>
                    </form>
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                </div>
            </div>
        </div>
    </div>
    <% } %>

</div>

<jsp:include page="footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
