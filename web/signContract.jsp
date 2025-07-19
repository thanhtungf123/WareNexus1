<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.warenexus.model.*, com.warenexus.dao.*, java.text.SimpleDateFormat, java.util.Date" %>
<%
    Account acc = (Account) session.getAttribute("acc");
    if (acc == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    Integer rentalOrderId = (Integer) session.getAttribute("paidRentalOrderId");
    if (rentalOrderId == null) {
        response.sendRedirect("userhome.jsp");
        return;
    }

    CustomerDAO cdao = new CustomerDAO();
    Customer customer = cdao.getByAccountId(acc.getAccountId());

    String today = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
%>
<!DOCTYPE html>
<html>
<head>
    <title>Ký Hợp Đồng</title>
    <meta charset="UTF-8">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

<!-- Header -->
<header class="header">
  <div class="container header-container d-flex justify-content-between align-items-center py-3">
    <div class="logo d-flex align-items-center">
      <img src="${pageContext.request.contextPath}/images/warehouse.png" alt="WareNexus Logo" class="logo-img" style="height: 40px; margin-right: 10px;">
      <span class="logo-text fs-4 fw-bold text-primary">WareNexus</span>
    </div>
    <nav class="nav">
      <ul class="nav-list list-unstyled d-flex gap-4 mb-0">
        <li><a href="userhome.jsp#search" class="text-decoration-none text-dark">Tìm kiếm</a></li>
        <li><a href="userhome.jsp#order" class="text-decoration-none text-dark">Đơn thuê</a></li>
        <li><a href="userhome.jsp#services" class="text-decoration-none text-dark">Dịch vụ</a></li>
      </ul>
    </nav>
    <div class="user-account position-relative">
      <div class="user-avatar bg-primary text-white rounded-circle d-flex align-items-center justify-content-center"
           id="userAvatar" style="width: 40px; height: 40px; cursor: pointer;">
        <%= acc.getEmail().substring(0,1).toUpperCase() %>
      </div>
      <div class="user-dropdown position-absolute bg-white border rounded shadow p-2 mt-2" id="userDropdown" style="display:none; right:0;">
        <div class="user-info mb-2">
          <div class="fw-bold"><%= acc.getEmail() %></div>
        </div>
        <a href="logout" class="dropdown-item text-danger d-block text-decoration-none" id="logoutBtn">Đăng xuất</a>
      </div>
    </div>
  </div>
</header>

<!-- Nội dung hợp đồng -->
<div class="container mt-5 mb-5">
  <div class="border p-4 rounded shadow-sm bg-white">
    <h3 class="text-center mb-4 text-uppercase">Hợp Đồng Thuê Kho</h3>

    <p><strong>Ngày ký:</strong> <%= today %></p>
    <p><strong>Mã đơn thuê:</strong> <%= rentalOrderId %></p>

    <hr>

    <h5>Thông Tin Các Bên</h5>
    <p><strong>Bên A (Khách hàng):</strong></p>
    <ul>
        <li>Họ và tên: <%= customer.getFullName() %></li>
        <li>Email: <%= acc.getEmail() %></li>
        <li>Số điện thoại: <%= customer.getPhone() %></li>
    </ul>

    <p><strong>Bên B (Hệ thống WareNexus):</strong></p>
    <ul>
        <li>Đại diện hệ thống cho thuê kho</li>
        <li>Website: www.warenexus.com</li>
        <li>Email: support@warenexus.com</li>
    </ul>

    <hr>

    <h5>Điều Khoản Thỏa Thuận</h5>
    <ul>
        <li>Bên A đồng ý thuê kho của Bên B theo nội dung và điều kiện trong đơn thuê đã xác nhận.</li>
        <li>Giá thuê, thời hạn thuê, diện tích, đặt cọc và các chi phí liên quan đã được hai bên thống nhất.</li>
        <li>Bên A cam kết thanh toán đúng hạn và sử dụng kho đúng mục đích.</li>
        <li>Bên B cam kết cung cấp kho đúng chất lượng, hỗ trợ kỹ thuật và an ninh trong suốt thời gian thuê.</li>
        <li>Hợp đồng có hiệu lực kể từ thời điểm ký và sau khi Bên A hoàn tất thanh toán.</li>
    </ul>

    <hr>

    <div class="row text-center mt-5">
        <div class="col-md-6">
            <p><strong>Bên A (Khách hàng)</strong></p>
            <p><%= customer.getFullName() %></p>
            <hr style="width: 60%; margin: 0 auto;">
        </div>
        <div class="col-md-6">
            <p><strong>Bên B (WareNexus)</strong></p>
            <p>Đại diện hệ thống</p>
            <hr style="width: 60%; margin: 0 auto;">
        </div>
    </div>

    <form action="signContract" method="post" class="text-center mt-4">
        <input type="hidden" name="rentalOrderId" value="<%= rentalOrderId %>">
        <button type="submit" class="btn btn-success px-4 py-2">Tôi đồng ý và ký hợp đồng</button>
    </form>
  </div>
</div>

<!-- Footer -->
<footer class="footer bg-light py-4 border-top">
  <div class="container text-center">
    <p class="mb-1">© 2025 WareNexus. All rights reserved.</p>
    <small>Đà Nẵng, Việt Nam | Liên hệ: diemtknde170116@fpt.edu.vn</small>
  </div>
</footer>

<!-- Script dropdown -->
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

</body>
</html>
