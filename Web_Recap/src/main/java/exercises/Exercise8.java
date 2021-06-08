package exercises;


import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
 


@WebServlet("/Exercise8")
public class Exercise8 extends HttpServlet{

	private static final long serialVersionUID = 1L;
	private String name;
	private int age;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Exercise8() {
		super();
		 
	}
	@SuppressWarnings("resource")
	public  String extractPostRequestBody(HttpServletRequest request) throws IOException {
	    if ("POST".equalsIgnoreCase(request.getMethod())) {
			Scanner s = new Scanner(request.getInputStream(), "UTF-8").useDelimiter("\\A");
	        return s.hasNext() ? s.next() : "";
	    }
	    return "";
	}
	public void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, ParseException{
		
		if ("POST".equalsIgnoreCase(req.getMethod())) {
			String data = this.extractPostRequestBody(req);
			JSONParser parser = new JSONParser(data);
			LinkedHashMap<String, Object> fields =  parser.parseObject();
			String nameJSON =(String)fields.get("name");
			BigInteger ageJSON =(BigInteger)fields.get("age");
			this.setName(nameJSON);
			this.setAge(ageJSON.intValue());
		    PrintWriter out = resp.getWriter();
		    this.printValues(out);
		    out.close();
		}else {
		    PrintWriter out = resp.getWriter();
		    out.write("<div>Access the following link : (Make a POST request)</div>");
		    out.write("<div> http://localhost:8080/Web_Recap/Exercise8 </div>");
		    out.write("<div>payload:</div>");
		    out.write("{\r\n"
		    		+ "    \"name\":\"Boala copiilor\",\r\n"
		    		+ "    \"age\":45\r\n"
		    		+ "}");
			out.close();
		}
		
	}
	public void printValues(PrintWriter out) {
		out.write("<div> Name :" + this.name + "</div>");
		out.write("<br/>");
		out.write("<div> Age :" + this.age + "</div>");
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			this.processRequest(req, resp);
		} catch (ServletException | IOException | ParseException e) {
			e.printStackTrace();
		}
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		try {
			this.processRequest(req, resp);
		} catch (ServletException | IOException | ParseException e) {
			e.printStackTrace();
		}
	}
	
	
}
