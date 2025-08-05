<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*, java.text.*" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="com.warenexus.model.*" %>
<%@ page import="com.warenexus.dao.RentalOrderDAO" %>

<%
    Account user = (Account) session.getAttribute("user");
    if (user.getRoleId() != 1) {
        response.sendRedirect("login.jsp");
        return;
    }

    RentalOrderDAO dao = new RentalOrderDAO();
    List<RentalOrder> list = dao.getOngoingOrdersByAccountId(-1); // Lấy tất cả đơn thuê
    Locale vietnam = new Locale("vi", "VN");
    NumberFormat vndFormat = NumberFormat.getCurrencyInstance(vietnam);
    Date today = new Date();
%>

<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8" />
  <title>Quản lý đơn thuê - Admin | WareNexus</title>
  <link rel="stylesheet" href="css/style.css" />
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

<jsp:include page="header.jsp" />

<div class="container mt-5">
  <div class="welcome-banner">
    <h2>Quản lý đơn thuê hiện tại</h2>
    <p>Danh sách các đơn thuê đã được duyệt. Gửi nhắc nhở hoặc hủy đơn quá hạn.</p>
  </div>

  <div class="mb-3">
    <button class="btn btn-outline-secondary" onclick="history.back()">
        <i class="bi bi-arrow-left-circle me-1"></i> Quay lại
    </button>
  </div>

  <table class="table table-bordered table-hover bg-white">
    <thead class="table-dark">
      <tr>
        <th>Order ID</th>
        <th>Kho</th>
        <th>Ngày bắt đầu</th>
        <th>Ngày kết thúc</th>
        <th>Tiền cọc</th>
        <th>Tổng tiền</th>
        <th>Trạng thái</th>
        <th>Còn lại</th>
        <th>Hành động</th>
      </tr>
    </thead>
    <tbody>
    <%
        for (RentalOrder ro : list) {
            Date end = ro.getEndDate();
            long daysLeft = (end.getTime() - today.getTime()) / (1000 * 60 * 60 * 24);
            boolean isOverdue = daysLeft < 0;
    %>
      <tr>
        <td><%= ro.getRentalOrderID() %></td>
        <td><%= ro.getWarehouseID() %></td>
        <td><%= ro.getStartDate() %></td>
        <td><%= ro.getEndDate() %></td>
        <td><%= vndFormat.format(ro.getDeposit()) %></td>
        <td><%= vndFormat.format(ro.getTotalPrice()) %></td>
        <td>
          <span class="badge <%= ro.getStatus().equals("Approved") ? "bg-success" : "bg-secondary" %>">
              <%= ro.getStatus() %>
          </span>
        </td>
        <td>
            <%= daysLeft >= 0 ? daysLeft + " ngày" : "Đã quá hạn " + (-daysLeft) + " ngày" %>
        </td>
        <td class="d-flex gap-2">
          <% if (ro.getStatus().equals("Approved")) { %>
              <% if (daysLeft <= 14 && daysLeft >= 0) { %>
              <form method="post" action="send-reminder">
                <input type="hidden" name="rentalOrderId" value="<%= ro.getRentalOrderID() %>">
                <button class="btn btn-warning btn-sm" onclick="return confirm('Gửi email nhắc nhở?')">Nhắc nhở</button>
              </form>
              <% } %>

              <% if (isOverdue) { %>
              <form method="post" action="cancel-rental" onsubmit="return confirm('Hủy đơn thuê quá hạn này?');">
                <input type="hidden" name="rentalOrderId" value="<%= ro.getRentalOrderID() %>">
                <button class="btn btn-danger btn-sm">Hủy</button>
              </form>
              <% } %>
          <% } else { %>
              <span class="text-muted">Không khả dụng</span>
          <% } %>
        </td>
      </tr>
    <% } %>

    <% if (list.isEmpty()) { %>
      <tr><td colspan="9" class="text-center text-muted">Không có đơn thuê nào.</td></tr>
    <% } %>
    </tbody>
  </table>
</div>

<jsp:include page="footer.jsp" />

</body>
</html>
