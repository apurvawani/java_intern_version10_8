package com.example.demo;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class Education {//tested
	//@Autowired
	 public DB db = new DB();
	public Map<String,String> entry(Map<String, Object> payload,String log) throws Exception{
		 Map<String,String>map = new HashMap<String,String>();
		
		String diploma = (String) payload.get("diploma");
		String class_diploma = (String) payload.get("class_diploma");
		String diplomamonthyr = (String) payload.get("diplomamonthyr");
		String diplomainst = (String) payload.get("diplomainst");
		String diplomaboard = (String) payload.get("diplomaboard");
		String qual_ug = (String) payload.get("qual_ug");
		String grade_ug = (String) payload.get("grade_ug");
		String ugmonthyr = (String) payload.get("ugmonthyr");
		String uginst = (String) payload.get("uginst");
		String uguniversity = (String) payload.get("uguniversity");
		String pgequal = (String) payload.get("pgequal");
		String pgclass = (String)payload.get("pgclass");
		String pgmonthyr =  (String) payload.get("pgmonthyr");
		String pginst =  (String) payload.get("pginst");
		String pguniversity =  (String) payload.get("pguniversity");
		
		String phdsubj =  (String) payload.get("phdsubj");
		
		String phdmonthyr =  (String) payload.get("phdmonthyr");
		String phdinst =  (String) payload.get("phdinst");
		String phduniversity =  (String) payload.get("phduniversity");
			
		String areaspecial =  (String) payload.get("areaspecial");
//		String pastteaching =  (String) payload.get("pastteaching");
//		String pastindustry =  (String) payload.get("pastindustry");
//		String pastresearch =  (String) payload.get("pastresearch");
		//int empid =  (int) payload.get("empid");
		
	
String sql = "INSERT INTO public.eduqualification(\r\n" + 
		"	diploma, class_diploma, diplomamonthyr, diplomainst, diplomaboard, qual_ug, grade_ug, ugmonthyr, uginst, uguniversity, pgequal, pgclass, pgmonthyr, pginst, pguniversity, phdsubj, phdmonthyr, phdinst, phduniversity, areaspecial, \"Employee_ID\")\r\n" + 
		"	VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

		try {
			PreparedStatement stmt = db.connect().prepareStatement(sql);
			stmt.setString(1, diploma);
			stmt.setString(2,class_diploma);
			stmt.setString(3, diplomamonthyr);
			stmt.setString(4, diplomainst);
			stmt.setString(5, diplomaboard);
			stmt.setString(6, qual_ug);
			stmt.setString(7, grade_ug);
			stmt.setString(8, ugmonthyr);
			stmt.setString(9, uginst);
			stmt.setString(10, uguniversity);
			stmt.setString(11, pgequal);
			stmt.setString(12, pgclass);
			stmt.setString(13, pgmonthyr);
			stmt.setString(14, pginst);
			stmt.setString(15, pguniversity);
			stmt.setString(16, phdsubj);
			stmt.setString(17, phdmonthyr);
			stmt.setString(18, phdinst);
			stmt.setString(19, phduniversity);
			stmt.setString(20,areaspecial);
			
			//stmt.setInt(24,empid);
			stmt.setString(21, log);
			
			
			
			stmt.executeUpdate();
			System.out.println("i am here");

		} catch (SQLException e) {
			
			e.printStackTrace();
			System.out.println(e.getMessage());

		}
		
		//Others o = new Others();
		//o.entry(payload,log);
		
		//Map<String, String> map = new HashMap<String, String>();
		map.put("Status", "Successfull");
		return map;

		
		
	}
	

}
