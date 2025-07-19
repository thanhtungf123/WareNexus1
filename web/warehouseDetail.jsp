<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Warehouse Details - ${warehouse.name}</title>

  <!-- Bootstrap -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

  <!-- Leaflet -->
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/leaflet@1.9.4/dist/leaflet.css">
  <script src="https://cdn.jsdelivr.net/npm/leaflet@1.9.4/dist/leaflet.js"></script>

  <!-- Bootstrap Icons -->
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">

  <style>
    :root {
      --primary-blue: #2563eb;
      --secondary-blue: #3b82f6;
      --light-blue: #eff6ff;
      --dark-blue: #1e40af;
      --text-dark: #1e293b;
      --text-light: #64748b;
      --border-color: #e2e8f0;
      --success-color: #10b981;
      --warning-color: #f59e0b;
      --danger-color: #ef4444;
    }

    body {
      background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
      color: var(--text-dark);
    }

    .hero-section {
      background: linear-gradient(135deg, var(--primary-blue) 0%, var(--secondary-blue) 100%);
      color: white;
      padding: 2rem 0;
      margin-bottom: 2rem;
      border-radius: 0 0 2rem 2rem;
    }

    .hero-section h1 {
      font-size: 2.5rem;
      font-weight: 700;
      margin-bottom: 0.5rem;
    }

    .breadcrumb-custom {
      background: transparent;
      margin-bottom: 1rem;
    }

    .breadcrumb-custom .breadcrumb-item a { color: rgba(255, 255, 255, 0.8); text-decoration: none; }
    .breadcrumb-custom .breadcrumb-item a:hover { color: white; }
    .breadcrumb-custom .breadcrumb-item.active { color: white; }

    .detail-card {
      background: white;
      border-radius: 1rem;
      box-shadow: 0 4px 6px -1px rgb(0 0 0 / 0.1);
      overflow: hidden;
      margin-bottom: 2rem;
    }

    .warehouse-image {
      width: 100%; height: 400px; object-fit: cover; border-radius: 1rem 1rem 0 0;
    }

    .detail-header { padding: 2rem; border-bottom: 2px solid var(--border-color); }
    .detail-header h2 { color: var(--primary-blue); font-weight: 600; margin-bottom: 1rem; }

    .warehouse-type-badge {
      display: inline-flex; align-items: center; gap: 0.5rem;
      padding: 0.5rem 1rem; border-radius: 2rem; font-weight: 600; font-size: 0.9rem; margin-bottom: 1rem;
    }
    .type-cold     { background: #dbeafe;  color: #1e40af; }
    .type-standard { background: #f3f4f6;  color: #374151; }
    .type-outdoor  { background: #fef3c7;  color: #92400e; }

    .detail-content { padding: 2rem; }

    .info-grid { display: grid; grid-template-columns: repeat(auto-fit,minmax(300px,1fr)); gap: 1.5rem; margin-bottom: 2rem; }
    .info-item {
      display: flex; align-items: center; gap: 1rem; padding: 1rem;
      background: var(--light-blue); border-radius: 0.75rem; border-left: 4px solid var(--primary-blue);
    }
    .info-icon {
      width: 40px; height: 40px; background: var(--primary-blue);
      color: white; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 1.2rem;
    }
    .info-text { flex: 1; }
    .info-label { font-size: 0.9rem; color: var(--text-light); margin-bottom: 0.25rem; }
    .info-value { font-size: 1.1rem; font-weight: 600; color: var(--text-dark); }

    .status-badge { padding: 0.5rem 1rem; border-radius: 2rem; font-size: 0.9rem; font-weight: 600; text-transform: uppercase; }
    .status-available { background: #dcfce7; color: #166534; }
    .status-occupied  { background: #fef3c7; color: #92400e; }
    .status-maintenance{ background: #fee2e2; color: #991b1b; }

    .description-section { margin-bottom: 2rem; }
    .description-section h3 { color: var(--primary-blue); font-weight: 600; margin-bottom: 1rem; }
    .description-text {
      background: #f8fafc; padding: 1.5rem; border-radius: 0.75rem;
      border-left: 4px solid var(--primary-blue); line-height: 1.6;
    }

    .map-section {
      background: white; border-radius: 1rem; box-shadow: 0 4px 6px -1px rgb(0 0 0 / 0.1);
      padding: 2rem; margin-bottom: 2rem;
    }
    .map-section h3 { color: var(--primary-blue); font-weight: 600; margin-bottom: 1.5rem; }
    #detailMap { height: 400px; border-radius: 0.75rem; overflow: hidden; }

    .btn-back, .btn-contact, .btn-rent { border: none; color: white; border-radius: 0.5rem;
      padding: 0.75rem 2rem; font-weight: 600; transition: all 0.3s ease;
      text-decoration: none; display: inline-flex; align-items: center; gap: 0.5rem; }
    .btn-back    { background: linear-gradient(135deg, var(--text-light) 0%, #6b7280 100%); }
    .btn-contact { background: linear-gradient(135deg, var(--success-color) 0%, #059669 100%); }
    .btn-rent    { background: linear-gradient(135deg, var(--primary-blue) 0%, var(--secondary-blue) 100%); }

    .btn-back:hover    { background: linear-gradient(135deg, #4b5563 0%, var(--text-light) 100%); }
    .btn-contact:hover { background: linear-gradient(135deg, #059669 0%, var(--success-color) 100%); }
    .btn-rent:hover    { background: linear-gradient(135deg, var(--dark-blue) 0%, var(--primary-blue) 100%); }

    .btn-back:hover, .btn-contact:hover, .btn-rent:hover {
      transform: translateY(-2px); box-shadow: 0 4px 12px rgb(0 0 0 / 0.15); color: white;
    }

    .not-found-section { text-align: center; padding: 4rem 2rem; color: var(--text-light); }
    .not-found-section i { font-size: 4rem; color: var(--primary-blue); margin-bottom: 1rem; }
    .not-found-section h2 { margin-bottom: 1rem; }

    @media (max-width: 768px) {
      .hero-section h1 { font-size: 2rem; }
      .info-grid { grid-template-columns: 1fr; }
      .detail-header, .detail-content { padding: 1.5rem; }
      .warehouse-image { height: 300px; }
    }
  </style>
</head>
<body>
  <c:choose>
    <c:when test="${not empty warehouse}">
      <!-- Hero Section -->
      <div class="hero-section">
        <div class="container">
          <nav aria-label="breadcrumb">
            <ol class="breadcrumb breadcrumb-custom">
              <li class="breadcrumb-item"><a href="warehouse"><i class="bi bi-house"></i> Home</a></li>
              <li class="breadcrumb-item"><a href="warehouse">Warehouse Search</a></li>
              <li class="breadcrumb-item active" aria-current="page">${warehouse.name}</li>
            </ol>
          </nav>
          <h1><i class="bi bi-building"></i> ${warehouse.name}</h1>
        </div>
      </div>

      <div class="container">
        <div class="row">
          <!-- Main Content -->
          <div class="col-lg-8">
            <div class="detail-card">
              <img src="${warehouse.imageUrl}"
                   class="warehouse-image"
                   alt="${warehouse.name}"
                   onerror="this.src='data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNDAwIiBoZWlnaHQ9IjIwMCIgdmlld0JveD0iMCAwIDQwMCAyMDAiIGZpbGw9Im5vbmUiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+CjxyZWN0IHdpZHRoPSI0MDAiIGhlaWdodD0iMjAwIiBmaWxsPSIjZjFmNWY5Ii8+CjxwYXRoIGQ9Ik0yMDAgNzBMMTcwIDkwVjE0MEgyMzBWOTBMMjAwIDcwWiIgZmlsbD0iIzJiNTJkZiIvPgo8cGF0aCBkPSJNMTcwIDkwTDE0MCAzMFYxNDBIMTcwVjkwWiIgZmlsbD0iIzM3NjNlMCIvPgo8cGF0aCBkPSJNMjMwIDkwTDI2MCAzMFYxNDBIMjMwVjkwWiIgZmlsbD0iIzM3NjNlMCIvPgo8dGV4dCB4PSIyMDAiIHk9IjE3MCIgdGV4dC1hbmNob3I9Im1pZGRsZSIgZmlsbD0iIzY0NzQ4YiIgZm9udC1zaXplPSIxNCIgZm9udC1mYW1pbHk9IkFyaWFsIj5XYXJlaG91c2UgSW1hZ2U8L3RleHQ+Cjwvc3ZnPg=='">

              <div class="detail-header">
                <h2>${warehouse.name}</h2>

                <!-- Warehouse Type Badge -->
                <c:choose>
                  <c:when test="${warehouse.warehouseTypeId == 1}">
                    <div class="warehouse-type-badge type-cold">
                      <i class="bi bi-snow"></i> Cold Storage
                    </div>
                  </c:when>
                  <c:when test="${warehouse.warehouseTypeId == 2}">
                    <div class="warehouse-type-badge type-standard">
                      <i class="bi bi-building"></i> Standard Warehouse
                    </div>
                  </c:when>
                  <c:when test="${warehouse.warehouseTypeId == 3}">
                    <div class="warehouse-type-badge type-outdoor">
                      <i class="bi bi-sun"></i> Outdoor Storage
                    </div>
                  </c:when>
                </c:choose>

                <p class="text-muted mb-0">
                  <i class="bi bi-geo-alt"></i> ${warehouse.address}, ${warehouse.ward}, ${warehouse.district}
                </p>
              </div>

              <div class="detail-content">
                <!-- Information Grid -->
                <div class="info-grid">
                  <div class="info-item">
                    <div class="info-icon"><i class="bi bi-arrows-expand"></i></div>
                    <div class="info-text">
                      <div class="info-label">Size</div>
                      <div class="info-value">${warehouse.size} m²</div>
                    </div>
                  </div>

                  <div class="info-item">
                    <div class="info-icon"><i class="bi bi-currency-dollar"></i></div>
                    <div class="info-text">
                      <div class="info-label">Price per m²</div>
                      <div class="info-value">
                        <fmt:formatNumber value="${warehouse.pricePerUnit}" type="currency" currencySymbol="VNĐ" groupingUsed="true"/>
                      </div>
                    </div>
                  </div>

                  <div class="info-item">
                    <div class="info-icon"><i class="bi bi-info-circle"></i></div>
                    <div class="info-text">
                      <div class="info-label">Status</div>
                      <div class="info-value">
                        <span class="status-badge status-${warehouse.status.toLowerCase().replace(' ', '-')}">${warehouse.status}</span>
                      </div>
                    </div>
                  </div>

                  <c:if test="${not empty warehouse.createdAt}">
                    <div class="info-item">
                      <div class="info-icon"><i class="bi bi-calendar-plus"></i></div>
                      <div class="info-text">
                        <div class="info-label">Listed Date</div>
                        <div class="info-value">
                          <fmt:formatDate value="${warehouse.createdAt}" pattern="MMM dd, yyyy" />
                        </div>
                      </div>
                    </div>
                  </c:if>
                </div>

                <!-- Description Section -->
                <c:if test="${not empty warehouse.description}">
                  <div class="description-section">
                    <h3><i class="bi bi-file-text"></i> Description</h3>
                    <div class="description-text">${warehouse.description}</div>
                  </div>
                </c:if>

                <!-- Action Buttons -->
                <div class="d-flex gap-3 flex-wrap">
                  <a href="warehouse" class="btn-back">
                    <i class="bi bi-arrow-left"></i> Back to Search
                  </a>

                  <!-- new Rent button -->
                  <form action="create-rental-order" method="post">
                        <input type="hidden" name="warehouseId" value="${warehouse.id}" />
                        <input type="hidden" name="accountId" value="${sessionScope.user.accountId}" />
                        <input type="hidden" name="startDate" value="<%= java.time.LocalDate.now() %>" />
                        <input type="hidden" name="endDate" value="<%= java.time.LocalDate.now().plusMonths(1) %>" />

                        <button type="submit" class="btn-rent">
                          <i class="bi bi-currency-exchange"></i> Rent Now
                        </button>
                 </form>

                  <a href="#" class="btn-contact">
                    <i class="bi bi-envelope"></i> Contact Owner
                  </a>
                </div>
              </div>
            </div>
          </div>

          <!-- Map Section -->
          <div class="col-lg-4">
            <c:if test="${not empty warehouse.latitude && not empty warehouse.longitude}">
              <div class="map-section">
                <h3><i class="bi bi-geo-alt"></i> Location</h3>
                <div id="detailMap"></div>
                <div class="mt-3">
                  <small class="text-muted">
                    <i class="bi bi-info-circle"></i>
                    Coordinates: ${warehouse.latitude}, ${warehouse.longitude}
                  </small>
                </div>
              </div>
            </c:if>
          </div>
        </div>
      </div>
    </c:when>
    <c:otherwise>
      <!-- Not Found Section -->
      <div class="hero-section">
        <div class="container">
          <h1><i class="bi bi-exclamation-triangle"></i> Warehouse Not Found</h1>
        </div>
      </div>

      <div class="container">
        <div class="detail-card">
          <div class="not-found-section">
            <i class="bi bi-search"></i>
            <h2>Warehouse Not Found</h2>
            <p>The warehouse you're looking for doesn't exist or has been removed.</p>
            <a href="warehouse" class="btn-back">
              <i class="bi bi-arrow-left"></i> Back to Search
            </a>
          </div>
        </div>
      </div>
    </c:otherwise>
  </c:choose>

  <!-- Map JavaScript -->
  <c:if test="${not empty warehouse && not empty warehouse.latitude && not empty warehouse.longitude}">
    <script>
      const detailMap = L.map('detailMap').setView([${warehouse.latitude}, ${warehouse.longitude}], 15);
      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© OpenStreetMap contributors'
      }).addTo(detailMap);

      const warehouseIcon = L.icon({
        iconUrl: 'https://cdn.jsdelivr.net/npm/leaflet@1.9.4/dist/images/marker-icon.png',
        iconRetinaUrl: 'https://cdn.jsdelivr.net/npm/leaflet@1.9.4/dist/images/marker-icon-2x.png',
        shadowUrl: 'https://cdn.jsdelivr.net/npm/leaflet@1.9.4/dist/images/marker-shadow.png',
        iconSize: [25,41], iconAnchor: [12,41], popupAnchor: [1,-34], shadowSize: [41,41]
      });

      const marker = L.marker([${warehouse.latitude}, ${warehouse.longitude}], { icon: warehouseIcon }).addTo(detailMap);
      marker.bindPopup(`
        <div style="min-width:200px;">
          <h6 style="color:#2563eb;margin-bottom:8px;">${warehouse.name}</h6>
          <p style="margin-bottom:4px;font-size:0.9rem;"><i class="bi bi-geo-alt" style="color:#2563eb;"></i> ${warehouse.address}</p>
          <p style="margin-bottom:4px;font-size:0.9rem;"><i class="bi bi-arrows-expand" style="color:#2563eb;"></i> ${warehouse.size} m²</p>
          <p style="margin-bottom:0;font-size:0.9rem;"><i class="bi bi-currency-dollar" style="color:#2563eb;"></i> $${warehouse.pricePerUnit}/m²</p>
        </div>
      `);
    </script>
  </c:if>
</body>
</html>
