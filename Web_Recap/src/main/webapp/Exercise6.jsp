
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
	<jsp:useBean id="Exercise6" class="exercises.Exercise6"></jsp:useBean>
</head>
<body>

	
	<form method="post" action="Exercise6">
	First name:<br>
	<input type="text" name="first_name">
	<br>
	Last name:<br>
	<input type="text" name="last_name">
	<br>
	City name:<br>
	<input type="text" name="city_name">
	<br>
	Email Id:<br>
	<input type="email" name="email">
	<br><br>
	<input type="submit" value="submit">
	</form>

</body>
</html>