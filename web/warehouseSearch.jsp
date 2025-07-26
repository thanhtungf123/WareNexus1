<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Warehouse Search - Find Your Perfect Space</title>

  <!-- Bootstrap -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  
  <!-- Leaflet & MarkerCluster -->
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/leaflet@1.9.4/dist/leaflet.css">
  <script src="https://cdn.jsdelivr.net/npm/leaflet@1.9.4/dist/leaflet.js"></script>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/leaflet.markercluster@1.5.3/dist/MarkerCluster.css">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/leaflet.markercluster@1.5.3/dist/MarkerCluster.Default.css">
  <script src="https://cdn.jsdelivr.net/npm/leaflet.markercluster@1.5.3/dist/leaflet.markercluster.js"></script>

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
    }

    body {
      background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
      font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
      color: var(--text-dark);
    }

    .hero-section {
      background: linear-gradient(135deg, var(--primary-blue) 0%, var(--secondary-blue) 100%);
      color: white;
      padding: 3rem 0;
      margin-bottom: 2rem;
      border-radius: 0 0 2rem 2rem;
    }

    .hero-section h1 {
      font-size: 2.5rem;
      font-weight: 700;
      margin-bottom: 0.5rem;
    }

    .hero-section p {
      font-size: 1.1rem;
      opacity: 0.9;
      margin-bottom: 0;
    }

    .search-section {
      background: white;
      border-radius: 1rem;
      box-shadow: 0 4px 6px -1px rgb(0 0 0 / 0.1);
      padding: 2rem;
      margin-bottom: 2rem;
    }

    .search-section h3 {
      color: var(--primary-blue);
      font-weight: 600;
      margin-bottom: 1.5rem;
    }

    .form-control {
      border: 2px solid var(--border-color);
      border-radius: 0.5rem;
      padding: 0.75rem 1rem;
      transition: all 0.3s ease;
    }

    .form-control:focus {
      border-color: var(--primary-blue);
      box-shadow: 0 0 0 3px rgb(37 99 235 / 0.1);
    }

    .btn-primary {
      background: linear-gradient(135deg, var(--primary-blue) 0%, var(--secondary-blue) 100%);
      border: none;
      border-radius: 0.5rem;
      padding: 0.75rem 2rem;
      font-weight: 600;
      transition: all 0.3s ease;
    }

    .btn-primary:hover {
      background: linear-gradient(135deg, var(--dark-blue) 0%, var(--primary-blue) 100%);
      transform: translateY(-2px);
      box-shadow: 0 4px 12px rgb(37 99 235 / 0.3);
    }

    .results-section {
      background: white;
      border-radius: 1rem;
      box-shadow: 0 4px 6px -1px rgb(0 0 0 / 0.1);
      padding: 1.5rem;
      margin-bottom: 2rem;
    }

    .results-header {
      display: flex;
      align-items: center;
      justify-content: space-between;
      margin-bottom: 1.5rem;
      padding-bottom: 1rem;
      border-bottom: 2px solid var(--border-color);
    }

    .results-header h3 {
      color: var(--primary-blue);
      font-weight: 600;
      margin: 0;
    }

    .results-count {
      background: var(--light-blue);
      color: var(--primary-blue);
      padding: 0.5rem 1rem;
      border-radius: 2rem;
      font-weight: 600;
    }

    .warehouse-card {
      background: white;
      border: 2px solid var(--border-color);
      border-radius: 1rem;
      transition: all 0.3s ease;
      overflow: hidden;
      height: 100%;
    }

    .warehouse-card:hover {
      border-color: var(--primary-blue);
      transform: translateY(-4px);
      box-shadow: 0 8px 25px rgb(37 99 235 / 0.15);
    }

    .warehouse-card img {
      height: 200px;
      object-fit: cover;
      transition: transform 0.3s ease;
    }

    .warehouse-card:hover img {
      transform: scale(1.05);
    }

    .card-body {
      padding: 1.5rem;
    }

    .card-title {
      color: var(--primary-blue);
      font-weight: 600;
      font-size: 1.25rem;
      margin-bottom: 0.75rem;
    }

    .card-text {
      color: var(--text-light);
      margin-bottom: 1rem;
    }

    .warehouse-details {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 0.5rem;
      margin-bottom: 1rem;
    }

    .detail-item {
      display: flex;
      align-items: center;
      gap: 0.5rem;
      font-size: 0.9rem;
    }

    .detail-item i {
      color: var(--primary-blue);
      width: 16px;
    }

    .status-badge {
      display: inline-block;
      padding: 0.25rem 0.75rem;
      border-radius: 2rem;
      font-size: 0.8rem;
      font-weight: 600;
      text-transform: uppercase;
    }

    .status-available {
      background: #dcfce7;
      color: #166534;
    }

    .status-occupied {
      background: #fef3c7;
      color: #92400e;
    }

    .status-maintenance {
      background: #fee2e2;
      color: #991b1b;
    }

    .btn-outline-primary {
      border: 2px solid var(--primary-blue);
      color: var(--primary-blue);
      border-radius: 0.5rem;
      padding: 0.5rem 1.5rem;
      font-weight: 600;
      transition: all 0.3s ease;
    }

    .btn-outline-primary:hover {
      background: var(--primary-blue);
      color: white;
      transform: translateY(-2px);
    }

    .map-section {
      background: white;
      border-radius: 1rem;
      box-shadow: 0 4px 6px -1px rgb(0 0 0 / 0.1);
      padding: 1.5rem;
      margin-bottom: 2rem;
    }

    .map-section h3 {
      color: var(--primary-blue);
      font-weight: 600;
      margin-bottom: 1.5rem;
    }

    #map {
      height: 600px;
      border-radius: 0.75rem;
      overflow: hidden;
    }

    .no-results {
      text-align: center;
      padding: 3rem;
      color: var(--text-light);
    }

    .no-results i {
      font-size: 4rem;
      color: var(--primary-blue);
      margin-bottom: 1rem;
    }

    .warehouse-type-buttons {
      display: flex;
      gap: 0.5rem;
      flex-wrap: wrap;
    }

    .warehouse-type-buttons-status {
      display: flex;
      gap: 0.5rem;
      margin-bottom: 1rem;
      flex-wrap: wrap;
    }

    .btn-type-filter {
      background: white;
      border: 2px solid var(--border-color);
      color: var(--text-dark);
      border-radius: 0.5rem;
      padding: 0.75rem 1rem;
      font-weight: 500;
      transition: all 0.3s ease;
      flex: 1;
      min-width: 120px;
    }

    .btn-type-filter:hover {
      border-color: var(--primary-blue);
      background: var(--light-blue);
      color: var(--primary-blue);
      transform: translateY(-2px);
    }

    .btn-type-filter.active {
      background: linear-gradient(135deg, var(--primary-blue) 0%, var(--secondary-blue) 100%);
      border-color: var(--primary-blue);
      color: white;
    }

    .btn-type-filter.active:hover {
      background: linear-gradient(135deg, var(--dark-blue) 0%, var(--primary-blue) 100%);
      transform: translateY(-2px);
    }

    .btn-type-filter-status {
      background: white;
      border: 2px solid var(--border-color);
      color: var(--text-dark);
      border-radius: 0.5rem;
      padding: 0.75rem 1rem;
      font-weight: 500;
      transition: all 0.3s ease;
      flex: 1;
      min-width: 120px;
    }

    .btn-type-filter-status:hover {
      border-color: var(--primary-blue);
      background: var(--light-blue);
      color: var(--primary-blue);
      transform: translateY(-2px);
    }

    .btn-type-filter-status.active {
      background: linear-gradient(135deg, var(--primary-blue) 0%, var(--secondary-blue) 100%);
      border-color: var(--primary-blue);
      color: white;
    }

    .btn-type-filter-status.active:hover {
      background: linear-gradient(135deg, var(--dark-blue) 0%, var(--primary-blue) 100%);
      transform: translateY(-2px);
    }

    @media (max-width: 768px) {
      .hero-section h1 {
        font-size: 2rem;
      }
      
      .search-section {
        padding: 1.5rem;
      }
      
      .warehouse-details {
        grid-template-columns: 1fr;
      }
    }
  </style>
