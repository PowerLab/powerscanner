<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="com.oreilly.servlet.MultipartRequest"%>
<%@ page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>PowerScanner Transmitted DB</title>
<jsp:useBean id="dao" class="bean.powerDAO" />
<jsp:useBean id="dto" class="bean.powerDTO" />
<jsp:setProperty property="*" name="dto"/>
</head>
<body>

<%
	String save = application.getRealPath("/Power_Scanner");
	int size = 10*1024*1024;
	try{		
		MultipartRequest multi = new MultipartRequest(request,null,size,"utf-8",new DefaultFileRenamePolicy());

		dto.setPackagename(new String(multi.getParameter("packagename").getBytes("UTF-8"),"iso-8859-1"));
		dto.setTotal(Integer.parseInt(new String(multi.getParameter("total").getBytes("UTF-8"),"iso-8859-1")));
		dto.setLed(Integer.parseInt(new String(multi.getParameter("led").getBytes("UTF-8"),"iso-8859-1")));
		dto.setCpu(Integer.parseInt(new String(multi.getParameter("cpu").getBytes("UTF-8"),"iso-8859-1")));
		dto.setWifi(Integer.parseInt(new String(multi.getParameter("wifi").getBytes("UTF-8"),"iso-8859-1")));
		dto.setThreeg(Integer.parseInt(new String(multi.getParameter("threeg").getBytes("UTF-8"),"iso-8859-1")));
		dto.setGps(Integer.parseInt(new String(multi.getParameter("gps").getBytes("UTF-8"),"iso-8859-1")));
		dto.setAudio(Integer.parseInt(new String(multi.getParameter("audio").getBytes("UTF-8"),"iso-8859-1")));

	}catch(Exception e){
		//e.printStackTrace();
	}
	dao.inputPower(dto);
%>
</body>
</html>