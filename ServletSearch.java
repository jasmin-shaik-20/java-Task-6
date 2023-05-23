package com.emp;

import java.io.BufferedReader;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

@WebServlet("/ServletSearch")
public class ServletSearch extends HttpServlet

{
	private static final long serialVersionUID = 1L;
	Logger log = LogManager.getLogger();

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Connection conn = null;
		try {

			Context initContext = new InitialContext();
			DataSource dataSource = (DataSource) initContext.lookup("java:comp/env/jdbc/mydb");
			conn = dataSource.getConnection();
			response.setContentType("application/json");

			StringBuffer jb = new StringBuffer();

			String line = null;

			BufferedReader reader = request.getReader();

			while ((line = reader.readLine()) != null) {
				jb.append(line);
			}

			JSONObject jsobj = new JSONObject(jb.toString());

			int empId = jsobj.getInt("empId");
			int emptype = jsobj.getInt("emptype");

			com.emp.EmpDb emp = new EmpDb();

			String jsonData = emp.searchRecord(empId, emptype, conn);

			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			PrintWriter out = response.getWriter();
			out.write(jsonData);

		} catch (Exception e) {

			log.info(e);
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {

				log.info(e);
			}
		}

	}
}
