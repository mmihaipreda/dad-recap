<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<ul>
		<LI>
			<B>Declaration (plus expression).</B> 
			<BR />
			<%!private int accessCount = 0;%>Accesses
			to page since server reboot: <%=++accessCount%>
		</LI>
	</ul>
</body>
</html>