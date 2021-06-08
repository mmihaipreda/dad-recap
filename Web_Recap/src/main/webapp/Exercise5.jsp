<%@page import="java.util.Base64"%>
<%@page import="javax.crypto.SecretKey"%>
<%@page import="javax.crypto.KeyGenerator"%>
<%@page import="java.security.Key"%>
<%@page import="javax.crypto.Cipher"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<!-- ### Exercise 5 -Write the JSP which will encrypt and decrypt a parameter with AES which is coming within a HTTP request -->
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<%
	response.setContentType("text/html");
	String input =request.getParameter("input");
/***************************ENCRYPTION********************************************************/	
	String algorithm="AES/ECB/PKCS5Padding";
    // init key
	KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
    keyGenerator.init(128);
    SecretKey key = keyGenerator.generateKey();
	//init AES cipher

	Cipher aesCipher =  Cipher.getInstance(algorithm); 
	aesCipher.init(Cipher.ENCRYPT_MODE, key);
	byte[] cipherText = aesCipher.doFinal(input.getBytes());

   String encodedEncrypted = Base64.getEncoder().encodeToString(cipherText);
   /******************************************************************************************/
   
   /***************************DECRYPTION*****************************************************/	
   
    aesCipher = Cipher.getInstance(algorithm);
    aesCipher.init(Cipher.DECRYPT_MODE, key);
    byte[] plainText = aesCipher.doFinal(Base64.getDecoder().decode(encodedEncrypted));
    String decoded = new String(plainText);
   
   /******************************************************************************************/	
	%>
	<div>
	INPUT :<%=input %>
	</div>
	<br/>
	
	<div>
	ENCRYPTED AND ENCODED :<%=encodedEncrypted %>
	</div>
	<br/>
	
	<div>
	DECODED :<%=decoded %>
	</div>
	<br/>
</body>
</html>