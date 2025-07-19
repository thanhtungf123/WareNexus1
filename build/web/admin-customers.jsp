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
                                <form method="post" action="admin-delete-customer" onsubmit="return confirm('Are you sure you want to delete this customer?');">
                                    <input type="hidden" name="accountId" value="<%= c.getAccountId() %>">
                                    <button type="submit" class="btn-delete">Delete</button>
                                </form>
                            </td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>
    </div>
</div>

<jsp:include page="footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
