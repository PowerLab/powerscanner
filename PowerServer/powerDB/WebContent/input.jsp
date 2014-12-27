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
<jsp:setProperty property="*" name="dto"/>
</head>
<body>
<% 
	boolean b = dao.inputPower(dto);
	
	if(b){
		response.sendRedirect("result.jsp");

	}else{
	%>
		<script>
		alert("입력성공");
		history.back();
		</script>
	<%
	}	
%>

</body>
</html>