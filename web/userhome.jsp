<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.warenexus.model.Account" %>
<%
    if (session == null || session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    Account user = (Account) session.getAttribute("user");
%>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <title>WareNexus - Dashboard</title>
  <link rel="stylesheet" href="css/style.css" />
  <style>
    .user-account { position: relative; display: inline-block; }
    .user-avatar {
      width: 40px; height: 40px; border-radius: 50%;
      background: linear-gradient(135deg, #2563eb, #1d4ed8);
      display: flex; align-items: center; justify-content: center;
      color: white; font-weight: 600; cursor: pointer;
      transition: all 0.3s; border: 2px solid transparent;
    }
    .user-avatar:hover { border-color: #2563eb; transform: scale(1.05); }
    .user-dropdown {
      position: absolute; top: 100%; right: 0;
      background: white; border-radius: 0.5rem;
      box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.1),
                  0 10px 10px -5px rgba(0, 0, 0, 0.04);
      min-width: 200px; opacity: 0; visibility: hidden;
      transform: translateY(-10px); transition: all 0.3s;
      z-index: 1000; margin-top: 0.5rem;
    }
    .user-dropdown.show { opacity: 1; visibility: visible; transform: translateY(0); }
    .user-info { padding: 1rem; border-bottom: 1px solid #e5e7eb; }
    .user-name { font-weight: 600; color: #1f2937; margin-bottom: 0.25rem; }
    .user-email { font-size: 0.875rem; color: #6b7280; }
    .dropdown-menu { list-style: none; padding: 0.5rem 0; margin: 0; }
    .dropdown-item {
      display: flex; align-items: center; gap: 0.75rem;
      padding: 0.75rem 1rem; color: #374151;
      text-decoration: none; transition: all 0.2s;
    }
    .dropdown-item:hover { background: #f3f4f6; color: #2563eb; }
    .dropdown-item.logout {
      color: #dc2626; border-top: 1px solid #e5e7eb; margin-top: 0.5rem;
    }
    .dropdown-item.logout:hover { background: #fef2f2; color: #dc2626; }
    .welcome-banner {
      background: linear-gradient(135deg, #2563eb, #1d4ed8);
      color: white; padding: 1rem; margin: 2rem 0;
      border-radius: 0.5rem; text-align: center;
    }
    @media (max-width: 768px) {
      .user-dropdown { right: -1rem; min-width: 180px; }
    }
  </style>
</head>
<body>

<header class="header">
  <div class="container header-container">
    <div class="logo">
      <img src="${pageContext.request.contextPath}/images/warehouse.png" alt="Logo" class="logo-img" />
      <span class="logo-text">WareNexus</span>
    </div>
    <nav class="nav">
      <ul class="nav-list">
        <li><a href="warehouseSearch.jsp">Search Warehouse</a></li>
        <li><a href="#report">Report Issue</a></li>
        <li><a href="#order">Place Rental Order</a></li>
        <li><a href="#warehouse-types">Warehouse Types</a></li>
        <li><a href="#services">Services</a></li>
      </ul>
    </nav>
    <div class="header-actions">
      <div class="user-account">
        <div class="user-avatar" id="userAvatar">
          <%= user.getEmail().substring(0,1).toUpperCase() %>
        </div>
        <div class="user-dropdown" id="userDropdown">
          <div class="user-info">
            <div class="user-name">Welcome</div>
            <div class="user-email"><%= user.getEmail() %></div>
          </div>
          <ul class="dropdown-menu">
            <% if (user.getRoleId() == 3) { %>
            <li><a href="view-profile" class="dropdown-item">View Profile</a></li>
            <% } %>
            <li><a href="#change-password" class="dropdown-item">Change Password</a></li>
            <% if (user.getRoleId() == 1) { %>
              <li><a href="admin-dashboard.jsp" class="dropdown-item">Admin Dashboard</a></li>
            <% } %>
            <li><a href="logout" class="dropdown-item logout" id="logoutBtn">Logout</a></li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</header>

<!-- Hero Block -->
  <section class="block-hero">
    <div class="container">
      <span class="badge">Leading Warehouse Rental Service in Vietnam</span>
      <h1>Perfect <span class="highlight">Warehouse Solutions</span> for Businesses</h1>
      <p>Rent warehouses with flexible sizes, strategic locations, high security, and competitive prices. 24/7 support to meet all your storage needs.</p>
      <div class="actions">
        <a href="#consult" class="btn btn-primary"><img src="${pageContext.request.contextPath}/images/telephone.png" class="icon" alt="">Free Consultation</a>
        <a href="#warehouse-details" class="btn btn-outline"><img src="${pageContext.request.contextPath}/images/location.png" class="icon" alt="">View Available Warehouses</a>
      </div>
    </div>
  </section>

  <!-- Stats Block -->
  <section class="block-stats">
    <div class="container stats-container">
      <div class="stat-card">
      <div class="number" style="color: #3366FF;">500+</div>
      <div class="label" style="color: #3366FF;">Available Warehouses</div>
    </div>
    <div class="stat-card">
      <div class="number" style="color: #22C55E;">1000+</div>
      <div class="label" style="color: #22C55E;">Trusted Customers</div>
    </div>
    <div class="stat-card">
      <div class="number" style="color: #8B5CF6;">24/7</div>
      <div class="label" style="color: #8B5CF6;">Customer Support</div>
    </div>
    <div class="stat-card">
      <div class="number" style="color: #F59E0B;">99%</div>
      <div class="label" style="color: #F59E0B;">Satisfaction Rate</div>
    </div>
    </div>
  </section>

  <!-- Services Block -->
  <section id="services" class="block-services">
    <div class="container">
      <h2>Our Services</h2>
      <p>A variety of warehouse types to meet all business needs</p>
      <div class="services-container">
        <div class="service-card">
          <img
    src="${pageContext.request.contextPath}/images/warehouse.png"
    class="icon"
    alt="Standard Warehouse Icon"
    style="width:50px; height:50px;"
  ><h3>Standard Warehouse</h3>
          <p>Warehouses designed for general storage with flexible sizes.</p>
        </div>
        <div class="service-card">
          <img src="${pageContext.request.contextPath}/images/verified.png" class="icon" alt=""><h3>High-Security Warehouse</h3>
          <p>Secure warehouses with advanced protection and 24/7 monitoring.</p>
        </div>
        <div class="service-card">
          <img src="${pageContext.request.contextPath}/images/truck.png" class="icon" alt=""><h3>Logistics Warehouse</h3>
          <p>Warehouses optimized for logistics and distribution needs.</p>
        </div>
      </div>
    </div>
  </section>

 <!-- Warehouse Details -->
  <section id="warehouse-details" class="block-details">
    <div class="container">
      <h2>Available Warehouses</h2>
      <div class="warehouse-container">

        <!-- Warehouse A1 -->
        <div class="warehouse-card">
          <img src="${pageContext.request.contextPath}/images/kho1.jpg" alt="Warehouse A1 - Da Nang">
          <h3>Warehouse A1 - Da Nang</h3>
          <p><img src="${pageContext.request.contextPath}/images/location.png" class="icon small" alt="Location"> Quận Hải Châu, Đà Nẵng</p>
          <p>Diện tích: <strong>500m²</strong></p>
          <p>Chiều cao: <strong>6m</strong></p>
          <p>Giá thuê: <strong>85.000₫/m²/tháng</strong></p>
          <div class="actions-small">
            <a href="#detailA1" class="btn btn-outline small">View Details</a>
            <a href="#contact" class="btn btn-primary small">Contact</a>
          </div>
        </div>

        <!-- Warehouse B2 -->
        <div class="warehouse-card">
          <img src="${pageContext.request.contextPath}/images/kho2.jpg" alt="Warehouse B2 - Da Nang">
          <h3>Warehouse B2 - Da Nang</h3>
          <p><img src="${pageContext.request.contextPath}/images/location.png" class="icon small" alt="Location"> Khu CN Hòa Khánh, Đà Nẵng</p>
          <p>Diện tích: <strong>800m²</strong></p>
          <p>Chiều cao: <strong>8m</strong></p>
          <p>Giá thuê: <strong>120.000₫/m²/tháng</strong></p>
          <div class="actions-small">
            <a href="#detailB2" class="btn btn-outline small">View Details</a>
            <a href="#contact" class="btn btn-primary small">Contact</a>
          </div>
        </div>

        <!-- Warehouse C3 -->
        <div class="warehouse-card">
          <img src="${pageContext.request.contextPath}/images/kho3.jpg" alt="Warehouse C3 - Da Nang">
          <h3>Warehouse C3 - Da Nang</h3>
          <p><img src="${pageContext.request.contextPath}/images/location.png" class="icon small" alt="Location"> Gần sân bay quốc tế Đà Nẵng</p>
          <p>Diện tích: <strong>1200m²</strong></p>
          <p>Chiều cao: <strong>10m</strong></p>
          <p>Giá thuê: <strong>150.000₫/m²/tháng</strong></p>
          <div class="actions-small">
            <a href="#detailC3" class="btn btn-outline small">View Details</a>
            <a href="#contact" class="btn btn-primary small">Contact</a>
          </div>
        </div>

      </div>
    </div>
  </section>

  <!-- Footer -->
 <!-- Footer -->
<jsp:include page="footer.jsp"/>

<script>
  document.addEventListener('DOMContentLoaded', function () {
    const userAvatar = document.getElementById('userAvatar');
    const userDropdown = document.getElementById('userDropdown');
    const logoutBtn = document.getElementById('logoutBtn');

    if (userAvatar && userDropdown) {
      userAvatar.addEventListener('click', function (e) {
        e.stopPropagation();
        userDropdown.classList.toggle('show');
      });

      document.addEventListener('click', function (e) {
        if (!userAvatar.contains(e.target) && !userDropdown.contains(e.target)) {
          userDropdown.classList.remove('show');
        }
      });
    }

    if (logoutBtn) {
      logoutBtn.addEventListener('click', function (e) {
        e.preventDefault();
        if (confirm('Are you sure you want to logout?')) {
          window.location.href = 'logout';
        }
      });
    }

    const dropdownItems = document.querySelectorAll('.dropdown-item:not(.logout)');
    dropdownItems.forEach(item => {
      item.addEventListener('click', function () {
        userDropdown.classList.remove('show');
      });
    });
  });
</script>

</body>
</html>
