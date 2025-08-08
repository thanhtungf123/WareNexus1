<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.warenexus.model.*, com.warenexus.dao.*, java.text.SimpleDateFormat, java.util.Date, java.text.DecimalFormat" %>
<%
    Account acc = (Account) session.getAttribute("acc");
    if (acc == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    Integer rentalOrderId = (Integer) session.getAttribute("paidRentalOrderId");
    if (rentalOrderId == null) {
        response.sendRedirect("userhome");
        return;
    }

    CustomerDAO cdao = new CustomerDAO();
    Customer customer = cdao.getByAccountId(acc.getAccountId());

    RentalOrderDAO rentalOrderDAO = new RentalOrderDAO();
    RentalOrder rentalOrder = rentalOrderDAO.getRentalOrderById(rentalOrderId);

    // Lấy tên kho theo warehouseID (đồng bộ với PDFGenerator)
    WarehouseDAO wdao = new WarehouseDAO();
    Warehouse wh = wdao.getById(rentalOrder.getWarehouseID());
    String warehouseName = (wh != null && wh.getName() != null) ? wh.getName() : "—";

    String today = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
    SimpleDateFormat ddf = new SimpleDateFormat("dd/MM/yyyy");
    String startStr = ddf.format(rentalOrder.getStartDate());
    String endStr   = ddf.format(rentalOrder.getEndDate());

    // Định dạng tiền giống PDF
    DecimalFormat moneyFmt = new DecimalFormat("#,###,###");
    String formattedPrice   = moneyFmt.format(rentalOrder.getTotalPrice() != null ? rentalOrder.getTotalPrice() : 0d);
    String formattedDeposit = moneyFmt.format(rentalOrder.getDeposit()     != null ? rentalOrder.getDeposit()     : 0d);

    // Tính số tháng chính xác bằng java.time.Period
    java.time.ZoneId zone = java.time.ZoneId.systemDefault();

    java.time.LocalDate start = java.time.Instant.ofEpochMilli(rentalOrder.getStartDate().getTime())
            .atZone(zone).toLocalDate();

    java.time.LocalDate end = java.time.Instant.ofEpochMilli(rentalOrder.getEndDate().getTime())
            .atZone(zone).toLocalDate();

    java.time.Period period = java.time.Period.between(start, end);
    int rentalMonths = period.getYears() * 12 + period.getMonths();


%>
<!DOCTYPE html>
<html>
<head>
    <title>Ký Hợp Đồng</title>
    <meta charset="UTF-8">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { font-family: 'Poppins', sans-serif; background-color: #f7f9fc; }
        .card { border-radius: 12px; box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1); }
        .card-header { background-color: #0056b3; color: white; border-radius: 12px 12px 0 0; }
        .card-body { background-color: white; padding: 30px; border-radius: 0 0 12px 12px; }
        .btn-primary { background-color: #007bff; border-color: #007bff; font-size: 14px; padding: 8px 16px; width: 100%; border-radius: 8px; }
        .btn-primary:hover { background-color: #0056b3; border-color: #0056b3; }
        .signature-box { border: 2px solid #ccc; width: 100%; height: 200px; margin-top: 20px; position: relative; }
        .signature-box canvas { width: 100%; height: 100%; border-radius: 8px; }
        .btn-clear { background-color: #f44336; color: white; border: none; font-size: 14px; padding: 8px 16px; border-radius: 8px; margin-bottom: 10px; }
        .btn-clear:hover { background-color: #d32f2f; }
    </style>
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
        <li><a href="userhome#search" class="text-decoration-none text-dark">Tìm kiếm</a></li>
        <li><a href="userhome#order" class="text-decoration-none text-dark">Đơn thuê</a></li>
        <li><a href="userhome#services" class="text-decoration-none text-dark">Dịch vụ</a></li>
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
    <p><strong>Tên kho:</strong> <%= warehouseName %></p>
    <p><strong>Giá thuê:</strong> <%= formattedPrice %> VND</p>
    <p><strong>Tiền đặt cọc:</strong> <%= formattedDeposit %> VND</p>
    <p><strong>Thời gian thuê:</strong> <%= rentalMonths %> tháng</p>
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
        <li>Email: tungptde170779@fpt.edu.vn</li>
    </ul>

    <hr>

    <h5 class="mb-3">CÁC ĐIỀU KHOẢN VÀ THỎA THUẬN</h5>

    <p><strong>Điều I:</strong> Bên A đồng ý thuê kho của Bên B theo nội dung và điều kiện trong đơn thuê đã xác nhận.</p>

    <p><strong>Điều II: Thời hạn cho thuê kho</strong><br/>
       - Thời hạn cho thuê là <%= rentalMonths %> tháng<br/>
    </p>

    <p><strong>Điều III: Giá cả và phương thức thanh toán</strong><br/>
       1. Giá trị hợp đồng: 30% số tiền tổng sẽ là tiền cọc đồng thời cũng là tiền giá trị hợp đồng<br/>
       &nbsp;&nbsp;&nbsp;&nbsp;- Tiền cọc: <%= formattedDeposit %> VND.<br/>
       &nbsp;&nbsp;&nbsp;&nbsp;- Tiền tổng: <%= formattedPrice %> VND.<br/>
       2. Phương thức thanh toán: Bên A sẽ thanh toán số tiền tổng còn lại trên hệ thống đã tích hợp PayOS.
    </p>

    <p><strong>Điều IV: Trách nhiệm của bên cho thuê kho</strong><br/>
       1. Tiến hành bàn giao đầy đủ kho, trang thiết bị và các dịch vụ đi kèm (nếu có) cho bên thuê.<br/>
       2. Cung cấp các giấy tờ có liên đến kho bãi cho thuê cho cơ quan nhà nước giúp bên A khi cần thiết.<br/>
       3. Đảm bảo quyền sử dụng trọn vẹn của bên thuê đối với diện tích ghi trong hợp đồng.
    </p>

    <p><strong>Điều V: Trách nhiệm của bên thuê kho</strong><br/>
       1. Trả tổng tiền thuê theo đúng thời hạn đã quy định (14 ngày bắt đầu từ ngày thuê).<br/>
       2. Sử dụng kho theo đúng mục đích.<br/>
       3. Chấp hành các quy định về giữ gìn vệ sinh môi trường và các quy định về trật tự an ninh chung.<br/>
       4. Không được cải tạo sửa chữa kho khi chưa có sự đồng ý của bên cho thuê.<br/>
       5. Không được chuyển nhượng hợp đồng thuê và cho thuê lại khi không có sự đồng ý của bên cho thuê.<br/>
       6. Trường hợp chấm dứt hợp đồng trước thời hạn đã thỏa thuận phải báo trước cho bên B ngay ít nhất 30 ngày. Nếu muốn thuê tiếp bên A phải thỏa thuận trước với bên B.
    </p>

    <p><strong>Điều VI: Hai bên cam kết</strong><br/>
       1. Thực hiện đúng các điều khoản ghi trong hợp đồng. Trường hợp có tranh chấp hoặc một trong hai bên vi phạm hợp đồng thì cùng nhau bàn bạc thống nhất trên tinh thần đoàn kết. Nếu không thỏa mãn thì yêu cầu cơ quan có thẩm quyền giải quyết.<br/>
       2. Chấm dứt hợp đồng trong các điều kiện sau:<br/>
       - Hết thời hạn hợp đồng.<br/>
       - Hai bên thỏa thuận chấm dứt hợp đồng trước thời hạn.<br/>
       - Nếu bên A không thanh toán toàn bộ số tiền tổng theo đúng thời hạn quy định thì bên B sẽ chấm dứt hợp đồng thuê, tiền cọc sẽ không được hoàn lại cho bên A.<br/>
       3. Tiền cọc sẽ được hoàn lại khi bên A đã thanh toán đầy đủ và hợp đồng hết hạn thuê.
    </p>

    <p><strong>Điều VII: Hiệu lực của hợp đồng</strong><br/>
       1. Hợp đồng này có hiệu lực kể từ ngày ký cho đến khi hết hạn hợp đồng.<br/>
       2. Hợp đồng được lập thành 02 bản có giá trị như nhau. Bên A sẽ giữ 01 bản, bên B giữ 01 bản.
    </p>

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

    <!-- Phần chữ ký -->
    <div class="form-group">
        <label for="signature">Chữ ký của bạn</label>
        <div class="signature-box" id="signature-box">
            <canvas id="signatureCanvas"></canvas>
        </div>
        <button type="button" class="btn btn-clear mt-2" id="clearSignature">Xóa chữ ký</button>
        <div id="warningMessage" class="alert alert-danger mt-2" style="display:none;">
            Bạn chưa ký. Chữ ký hệ thống mặc định sẽ được sử dụng.
        </div>
    </div>

    <!-- Form để gửi chữ ký -->
    <form id="signatureForm" action="requestOTP" method="POST" onsubmit="return validateSignature()">
        <input type="hidden" name="rentalOrderId" value="<%= rentalOrderId %>">
        <input type="hidden" name="signatureImage" id="signatureImage" />
        <button type="button" class="btn btn-success" onclick="confirmAndProceed()">Tôi đồng ý và ký hợp đồng</button>
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

<script>
    // Signature canvas functionality
    const canvas = document.getElementById('signatureCanvas');
    const ctx = canvas.getContext('2d');
    const signatureImageInput = document.getElementById('signatureImage');
    const clearButton = document.getElementById('clearSignature');
    const warningMessage = document.getElementById('warningMessage');

    let drawing = false;

    // Set canvas size
    canvas.width = document.querySelector('.signature-box').offsetWidth;
    canvas.height = document.querySelector('.signature-box').offsetHeight;

    // Start drawing
    canvas.addEventListener('mousedown', (e) => {
        drawing = true;
        ctx.beginPath();
        ctx.moveTo(e.offsetX, e.offsetY);
    });

    // Draw while mouse is down
    canvas.addEventListener('mousemove', (e) => {
        if (drawing) {
            ctx.lineTo(e.offsetX, e.offsetY);
            ctx.stroke();
        }
    });

    // Stop drawing
    canvas.addEventListener('mouseup', () => {
        drawing = false;
        saveSignature();
    });

    // Clear the signature canvas
    clearButton.addEventListener('click', () => {
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        signatureImageInput.value = '';
        warningMessage.style.display = 'none';
    });

    // Save the signature as an image (base64)
    function saveSignature() {
        const signatureDataUrl = canvas.toDataURL();
        signatureImageInput.value = signatureDataUrl; // Store the image as base64 in the hidden input
    }

    // Xác nhận và tiếp tục
    function confirmAndProceed() {
        const signatureData = signatureImageInput.value;  // Lấy giá trị chữ ký từ canvas

        if (!signatureData) {  // Nếu không có chữ ký
            warningMessage.style.display = 'block';  // Hiển thị cảnh báo
            signatureImageInput.value = ''; // chữ ký mặc định

            // Hiển thị hộp thoại xác nhận
            const userConfirmed = confirm("Bạn chưa ký. Chữ ký hệ thống mặc định sẽ được sử dụng. Bạn có đồng ý không?");
            if (!userConfirmed) {
                return;  // Nếu người dùng nhấn "Cancel", không gửi OTP
            }
        }

        // Lưu chữ ký trước khi gửi form
        saveSignature();

        if (!signatureData) {
            signatureImageInput.value = ''; // Đảm bảo không có hash hoặc base64 nếu không có chữ ký
        }

        // Tiến hành gửi yêu cầu OTP
        document.getElementById('signatureForm').submit();
    }

</script>

</body>
</html>
