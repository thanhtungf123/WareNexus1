<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html lang="en">
<head>
    <meta charset="utf-8"/>
    <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="css/style.css" rel="stylesheet">
    <title>Warehouse List</title>
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            background: #f4f6f8;
            padding: 20px;
        }

        h2 {
            text-align: center;
            margin-bottom: 30px;
        }

        .card-container {
            display: flex;
            flex-wrap: wrap;
            gap: 20px;
            justify-content: center;
        }

        .warehouse-card {
            background: #fff;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            padding: 20px;
            width: 300px;
            cursor: pointer;
            transition: transform 0.3s ease;
        }

        .warehouse-card:hover {
            transform: scale(1.03);
        }

        .progress-container {
            display: none;
            flex-wrap: wrap;
            justify-content: space-between;
            align-items: center;
            max-width: 600px;
            margin: 20px auto;
            position: relative;
            padding: 20px 0;
        }

        .progress-step {
            text-align: center;
            flex: 1 1 80px;
            min-width: 60px;
            position: relative;
            z-index: 1;
            transition: all 0.3s ease;
        }

        .step-icon {
            width: 36px;
            height: 36px;
            line-height: 36px;
            border-radius: 50%;
            background: #ccc;
            color: white;
            margin: auto;
            font-size: 18px;
            transition: all 0.3s ease;
        }

        .step-label {
            margin-top: 6px;
            font-size: 12px;
            transition: color 0.3s ease;
        }

        .active .step-icon {
            background: #1976d2;
            transform: scale(1.2);
            box-shadow: 0 0 8px rgba(25, 118, 210, 0.6);
        }

        .active .step-label {
            font-weight: bold;
            color: #1976d2;
        }

        .progress-step:hover .step-icon {
            transform: scale(1.3);
            background: #0d47a1;
        }

        .progress-step:hover .step-label {
            color: #0d47a1;
        }

        .progress-container::before {
            content: '';
            position: absolute;
            top: 36px;
            left: 5%;
            right: 5%;
            height: 4px;
            background: #ccc;
            z-index: 0;
        }

        .progress-fill {
            content: '';
            position: absolute;
            top: 36px;
            left: 5%;
            height: 4px;
            background: #1976d2;
            z-index: 0;
            width: 0;
            transition: width 1s ease;
        }

        .warehouse-card.active {
            border: 2px solid #1976d2;
            box-shadow: 0 0 12px rgba(25, 118, 210, 0.3);
        }
    </style>
    <style>
        .layout-container {
            display: flex;
            gap: 20px;
            align-items: flex-start;
        }

        .left-column {
            flex: 1;
            display: flex;
            flex-direction: column;
            gap: 16px;
        }

        .right-column {
            flex: 2;
        }

        .separator {
            width: 2px;
            background: #ccc;
            height: 100%;
            margin: 0 10px;
        }

        .detail-card {
            background: #fff;
            border-radius: 10px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
            padding: 16px;
            margin-top: 20px;
        }
    </style>
