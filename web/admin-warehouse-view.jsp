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
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
  <link rel="stylesheet" href="css/style.css">
</head>
<body>

<jsp:include page="header.jsp" />

<div class="container mt-5 mb-5">
  <div class="d-flex justify-content-between align-items-center mb-4">
    <h2 class="text-primary">Warehouse Management</h2>
    <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#createModal">Add New Warehouse</button>
  </div>
  <!-- Back Button -->
  <div class="mb-3">
    <button class="btn btn-outline-secondary" onclick="history.back()">
      <i class="bi bi-arrow-left-circle me-1"></i> Back
    </button>
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
                <div class="d-flex justify-content-center gap-2">
                  <!-- View Button -->
                  <a href="admin-warehouse-detail?id=<%= w.getId() %>" class="btn btn-sm btn-info" title="View">
                    <i class="bi bi-eye"></i>
                  </a>

                  <!-- Edit Button -->
                  <button class="btn btn-sm btn-warning" data-bs-toggle="modal" data-bs-target="#editModal_<%= w.getId() %>" title="Edit">
                    <i class="bi bi-pencil-square"></i>
                  </button>

                  <!-- Delete Button -->
                  <button type="button" class="btn btn-sm btn-danger" data-bs-toggle="modal" data-bs-target="#deleteModal_<%= w.getId() %>" title="Delete">
                    <i class="bi bi-trash"></i>
                  </button>
                </div>

                <!-- Delete Modal -->
                <div class="modal fade" id="deleteModal_<%= w.getId() %>" tabindex="-1">
                  <div class="modal-dialog modal-dialog-centered">
                    <div class="modal-content">
                      <div class="modal-header">
                        <h5 class="modal-title">Confirm Delete</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                      </div>
                      <div class="modal-body">
                        Are you sure you want to delete warehouse "<%= w.getName() %>"?
                      </div>
                      <div class="modal-footer">
                        <form action="admin-warehouse" method="post">
                          <input type="hidden" name="warehouseId" value="<%= w.getId() %>">
                          <input type="hidden" name="action" value="delete">
                          <button type="submit" class="btn btn-danger">Yes, Delete</button>
                        </form>
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                      </div>
                    </div>
                  </div>
                </div>
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
  <div class="modal-dialog modal-lg modal-dialog-scrollable">
    <form action="admin-warehouse" method="post" class="modal-content">
      <div class="modal-header bg-primary-subtle">
        <h5 class="modal-title fw-bold text-dark">
          <i class="bi bi-plus-circle me-2"></i>Add New Warehouse
        </h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
      </div>

      <div class="modal-body px-4 py-3">
        <input type="hidden" name="action" value="create">

        <div class="row g-3">
          <div class="col-md-6">
            <label class="form-label fw-semibold text-secondary text-uppercase small">Name</label>
            <input name="name" class="form-control" required>
          </div>
          <div class="col-md-6">
            <label class="form-label fw-semibold text-secondary text-uppercase small">Warehouse Type ID</label>
            <input name="typeId" type="number" class="form-control" required>
          </div>

          <div class="col-md-12">
            <label class="form-label fw-semibold text-secondary text-uppercase small">Address</label>
            <input name="address" class="form-control" required>
          </div>

          <div class="col-md-6">
            <label class="form-label fw-semibold text-secondary text-uppercase small">Ward</label>
            <input name="ward" class="form-control">
          </div>
          <div class="col-md-6">
            <label class="form-label fw-semibold text-secondary text-uppercase small">District</label>
            <input name="district" class="form-control">
          </div>

          <div class="col-md-4">
            <label class="form-label fw-semibold text-secondary text-uppercase small">Size (m²)</label>
            <input name="size" type="number" step="0.1" class="form-control" required>
          </div>
          <div class="col-md-4">
            <label class="form-label fw-semibold text-secondary text-uppercase small">Price (₫)</label>
            <input name="price" type="number" step="0.01" class="form-control" required>
          </div>
          <div class="col-md-4">
            <label class="form-label fw-semibold text-secondary text-uppercase small">Status</label>
            <select name="status" class="form-select">
              <option value="Available">Available</option>
              <option value="Unavailable">Unavailable</option>
              <option value="Rented">Rented</option>
              <option value="Maintenance">Maintenance</option>
            </select>
          </div>

          <div class="col-md-6">
            <label class="form-label fw-semibold text-secondary text-uppercase small">Latitude</label>
            <input name="latitude" class="form-control">
          </div>
          <div class="col-md-6">
            <label class="form-label fw-semibold text-secondary text-uppercase small">Longitude</label>
            <input name="longitude" class="form-control">
          </div>

          <div class="col-md-12">
            <label class="form-label fw-semibold text-secondary text-uppercase small">Description</label>
            <textarea name="description" class="form-control" rows="3"></textarea>
          </div>
        </div>
      </div>

      <div class="modal-footer bg-light">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
          <i class="bi bi-x-circle"></i> Cancel
        </button>
        <button type="submit" class="btn btn-primary">
          <i class="bi bi-save"></i> Save
        </button>
      </div>
    </form>
  </div>
