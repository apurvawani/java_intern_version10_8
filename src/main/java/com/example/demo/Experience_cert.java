package com.example.demo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


public class Experience_cert {
	
	DB db = new DB();
	
	public  Map<String , String> Employee_exp(String log) throws SQLException{
		
		System.out.println("Employee_id" + log);
		Map<String , String> map = new HashMap <String, String>();
		String sql1 = "SELECT * FROM public.\"Personal\" where \"Employee_ID\" = '"+log+"';";
		Statement st = db.connect().createStatement();
		ResultSet rs1 = st.executeQuery(sql1);
		rs1.next();
		
		map.put("Name" , rs1.getString("First_Name") + rs1.getString("Middle_Name") + rs1.getString("Last_Name"));
		
		String sql2 = "SELECT * FROM public.pastteaching WHERE employee_id ='"+log+"';";
		Statement st2 = db.connect().createStatement();
		ResultSet rs2 = st2.executeQuery(sql2);
		rs2.next();
		int j = 1;
		String i;
		do {
			i = String.valueOf(j);
			map.put("Designation"+i , rs2.getString("design_teach"));
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd"); 
			
			String frm_date = dateFormat.format(rs2.getDate("startdate"));
			map.put("From"+i , frm_date); 
			
			String to_date = dateFormat.format(rs2.getDate("enddate"));
			map.put("To"+i , to_date);
			
			map.put("Department"+i , rs2.getString("department"));
			j++;
		}while(rs2.next()); 
		return map; 
	}

}
