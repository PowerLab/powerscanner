<%@page import="bean.appDTO"%>
<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%
	request.setCharacterEncoding("UTF-8");
%>
<title>Result App DB</title>
<jsp:useBean id="dao" class="bean.appDAO" />
<jsp:useBean id="dto" class="bean.appDTO" />
</head>
<body>
	<table border="1" align="center" bgcolor="white">
		<tr align="center">
			<td width="50">id</td>
			<td width="350">packagename</td>
			<td width="150">avg_power</td>
		</tr>
		<%
			Vector<appDTO> list = dao.getapp();
			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					dto = list.get(i);
		%>
		<tr>
			<td width="50" align="center"><%=dto.getId()%></td>
			<td width="350"><%=dto.getPackagename()%></td>
			<td width="150"><%=dto.getAvg_power()%></td>
		</tr>
		<%
			}
		%>
		<%
			} else {
		%>
		<tr>
			<td align="center">-</td>
			<td align="center">Not exist Data.</td>
			<td align="center">-</td>
		</tr>
		<%
			}
		%>
	</table>
</body>
</html>