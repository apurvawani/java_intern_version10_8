package com.example.demo;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class List_of_emp {
	
	DB db = new DB();
	
	public List Employee_list(@RequestBody Map<String, String> payload) throws Exception {
		String dept = (String) payload.get("Department");
		
		String sql1 = "SELECT public.officeinfo.\"Employee_ID\",\"First_Name\",\"Middle_Name\",\"Last_Name\",designation FROM public.officeinfo inner join public.\"Personal\" on public.\"Personal\".\"Employee_ID\"=public.officeinfo.\"Employee_ID\" WHERE public.officeinfo.dep = '"+dept+"';;";
		Statement st = db.connect().createStatement();
		ResultSet rs = st.executeQuery(sql1);
		
		List<Map<String,String>> list = new  ArrayList<Map<String,String>>();

		
		while(rs.next()) {
			Map<String,String> map= new HashMap<String,String>();
			map.put("Employee", rs.getString("Employee_ID"));

			map.put("Name",rs.getString("First_Name")+" "+rs.getString("Middle_Name")+" "+rs.getString("Last_Name"));
			map.put("Designation",rs.getString("designation"));
			map.put("Department", dept);
			
			list.add(map);
			
			map= null;

		}
		return list;
			

	

	
	
	
	
}
	public List Employee_listall(@RequestBody Map<String, String> payload) throws Exception {
		
		String sql1 = "SELECT public.officeinfo.\"Employee_ID\",\"First_Name\",\"Middle_Name\",\"Last_Name\",designation,dep FROM public.officeinfo inner join public.\"Personal\" on public.\"Personal\".\"Employee_ID\"=public.officeinfo.\"Employee_ID\";";
		Statement st = db.connect().createStatement();
		ResultSet rs = st.executeQuery(sql1);
		
		List<Map<String,String>> list = new  ArrayList<Map<String,String>>();

		
		while(rs.next()) {
			Map<String,String> map= new HashMap<String,String>();
			map.put("Employee", rs.getString("Employee_ID"));

			map.put("Name",rs.getString("First_Name")+" "+rs.getString("Middle_Name")+" "+rs.getString("Last_Name"));
			map.put("Designation",rs.getString("designation"));
			map.put("Department", rs.getString("dep"));
			
			list.add(map);
			
			map= null;

		}
		return list;
			

	

	
	
	
	
}
}
