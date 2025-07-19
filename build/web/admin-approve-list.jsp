<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="com.warenexus.model.Account" %>
<%@ page import="com.warenexus.dao.RentalOrderDAO, com.warenexus.model.RentalOrder" %>
<%
    if (session == null || session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    Account user = (Account) session.getAttribute("user");
    if (user.getRoleId() != 1) {
        response.sendRedirect("login.jsp");
        return;
    }

    RentalOrderDAO dao = new RentalOrderDAO();
    List<RentalOrder> list = dao.getPendingApprovalOrders();
%>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>Approve Contracts - Admin | WareNexus</title>
  <link rel="stylesheet" href="css/style.css" />
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

<jsp:include page="header.jsp" />

<div class="container mt-5">
  <div class="welcome-banner">
    <h2>Pending Rental Contracts</h2>
    <p>Review and approve customer-submitted rental orders with completed deposits.</p>
  </div>

  <table class="table table-bordered table-hover mt-4 bg-white">
    <thead class="table-dark">
      <tr>
        <th>Order ID</th>
        <th>Warehouse ID</th>
        <th>Start Date</th>
        <th>End Date</th>
        <th>Deposit</th>
        <th>Status</th>
        <th>Action</th>
      </tr>
    </thead>
    <tbody>
      <% for (RentalOrder ro : list) { %>
        <tr>
          <td><%= ro.getRentalOrderID() %></td>
          <td><%= ro.getWarehouseID() %></td>
          <td><%= ro.getStartDate() %></td>
          <td><%= ro.getEndDate() %></td>
          <td>$<%= ro.getDeposit() %></td>
          <td><span class="badge bg-warning text-dark"><%= ro.getStatus() %></span></td>
          <td>
            <form method="post" action="admin/approve-rental" onsubmit="return confirm('Are you sure to approve this rental?');">
              <input type="hidden" name="rentalOrderId" value="<%= ro.getRentalOrderID() %>">
              <button type="submit" class="btn btn-success btn-sm">Approve</button>
            </form>
          </td>
        </tr>
      <% } %>
      <% if (list.isEmpty()) { %>
        <tr>
          <td colspan="7" class="text-center text-muted">No rental orders pending approval.</td>
        </tr>
      <% } %>
    </tbody>
  </table>
</div>

<jsp:include page="footer.jsp" />

<script>
  // Reuse user dropdown JS from userhome.jsp if header.jsp includes it
</script>

</body>
</html>
