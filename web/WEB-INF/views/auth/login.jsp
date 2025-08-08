<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8"/>
  <title>Login – WareNexus</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"/>
</head>
<body>
  <div class="auth-card">
    <h2>Sign In</h2>
    <c:if test="${not empty param.error}">
      <p class="error">${param.error}</p>
    </c:if>
    <form action="${pageContext.request.contextPath}/login" method="post">
      <input name="email"    type="email"    placeholder="Email"    required/>
      <input name="password" type="password" placeholder="Password" required/>
      <button type="submit">Login</button>
    </form>
    <p><a href="${pageContext.request.contextPath}/register">Create an account</a></p>
  </div>
</body>
</html>