<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Input Power DB</title>
<jsp:useBean id="dao" class="bean.powerDAO" />
<jsp:useBean id="dto" class="bean.powerDTO" />
<jsp:setProperty property="*" name="dto" />
</head>
<body>
	<%
		dto.setPackagename(request.getParameter("packagename"));
		dto.setTotal(Integer.parseInt(request.getParameter("total")));
		dto.setLed(Integer.parseInt(request.getParameter("led")));
		dto.setCpu(Integer.parseInt(request.getParameter("cpu")));
		dto.setWifi(Integer.parseInt(request.getParameter("wifi")));
		dto.setThreeg(Integer.parseInt(request.getParameter("threeg")));
		dto.setGps(Integer.parseInt(request.getParameter("gps")));
		dto.setAudio(Integer.parseInt(request.getParameter("audio")));
		dto.setTime(Integer.parseInt(request.getParameter("time")));

		dao.inputPower(dto);
	%>
</body>
</html>