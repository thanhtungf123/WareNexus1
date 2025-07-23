<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.warenexus.model.Account" %>
<%@ page import="com.warenexus.model.Warehouse" %>
<%
    Account user = (Account) session.getAttribute("user");
    if (user == null || user.getRoleId() != 1) {
        response.sendRedirect("login.jsp");
        return;
    }

    List<Warehouse> warehouses = (List<Warehouse>) request.getAttribute("warehouses");
    if (warehouses == null) warehouses = new java.util.ArrayList<>();
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Admin - Manage Warehouses</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
  <link rel="stylesheet" href="css/style.css">
</head>
<body>

<jsp:include page="header.jsp" />

<div class="container mt-5 mb-5">
  <div class="d-flex justify-content-between align-items-center mb-4">
    <h2 class="text-primary">Warehouse Management</h2>
    <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#createModal">Add New Warehouse</button>
  </div>

  <div class="card shadow-sm">
    <div class="card-body">
      <table class="table table-bordered table-hover align-middle">
        <thead class="table-primary text-center">
          <tr>
            <th>#</th>
            <th>Name</th>
            <th>Address</th>
            <th>District</th>
            <th>Ward</th>
            <th>Size (m²)</th>
            <th>Price</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <% for (Warehouse w : warehouses) { %>
            <tr>
              <td class="text-center"><%= w.getId() %></td>
              <td><%= w.getName() %></td>
              <td><%= w.getAddress() %></td>
              <td><%= w.getDistrict() %></td>
              <td><%= w.getWard() %></td>
              <td class="text-end"><%= w.getSize() %></td>
              <td class="text-end"><%= w.getPricePerUnit() %>₫</td>
              <td class="text-center">
                  <span class="badge bg-<%= "Available".equalsIgnoreCase(w.getStatus()) ? "success" : ("Rented".equalsIgnoreCase(w.getStatus()) ? "warning" : "secondary") %>">
                    <%= w.getStatus() %>
                  </span>
              </td>
              <td class="text-center">
                <a href="admin-warehouse-detail?id=<%= w.getId() %>" class="btn btn-sm btn-info">View</a>
                <button class="btn btn-sm btn-warning" data-bs-toggle="modal" data-bs-target="#editModal_<%= w.getId() %>">Edit</button>
                <form action="admin-warehouse" method="post" class="d-inline">
                  <input type="hidden" name="warehouseId" value="<%= w.getId() %>">
                  <input type="hidden" name="action" value="delete">
                  <button type="submit" class="btn btn-sm btn-danger" onclick="return confirm('Are you sure you want to delete this warehouse?');">
                    Delete
                  </button>
                </form>
              </td>
            </tr>
          <% } %>
        </tbody>
      </table>
    </div>
  </div>
</div>

<!-- Modal: Create -->
<div class="modal fade" id="createModal" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog modal-lg">
    <form action="admin-warehouse" method="post" class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">Add New Warehouse</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
      </div>
      <div class="modal-body row g-3 px-3">
        <input type="hidden" name="action" value="create">
        <div class="col-md-6">
          <label class="form-label">Name</label>
          <input name="name" class="form-control" required>
        </div>
        <div class="col-md-6">
          <label class="form-label">Warehouse Type ID</label>
          <input name="typeId" type="number" class="form-control" required>
        </div>
        <div class="col-md-12">
          <label class="form-label">Address</label>
          <input name="address" class="form-control" required>
        </div>
        <div class="col-md-6">
          <label class="form-label">Ward</label>
          <input name="ward" class="form-control">
        </div>
        <div class="col-md-6">
          <label class="form-label">District</label>
          <input name="district" class="form-control">
        </div>
        <div class="col-md-4">
          <label class="form-label">Size</label>
          <input name="size" type="number" step="0.1" class="form-control" required>
        </div>
        <div class="col-md-4">
          <label class="form-label">Price</label>
          <input name="price" type="number" step="0.01" class="form-control" required>
        </div>
        <div class="col-md-4">
          <label class="form-label">Status</label>
          <select name="status" class="form-select">
            <option value="Available">Available</option>
            <option value="Unavailable">Unavailable</option>
          </select>
        </div>
        <div class="col-md-6">
          <label class="form-label">Latitude</label>
          <input name="latitude" class="form-control">
        </div>
        <div class="col-md-6">
          <label class="form-label">Longitude</label>
          <input name="longitude" class="form-control">
        </div>
        <div class="col-md-12">
          <label class="form-label">Description</label>
          <textarea name="description" class="form-control" rows="2"></textarea>
        </div>
      </div>
      <div class="modal-footer">
        <button class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
        <button class="btn btn-primary">Save</button>
      </div>
    </form>
  </div>
</div>

<!-- Modal: Edit -->
<% for (Warehouse w : warehouses) { %>
<div class="modal fade" id="editModal_<%= w.getId() %>" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog modal-lg">
    <form action="admin-warehouse" method="post" class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title">Edit Warehouse - <%= w.getName() %></h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
      </div>
      <div class="modal-body row g-3 px-3">
        <input type="hidden" name="action" value="update">
        <input type="hidden" name="warehouseId" value="<%= w.getId() %>">
        <div class="col-md-6">
          <label class="form-label">Name</label>
          <input name="name" class="form-control" value="<%= w.getName() %>" required>
        </div>
        <div class="col-md-6">
          <label class="form-label">Warehouse Type ID</label>
          <input name="typeId" type="number" class="form-control" value="<%= w.getWarehouseTypeId() %>" required>
        </div>
        <div class="col-md-12">
          <label class="form-label">Address</label>
          <input name="address" class="form-control" value="<%= w.getAddress() %>" required>
        </div>
        <div class="col-md-6">
          <label class="form-label">Ward</label>
          <input name="ward" class="form-control" value="<%= w.getWard() %>">
        </div>
        <div class="col-md-6">
          <label class="form-label">District</label>
          <input name="district" class="form-control" value="<%= w.getDistrict() %>">
        </div>
        <div class="col-md-4">
          <label class="form-label">Size</label>
          <input name="size" type="number" step="0.1" class="form-control" value="<%= w.getSize() %>" required>
        </div>
        <div class="col-md-4">
          <label class="form-label">Price</label>
          <input name="price" type="number" step="0.01" class="form-control" value="<%= w.getPricePerUnit() %>" required>
        </div>
        <div class="col-md-4">
          <label class="form-label">Status</label>
          <select name="status" class="form-select">
            <option value="Available" <%= "Available".equals(w.getStatus()) ? "selected" : "" %>>Available</option>
            <option value="Unavailable" <%= "Unavailable".equals(w.getStatus()) ? "selected" : "" %>>Unavailable</option>
          </select>
        </div>
        <div class="col-md-6">
          <label class="form-label">Latitude</label>
          <input name="latitude" class="form-control" value="<%= w.getLatitude() != null ? w.getLatitude() : "" %>">
        </div>
        <div class="col-md-6">
          <label class="form-label">Longitude</label>
          <input name="longitude" class="form-control" value="<%= w.getLongitude() != null ? w.getLongitude() : "" %>">
        </div>
        <div class="col-md-12">
          <label class="form-label">Description</label>
          <textarea name="description" class="form-control" rows="2"><%= w.getDescription() != null ? w.getDescription() : "" %></textarea>
        </div>
      </div>
      <div class="modal-footer">
        <button class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
        <button class="btn btn-success">Update</button>
      </div>
    </form>
  </div>
</div>
<% } %>

<jsp:include page="footer.jsp" />
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
