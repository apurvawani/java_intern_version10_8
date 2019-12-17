package com.example.demo;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

public class Personal {
	@Autowired
	DB db = new DB();
	public Map<String,String> entry(Map<String, Object> payload) throws Exception{
		Map<String,String>map = new HashMap<String,String>();
		String empid = (String) payload.get("empid");

		String salutation = (String) payload.get("salutation");
		String fname = (String) payload.get("fname");
		String mname = (String) payload.get("mname");
		String lname = (String) payload.get("lname");
		String fathersname = (String) payload.get("fathersName");
		String mothersname = (String) payload.get("mothersName");
		String gender = (String) payload.get("gender");
		String caddr = (String) payload.get("caddress");
		String paddr = (String) payload.get("paddress");
		String mstatus = (String) payload.get("mstatus");
		int nchild = (int)payload.get("nchild");
		String religion =  (String) payload.get("religion");
		String caste =  (String) payload.get("caste");
 
		
		
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date date = sdf1.parse((String)payload.get("dob"));
		java.sql.Date dob = new java.sql.Date(date.getTime()); 
		
		
		String pan = (String) payload.get("pan");
		String aadhar = (String) payload.get("aadhar");
//		String mobile = String.valueOf((int) payload.get("phone"));
		String mobile = (String) payload.get("phone");

		String oemail = (String) payload.get("oemail");
		String pemail = (String) payload.get("pemail");
		String tmobile = (String) payload.get("tphone");
		
	
		
		String sqlcheck ="Select \"Employee_ID\" from public.\"Personal\" where \"Employee_ID\" = '"+empid+"';";
		try{
			Statement st = db.connect().createStatement();
			ResultSet rs = st.executeQuery(sqlcheck);
			while(rs.next()) {
			if(empid.equals(rs.getString("Employee_ID"))) {
				map.put("Status","Employee already registered");
				return map;    
		}
			}
		}
		catch (SQLException e) {
			
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		String sql ="INSERT INTO public.\"Personal\"(\"Employee_ID\", \"Salutation\", \"First_Name\", \"Middle_Name\", \"Last_Name\", \"Father_Name\", \"Mother_Name\", \"Gender\", \"caddress\", \"paddress\", \"Marital_Status\", \"Number_of_Children\", \"Religion\", \"Caste\", \"Date_of_Birth\", \"PAN\", \"Aadhar\", \"Mobile_Number\", \"Official_Email_ID\", \"Personal_Email_ID\",\"Alternate_Mobile_Number\")VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?);";
		try {
			PreparedStatement stmt = db.connect().prepareStatement(sql);
			stmt.setString(1, empid);
			stmt.setString(2,salutation);
			stmt.setString(3, fname);
			stmt.setString(4, mname);
			stmt.setString(5, lname);
			stmt.setString(6, fathersname);
			stmt.setString(7, mothersname);
			stmt.setString(8, gender);
			stmt.setString(9, caddr);
			stmt.setString(10, paddr);
			stmt.setString(11, mstatus);
			stmt.setInt(12, nchild);
			stmt.setString(13, religion);
			stmt.setString(14, caste);
			stmt.setDate(15, dob);
			stmt.setString(16, pan);
			stmt.setString(17, aadhar);
			stmt.setString(18, mobile);
			stmt.setString(19, oemail);
			stmt.setString(20, pemail);
			stmt.setString(21,tmobile);

			stmt.executeUpdate();
			System.out.println("i am here");

		} catch (SQLException e) {
			
			e.printStackTrace();
			System.out.println(e.getMessage());

		}
//		
		//Map<String, String> map = new HashMap<String, String>();
		//map.put("value", "DONE");
		map.put("Status","Employee successfully registered");
		return map; 
		
		
	}

}