</head>
<body>
<jsp:include page="header.jsp" />
<div class="layout-container" style="margin-top: 10px; margin-bottom: 10px">
    <div class="left-column">
        <div class="card-container">
            <c:forEach var="order" items="${rentalHistory}">
                <div class="warehouse-card" onclick="showProgress('${order.getRentalOrderID()}', '${order.getStatus()}')">
                    <h3>🏢 ${order.getWarehouseID()}</h3>
                    <p><strong>Address:</strong> ${order.getWarehouseID()}</p>
                    <p><strong>Order Status:</strong> ${order.getStatus()}</p>
                </div>
            </c:forEach>
        </div>
    </div>
    <div class="separator"></div>
    <div class="right-column">
        <div class="progress-container" id="progress1">
            <div class="progress-fill"></div>
            <div class="progress-step">
                <div class="step-icon">⏳</div>
                <div class="step-label">Pending Approval</div>
            </div>
            <div class="progress-step">
                <div class="step-icon">✅</div>
                <div class="step-label">Approved</div>
            </div>
            <div class="progress-step">
                <div class="step-icon">💰</div>
                <div class="step-label">Paid</div>
            </div>
            <div class="progress-step">
                <div class="step-icon">📝</div>
                <div class="step-label">Contract Signed</div>
            </div>
            <div class="progress-step">
                <div class="step-icon">🎉</div>
                <div class="step-label">Completed</div>
            </div>
        </div>
        <div class="progress-container" id="progress2">
            <div class="progress-fill"></div>
            <div class="progress-step">
                <div class="step-icon">⏳</div>
                <div class="step-label">Pending Approval</div>
            </div>
            <div class="progress-step">
                <div class="step-icon">✅</div>
                <div class="step-label">Approved</div>
            </div>
            <div class="progress-step">
                <div class="step-icon">💰</div>
                <div class="step-label">Paid</div>
            </div>
            <div class="progress-step">
                <div class="step-icon">📝</div>
                <div class="step-label">Contract Signed</div>
            </div>
            <div class="progress-step">
                <div class="step-icon">🎉</div>
                <div class="step-label">Completed</div>
            </div>
        </div>
        <div class="progress-container" id="progress3">
            <div class="progress-fill"></div>
            <div class="progress-step">
                <div class="step-icon">⏳</div>
                <div class="step-label">Pending Approval</div>
            </div>
            <div class="progress-step">
                <div class="step-icon">✅</div>
                <div class="step-label">Approved</div>
            </div>
            <div class="progress-step">
                <div class="step-icon">💰</div>
                <div class="step-label">Paid</div>
            </div>
            <div class="progress-step">
                <div class="step-icon">📝</div>
                <div class="step-label">Contract Signed</div>
            </div>
            <div class="progress-step">
                <div class="step-icon">🎉</div>
                <div class="step-label">Completed</div>
            </div>
        </div>
        <div class="detail-card" id="warehouse-detail" style="display: flex; justify-content: center; align-items: center; text-align: center; flex-direction: column;">
            Warehouse details will appear here when a warehouse is selected.
        </div>
    </div>
</div>
<jsp:include page="footer.jsp" />
<script>
    document.addEventListener("DOMContentLoaded", function() {
        const statusOrder = ['pending', 'approved', 'paid', 'contract', 'completed'];
        const statusLabels = {
            pending: '⏳ Pending Approval',
            approved: '✅ Approved',
            paid: '💰 Paid',
            contract: '📝 Contract Signed',
            completed: '🎉 Completed'
        };

        const warehouses = [
            {
                id: 'progress1',
                name: 'Hanoi Warehouse',
                address: '12 Tran Duy Hung, Cau Giay',
                status: 'paid'
            },
            {
                id: 'progress2',
                name: 'Ho Chi Minh City Warehouse',
                address: '45 Le Loi, District 1',
                status: 'approved'
            },
            {
                id: 'progress3',
                name: 'Da Nang Warehouse',
                address: '123 Nguyen Van Linh',
                status: 'completed'
            }
        ];

        function showProgress(id, currentStatus) {
            document.querySelectorAll('.progress-container').forEach(p => p.style.display = 'none');
            const container = document.getElementById(id);
            if (!container) return;
            container.style.display = 'flex';

            const steps = container.querySelectorAll('.progress-step');
            const fill = container.querySelector('.progress-fill');
            const currentIndex = statusOrder.indexOf(currentStatus);

            steps.forEach((step, index) => {
                step.classList.remove('active');
                if (index <= currentIndex) {
                    step.classList.add('active');
                }
            });

            const percent = (currentIndex / (statusOrder.length - 1)) * 90 + 5;
            fill.style.width = percent + '%';
        }

        const cards = document.querySelectorAll('.warehouse-card');
        cards.forEach((card, index) => {
            card.addEventListener('click', () => {
                const warehouse = warehouses[index];
                showProgress(warehouse.id, warehouse.status);

                const detail = document.getElementById('warehouse-detail');
                if (detail) {
                    detail.innerHTML = `
                            <div class="warehouse-card" style="width:100%; max-width:500px; margin-top:20px;">
                                <h3>🏢 ${warehouse.name}</h3>
                                <p><strong>Address:</strong> ${warehouse.address}</p>
                                <p><strong>Order Status:</strong> ${statusLabels[warehouse.status]}</p>
                            </div>
                        `;
                }
            });
        });
    });
</script>
</body>

</html>
