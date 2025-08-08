<%@ page import="com.warenexus.model.*, com.warenexus.dao.*" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%
    Account acc = (Account) session.getAttribute("acc");
    if (acc == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    int rentalOrderId = (request.getAttribute("rentalOrderId") != null)
        ? (Integer) request.getAttribute("rentalOrderId")
        : 0;
    int warehouseId = (request.getAttribute("warehouseId") != null)
        ? (Integer) request.getAttribute("warehouseId")
        : 0;

    WarehouseDAO wdao = new WarehouseDAO();
    Warehouse wh = wdao.getById(warehouseId);
    ServiceFeesDAO serviceFeesDAO = new ServiceFeesDAO();
    List<ServiceFee> serviceFees = serviceFeesDAO.getAllServiceFees();
    CustomerDAO cdao = new CustomerDAO();
    Customer cu = cdao.getByAccountId(acc.getAccountId());
%>

<!DOCTYPE html>
<html>
<head>
    <title>Rent Warehouse - WareNexus</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

<jsp:include page="header.jsp"/>

<div class="container my-5">
    <div class="bg-white shadow rounded p-4">
        <h3 class="mb-4 text-primary">Warehouse Rental Form</h3>

        <form action="payos-payment" method="post">
            <input type="hidden" name="rentalOrderId" value="<%= rentalOrderId %>">
            <input type="hidden" name="warehouseId" value="<%= warehouseId %>">
            <input type="hidden" name="currentURL" id="currentURL" />
            <input type="hidden" name="deposit" id="depositHidden">
            <input type="hidden" name="totalPrice" id="totalPriceHidden">
            <input type="hidden" name="startDate" id="startDateHidden">
            <input type="hidden" name="endDate" id="endDateHidden">

            <!-- Customer Info -->
            <h5 class="text-dark">Customer Information</h5>
            <div class="mb-3 row">
                <label class="col-sm-3 col-form-label">Full Name</label>
                <div class="col-sm-9"><input class="form-control" value="<%= cu.getFullName() %>" readonly></div>
            </div>
            <div class="mb-4 row">
                <label class="col-sm-3 col-form-label">Phone Number</label>
                <div class="col-sm-9"><input class="form-control" value="<%= cu.getPhone() %>" readonly></div>
            </div>

            <!-- Warehouse Info -->
            <h5 class="text-dark">Warehouse Information</h5>
            <div class="mb-3 row">
                <label class="col-sm-3 col-form-label">Warehouse Name</label>
                <div class="col-sm-9"><input class="form-control" value="<%= wh.getName() %>" readonly></div>
            </div>
            <div class="mb-3 row">
                <label class="col-sm-3 col-form-label">Address</label>
                <div class="col-sm-9">
                    <input class="form-control" value="<%= wh.getAddress() %>, <%= wh.getWard() %>, <%= wh.getDistrict() %>" readonly>
                </div>
            </div>
            <div class="mb-3 row">
                <label class="col-sm-3 col-form-label">Size (m²)</label>
                <div class="col-sm-9"><input class="form-control" value="<%= wh.getSize() %>" readonly></div>
            </div>
            <div class="mb-4 row">
                <label class="col-sm-3 col-form-label">Price per m² (VNĐ)</label>
                <div class="col-sm-9"><input class="form-control" value="<%= wh.getPricePerUnit() %>" readonly></div>
            </div>

            <!-- Rental Info -->
            <h5 class="text-dark">Rental Period</h5>
            <div class="mb-3 row">
                <label class="col-sm-3 col-form-label">Start Date</label>
                <div class="col-sm-9">
                    <input type="date" id="startDateInput" class="form-control" onchange="calculateAll()" required>
                </div>
            </div>
            <div class="mb-3 row">
                <label class="col-sm-3 col-form-label">Months of Rental</label>
                <div class="col-sm-9">
                    <select id="months" class="form-select" onchange="calculateAll()" required>
                        <option value="3">3</option>
                        <option value="6">6</option>
                        <option value="9">9</option>
                        <option value="12">12</option>
                    </select>
                </div>
            </div>
            <div class="mb-3 row">
                <label class="col-sm-3 col-form-label">End Date</label>
                <div class="col-sm-9">
                    <input type="text" id="endDateDisplay" class="form-control bg-light" readonly>
                </div>
            </div>

            <!--  -->
            <!-- Services -->
            <h5 class="text-dark">Services</h5>
            <div class="mb-3">
                <label class="form-label">📦 Additional Services</label>
                <%
                    for (ServiceFee sf : serviceFees) {
                        String code = sf.getServiceCode();
                        String name = sf.getServiceName();
                        int fee = sf.getFee();
                %>
                <div class="form-check">
                    <input class="form-check-input" type="checkbox" name="services" value="<%= code %>" id="<%= code %>" onchange="calculateAll()">
                    <label class="form-check-label" for="<%= code %>">
                        <%= name %> - <%= String.format("%,d", fee) %> VNĐ
                    </label>
                </div>
                <%
                    }
                %>
            </div>

            <!-- Deposit -->
            <h5 class="text-dark">Deposit Calculation</h5>
            <div class="mb-3 row">
                <label class="col-sm-3 col-form-label">Deposit Percentage</label>
                <div class="col-sm-9">
                    <span class="form-control bg-light">20%</span>
                </div>
            </div>
            <div class="mb-3 row">
                <label class="col-sm-3 col-form-label">Total Deposit</label>
                <div class="col-sm-9">
                    <span id="depositDisplay" class="form-control bg-light text-primary fw-bold">Select options</span>
                </div>
            </div>
            <div class="mb-3 row">
                <label class="col-sm-3 col-form-label">Total Deposit</label>
                <div class="col-sm-9">
                    <span id="depositDisplay" class="form-control bg-light text-primary fw-bold">Select options</span>
                </div>
            </div>

            <div class="mb-4 row">
                <label class="col-sm-3 col-form-label">Total Rental Price</label>
                <div class="col-sm-9">
                    <span id="totalDisplay" class="form-control bg-light text-dark fw-bold">Select options</span>
                </div>
            </div>

            <div class="text-end">
                <button type="button" class="btn btn-warning px-4" onclick="history.back()">Back</button>
                <button type="submit" class="btn btn-success px-4">Pay with PayOS</button>
            </div>
        </form>
    </div>
</div>

<jsp:include page="footer.jsp"/>

<script>
    const DEPOSIT_RATE = 0.20; // cọc cố định 20%

    function addMonthsToDate(startDate, months) {
        const date = new Date(startDate);
        const originalDay = date.getDate();
        date.setMonth(date.getMonth() + months);
        if (date.getDate() < originalDay) date.setDate(0);
        return date;
    }

    function calculateAll() {
        const price = <%= wh.getPricePerUnit() %>;
        const size = <%= wh.getSize() %>;
        const months = parseInt(document.getElementById("months").value);
        const startDateStr = document.getElementById("startDateInput").value;

        // Base rental fee
        const baseTotal = price * size * months;

        // Extra services
        let serviceTotal = 0;
        <% for (ServiceFee sf : serviceFees) { %>
        if (document.getElementById("<%= sf.getServiceCode() %>").checked) {
            serviceTotal += <%= sf.getFee() %>;
        }
        <% } %>

        const total = baseTotal + serviceTotal;
        const deposit = total * DEPOSIT_RATE;

        // Display
        document.getElementById("depositDisplay").innerText = deposit.toLocaleString('vi-VN') + " VNĐ";
        document.getElementById("totalDisplay").innerText = total.toLocaleString('vi-VN') + " VNĐ";

        // Hidden inputs (gửi lên server)
        document.getElementById("depositHidden").value = Math.round(deposit);
        document.getElementById("totalPriceHidden").value = Math.round(total);

        // Dates
        if (startDateStr) {
            const endDate = addMonthsToDate(startDateStr, months);
            const endDateFormatted = endDate.toISOString().split('T')[0];
            document.getElementById("endDateDisplay").value = endDateFormatted;
            document.getElementById("startDateHidden").value = startDateStr;
            document.getElementById("endDateHidden").value = endDateFormatted;
        }
    }

    document.getElementById("currentURL").value = window.location.href;
    window.onload = () => {
        const today = new Date().toISOString().split('T')[0];
        const input = document.getElementById("startDateInput");
        input.min = today;
        input.value = today;
        calculateAll();
    };
</script>


</body>
</html>
