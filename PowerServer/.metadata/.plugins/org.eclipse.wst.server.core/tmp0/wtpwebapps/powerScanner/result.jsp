<%@page import="bean.powerDTO"%>
<%@page import="java.util.Vector"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%
	request.setCharacterEncoding("UTF-8");
%>
<title>Result Power DB</title>
<jsp:useBean id="dao" class="bean.powerDAO" />
<jsp:useBean id="dto" class="bean.powerDTO" />
</head>
<body>

	<table border="1" align="center" bgcolor="white">
		<tr align="center">
			<td width="50">id</td>
			<td width="80">time(sec)</td>
			<td width="350">packagename</td>
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
			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					dto = list.get(i);
		%>
		<tr>
			<td width="50" align="center"><%=dto.getId()%></td>
			<td width="80"><%=dto.getTime() / 60 + "분 " + dto.getTime() % 60 + "초"%></td>
			<td width="350"><%=dto.getPackagename()%></td>
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
		<%
			} else {
		%>
		<tr>
			<td align="center">-</td>
			<td align="center">-</td>
			<td align="center">Not exist Data.</td>
			<td align="center">-</td>
			<td align="center">-</td>
			<td align="center">-</td>
			<td align="center">-</td>
			<td align="center">-</td>
			<td align="center">-</td>
			<td align="center">-</td>
		</tr>
		<%
			}
		%>
	</table>

</body>
</html>