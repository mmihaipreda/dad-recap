package exercises;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Exercise6")
public class Exercise6 extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Exercise6() {
		super();
	}

	public void duplicate(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String first_name = req.getParameter("first_name");
		String last_name = req.getParameter("last_name");
		String city_name = req.getParameter("city_name");
		String email = req.getParameter("email");
		System.out.println(first_name + " " + last_name + " " + city_name + " " + email);
		try {
			PrintWriter out = resp.getWriter();
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "");
			String insertStatement = "insert into users values(?,?,?,?)";
			PreparedStatement pStatement = conn.prepareStatement(insertStatement);
			pStatement.setString(1, first_name);
			pStatement.setString(2, last_name);
			pStatement.setString(3, city_name);
			pStatement.setString(4, email);
			int row = pStatement.executeUpdate();
			resp.setStatus(row, "Data is successfully inserted!");
			out.println("Data is successfully inserted!");
			out.close();

		} catch (Exception e) {
			System.out.print(e);
			e.printStackTrace();
		}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.duplicate(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.duplicate(req, resp);
	}

}
