package com.example.demo;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;




public class noc {
	
	
	
	public DB db = new DB();
	
	//request for NOC
	public Map<String,String> req(@RequestBody Map<String, Object> payload) throws Exception{
		
		//CREATE A NEW TABLE named 'noc' SUCH THAT IT CONTAINS APPLICATION NUMBER<EMPID+date>
		 
		
		String emp_id = (String)payload.get("Employee_ID");
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		LocalDate localDate = LocalDate.now();
		Date date = java.sql.Date.valueOf(localDate);
		
		String dat=String.valueOf(date);
		
		Calendar cal = Calendar.getInstance();
		java.util.Date date3 = cal.getTime();
		DateFormat dateformat = new SimpleDateFormat("HH:mm:ss");
		String formattedDate = dateformat.format(date3);
		
		
		String noc_id=null;
		noc_id=emp_id+dat+formattedDate;
		boolean hod=false;
		boolean principal=false;
		boolean fin=false;
		boolean request=false;
		String reason = (String)payload.get("reason");
		Boolean admin_approval = true;
		
		String sql="INSERT INTO public.noc(\"Employee_ID\", noc_id, reason, hod, principal, admin_approval, request, fin, date)VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
		
		try {
				PreparedStatement stmt = db.connect().prepareStatement(sql);
				stmt.setString(1, emp_id);
				stmt.setString(2,noc_id);
				stmt.setString(3, reason);
				stmt.setBoolean(4, hod);
				stmt.setBoolean(5, principal);
				stmt.setBoolean(6, admin_approval); 
				stmt.setBoolean(7, request);
				stmt.setBoolean(8,fin);
				stmt.setDate(9,date);

				stmt.executeUpdate();
				
			} catch (SQLException e) {
				
				e.printStackTrace();
				System.out.println(e.getMessage());

			}
		Map<String, String> map = new HashMap<String, String>();
		map.put("status", "Your request will be processed");

			return map;
	}

	
	public Map<String, String> check_req(@RequestBody Map<String, Object> payload) throws Exception{
		System.out.println("Finally here");
		String emp = (String)payload.get("Employee_ID");

		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		
		Map<String, String> map = new HashMap<String, String>();
			
		String sql="SELECT * FROM public.noc where \"Employee_ID\" = '"+emp+"';";
		Statement stmt = db.connect().createStatement();
		ResultSet rs=stmt.executeQuery(sql);
		int i=1;
		while(i==1&&rs.next()){ 
			i=2;
			try {
				 
				
				//REQUIRED RECORD FOUND
				
					//IF FINAL IS FALSE,THEN IT COULD BE INCOMPLETE PROCESS OR A PREVIOUS REJECTED REQUEST.
					boolean field1=rs.getBoolean("principal"); //PRINCIPAL APPROVAL
					boolean field2=rs.getBoolean("hod"); //HOD APPROVAL
					boolean field3=rs.getBoolean("fin"); //FINAL APPROVAL
					boolean field4=rs.getBoolean("request"); //handled?
					boolean field5= rs.getBoolean("admin_approval");//if admin printed the report
					System.out.println(field1+" "+field2+" "+field3+" "+field4+" "+field5);
					
					if(field3==false) 
					{
						String PRINCIPAL = Boolean.toString(field1);
						String HOD = Boolean.toString(field2);
						String ADMIN = Boolean.toString(field5);
						String TYPE = "No Objection Certificate";

						map.put("Principal",PRINCIPAL);
						map.put("HOD",HOD);
						map.put("Admin",ADMIN);
						map.put("Type",TYPE);
						
						return map;

						
						//name,designation,dept,joiningdate,releiveing date,
					}
					else if( field1==true && field2==true && field3==true && field4==false )
					{
						
						String sql4="SELECT \"Employee_ID\", \"Salutation\", \"First_Name\", \"Middle_Name\", \"Last_Name\"\r\n" + 
								"	FROM public.\"Personal\" where \"Employee_ID\"='"+emp+"';";
						Statement stmt1 = db.connect().createStatement();
						ResultSet rs4=stmt1.executeQuery(sql4);
						rs4.next();
						String empname=rs4.getString(2)+rs4.getString(3)+rs4.getString(4)+rs4.getString(5);
						System.out.println("1st done");
						
						
						
						String sql2="SELECT dep,designation,date_join,relieve_date from public.officeinfo where \"Employee_ID\"='"+emp+"';";
						
						Statement stmt2 = db.connect().createStatement();
						ResultSet rs2=stmt2.executeQuery(sql2);
						rs2.next();
						System.out.println("2st done");
						
						String designation=rs2.getString(2);
						String dep=(String)rs2.getString(1);
						
						Date fromdate=rs2.getDate(3);
						Date todate=rs2.getDate(4);

						SimpleDateFormat simpleDateformat = new SimpleDateFormat("yyyy/MM/dd");
						
						DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
						LocalDate localDate = LocalDate.now();
						Date datetoday = java.sql.Date.valueOf(localDate);										
						
						map.put("Report_date",String.valueOf(datetoday));
						map.put("name",empname);
						map.put("designation",designation);
						map.put("dep",dep);
						
						map.put("joindate",simpleDateformat.format(fromdate)); 
						map.put("relievedate",simpleDateformat.format(todate));
										
						return map;	
					}
					
				
				
			}
			catch (SQLException e) {
				
				e.printStackTrace();
				System.out.println(e.getMessage());

			}
			
		}
		map.put("Status","Not Succesful");
		return map;
		
	}

