<%@ page import="main.java.wse.query.processor.QueryResult" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="main.java.wse.query.Main" %><%--
  Created by IntelliJ IDEA.
  User: zijie_zhu
  Date: 11/17/17
  Time: 01:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%
    String query = request.getParameter("query");
    Object queryer = session.getAttribute("queryer");
    Main queryer2 = (Main)queryer;
    List<QueryResult> list = queryer2.orQuery(query);
    if (list != null && list.size() > 0) {
%>
<p><%
    for (int i = 0;i < list.size(); i++ ){

%></p>
<p>------------Result <%=i + 1%>----------------</p>
<p><%=list.get(i).getDocID()%></p>
<p><%=list.get(i).getScore()%></p
<p><%=list.get(i).getUrl()%></p>
<p><%=list.get(i).getSnippet()%></p>
<% }}else {
%>
<p>Nothing matched!</p>
<%}%>
</body>
</html>
