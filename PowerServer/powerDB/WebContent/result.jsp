<%@page import="bean.powerDTO"%>
<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<%
	request.setCharacterEncoding("UTF-8");
%>
<jsp:useBean id="dao" class="bean.powerDAO" />
<jsp:useBean id="dto" class="bean.powerDTO" />
<%-- <jsp:setProperty property="*" name="dto"/> --%>


</head>
<body>
	<table border="1" align="center" bgcolor="white">
		<tr align="center">
			<td width="50">id</td>
			<td width="300">packagename</td>
			<td width="50">total</td>
			<td width="50">led</td>
			<td width="50">cpu</td>
			<td width="50">wifi</td>
			<td width="50">threeg</td>
			<td width="50">gps</td>
			<td width="50">audio</td>
		</tr>

		<%
			Vector<powerDTO> list = dao.getpower();
			for (int i = 0; i < list.size(); i++) {
				dto = list.get(i);
		%>
		<tr>
			<td width="50" ><%=dto.getId()%></td>
			<td width="300"><%=dto.getPackagename()%></td>
			<td width="50"><%=dto.getTotal()%></td>
			<td width="50"><%=dto.getLed()%></td>
			<td width="50"><%=dto.getCpu()%></td>
			<td width="50"><%=dto.getWifi()%></td>
			<td width="50"><%=dto.getThreeg()%></td>
			<td width="50"><%=dto.getGps()%></td>
			<td width="50"><%=dto.getAudio()%></td>
		</tr>
		<%
			}
		%>


	</table>


</body>
</html>