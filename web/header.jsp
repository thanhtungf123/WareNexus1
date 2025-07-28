<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.warenexus.model.Account" %>
<%
  if (session == null || session.getAttribute("user") == null) {
    response.sendRedirect("login.jsp");
    return;
  }
  Account user = (Account) session.getAttribute("user");
%>

<header class="header bg-light border-bottom shadow-sm">
  <div class="container header-container d-flex justify-content-between align-items-center py-3">
    <!-- Logo -->
    <div class="logo d-flex align-items-center">
      <a href="userhome.jsp#search" class="d-flex align-items-center text-decoration-none">
        <img src="${pageContext.request.contextPath}/images/warehouse.png" alt="WareNexus Logo" class="logo-img" style="height: 40px; margin-right: 10px;">
        <span class="logo-text fs-4 fw-bold text-primary">WareNexus</span>
      </a>
    </div>

    <!-- Navigation -->
    <nav class="nav">
      <ul class="nav-list list-unstyled d-flex gap-4 mb-0">
        <li><a href="userhome.jsp#search" class="text-decoration-none text-dark fw-semibold">Search</a></li>
        <li><a href="userhome.jsp#order" class="text-decoration-none text-dark fw-semibold">Order</a></li>
        <li><a href="userhome.jsp#services" class="text-decoration-none text-dark fw-semibold">Services</a></li>
      </ul>
    </nav>

    <!-- User Account -->
    <div class="user-account position-relative">
      <div class="user-avatar bg-primary text-white rounded-circle d-flex align-items-center justify-content-center"
           id="userAvatar" style="width: 40px; height: 40px; cursor: pointer;">
        <%= user.getEmail().substring(0,1).toUpperCase() %>
      </div>
      <div class="user-dropdown position-absolute bg-white border rounded shadow-sm p-3 mt-2" id="userDropdown" style="display:none; right:0; min-width: 200px;">
        <div class="user-info mb-2">
          <div class="fw-bold text-dark"><%= user.getEmail() %></div>
        </div>
        <hr class="my-2">
        <a href="logout" class="dropdown-item text-danger d-block text-decoration-none fw-semibold" id="logoutBtn">
          <i class="bi bi-box-arrow-right me-2"></i>Logout
        </a>
      </div>
    </div>
  </div>
</header>

<!-- Script: Dropdown toggle -->
<script>
  document.addEventListener("DOMContentLoaded", function() {
    const avatar = document.getElementById('userAvatar');
    const dropdown = document.getElementById('userDropdown');

    avatar.addEventListener('click', function(e) {
      e.stopPropagation();
      dropdown.style.display = dropdown.style.display === 'block' ? 'none' : 'block';
    });

    document.addEventListener('click', function(e) {
      if (!avatar.contains(e.target) && !dropdown.contains(e.target)) {
        dropdown.style.display = 'none';
      }
    });

    document.getElementById('logoutBtn').addEventListener('click', function(e) {
      e.preventDefault();
      if (confirm('Bạn chắc chắn muốn đăng xuất?')) {
        window.location.href = 'login.jsp';
      }
    });
  });
</script>
