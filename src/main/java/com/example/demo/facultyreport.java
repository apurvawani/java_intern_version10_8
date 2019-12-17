package com.example.demo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.lang.Math; 


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class facultyreport {

	AdminDB db = new AdminDB();
	
	public List facultyinfo(@RequestBody Map<String, Object> payload,AdminDB adb) throws SQLException {
		Map<String, Integer> faculty = new HashMap<String, Integer>();
		String Department = (String) payload.get("department");
		String Shift = (String) payload.get("shift");
		java.util.List<Map<String,Integer>> mymap = new ArrayList<Map<String, Integer>>();


		ResultSet rs = null;
		String sql="SELECT * FROM public.students";
	    System.out.println("Query execution");

		
		
			Statement stmt = adb.connect().createStatement();
		     rs = stmt.executeQuery(sql); 
			    System.out.println("Query executed");

		    

		while (rs.next())
		{
		    System.out.println("inside while");

			if((rs.getString(1).equals(Department))&& (rs.getString(2).equals(Shift)))
			{		   

		    System.out.print("Column 1 returned ");
		    int prof=0,asso=0,asst=0,students=0;
		    students = (int)rs.getInt("students");
		    System.out.println(students);

		    int fac=(int) Math.ceil(students/20.0);
		    System.out.println(fac);
		    
		    prof = fac/9;
		    System.out.println(prof);

		    asso = fac*2/9;
		    System.out.println(asso);

		    asst = fac-prof-asso;
		    System.out.println(asst);
		    
		    faculty.put("Professor",prof);
		    faculty.put("Associate_Professor",asso);
		    faculty.put("Assistant Professor",asst);
		    break;
		    }
		}
		rs.close();
		mymap.add(0,faculty);
		
		return mymap;
	}
}