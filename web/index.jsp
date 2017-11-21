<%--
  Created by IntelliJ IDEA.
  User: zijie_zhu
  Date: 11/16/17
  Time: 22:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    main.java.wse.query.Main queryer = new main.java.wse.query.Main();
    session.setAttribute("queryer", queryer);
%>
<html>
<head>
  <title>Flat Search Box Responsive Widget Template | </title>
  <!-- Custom Theme files -->
  <link href="css/style.css" rel="stylesheet" type="text/css" media="all"/>
  <!-- Custom Theme files -->
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <meta name="keywords" content="Flat Search Box Responsive, Login form web template, Sign up Web Templates, Flat Web Templates, Login signup Responsive web template, Smartphone Compatible web template, free webdesigns for Nokia, Samsung, LG, SonyErricsson, Motorola web design" />
  <!--Google Fonts-->
  <link href='fonts.googleapis.com/css?family=Open+Sans:300italic,400italic,600italic,700italic,800italic,400,300,600,700,800' rel='stylesheet' type='text/css'>
  <!--Google Fonts-->
</head>
<body>
<!--search start here-->
<div class="search">
  <i> </i>
  <div class="s-bar">
    <form id = "search" name = "search" method = "post" action="search.jsp">
      <input type="text" name = "query" value="Search Template" onfocus="this.value = '';" onblur="if (this.value == '') {this.value = 'Search Template';}">
      <input type="submit"  value="Search"/>
    </form>
  </div>
</div>
<!--search end here-->
<div class="copyright">
  <p>Copyright &copy; 2017 Zijie Zhu</p>
</div>
</body>
</html>