</div>


<!-- Modal: Edit -->
<% for (Warehouse w : warehouses) { %>
<div class="modal fade" id="editModal_<%= w.getId() %>" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog modal-lg modal-dialog-scrollable">
    <form action="admin-warehouse" method="post" class="modal-content">
      <div class="modal-header bg-warning-subtle">
        <h5 class="modal-title fw-bold text-dark">
          <i class="bi bi-pencil-square me-2"></i>Edit Warehouse - <%= w.getName() %>
        </h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
      </div>

      <div class="modal-body px-4 py-3">
        <input type="hidden" name="action" value="update">
        <input type="hidden" name="warehouseId" value="<%= w.getId() %>">

        <div class="row g-3">
          <div class="col-md-6">
            <label class="form-label fw-semibold text-secondary text-uppercase small">Name</label>
            <input name="name" class="form-control" value="<%= w.getName() %>" required>
          </div>
          <div class="col-md-6">
            <label class="form-label fw-semibold text-secondary text-uppercase small">Warehouse Type ID</label>
            <input name="typeId" type="number" class="form-control" value="<%= w.getWarehouseTypeId() %>" required>
          </div>

          <div class="col-md-12">
            <label class="form-label fw-semibold text-secondary text-uppercase small">Address</label>
            <input name="address" class="form-control" value="<%= w.getAddress() %>" required>
          </div>

          <div class="col-md-6">
            <label class="form-label fw-semibold text-secondary text-uppercase small">Ward</label>
            <input name="ward" class="form-control" value="<%= w.getWard() %>">
          </div>
          <div class="col-md-6">
            <label class="form-label fw-semibold text-secondary text-uppercase small">District</label>
            <input name="district" class="form-control" value="<%= w.getDistrict() %>">
          </div>

          <div class="col-md-4">
            <label class="form-label fw-semibold text-secondary text-uppercase small">Size (m²)</label>
            <input name="size" type="number" step="0.1" class="form-control" value="<%= w.getSize() %>" required>
          </div>
          <div class="col-md-4">
            <label class="form-label fw-semibold text-secondary text-uppercase small">Price (₫)</label>
            <input name="price" type="number" step="0.01" class="form-control" value="<%= w.getPricePerUnit() %>" required>
          </div>
          <div class="col-md-4">
            <label class="form-label fw-semibold text-secondary text-uppercase small">Status</label>
            <select name="status" class="form-select">
              <option value="Available" <%= "Available".equals(w.getStatus()) ? "selected" : "" %>>Available</option>
              <option value="Unavailable" <%= "Unavailable".equals(w.getStatus()) ? "selected" : "" %>>Unavailable</option>
              <option value="Rented" <%= "Rented".equals(w.getStatus()) ? "selected" : "" %>>Rented</option>
              <option value="Maintenance" <%= "Maintenance".equals(w.getStatus()) ? "selected" : "" %>>Maintenance</option>
            </select>
          </div>

          <div class="col-md-6">
            <label class="form-label fw-semibold text-secondary text-uppercase small">Latitude</label>
            <input name="latitude" class="form-control" value="<%= w.getLatitude() != null ? w.getLatitude() : "" %>">
          </div>
          <div class="col-md-6">
            <label class="form-label fw-semibold text-secondary text-uppercase small">Longitude</label>
            <input name="longitude" class="form-control" value="<%= w.getLongitude() != null ? w.getLongitude() : "" %>">
          </div>

          <div class="col-md-12">
            <label class="form-label fw-semibold text-secondary text-uppercase small">Description</label>
            <textarea name="description" class="form-control" rows="3"><%= w.getDescription() != null ? w.getDescription() : "" %></textarea>
          </div>
        </div>
      </div>

      <div class="modal-footer bg-light">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
          <i class="bi bi-x-circle"></i> Cancel
        </button>
        <button type="submit" class="btn btn-success">
          <i class="bi bi-check-circle"></i> Update
        </button>
      </div>
    </form>
  </div>
</div>
<% } %>


<jsp:include page="footer.jsp" />
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
