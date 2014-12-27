<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript">
function getlist() {
	ff.action = "result.jsp";
	ff.submit();
}
function setlist() {
	ff.action = "input.jsp";
	ff.submit();
}

</script>



</head>
<body>
	<form id="ff" action="" method="post">
		<table border="1">
			<tr>
				<td>id</td>
				<td><input type="text" name="id"></td>
			</tr>
			<tr>
				<td>packagename</td>
				<td><input type="text" name="packagename"></td>
			</tr>
			<tr>
				<td>total</td>
				<td><input type="text" name="total"></td>
			</tr>
			<tr>
				<td>led</td>
				<td><input type="text" name="led"></td>
			</tr>
			<tr>
				<td>cpu</td>
				<td><input type="text" name="cpu"></td>
			</tr>
			<tr>
				<td>wifi</td>
				<td><input type="text" name="wifi"></td>
			</tr>
			<tr>
				<td>threeg</td>
				<td><input type="text" name="threeg"></td>
			</tr>
			<tr>
				<td>gps</td>
				<td><input type="text" name="gps"></td>
			</tr>
			<tr>
				<td>audio</td>
				<td><input type="text" name="audio"></td>
			</tr>
			<tr>
				<td colspan="2"><input type="button" value="전송" onclick="setlist()"> <input type="button" value="리스트" onclick="getlist()"></td>
			</tr>

		</table>
	</form>
</body>
</html>