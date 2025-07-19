<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.warenexus.model.Account" %>
<%
    if (session == null || session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    Account user = (Account) session.getAttribute("user");
%>
<header class="header">
  <div class="container header-container d-flex justify-content-between align-items-center py-3">
    <div class="logo d-flex align-items-center">
      <img src="${pageContext.request.contextPath}/images/warehouse.png" alt="WareNexus Logo" class="logo-img" style="height: 40px; margin-right: 10px;">
      <span class="logo-text fs-4 fw-bold text-primary">WareNexus</span>
    </div>
    <nav class="nav">
      <ul class="nav-list list-unstyled d-flex gap-4 mb-0">
        <li><a href="userhome.jsp#search" class="text-decoration-none text-dark">Search</a></li>
        <li><a href="userhome.jsp#order" class="text-decoration-none text-dark">Order</a></li>
        <li><a href="userhome.jsp#services" class="text-decoration-none text-dark">Services</a></li>
      </ul>
    </nav>
    <div class="user-account position-relative">
      <div class="user-avatar bg-primary text-white rounded-circle d-flex align-items-center justify-content-center"
           id="userAvatar" style="width: 40px; height: 40px; cursor: pointer;">
        <%= user.getEmail().substring(0,1).toUpperCase() %>
      </div>
      <div class="user-dropdown position-absolute bg-white border rounded shadow p-2 mt-2" id="userDropdown" style="display:none; right:0;">
        <div class="user-info mb-2">
          <div class="fw-bold"><%= user.getEmail() %></div>
        </div>
        <a href="logout" class="dropdown-item text-danger d-block text-decoration-none" id="logoutBtn">Logout</a>
      </div>
    </div>
  </div>
</header>
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
