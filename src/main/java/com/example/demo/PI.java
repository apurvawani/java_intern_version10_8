package com.example.demo;
import java.sql.Array;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.qos.logback.core.util.SystemInfo;

import java.util.ArrayList;
import java.util.Date.*;

@RestController
@RequestMapping(value = "/api",  produces = "application/json")
public class PI {
	public String log;
	public boolean admin_log=true;
	public boolean emp_log = false;
	
	@Autowired
	DB db = new DB();
	
	
	//@Autowired
	AdminDB adb = new AdminDB();
	
	


	
	@GetMapping("/pi/emp/t")
	public Map<String, String> personalInfo() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("value", "hello");
		return map;
	}
	@GetMapping("/pi/emp/autoempid")
	public Map<String,String> autoempid() {
		String sql = "Select \"Employee_ID\" from public.\"Personal\" where \"Employee_ID\" = (Select max(\"Employee_ID\") from public.\"Personal\")";
		Statement st = null;
		ResultSet rs = null;
		String newemp = null;
		Map<String,String> map = new HashMap<String,String>();
		try {
			System.out.println("Here");
			st = db.connect().createStatement();
			rs = st.executeQuery(sql);
			rs.next();
			String prev=(String)rs.getString("Employee_ID");;
			String[] parts = prev.split("p");
			int part2 = Integer.parseInt(parts[1]); 
			part2++;
			newemp=parts[0].concat("p".concat(String.valueOf(part2)));
			map.put("empid",newemp);

			
			
		} catch (SQLException e) {
			newemp = "Emp000";
			map.put("empid",newemp);
			e.printStackTrace();
		}
		return map;
		
	}
	
	
	@PostMapping("/pi/emp/enter")//tested
	public Map<String,String> NewUser(@RequestBody Map<String, Object> payload) throws Exception{
		Map<String,String>s= new HashMap<String,String>();
		if(admin_log) {
		System.out.println(payload);
		Personal p = new Personal();
		 s = p.entry(payload);

String sql="INSERT INTO public.credentials(	\"Login\", password)VALUES (?, ?);";

try {
	PreparedStatement stmt = adb.connect().prepareStatement(sql);
	stmt.setString(1, (String)payload.get("empid"));
	stmt.setString(2,(String)payload.get("empid"));
	stmt.executeUpdate();
}
catch (SQLException e) {
	
	e.printStackTrace();
	System.out.println(e.getMessage());

}
		}
		else {
			s.put("Status","Please login");
			return s; 
		}
		//System.out.println("here");
		return s;
	}
	
	
	@GetMapping("/pi/emp/enter/admin/login/employee_exist")//tested
	public  Map<String,Boolean> Employee_exists(@RequestBody Map<String, Object> payload) throws SQLException{
		Map<String, Boolean> map = new HashMap<String, Boolean>();
		String sql ="SELECT \"Login\"FROM public.credentials where \"Login\" = '"+payload.get("Employee_ID")+"';";
		Statement st = adb.connect().createStatement();
		ResultSet rs = st.executeQuery(sql);
		rs.next();
			if(rs.getString("Login").equals((String)payload.get("Employee_ID"))) {
				map.put("Employee_ID", true);	
		   		return map;
			}
		map.put("Employee_ID", false);
		return map;
	}
	


	
	
	
	@PostMapping("/pi/emp/enter/admin/login/details")   //for data filling by admin //tested
	public Map<String,String> data1(@RequestBody Map<String, Object> payload) throws Exception{
	Map<String,String>map = new HashMap<String,String>();

	if(admin_log) {
	String log = (String) payload.get("Employee_ID");
	String sql = "Select \"Employee_ID\" from public.\"Personal\" where \"Employee_ID\" = '"+log+"';";
	Statement st = db.connect().createStatement();
	ResultSet rs = st.executeQuery(sql);
	
		if(rs.next()) {
			Officeinfo oi = new Officeinfo();
			String s = oi.entry(payload, log);
			map.put("status", "entry successfull");
			return map;
			}
		else {
			map.put("Status", "Entry not seccessfull");
			return map;
		}
	}
	
	else {
		map.put("status","Please sign in");
		return map ;
	}
	
}
	
	
	
	@PostMapping("/pi/emp/enter/admin/login/details/education")   //for data filling by admin //tested
	public Map<String,String> data(@RequestBody Map<String, Object> payload) throws Exception{
		Map<String,String>s= new HashMap<String,String>();
	if(admin_log) {
	String log = (String) payload.get("empid");
	System.out.println(payload);
//	if(log==null) {
//		log = "emp12327";
//	}
	Education E= new Education();
	
	 s = E.entry(payload,log);
	
	
	return s;
	}
	else {
		s.put("Status","Please sign in");
		return s;

	}
	
}
	
	
	
	
	
	@PostMapping("/pi/emp/enter/admin/login/details/publications/national_journal")   //national international journal publications functions 
	public Map<String,String> national_journal(@RequestBody Map<String, Object> payload) throws Exception{
	
	Map<String,String>map = new HashMap<String,String>();
		
	if(admin_log) {

		String log = (String) payload.get("Employee_ID");
		String author = (String) payload.get("author");
		String title = (String) payload.get("title");
		String name = (String) payload.get("name");
		String ISSN = (String) payload.get("ISSN");
		int vol_no = (int)payload.get("vol_no");	
		int issue_no = (int)payload.get("issue_no");		
		int pages = (int) payload.get("pages");		
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date1 = sdf1.parse((String)payload.get("date"));
		java.sql.Date date = new java.sql.Date(date1.getTime());
		String primarykey = (String) String.valueOf(issue_no);
		
		Statement st = null;
		ResultSet rs = null;
		try {
		String sql1 = "Select \"Employee_ID\" from public.\"Personal\" where \"Employee_ID\" = '"+log+"'";
		 st = db.connect().createStatement();
		 rs = st.executeQuery(sql1);
		}
		catch(Exception e){
			map.put("Status", "Error");
			return map;
			
		}
		if(!rs.next()) {
			map.put("Status", "Error");
			return map;
			}
		
		
		if(rs.getString("Employee_ID").equals(log)) {
			String sql = "INSERT INTO public.nationaljournal(author,title,name,\"ISSN\",vol_no,issue_no,pages,date,prikey,\"Employee_ID\")VALUES (?, ?,?,?,?,?,?,?,?,?);";
			
			try {
				PreparedStatement stmt = db.connect().prepareStatement(sql);
				stmt.setString(1, author);
				stmt.setString(2, title);
				stmt.setString(3, name);
				stmt.setString(4, ISSN);
				stmt.setInt(5, vol_no);
				stmt.setInt(6, issue_no);
				stmt.setInt(7, pages);
			    stmt.setDate(8,date);
				
				stmt.setString(9,primarykey);
				stmt.setString(10, log);
		
				System.out.println("LOGIN ID IS10"+log);				
				stmt.executeUpdate();
				System.out.println("done");
				map.put("status","Entry Successfull");
			} 
			catch (SQLException e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
				map.put("status","Not Successfull");
				return map;
				}
			
		return map;
		}
		
		else {
			map.put("Status","Employee_ID not found");
			return map;
		}
		
	}
	
	else {
		map.put("status", "Please Login");
		return map;
	}
	
}
	
	
	
	@PostMapping("/pi/emp/enter/admin/login/details/publications/international_journal")   //national international journal publications functions 
	public Map<String,String> international_journal(@RequestBody Map<String, Object> payload) throws Exception{
		
	Map<String,String>map = new HashMap<String,String>();
	if(admin_log) {

		String log = (String) payload.get("Employee_ID");
		String author = (String) payload.get("author");
		String title = (String) payload.get("title");
		String name = (String) payload.get("name");
		String ISSN = (String) payload.get("ISSN");
		int vol_no = (int)payload.get("vol_no");
		int issue_no = (int)payload.get("issue_no");
		long pages = (long)payload.get("pages");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date1 = sdf1.parse((String)payload.get("date"));
		java.sql.Date date = new java.sql.Date(date1.getTime());
		String primarykey = String.valueOf(issue_no);
		
		String sql1 = "Select \"Employee_ID\" from public.\"Personal\" where \"Employee_ID\" = '"+log+"'";
		Statement st = db.connect().createStatement();
		ResultSet rs = st.executeQuery(sql1);
		if(!rs.next()) {
			map.put("Status", "Error");
			return map;
			}		
		
		if(rs.getString("Employee_ID").equals(log)) {
		
		String sql = "INSERT INTO public.inter_natjournal(author,title,name,\"ISSN\",vol_no,issue_no,pages,date,prikey,\"Employee_ID\")VALUES (?, ?,?,?,?,?,?,?,?,?);";
			
			try {
				PreparedStatement stmt = db.connect().prepareStatement(sql);
				stmt.setString(1, author);
				stmt.setString(2, title);
				stmt.setString(3, name);
				stmt.setString(4, ISSN);
				stmt.setInt(5, vol_no);
				stmt.setInt(6, issue_no);
				stmt.setLong(7, pages);
			    stmt.setDate(8,date);
				
				stmt.setString(9,primarykey);
				stmt.setString(10, log);
		
				System.out.println("LOGIN ID IS10"+log);				
				stmt.executeUpdate();
				System.out.println("done");
			} catch (SQLException e) {
				
				e.printStackTrace();
				System.out.println(e.getMessage());
				map.put("Status", "Not Successfull");
				return map;

			}
			
		map.put("Status", "Entry Successfull");
		return map;
		}
		
		else {
			map.put("Status","Employee_ID not found");
			return map;
		}
	}
	else {
		map.put("status", "Please sign in");
		return map;
	}
	
}


	@PostMapping("/pi/emp/enter/admin/login/details/publications/national_conf")   //national international journal publications functions 
	public Map<String,String> national_conf(@RequestBody Map<String, Object> payload) throws Exception{
		
	Map<String,String>map= new HashMap<String,String>();
	if(admin_log) {

		String log = (String) payload.get("Employee_ID");
		String author = (String) payload.get("author");
		String title = (String) payload.get("title");
		String name = (String) payload.get("name");
		String ISSN = (String) payload.get("ISSN");
		int vol_no = (int)payload.get("vol_no");
		int issue_no = (int)payload.get("issue_no");
		int pages = (int)payload.get("pages");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date1 = sdf1.parse((String)payload.get("date"));
		java.sql.Date date = new java.sql.Date(date1.getTime());
		String primarykey = String.valueOf(issue_no);
		
		String sql1 = "Select \"Employee_ID\" from public.\"Personal\" where \"Employee_ID\" = '"+log+"'";
		Statement st = db.connect().createStatement();
		ResultSet rs = st.executeQuery(sql1);
		if(!rs.next()) {
			map.put("Status", "Error");
			return map;
			}		
		if(rs.getString("Employee_ID").equals(log)) {


		String sql = "INSERT INTO public.nationalconf(author,title,name,\"ISSN\",vol_no,issue_no,pages,date,prikey,\"Employee_ID\")VALUES (?, ?,?,?,?,?,?,?,?,?);";
			
			try {
				PreparedStatement stmt = db.connect().prepareStatement(sql);
				stmt.setString(1, author);
				stmt.setString(2, title);
				stmt.setString(3, name);
				stmt.setString(4, ISSN);
				stmt.setInt(5, vol_no);
				stmt.setInt(6, issue_no);
				stmt.setInt(7, pages);
			    stmt.setDate(8,date);
				
				stmt.setString(9,primarykey);
				stmt.setString(10, log);
		
				System.out.println("LOGIN ID IS10"+log);				
				stmt.executeUpdate();
				System.out.println("done");
			} catch (SQLException e) {
				
				e.printStackTrace();
				System.out.println(e.getMessage());
				map.put("Status","Not Successfull");
				return map;
			}
			
			map.put("Status","Entry successfull");
			return map;

		}
		else {
			map.put("Status","Employee_ID not found");
			return map;
		}
			
		}
	else {
		map.put("Status", "Please Login");
		return map;
	}
	
}


	@PostMapping("/pi/emp/enter/admin/login/details/publications/international_conf")   //national international journal publications functions 
	public Map<String,String> international_conf(@RequestBody Map<String, Object> payload) throws Exception{
		
	Map<String,String>map= new HashMap<String,String>();
	if(admin_log) {

		String log = (String) payload.get("Employee_ID");
		String author = (String) payload.get("author");
		String title = (String) payload.get("title");
		String name = (String) payload.get("name");
		String ISSN = (String) payload.get("ISSN");
		int vol_no = (int)payload.get("vol_no");
		int issue_no = (int)payload.get("issue_no");
		int pages = (int)payload.get("pages");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date1 = sdf1.parse((String)payload.get("date"));
		java.sql.Date date = new java.sql.Date(date1.getTime());
		String primarykey = String.valueOf(issue_no);

		String sql1 = "Select \"Employee_ID\" from public.\"Personal\" where \"Employee_ID\" = '"+log+"'";
		Statement st = db.connect().createStatement();
		ResultSet rs = st.executeQuery(sql1);
		if(!rs.next()) {
			map.put("Status", "Error");
			return map;
			}		
		if(rs.getString("Employee_ID").equals(log)) {

			String sql = "INSERT INTO public.inter_natconf(author,title,name,\"ISSN\",vol_no,issue_no,pages,date,prikey,\"Employee_ID\")VALUES (?, ?,?,?,?,?,?,?,?,?);";
			
			try {
				PreparedStatement stmt = db.connect().prepareStatement(sql);
				stmt.setString(1, author);
				stmt.setString(2, title);
				stmt.setString(3, name);
				stmt.setString(4, ISSN);
				stmt.setInt(5, vol_no);
				stmt.setInt(6, issue_no);
				stmt.setInt(7, pages);
			    stmt.setDate(8,date);
				
				stmt.setString(9,primarykey);
				stmt.setString(10, log);
		
				System.out.println("LOGIN ID IS10"+log);				
				stmt.executeUpdate();
				System.out.println("done");
			} catch (SQLException e) {
				
				e.printStackTrace();
				System.out.println(e.getMessage());
				map.put("Status","Not Successfull");
				return map;

			}
			
			map.put("Status","Entry Successfull");
			return map;
		}
		else {
			map.put("Status","EmployeeID not found");
			return map;
			
		}
			
		
	
	}
	else {
		map.put("Status","Please Login");

		return map;
	}
	
}
	
	
	@PostMapping("/pi/emp/enter/admin/login/details/publications/book")   //Books
	public Map<String,String> books(@RequestBody Map<String, Object> payload) throws Exception{
		
	Map<String,String>map = new HashMap<String,String>();
	if(admin_log) {

		String log = (String) payload.get("Employee_ID");
		String author = (String) payload.get("author");
		String title = (String) payload.get("title");
		int pages = (int) payload.get("pages");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date1 = sdf1.parse((String)payload.get("date"));
		java.sql.Date date = new java.sql.Date(date1.getTime());
		String primarykey = author+title;
		
		String sql1 = "Select \"Employee_ID\" from public.\"Personal\" where \"Employee_ID\" = '"+log+"'";
		Statement st = db.connect().createStatement();
		ResultSet rs = st.executeQuery(sql1);
		if(!rs.next()) {
			map.put("Status", "Error");
			return map;
			}		
		if(rs.getString("Employee_ID").equals(log)) {
			String sql = "INSERT INTO public.book(author,title,pages,date,prikey,\"Employee_ID\")VALUES (?, ?,?,?,?,?);";
			
			try {
				PreparedStatement stmt = db.connect().prepareStatement(sql);
				stmt.setString(1, author);
				stmt.setString(2, title);
				stmt.setInt(3, pages);
				stmt.setDate(4,date); 
				
				stmt.setString(5,primarykey);
				stmt.setString(6, log);
		
				System.out.println("LOGIN ID IS10"+log);				
				stmt.executeUpdate();
				System.out.println("done");
			} catch (SQLException e) {
				
				e.printStackTrace();
				System.out.println(e.getMessage());
				map.put("status","Not Successfull");
				return map;

			}
			
		
			map.put("status","Entry Successfull");
			return map;

		}
		else {
			map.put("status","Employee_ID not found");
			return map;
		}
	}
	else {
		map.put("status","Please sign in");
		return map;
	}
	
}
	
	
	
	
	@PostMapping("/pi/emp/enter/admin/login/details/publications/awardsrecieved")   //Books
	public Map<String,String> awards(@RequestBody Map<String, Object> payload) throws Exception{
		
	Map<String,String>map = new HashMap<String,String>();
	if(admin_log) {

		String log = (String) payload.get("Employee_ID");
		String award_name = (String) payload.get("noa");
		String issueing_agency = (String) payload.get("ia");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date1 = sdf1.parse((String)payload.get("date"));
		java.sql.Date date = new java.sql.Date(date1.getTime());
		String primarykey = award_name+log;
		
		String sql1 = "Select \"Employee_ID\" from public.\"Personal\" where \"Employee_ID\" = '"+log+"'";
		Statement st = db.connect().createStatement();
		ResultSet rs = st.executeQuery(sql1);
		if(!rs.next()) {
			map.put("Status", "Error");
			return map;
			}		
		if(rs.getString("Employee_ID").equals(log)) {
			String sql = "INSERT INTO public.awards(award_name,issueing_agency,date,prikey,\"Employee_ID\")VALUES (?, ?,?,?,?);";
			
			try {
				PreparedStatement stmt = db.connect().prepareStatement(sql);
				stmt.setString(1, award_name);
				stmt.setString(2, issueing_agency);
				stmt.setDate(3,date); 

				
				stmt.setString(4,primarykey);
				stmt.setString(5, log);
		
				System.out.println("LOGIN ID IS10"+log);				
				stmt.executeUpdate();
				System.out.println("done");
			} catch (SQLException e) {
				
				e.printStackTrace();
				System.out.println(e.getMessage());
				map.put("status","Not Successfull");
				return map;

			}
			
		
			map.put("status","Entry Successfull");
			return map;

		}
		else {
			map.put("status","Employee_ID not found");
			return map;
		}
	}
	else {
		map.put("status","Please sign in");
		return map;
	}
	
}
	
	
	
	
	
	
	
	
	@PostMapping("/pi/emp/enter/admin/login/details/publications/patentsgranted")   //Books
	public Map<String,String> patentsgranted(@RequestBody Map<String, Object> payload) throws Exception{
		
	Map<String,String>map = new HashMap<String,String>();
	if(admin_log) {

		String log = (String) payload.get("Employee_ID");
		String author = (String) payload.get("author");
		String title = (String) payload.get("title");
		String name = (String) payload.get("name");
		String issn = (String) payload.get("ISSN");
		int vol_no = (int) payload.get("vol_no");
		int issue_no = (int) payload.get("issue_no");
		int pages = (int) payload.get("pages");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date1 = sdf1.parse((String)payload.get("date"));
		java.sql.Date date = new java.sql.Date(date1.getTime());
		String primarykey = String.valueOf(issue_no);
		
		String sql1 = "Select \"Employee_ID\" from public.\"Personal\" where \"Employee_ID\" = '"+log+"'";
		Statement st = db.connect().createStatement();
		ResultSet rs = st.executeQuery(sql1);
		if(!rs.next()) {
			map.put("Status", "Error");
			return map;
			}		
		if(rs.getString("Employee_ID").equals(log)) {
			String sql = "INSERT INTO public.patentsgranted(author,title,pages,date,prikey,\"Employee_ID\",name,issn,vol_no,issue_no)VALUES (?, ?,?,?,?,?,?,?,?,?);";
			
			try {
				PreparedStatement stmt = db.connect().prepareStatement(sql);
				stmt.setString(1, author);
				stmt.setString(2, title);
				stmt.setInt(3, pages);
				stmt.setDate(4,date); 
				
				stmt.setString(5,primarykey);
				stmt.setString(6, log);
				stmt.setString(7, name);
				stmt.setString(8, issn);
				stmt.setInt(9, vol_no);
				stmt.setInt(10, vol_no);

		
				System.out.println("LOGIN ID IS10"+log);				
				stmt.executeUpdate();
				System.out.println("done");
			} catch (SQLException e) {
				
				e.printStackTrace();
				System.out.println(e.getMessage());
				map.put("status","Not Successfull");
				return map;

			}
			
		
			map.put("status","Entry Successfull");
			return map;

		}
		else {
			map.put("status","Employee_ID not found");
			return map;
		}
	}
	else {
		map.put("status","Please sign in");
		return map;
	}
	
}
	
	
	
	
	
	
	
	@PostMapping("/pi/emp/enter/admin/login/details/publications/grantsrecieved")   //Books
	public Map<String,String> grantsrecieved(@RequestBody Map<String, Object> payload) throws Exception{
		
	Map<String,String>map = new HashMap<String,String>();
	if(admin_log) {

		String log = (String) payload.get("Employee_ID");
		String pog = (String) payload.get("pog");
		String ta = (String) payload.get("ta");
		String fa = (String) payload.get("fa");

		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date1 = sdf1.parse((String)payload.get("date"));
		java.sql.Date date = new java.sql.Date(date1.getTime());
		String primarykey = log+ta;
		
		String sql1 = "Select \"Employee_ID\" from public.\"Personal\" where \"Employee_ID\" = '"+log+"'";
		Statement st = db.connect().createStatement();
		ResultSet rs = st.executeQuery(sql1);
		if(!rs.next()) {
			map.put("Status", "Error");
			return map;
			}		
		if(rs.getString("Employee_ID").equals(log)) {
			String sql = "INSERT INTO public.grantsrecieved(pog,ta,fa,date,prikey,\"Employee_ID\")VALUES (?, ?,?,?,?,?);";
			
			try {
				PreparedStatement stmt = db.connect().prepareStatement(sql);
				stmt.setString(1, pog);
				stmt.setString(2, ta);
				stmt.setString(3, fa);
				stmt.setDate(4,date); 

				
				stmt.setString(5,primarykey);
				stmt.setString(6, log);
		
				System.out.println("LOGIN ID IS10"+log);				
				stmt.executeUpdate();
				System.out.println("done");
			} catch (SQLException e) {
				
				e.printStackTrace();
				System.out.println(e.getMessage());
				map.put("status","Not Successfull");
				return map;

			}
			
		
			map.put("status","Entry Successfull");
			return map;

		}
		else {
			map.put("status","Employee_ID not found");
			return map;
		}
	}
	else {
		map.put("status","Please sign in");
		return map;
	}
	
}
	
	@PostMapping("/pi/emp/admin/login")
	public boolean adminlog(@RequestBody Map<String, Object> payload) throws Exception{
		 final String adminid = (String)payload.get("Admin_ID");
		 final String pass = (String)payload.get("Password");
		 String sql2 ="SELECT password FROM public.\"Admin\" where login = '"+ adminid +"';";
			try{
				Statement st = adb.connect().createStatement();
				ResultSet rs = st.executeQuery(sql2);
				rs.next();
				if(pass.equals(rs.getString("password"))) {
					admin_log=true;
					System.out.println(rs.getString("password"));
					return true;    
			}
			}
			catch (SQLException e) {
				
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
		 return false;	
	}
	
	
	
	@GetMapping("/pi/emp/admin/login/approval_list")
	public Map<Integer,String> adminapproval_list() throws Exception{
		if(admin_log) {
		String sql2="select employee_data from public.temp;";
			Statement st = db.connect().createStatement();
			ResultSet rs = st.executeQuery(sql2);
			Map<Integer, String> approve = new HashMap<Integer, String>();
			 int j=0;
			while (rs.next())
			{ approve.put(j,rs.getString("employee_data"));
				    j++;
			}
			rs.close();
			st.close();
			return approve;
		}
		else {
			Map<Integer, String> approve = new HashMap<Integer, String>();
			approve.put(1,"admin not logged in");
			return approve;
					
		}
				}
	
	
	@GetMapping("/pi/emp/admin/login/emp_temp_data")
	public Map<String,String> emp_temp_data(@RequestBody Map<String, Object> payload) throws Exception{
		if(admin_log) {
		Map<String, String> emp_temp = new HashMap<String, String>();
		String sql2="SELECT * From public.otherinfo1";
			Statement st = db.connect().createStatement();
			ResultSet rs = st.executeQuery(sql2);
			
			while (rs.next())
			{ 
			
				if(payload.get("employee_id").equals(rs.getString(10))) {
					emp_temp.put("nop",rs.getString(1));
					emp_temp.put("nop_int",rs.getString(2));
					emp_temp.put("nop_conf",rs.getString(3));
					emp_temp.put("nop_intconf",rs.getString(4));
					emp_temp.put("nob",rs.getString(5));
					emp_temp.put("nopatents",rs.getString(6));
					emp_temp.put("pggrant",rs.getString(7));
					emp_temp.put("awarddets",rs.getString(8));
					emp_temp.put("grantr",rs.getString(9));
					emp_temp.put("employee_id",rs.getString(10));
					break;


			}
			}
					   
			rs.close();
			st.close();
			return emp_temp;
		}
		else {
			Map<String, String> emp_temp = new HashMap<String, String>();
			emp_temp.put("Error","Please Login");
			return emp_temp;
			}
			
}
	
	
	
	
	
	
	@PostMapping("/pi/emp/admin/login/check_approve")//tested
	public Map<String,String> admin_approve(@RequestBody Map<String, Object> payload) throws Exception{
		if(admin_log) {
			PastInfo p = new PastInfo();
			Map<String, String> s = p.approval_list(payload);
					
		Map<String, String> emp_temp = new HashMap<String, String>();
		emp_temp.put("Query","Query Excecuted");
		return s;
		}
		else {
			Map<String, String> emp_temp = new HashMap<String, String>();
			emp_temp.put("Error","Query not Excecuted");
			return emp_temp;
			
		}
				
						
		 
	}
	
	
	@PostMapping("/pi/emp/admin/login/approve")//tested
	public String approved(@RequestBody Map<String, Object> payload) throws Exception{
		if(admin_log) {
			PastInfo p = new PastInfo();
			String s = p.approve(payload);
			
		}
		return "approved";
	}
	
	
	
	
	
	
	@PostMapping("/pi/emp/enter/login")//employee login//tested
	public boolean loginmethod(@RequestBody Map<String, Object> login) throws Exception{
		 log = (String)login.get("ID");
		String pass =(String)login.get("Password");
		
		System.out.println("here1");
		String sql2 ="SELECT password  FROM public.credentials where \"Login\" = '"+log+"';";
		
		Statement st = adb.connect().createStatement();
		//System.out.println("......");
		ResultSet rs = st.executeQuery(sql2);
		rs.next();
			System.out.print("Column 1 returned ");
		    if(rs.getString("Password").equals(pass)) {
		    	emp_log=true;
		    return true;
		}
		System.out.println("incorrect id or password");
		rs.close();
		st.close();
		
		return false;
	}
	
	
	
	
	@PostMapping("/pi/emp/enter/login/info")
	public String tempdata(@RequestBody Map<String, Object> payload) throws Exception{
		if(!emp_log) {
			return "You aren't logged in";
		}
	PastInfo p = new PastInfo();
	String s = p.entry(payload, log);

	String sql ="Select \"Employee_ID\" from public.approve where \"Employee_ID\" = '"+log+"';";
	Statement st = db.connect().createStatement();
	ResultSet rs = st.executeQuery(sql);
	rs.next();
	if(rs.getString("Employee_ID").equals(log)) {
		return"Done";
	}
	
		String sql3 ="INSERT INTO public.approve(\r\n" + 
				"	\"Employee_ID\", approve)\r\n" + 
				"	VALUES (?,?);";

PreparedStatement stmt1 = db.connect().prepareStatement(sql3);
stmt1.setString(1,log);
stmt1.setBoolean(2,false);
stmt1.executeUpdate();
System.out.println("Approval pending try block");
System.out.println("Approval pending");
		

				return "Done";
	}
	
	
	@PostMapping("/pi/emp/faculty")
	public List facultyinfo(@RequestBody Map<String, Object> payload) throws SQLException {
		Map<String, Integer> faculty = new HashMap<String, Integer>();
		facultyreport f = new facultyreport();
		return f.facultyinfo(payload,adb);
	
	}
	
	
	
	
@PostMapping("/pi/emp/salary_certificate")
public Map<String,String> salary_request(@RequestBody Map<String, Object> payload) throws Exception {
	Map<String, String> salary = new HashMap<String, String>();
	System.out.println("hello");
	Salary_certificate s = new Salary_certificate();
	s.req(payload);
	
    salary.put("Status","Request Pending");
	return salary;
	
}

//request
@GetMapping("/pi/emp/salary_check")
public List salary_check(@RequestParam("Employee_ID") String Employee_ID,@RequestParam("salaryid") String Salary_ID) throws Exception {
	System.out.println("Here");
	Map<String, Object> payload = new HashMap<String, Object>();
	payload.put("Employee_ID",Employee_ID);
	payload.put("salaryid",Salary_ID);


	List<Map<String, String>> mymap = new ArrayList<Map<String, String>>();
	Map<String, String> salary = new HashMap<String, String>();
	
	Salary_certificate s = new Salary_certificate();
	System.out.println("Here");
	salary.putAll(s.check_req(payload));
	mymap.add(0,salary);
	
//	Map<String, String> test = new HashMap<String, String>();
//	System.out.println(test.get("PRINCIPAL"));
//	test.put("PRINCIPAL","true");

	return mymap;
 
}
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


@GetMapping("/pi/emp/salary_check/admin/salary")
public List salary_check_cert(@RequestBody Map<String, Object> payload) throws Exception {
	System.out.println("Here");
	

	List<Map<String, String>> mymap = new ArrayList<Map<String, String>>();
	Map<String, String> salary = new HashMap<String, String>();

	
	Salary_certificate s = new Salary_certificate();
	System.out.println("Here");
	salary.putAll(s.check_req(payload));
	mymap.add(salary);
	
	return mymap;
 
}



















//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


@GetMapping("/pi/emp/salary_check/admin")
public List salary_check_admin() throws Exception {
//	System.out.println("Here");
	List<Map<String, String>> mymap = new ArrayList<Map<String, String>>();
	
	String sql ="SELECT * FROM public.salary where hod= true and principal = true";
	Statement stmt = db.connect().createStatement();
	ResultSet rs=stmt.executeQuery(sql);
	
	String sql1 =null;
	Statement stmt1 =null;
	ResultSet rs1=null;
	
	String sql2 =null;
	Statement stmt2=null;
	ResultSet rs2=null;
	while(rs.next()) {
		Map<String, String> salary = new HashMap<String, String>();

		System.out.println("Here");
		sql1 = "SELECT \"Employee_ID\", \"Salutation\", \"First_Name\", \"Middle_Name\", \"Last_Name\", \"Father_Name\", \"Mother_Name\"\r\n" + 
				"	FROM public.\"Personal\" where \"Employee_ID\"='"+rs.getString("Employee_ID")+"';";
		 stmt1 = db.connect().createStatement();
		 rs1=stmt1.executeQuery(sql1);
		 rs1.next();
		 
			 sql2="SELECT designation from public.officeinfo where \"Employee_ID\"='"+rs.getString("Employee_ID")+"';";
			
			 stmt2 = db.connect().createStatement();
			 rs2=stmt2.executeQuery(sql2);
			rs2.next();
		
		
		salary.put("EMPID",rs.getString("Employee_ID"));
		salary.put("name",rs1.getString("First_Name"));
		salary.put("lastname",rs1.getString("Last_Name"));
		salary.put("designation",rs2.getString("designation"));
		salary.put("Type","Salary Certificate");
		salary.put("salaryid",rs.getString("salary_id"));

		System.out.println(salary);

		mymap.add(salary);
	}
	
	
	return mymap;
 
}



//LIVE REQUEST HOD
@GetMapping("/pi/emp/livehod")            //tested
public List livehod() throws Exception {
	
	Salary_certificate s = new Salary_certificate();

	return s.live_reqhod();	
}



//LIVE REQUEST PRINCIPAL		
@GetMapping("/pi/emp/liveprincipal")            //tested
public List liveprincipal() throws Exception {
	
	Salary_certificate s = new Salary_certificate();
	return s.live_requestp();
		
   
}


@PostMapping("/pi/emp/salary/approvehod_salary") //HOD APPROVAL
public Map<String,String> approvehod(@RequestBody Map<String, Object> payload) throws Exception{
	
	String salary_id=(String)payload.get("Certificate_id");
	String empid = (String)payload.get("EMPID");
	Boolean hod_approval=Boolean.parseBoolean((String) payload.get("flag"));
	System.out.println(salary_id);
	Map<String,String> map= new HashMap<String,String>();

	
//	boolean approval=(boolean)payload.get("hod_approval");
	
	String sql="SELECT hod,fin,request FROM public.salary WHERE salary_id='"+ salary_id +"';";
	try
	{
		Statement stmt = db.connect().createStatement();
		ResultSet rs=stmt.executeQuery(sql);//problem here
		while(rs.next())
		{
			boolean request=rs.getBoolean("request");
   			boolean fin=rs.getBoolean("fin");
			if( fin==false && request==false)
			{	//latest request of salary certificate
				String sql1="UPDATE public.salary SET hod=?,fin=? WHERE salary_id='"+salary_id+"';";
				PreparedStatement stmt1 = db.connect().prepareStatement(sql1);
				stmt1.setBoolean(1,hod_approval);
				if(hod_approval==false) {
				stmt1.setBoolean(2,true);
				}
				else {
					stmt1.setBoolean(2,false);

				}

				stmt1.executeUpdate();	
				map.put("Status", "Approved by hod");
				return map;
			}
			
		}
				
	}
	catch (SQLException e) {
		
		e.printStackTrace();
		System.out.println(e.getMessage());
		map.put("Status","Unsuccessful");
	}	
	return map;
}



@PostMapping("/pi/emp/salary/approveprinci") //PRINCIPAL APPROVAL
public Map<String, String> approvep(@RequestBody Map<String, Object> payload) throws Exception{
	
	String salary_id=(String)payload.get("Certificate_id");
	String empid = (String)payload.get("EMPID");
	Boolean princi_approval=Boolean.parseBoolean((String) payload.get("flag"));
	System.out.println(salary_id);
	Map<String,String> map= new HashMap<String,String>();

	
//	boolean approval=(boolean)payload.get("hod_approval");
	
	String sql="SELECT hod,fin,request FROM public.salary WHERE salary_id='"+ salary_id +"';";
	try
	{
		Statement stmt = db.connect().createStatement();
		ResultSet rs=stmt.executeQuery(sql);//problem here
		while(rs.next())
		{
			boolean request=rs.getBoolean("request");
   			boolean fin=rs.getBoolean("fin");
   			boolean hod = rs.getBoolean("hod");
			if( fin==false && request==false&& hod == true)
			{	//latest request of salary certificate
				String sql1="UPDATE public.salary SET principal=?,request=? WHERE salary_id='"+salary_id+"';";
				PreparedStatement stmt1 = db.connect().prepareStatement(sql1);
				stmt1.setBoolean(1,princi_approval);
				
				stmt1.setBoolean(2,true);
				
				stmt1.executeUpdate();	
				map.put("Status", "Approved by principal");
				return map;
			}
			
		}
				
	}
	catch (SQLException e) {
		
		e.printStackTrace();
		System.out.println(e.getMessage());
		map.put("Status","Unsuccessful");
	}	
	return map;

}
	
	
	
//No objection certificate request
@PostMapping("/pi/emp/noc")
public Map<String,String> noc_request(@RequestBody Map<String, Object> payload) throws Exception {
	Map<String, String> noc = new HashMap<String, String>();
	System.out.println("hello");
	noc s = new noc();
	s.req(payload);
	
    noc.put("Status","Request Pending");
	return noc;	
}
		

	@GetMapping("/pi/emp/noc_check")
	public List noc_check(@RequestParam("Employee_ID") String Employee_ID) throws Exception {
		System.out.println("Here");
		Map<String, Object> payload = new HashMap<String, Object>();
		payload.put("Employee_ID",Employee_ID);

		List<Map<String, String>> mymap = new ArrayList<Map<String, String>>();
		Map<String, String> noc = new HashMap<String, String>();
		
		noc n = new noc();
		System.out.println("Here");
		noc.putAll(n.check_req(payload));
		mymap.add(0,noc);
		
		Map<String, String> test = new HashMap<String, String>();
		System.out.println(test.get("PRINCIPAL"));
		test.put("PRINCIPAL","true");

		return mymap;
	 
	}


	//LIVE REQUEST PRINCIPAL		
	@GetMapping("/pi/emp/nocliveprincipal")            //tested
	public List nocliveprincipal() throws Exception {
		
		noc n = new noc();
		return n.live_requestp();
			
	   
	}


	//LIVE REQUEST HOD
			@GetMapping("/pi/emp/noclivehod")            //tested
			public List noclivehod() throws Exception {
				
				noc n = new noc();

				return n.live_reqhod();	
			}
			
			
			
			
			@PostMapping("/pi/emp/noc/approvehod") //HOD APPROVAL
			public String approvehod_noc(@RequestBody Map<String, Object> payload) throws Exception{
				
				String empid=(String)payload.get("Employee_ID");
				String noc_id=(String)payload.get("noc_id");
				System.out.println(empid);
				boolean approval=(boolean)payload.get("hod_approval");
				
				String sql="SELECT hod,fin,request FROM public.noc WHERE noc_id='"+ noc_id +"';";
				try
				{
					Statement stmt = db.connect().createStatement();
					ResultSet rs=stmt.executeQuery(sql);//problem here
					while(rs.next())
					{
						boolean request=rs.getBoolean("request");
			   			boolean fin=rs.getBoolean("fin");
						if( fin==false && request==false)
						{	//latest request of salary certificate
							String sql1="UPDATE public.noc SET hod=? WHERE noc_id='"+empid+"';";
							PreparedStatement stmt1 = db.connect().prepareStatement(sql1);
							stmt1.setBoolean(1,approval);
							stmt1.executeUpdate();	
							return "HOD decision done";
						}
						
					}
							
				}
				catch (SQLException e) {
					
					e.printStackTrace();
					System.out.println(e.getMessage());
				}	
				return "Unsuccessful attempt";
			}



			@PostMapping("/pi/emp/noc/approveprinci") //PRINCIPAL APPROVAL
			public String approvep_noc(@RequestBody Map<String, Object> payload) throws Exception{
				
				String empid=(String)payload.get("Employee_ID");
				String noc_id=(String)payload.get("noc_id");
				boolean approval=(boolean)payload.get("principal_approval");
				
				String sql="SELECT principal, \"Employee_ID\", hod,fin\r\n" + 
						"	FROM public.noc where noc_id='"+ noc_id +"';";
				
				try
				{
					Statement stmt = db.connect().createStatement();
					ResultSet rs=stmt.executeQuery(sql);
					while(rs.next())
					{
						System.out.println("hello");//works till here.
						boolean hod=rs.getBoolean("hod");
						
						boolean fin=rs.getBoolean("fin");//IF FINAL IS SET FALSE THEN REJECTED OLD REQUEST/NOT APPROVED YET.
						//IF PRINCIPAL REJECTS,HOD VALUE IS ALSO BY DEFAULT FALSE.
						
						if(fin==false && hod==true && approval==true)
						{	//latest request of salary certificate
							String sql1="UPDATE public.noc SET principal='"+approval+"',fin='"+approval+"',request='"+approval+"' where noc_id='"+ noc_id +"';";
							PreparedStatement stmt1=db.connect().prepareStatement(sql1);
						
							stmt1.executeUpdate();	
							System.out.println("done");
							return "Principal decision done";
						}
						else if(fin==false && hod==false)
						{
							
							String sql1="UPDATE public.noc SET principal='"+hod+"',fin='"+fin+"',request='"+fin+"' where noc_id='"+ noc_id +"';";
							PreparedStatement stmt1=db.connect().prepareStatement(sql1);
							
							stmt1.executeUpdate();	
							return "Rejected";
						}
					}
				}
				catch (SQLException e) {
					
					e.printStackTrace();
					System.out.println(e.getMessage());
				}		
				return "Unsuccessful attempt";
			}
		

@GetMapping("/pi/emp/exp_certificate")
public Map<String,String> exp_request(@RequestBody Map<String, Object> payload) throws Exception {
	Experience_cert e = new Experience_cert();
	return e.Employee_exp(log);
	
}


@GetMapping("/pi/emp/list")    //not tested 
public List list_of_emp(@RequestParam("Department") String Department) throws Exception {
	Map<String,String>payload = new HashMap<String,String>();
	payload.put("Department", Department);
	if(!Department.equals("All")) {
		List_of_emp l = new List_of_emp();
		return l.Employee_list(payload);
	}
	else {
		List_of_emp l = new List_of_emp();
		return l.Employee_listall(payload);
	}
	
}


//
/*@GetMapping("/pi/emp/changedesignation")
public Map<String,String> changed(@RequestBody Map<String, Object> payload) throws Exception {
	
	Map<String, String> salary = new HashMap<String, String>();
	String newdesign=(String)payload.get("newdesign");//STAFF ENTERS NEW EMPLOYEE
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	java.util.Date tempdate1 = sdf1.parse((String)payload.get("datestart"));
	java.util.Date tempdate2= sdf1.parse((String)payload.get("oldend")); //END DATE FOR PREVIOUS DESIGNATION
    Date datestart = new java.sql.Date(tempdate1.getTime()); //DATE OF NEW POST
    Date endold = new java.sql.Date(tempdate2.getTime()); //END DATE OF OLD POST
    Salary_certificate s = new Salary_certificate();
	s.req(payload);
    salary.put("Status","Request Pending");
	return salary;
	
}
*/

//IF 'request'=false,not handled
//IF 'request'=true,handled.


//
@GetMapping("/pi/emp/changedesignation")
public Map<String,String> changed(@RequestBody Map<String, Object> payload) throws Exception {
	
	Map<String, String> salary = new HashMap<String, String>();
	String newdesign=(String)payload.get("newdesign");//STAFF ENTERS NEW EMPLOYEE
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	java.util.Date tempdate1 = sdf1.parse((String)payload.get("datestart"));
	java.util.Date tempdate2= sdf1.parse((String)payload.get("oldend")); //END DATE FOR PREVIOUS DESIGNATION
    Date datestart = new java.sql.Date(tempdate1.getTime()); //DATE OF NEW POST
    Date endold = new java.sql.Date(tempdate2.getTime()); //END DATE OF OLD POST
    
//	    String sql="insert  public.pastteaching   "
    
    salary.put("Status","Request Pending");
	return salary;
	
}




@PostMapping("/pi/emp/logout")
public String logouteveryone() {
	 admin_log=false;
	 emp_log = false;
	 return "done";
	}
}








	
	