</head>
<body>
  <!-- Hero Section -->
  <div class="hero-section" style="background-color: #343a40; padding: 60px 0; color: white;">
    <div class="container">
      <div class="row align-items-center">
        <div class="col-lg-8">
          <a href="index.jsp" style="color: white; text-decoration: none;">
            <h1><i class="bi bi-buildings"></i> Warehouse Search</h1>
          </a>
          <p>Find the perfect warehouse space for your business needs</p>
        </div>
      </div>
    </div>
  </div>


  <div class="container">
    <!-- Search Section -->
    <div class="search-section">
      <h3><i class="bi bi-search"></i> Search Filters</h3>
      <form id="searchForm" method="get" action="warehouse">
        <div class="row g-3">
          <div class="col-md-6">
            <label class="form-label">Warehouse Type</label>
            <div class="warehouse-type-buttons">
              <input type="hidden" id="typeId" name="typeId" value="${param.typeId}">
              <button type="button" class="btn btn-type-filter ${param.typeId == '1' ? 'active' : ''}" data-type="1">
                <i class="bi bi-snow"></i> Cold
              </button>
              <button type="button" class="btn btn-type-filter ${param.typeId == '2' ? 'active' : ''}" data-type="2">
                <i class="bi bi-building"></i> Standard
              </button>
              <button type="button" class="btn btn-type-filter ${param.typeId == '3' ? 'active' : ''}" data-type="3">
                <i class="bi bi-sun"></i> Outdoor
              </button>
              <button type="button" class="btn btn-type-filter ${empty param.typeId ? 'active' : ''}" data-type="">
                <i class="bi bi-grid"></i> All Types
              </button>
            </div>
          </div>
          <div class="col-md-3">
            <label for="sizeMin" class="form-label">Min Size (m²)</label>
            <div class="input-group">
              <span class="input-group-text"><i class="bi bi-arrows-expand"></i></span>
              <input type="number" id="sizeMin" name="sizeMin" value="${param.sizeMin}" class="form-control" placeholder="0">
            </div>
          </div>
          <div class="col-md-3">
            <label for="sizeMax" class="form-label">Max Size (m²)</label>
            <div class="input-group">
              <span class="input-group-text"><i class="bi bi-arrows-expand"></i></span>
              <input type="number" id="sizeMax" name="sizeMax" value="${param.sizeMax}" class="form-control" placeholder="No limit">
            </div>
          </div>
          <div class="col-md-3">
            <label for="priceMin" class="form-label">Min Price ($/m²)</label>
            <div class="input-group">
              <span class="input-group-text"><i class="bi bi-currency-dollar"></i></span>
              <input type="number" step="0.01" id="priceMin" name="priceMin" value="${param.priceMin}" class="form-control" placeholder="0.00">
            </div>
          </div>
          <div class="col-md-3">
            <label for="priceMax" class="form-label">Max Price ($/m²)</label>
            <div class="input-group">
              <span class="input-group-text"><i class="bi bi-currency-dollar"></i></span>
              <input type="number" step="0.01" id="priceMax" name="priceMax" value="${param.priceMax}" class="form-control" placeholder="No limit">
            </div>
          </div>
          <div class="col-md-6 d-flex align-items-end">
            <button type="submit" class="btn btn-primary w-100">
              <i class="bi bi-search"></i> Search Warehouses
            </button>
          </div>
          <div class="warehouse-type-buttons-status">
            <input type="hidden" id="status" name="status" value="${param.status}">
            <button type="button" class="btn btn-type-filter-status ${param.status == 'Rented' ? 'active' : ''}" data-status="Rented">
              <i class="bi-box-seam"></i> Rented
            </button>
            <button type="button" class="btn btn-type-filter-status ${param.status == 'Available' ? 'active' : ''}" data-status="Available">
              <i class="bi-check-circle"></i> Available
            </button>
            <button type="button" class="btn btn-type-filter-status ${param.status == 'Maintenance' ? 'active' : ''}" data-status="Maintenance">
              <i class="bi-tools"></i> Maintenance
            </button>
            <button type="button" class="btn btn-type-filter-status ${empty param.status ? 'active' : ''}" data-status="">
              <i class="bi-layers"></i> All Types
            </button>
          </div>
        </div>
      </form>
    </div>

    <!-- Results Layout -->
    <div class="row g-4">
      <!-- Results Section -->
      <div class="col-lg-6">
        <div class="results-section">
          <div class="results-header">
            <h3><i class="bi bi-list-ul"></i> Search Results</h3>
            <c:if test="${not empty warehouses}">
              <div class="results-count">
                ${warehouses.size()} warehouse<c:if test="${warehouses.size() != 1}">s</c:if> found
              </div>
            </c:if>
          </div>

          <div class="row g-3">
            <c:forEach var="w" items="${warehouses}">
              <div class="col-12">
                <div class="warehouse-card">
                  <img src="${w.imageUrl}" class="card-img-top" alt="Warehouse ${w.id}" onerror="this.src='data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNDAwIiBoZWlnaHQ9IjIwMCIgdmlld0JveD0iMCAwIDQwMCAyMDAiIGZpbGw9Im5vbmUiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+CjxyZWN0IHdpZHRoPSI0MDAiIGhlaWdodD0iMjAwIiBmaWxsPSIjZjFmNWY5Ii8+CjxwYXRoIGQ9Ik0yMDAgNzBMMTcwIDkwVjE0MEgyMzBWOTBMMjAwIDcwWiIgZmlsbD0iIzJiNTJkZiIvPgo8cGF0aCBkPSJNMTcwIDkwTDE0MCAzMFYxNDBIMTcwVjkwWiIgZmlsbD0iIzM3NjNlMCIvPgo8cGF0aCBkPSJNMjMwIDkwTDI2MCAzMFYxNDBIMjMwVjkwWiIgZmlsbD0iIzM3NjNlMCIvPgo8dGV4dCB4PSIyMDAiIHk9IjE3MCIgdGV4dC1hbmNob3I9Im1pZGRsZSIgZmlsbD0iIzY0NzQ4YiIgZm9udC1zaXplPSIxNCIgZm9udC1mYW1pbHk9IkFyaWFsIj5XYXJlaG91c2UgSW1hZ2U8L3RleHQ+Cjwvc3ZnPg=='">
                  <div class="card-body">
                    <h5 class="card-title">${w.name}</h5>
                    <p class="card-text">
                      <i class="bi bi-geo-alt"></i> ${w.address}, ${w.ward}, ${w.district}
                    </p>
                    <div class="warehouse-details">
                      <div class="detail-item">
                        <i class="bi bi-arrows-expand"></i>
                        <span>${w.size} m²</span>
                      </div>
                      <div class="detail-item">
                        <i class="bi bi-currency-dollar"></i>
                        <span>$${w.pricePerUnit}/m²</span>
                      </div>
                    </div>
                    <div class="mb-3">
                      <span class="status-badge status-${w.status.toLowerCase().replace(' ', '-')}">${w.status}</span>
                    </div>
                    <div class="d-flex justify-content-between align-items-center">
                      <small class="text-muted">
                        <i class="bi bi-info-circle"></i> ID: ${w.id}
                      </small>
                      <a href="warehouse?id=${w.id}" class="btn btn-outline-primary">
                        <i class="bi bi-eye"></i> View Details
                      </a>
                    </div>
                  </div>
                </div>
              </div>
            </c:forEach>
            
            <c:if test="${empty warehouses}">
              <div class="col-12">
                <div class="no-results">
                  <i class="bi bi-search"></i>
                  <h4>No warehouses found</h4>
                  <p>Try adjusting your search filters to find more results.</p>
                </div>
              </div>
            </c:if>
          </div>
        </div>
      </div>

      <!-- Map Section -->
      <div class="col-lg-6">
        <div class="map-section">
          <h3><i class="bi bi-map"></i> Location Map</h3>
          <div id="map"></div>
        </div>
      </div>
    </div>
  </div>

  <!-- Scripts -->
  <script>
    const ctx = '<%= request.getContextPath() %>';

    // Custom blue marker icon
    const blueIcon = L.icon({
      iconUrl: 'https://cdn.jsdelivr.net/npm/leaflet@1.9.4/dist/images/marker-icon.png',
      iconRetinaUrl: 'https://cdn.jsdelivr.net/npm/leaflet@1.9.4/dist/images/marker-icon-2x.png',
      shadowUrl: 'https://cdn.jsdelivr.net/npm/leaflet@1.9.4/dist/images/marker-shadow.png',
      iconSize: [25, 41],
      iconAnchor: [12, 41],
      popupAnchor: [1, -34],
      shadowSize: [41, 41]
    });

    // Initialize map
    const map = L.map('map').setView([16.0544, 108.2022], 11);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '© OpenStreetMap contributors'
    }).addTo(map);

    // Marker cluster group
    const markerCluster = L.markerClusterGroup({
      disableClusteringAtZoom: 14,
      maxClusterRadius: 50
    });
    map.addLayer(markerCluster);

    // Optional: show loading overlay
    function showLoadingOverlay() {
      document.getElementById('map').classList.add('loading');
    }
    function hideLoadingOverlay() {
      document.getElementById('map').classList.remove('loading');
    }

    // Geocoding fallback
    async function getCoordinates(address) {
      const response = await fetch(`https://nominatim.openstreetmap.org/search?format=json&q=${param.address}`);
      const data = await response.json();
      if (data.length > 0) {
        return { lat: parseFloat(data[0].lat), lon: parseFloat(data[0].lon) };
      }
      return null;
    }

    // Build query string from form
    function buildQueryString() {
      const form = document.getElementById('searchForm');
      const formData = new FormData(form);
      const params = new URLSearchParams(formData);
      params.append('json', '1');
      return params.toString();
    }

    // Load markers on map
    async function loadMarkers() {
      markerCluster.clearLayers();
      showLoadingOverlay();

      try {
        const response = await fetch(ctx + '/warehouse?' + buildQueryString());
        const warehouses = await response.json();

        const validMarkers = [];

        for (const warehouse of warehouses) {
          let lat = warehouse.latitude;
          let lon = warehouse.longitude;

          // Fallback if coordinates are missing
          if (!lat || !lon) {
            const fullAddress = `${warehouse.address}, ${warehouse.ward}, ${warehouse.district}, Đà Nẵng, Vietnam`;
            const coords = await getCoordinates(fullAddress);
            if (coords) {
              lat = coords.lat;
              lon = coords.lon;
            } else {
              console.warn(`Warehouse ${warehouse.id} has invalid address`);
              continue;
            }
          }

          const popupContent = `
        <div style="min-width: 200px;">
          <h6 style="color: #2563eb; margin-bottom: 8px;">${warehouse.name}</h6>
          <p style="margin-bottom: 4px; font-size: 0.9rem;">
            <i class="bi bi-geo-alt" style="color: #2563eb;"></i>
            ${warehouse.address}, ${warehouse.ward}, ${warehouse.district}
          </p>
          <p style="margin-bottom: 4px; font-size: 0.9rem;">
            <i class="bi bi-arrows-expand" style="color: #2563eb;"></i>
            ${warehouse.size} m²
          </p>
          <p style="margin-bottom: 8px; font-size: 0.9rem;">
            <i class="bi bi-currency-dollar" style="color: #2563eb;"></i>
            $${warehouse.pricePerUnit}/m²
          </p>
          <button
            class="btn btn-sm btn-primary"
            onclick="location.href='${ctx}/warehouse?id=${warehouse.id}'"
            style="background: linear-gradient(135deg, #2563eb 0%, #3b82f6 100%); border: none;"
          >
            <i class="bi bi-eye"></i> View Details
          </button>
        </div>
      `;

          const marker = L.marker([lat, lon], { icon: blueIcon });
          marker.bindPopup(popupContent);
          validMarkers.push(marker);
        }

        validMarkers.forEach(marker => markerCluster.addLayer(marker));

        // Adjust map view
        if (validMarkers.length === 1) {
          map.setView(validMarkers[0].getLatLng(), 15);
        } else if (validMarkers.length > 1) {
          map.fitBounds(markerCluster.getBounds());
        }

      } catch (error) {
        console.error('Error loading markers:', error);
      } finally {
        hideLoadingOverlay();
      }
    }

    // Load initial markers
    loadMarkers();

    // Handle form submission
    document.getElementById('searchForm').addEventListener('submit', function(e) {
      setTimeout(loadMarkers, 100);
    });

    // Handle warehouse type filter buttons
    document.querySelectorAll('.btn-type-filter').forEach(button => {
      button.addEventListener('click', function(e) {
        e.preventDefault();
        document.querySelectorAll('.btn-type-filter').forEach(btn => btn.classList.remove('active'));
        this.classList.add('active');
        document.getElementById('typeId').value = this.getAttribute('data-type');
        document.getElementById('searchForm').submit();
      });
    });

    // Handle warehouse status filter buttons
    document.querySelectorAll('.btn-type-filter-status').forEach(button => {
      button.addEventListener('click', function(e) {
        e.preventDefault();
        document.querySelectorAll('.btn-type-filter-status').forEach(btn => btn.classList.remove('active'));
        this.classList.add('active');
        document.getElementById('status').value = this.getAttribute('data-status');
        document.getElementById('searchForm').submit();
      });
    });

    // Add loading animation to search button
    document.getElementById('searchForm').addEventListener('submit', function(e) {
      const button = this.querySelector('button[type="submit"]');
      const originalText = button.innerHTML;
      button.innerHTML = '<i class="bi bi-hourglass-split"></i> Searching...';
      button.disabled = true;
      setTimeout(() => {
        button.innerHTML = originalText;
        button.disabled = false;
      }, 1000);
    });
  </script>
</body>
</html>