	//HOD

	public List live_reqhod() throws SQLException {//tested
		
		
	 	
		String sql1="SELECT * FROM public.noc where request= false ;";
		
		
		Statement st = db.connect().createStatement();
		ResultSet rs = st.executeQuery(sql1);
		rs.next();
		
		String empid=rs.getString("Employee_ID");
		System.out.println(empid);

		String sql2="SELECT \"First_Name\", \"Last_Name\"\r\n" + 
				"	FROM public.\"Personal\" where \"Employee_ID\"='"+empid+"';";
		Statement st2 = db.connect().createStatement();
		ResultSet rs1 = st2.executeQuery(sql2);
		rs1.next();

		String random=rs1.getString("First_Name");
		System.out.println(random);

	System.out.println(rs.getBoolean("fin"));

	List<Map<String, String>> mymap = new ArrayList<Map<String, String>>();
		
		int j=0;
		do{
			Map<String, String> emp_list = new HashMap<String, String>();
			
			if(rs.getBoolean("fin")==false && rs.getBoolean("request")==false && rs.getBoolean("hod")==false ) {
				
				
				emp_list.put("name", rs1.getString("First_Name"));
				emp_list.put("lastname", rs1.getString("Last_Name"));
				emp_list.put("EMPID", empid);
				emp_list.put("designation", "Teacher");
				emp_list.put("Type","No Objection Certificate");
				emp_list.put("reason", rs.getString("reason"));
				
				mymap.add(j,emp_list);
				j++;	
		}
		
		}while(rs.next());
		return mymap;	
		}  

	//PRINCIPAL
		public List live_requestp() throws SQLException {
			
			String sql1="SELECT * FROM public.noc where request = false;";
			
			Statement st = db.connect().createStatement();
			ResultSet rs = st.executeQuery(sql1);
			rs.next();
			
			String empid=rs.getString("Employee_ID");
			System.out.println(empid);

			String sql2="SELECT \"First_Name\", \"Last_Name\"\r\n" + 
					"	FROM public.\"Personal\" where \"Employee_ID\"='"+empid+"';";
			Statement st2 = db.connect().createStatement();
			ResultSet rs1 = st2.executeQuery(sql2);
			rs1.next();

			String random=rs1.getString("First_Name");
			System.out.println(random);

		System.out.println(rs.getBoolean("fin"));

		List<Map<String, String>> mymap = new ArrayList<Map<String, String>>();
			System.out.println("Starting do-while");
			int j=0;
			do{
				System.out.println("Inside do-while");
				Map<String, String> emp_list = new HashMap<String, String>();
				
				if(rs.getBoolean("fin")==false && rs.getBoolean("request")==false && rs.getBoolean("hod")==true && rs.getBoolean("principal") == false ) {
					
					System.out.println("Inside if");
					emp_list.put("name", rs1.getString("First_Name"));
					emp_list.put("lastname", rs1.getString("Last_Name"));
					emp_list.put("EMPID", empid);
					emp_list.put("designation", "Teacher");
					emp_list.put("Type","No Objection Certificate");
					emp_list.put("reason", rs.getString("reason"));
					
					mymap.add(j,emp_list);
					j++;	
				}
			
			}while(rs.next());
			return mymap;	
		}
	
		

}
