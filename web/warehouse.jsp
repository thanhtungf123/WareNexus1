<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>WareNexus</title>
  <link rel="stylesheet" href="css/style.css">
</head>
<body>

  <!-- Header -->
    <header class="header">
    <div class="container header-container">
      <div class="logo">
        <img  src="${pageContext.request.contextPath}/images/warehouse.png"  alt="WareNexus Logo" class="logo-img">
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
    <!-- Trỏ tới signin.jsp -->
    <a href="${pageContext.request.contextPath}/login.jsp"  class="btn btn-outline">Sign in</a>
    <!-- Trỏ tới signup.jsp -->
    <a href="${pageContext.request.contextPath}/register.jsp"  class="btn btn-primary">Sign up</a>
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
  <footer class="footer">
    <div class="container footer-container">
      <div class="footer-section">
        <div class="logo">
          <img  src="${pageContext.request.contextPath}/images/warehouse.png"  alt="WareNexus Logo" class="logo-img"><h4>WareNexus</h4>
          <span class="logo-text">WareNexus</span>
        </div>
        <p>DaNang's leading warehouse solution with a diverse and constantly updated warehouse ecosystem.</p>
      </div>
      <div class="footer-section">
        <h4>Services</h4>
        <ul>
          <li>Standard Warehouse Rental</li>
          <li>High-Security Warehouse</li>
          <li>Logistics Warehouse</li>
          <li>Transportation Services</li>
        </ul>
      </div>
      <div class="footer-section">
        <h4>Support</h4>
        <ul>
          <li>Free Consultation</li>
          <li>24/7 Support</li>
          <li>Quick Quote</li>
          <li>Site Survey</li>
        </ul>
      </div>
      <div class="footer-section">
        <h4>Contact</h4>
        <ul>
          <li>Hotline: 1900-1234</li>
          <li>Email: diemtknde170116@fpt.edu.vn</li>
          <li>Vo Qui Huan, FPT City Urban Area, Ngu Hanh Son, Da Nang, Vietnam</li>
        </ul>
      </div>
    </div>
    <div class="footer-bottom">
      <p>© 2025 WareNexus. All rights reserved.</p>
    </div>
  </footer>

</body>
</html>
