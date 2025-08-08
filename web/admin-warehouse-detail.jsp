<%@ page import="com.warenexus.model.Warehouse, com.warenexus.model.Customer, com.warenexus.model.RentalOrder" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Warehouse warehouse = (Warehouse) request.getAttribute("warehouse");
    RentalOrder rentalOrder = (RentalOrder) request.getAttribute("rentalOrder");
    Customer customer = (Customer) request.getAttribute("customer");

    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd-MM-yyyy");
    java.text.NumberFormat nf = java.text.NumberFormat.getInstance(new java.util.Locale("vi", "VN"));
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Admin - Warehouse Detail</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
  <link rel="stylesheet" href="css/style.css">
</head>
<body>

<jsp:include page="header.jsp"/>

<div class="container mt-5 mb-5">
  <h2 class="text-primary text-center mb-4">Warehouse Detail</h2>

  <div class="row">
    <!-- Warehouse Info -->
    <div class="col-md-6 mb-4">
      <div class="card shadow-sm">
        <div class="card-header bg-primary text-white">
          <h5 class="mb-0">Warehouse Information</h5>
        </div>
        <div class="card-body">
          <div class="text-center mb-3">
            <c:choose>
              <c:when test="${not empty warehouse.imageUrl}">
                <img src="image?id=${warehouse.imageUrl}"
                     alt="Warehouse Image" class="img-fluid rounded shadow-sm"
                     style="max-height: 300px; object-fit: cover;">
              </c:when>
              <c:otherwise>
                <img src="images/default-warehouse.jpg"
                     alt="Default Warehouse Image" class="img-fluid rounded shadow-sm"
                     style="max-height: 300px; object-fit: cover;">
              </c:otherwise>
            </c:choose>
          </div>
          <p><strong>Name:</strong> <%= warehouse.getName() %></p>
          <p><strong>Type ID:</strong> <%= warehouse.getWarehouseTypeId() %></p>
          <p><strong>Status:</strong> <%= warehouse.getStatus() %></p>
          <p><strong>Size:</strong> <%= warehouse.getSize() %> m²</p>
          <p><strong>Price/Unit:</strong> <%= nf.format(warehouse.getPricePerUnit()) %> VND</p>
          <p><strong>Address:</strong> <%= warehouse.getAddress() %>, <%= warehouse.getWard() %>, <%= warehouse.getDistrict() %></p>
          <p><strong>Description:</strong> <%= warehouse.getDescription() %></p>
        </div>
      </div>
    </div>

    <!-- Renter Info -->
    <div class="col-md-6 mb-4">
      <% if ("Rented".equalsIgnoreCase(warehouse.getStatus()) && rentalOrder != null && customer != null) { %>
        <div class="card shadow-sm">
          <div class="card-header bg-success text-white">
            <h5 class="mb-0">Current Renter Information</h5>
          </div>
          <div class="card-body">
            <p><strong>Full Name:</strong> <%= customer.getFullName() %></p>
            <p><strong>Phone:</strong> <%= customer.getPhone() %></p>
            <p><strong>Email:</strong> <%= customer.getEmail() %></p>
            <p><strong>Rental Period:</strong> 
              <%= sdf.format(rentalOrder.getStartDate()) %> to <%= sdf.format(rentalOrder.getEndDate()) %>
            </p>
            <p><strong>Status:</strong> <%= rentalOrder.getStatus() %></p>
            <p><strong>Deposit:</strong> <%= nf.format(rentalOrder.getDeposit()) %> VND</p>
            <p><strong>Total Price:</strong> <%= nf.format(rentalOrder.getTotalPrice()) %> VND</p>
          </div>
        </div>
      <% } else { %>
        <div class="alert alert-warning shadow-sm">
          This warehouse is currently <strong>not rented</strong>.
        </div>
      <% } %>
    </div>
  </div>

  <div class="text-center">
    <a href="admin-warehouse" class="btn btn-secondary mt-3">← Back to Warehouse List</a>
  </div>
</div>

<jsp:include page="footer.jsp"/>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
