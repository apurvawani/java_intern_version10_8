package com.example.demo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;

public class Salary_certificate {

	public DB db = new DB();
	
public Map<String,String> req(@RequestBody Map<String, Object> payload) throws Exception{
	
	//CREATE A NEW TABLE SUCH THAT IT CONTAINS APPLICATION NUMBER<EMPIDdate>
	 
	
	String emp_id = (String)payload.get("Employee_ID");
	
	
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	java.util.Date date2 = sdf1.parse((String)payload.get("from_month"));
	Date from_date = new java.sql.Date(date2.getTime()); 
	
	java.util.Date date1 = sdf1.parse((String)payload.get("to_month"));
	Date to_date = new java.sql.Date(date1.getTime()); 
	
	
	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
	LocalDate localDate = LocalDate.now();
	Date date = java.sql.Date.valueOf(localDate);
	
	String dat=String.valueOf(date);
	
	Calendar cal = Calendar.getInstance();
	java.util.Date date3 = cal.getTime();
	DateFormat dateformat = new SimpleDateFormat("HH:mm:ss");
	String formattedDate = dateformat.format(date3);
	
	
	String salary_id=null;
	salary_id=emp_id+dat+formattedDate;
	boolean hod=false;
	boolean principal=false;
	boolean fin=false;
	boolean request=false;
	
	
	String sql="INSERT INTO public.salary(\r\n" + 
	"	principal,hod,\"Employee_ID\",fin,from_date,to_date,date,salary_id,request)\r\n" + 
"	VALUES ( ?, ?, ?, ?, ?, ?,?,?,?);";
	
	try {
			PreparedStatement stmt = db.connect().prepareStatement(sql);
			stmt.setBoolean(1,principal);
			stmt.setBoolean(2, hod);
			stmt.setString(3, emp_id);
			stmt.setBoolean(4, fin);
			stmt.setDate(5, from_date);
			stmt.setDate(6, to_date);
			stmt.setDate(7, date); 
			stmt.setString(8, salary_id);
			stmt.setBoolean(9,request);

			
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
	String emp = (String)payload.get("Employee_ID");
	String salaryid;
	
	salaryid = (String)payload.get("salaryid");
	
	System.out.println(emp);

	Date fromdate;
	Date todate;
	
	
	Map<String, String> map = new HashMap<String, String>();

		
	String sql="SELECT * FROM public.salary where salary_id= '"+salaryid+"';";
	Statement stmt = db.connect().createStatement();
	ResultSet rs=stmt.executeQuery(sql);
	int i=1;
	while(rs.next()){ 
		try {
			 
			 fromdate=rs.getDate("from_date");
			
			 SimpleDateFormat simpleDateformat = new SimpleDateFormat("MMMM-YYYY");
			
			 todate=rs.getDate("to_date");
			
			
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
					String TYPE = "Salary Certificate";

					map.put("Principal",PRINCIPAL);
					map.put("HOD",HOD);
					map.put("Admin",ADMIN);
					map.put("Type",TYPE);
					return map;

					
						
				}
				else if( field1==true && field2==true && field3==true && field4==false )
				{
					
					String sql4="SELECT \"Employee_ID\", \"Salutation\", \"First_Name\", \"Middle_Name\", \"Last_Name\", \"Father_Name\", \"Mother_Name\"\r\n" + 
							"	FROM public.\"Personal\" where \"Employee_ID\"='"+emp+"';";
					Statement stmt1 = db.connect().createStatement();
					ResultSet rs4=stmt1.executeQuery(sql4);
					rs4.next();
					String empname=rs4.getString(3)+rs4.getString(5);
					System.out.println("1st done");
					
					
					
					String sql2="SELECT dep,designation,pay,paygrade from public.officeinfo where \"Employee_ID\"='"+emp+"';";
					
					Statement stmt2 = db.connect().createStatement();
					ResultSet rs2=stmt2.executeQuery(sql2);
					rs2.next();
					System.out.println("2st done");

					
					
					DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
					LocalDate localDate = LocalDate.now();
					Date datetoday = java.sql.Date.valueOf(localDate);
					
					
					
					
					int pay=rs2.getInt(3);
					System.out.println("3rd done");

					String designation=rs2.getString(2);
					String dep=(String)rs2.getString(3);
					
					int paygrade=rs2.getInt(4);
					int hra_m1=(int) ((pay+paygrade)*0.2);
					int hra_m2=(int) ((pay+paygrade)*0.21);
					int hra_m3=(int) ((pay+paygrade)*0.22);
					
					int da_m1=(int) ((pay+paygrade)*0.4);
					int da_m2=(int) ((pay+paygrade)*0.41);
					int da_m3=(int) ((pay+paygrade)*0.42);
					
					int dp_m1=(int) ((pay+paygrade)*0.3);
					int dp_m2=(int) ((pay+paygrade)*0.31);
					int dp_m3=(int) ((pay+paygrade)*0.32);
					
					int cca_m1=(int) ((pay+paygrade)*0.2);
					int cca_m2=(int) ((pay+paygrade)*0.21);
					int cca_m3=(int) ((pay+paygrade)*0.22);
					
					int ta_m1=(int) ((pay+paygrade)*0.2);
					int ta_m2=(int) ((pay+paygrade)*0.21);
					int ta_m3=(int) ((pay+paygrade)*0.22);
					
					
					
					//PROVIDENT FUND
					int pf_m1=(int) ((pay+da_m1)*0.12);
					int pf_m2=(int) ((pay+da_m2)*0.12);
					int pf_m3=(int) ((pay+da_m3)*0.12);
					
					//PROFESSIONAL TAX
					int pt_m1=(int) ((paygrade)*0.10);
					int pt_m2=(int) ((paygrade)*0.10);
					int pt_m3=(int) ((paygrade)*0.10);
					
					//INCOME TAX
					int it_m1=(int) ((pay)*0.30);
					int it_m2=(int) ((pay)*0.30);
					int it_m3=(int) ((pay)*0.30);
					
					int revenue_m1=5;
					int revenue_m2=5;
					int revenue_m3=5;//revenue stamp
					
					int other_m1=(int) ((pay)*0.02);
					int other_m2=(int) ((pay)*0.02);
					int other_m3=(int) ((pay)*0.02);
					
					int total_m1=pf_m1+pt_m1+it_m1+revenue_m1+other_m1;
					int total_m2=pf_m2+pt_m2+it_m2+revenue_m2+other_m2;
					int total_m3=pf_m3+pt_m3+it_m3+revenue_m3+other_m3;
									
					
					int gross1=pay+hra_m1+cca_m1+da_m1+cca_m1+dp_m1;
					int gross2=pay+hra_m2+cca_m2+da_m2+cca_m2+dp_m2;
					int gross3=pay+hra_m3+cca_m3+da_m3+cca_m3+dp_m3;
					
					int net1=gross1-total_m1;
					int net2=gross2-total_m2;
					int net3=gross3-total_m3;
					
					
					
					
					map.put("Report_date",String.valueOf(datetoday));
					map.put("name",empname);
					map.put("designation",designation);
					map.put("dep",dep);
					
					map.put("fromdate",simpleDateformat.format(fromdate)); 
					map.put("todate",simpleDateformat.format(todate));
					
					map.put("pay1",String.valueOf(pay));
					map.put("pay2",String.valueOf(pay));
					map.put("pay3",String.valueOf(pay));
					
					map.put("hra1",String.valueOf(hra_m1));
					map.put("hra2",String.valueOf(hra_m2));
					map.put("hra3",String.valueOf(hra_m3));
					
					map.put("da1",String.valueOf(da_m1));
					map.put("da2",String.valueOf(da_m2));
					map.put("da3",String.valueOf(da_m3));
					
					map.put("dp1",String.valueOf(dp_m1));
					map.put("dp2",String.valueOf(dp_m2));
					map.put("dp3",String.valueOf(dp_m3));
					
					map.put("cca1",String.valueOf(cca_m1));
					map.put("cca2",String.valueOf(cca_m2));
					map.put("cca3",String.valueOf(cca_m3));

					
					map.put("ta1",String.valueOf(ta_m1));
					map.put("ta2",String.valueOf(ta_m2));
					map.put("ta3",String.valueOf(ta_m3));
					
					map.put("pf1",String.valueOf(pf_m1));
					map.put("pf2",String.valueOf(pf_m2));
					map.put("pf3",String.valueOf(pf_m3));
					
					map.put("pt1",String.valueOf(pt_m1));
					map.put("pt2",String.valueOf(pt_m2));
					map.put("pt3",String.valueOf(pt_m3));
					
					map.put("it1",String.valueOf(it_m1));
					map.put("it2",String.valueOf(it_m2));
					map.put("it3",String.valueOf(it_m3));
					
					map.put("r1",String.valueOf(revenue_m1));
					map.put("r2",String.valueOf(revenue_m2));
					map.put("r3",String.valueOf(revenue_m3));
					
					map.put("o1",String.valueOf(other_m1));
					map.put("o2",String.valueOf(other_m2));
					map.put("o3",String.valueOf(other_m3));
					
					map.put("t1",String.valueOf(total_m1));
					map.put("t2",String.valueOf(total_m2));
					map.put("t3",String.valueOf(total_m3));
					
					map.put("g1",String.valueOf(gross1));
					map.put("g2",String.valueOf(gross2));
					map.put("g3",String.valueOf(gross3));
					
					map.put("n1",String.valueOf(net1));
					map.put("n2",String.valueOf(net2));
					map.put("n3",String.valueOf(net3));
					
					
					 
					map.put("m1",String.valueOf(fromdate));
					
					Calendar cal = Calendar.getInstance();
					 cal.setTime(fromdate); 
						cal.add(Calendar.DAY_OF_MONTH,30);  					 
						System.out.println(cal.getTime());
						SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

						String newDate = sdf1.format(cal.getTime()); 
						
						 cal.setTime(fromdate); 
						cal.add(Calendar.DAY_OF_MONTH,61);  					 
						System.out.println(cal.getTime());
						String to_date = sdf1.format(cal.getTime()); 

//					 System.out.println(fromdate.format(cal.getTime())); 
					map.put("m2",String.valueOf(newDate));
					map.put("m3",String.valueOf(to_date));
					
					map.put("x1",String.valueOf(fromdate));
					map.put("x2",String.valueOf(newDate));
					map.put("x3",String.valueOf(to_date));
					
					
//					map.put(empname, "name");
				
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
	
	
 	//String false1="false";
	String sql1="SELECT * FROM public.salary where request= false and hod = false;";
	
	
	Statement st = db.connect().createStatement();
	ResultSet rs = st.executeQuery(sql1);
	
	String empid = null;
	String sql2=null;
	Statement st2 = null;
	ResultSet rs1 = null;
	String random=null;
	
	
	String sql3=null;
	Statement st3 = null;
	ResultSet rs3 = null;
	
	


List<Map<String, String>> mymap = new ArrayList<Map<String, String>>();
	
	int j=0;
	while(rs.next()){
		
		
		 empid=rs.getString("Employee_ID");
		 System.out.println(empid);

		 sql2="SELECT \"First_Name\", \"Last_Name\"\r\n" + 
				"	FROM public.\"Personal\" where \"Employee_ID\"='"+empid+"';";
		 st2 = db.connect().createStatement();
		 rs1 = st2.executeQuery(sql2);
		 rs1.next();
		
		
		
		sql3="SELECT designation FROM public.officeinfo where \"Employee_ID\"='"+empid+"';";
		st3 = db.connect().createStatement();
	    rs3 = st3.executeQuery(sql3);
		rs3.next();
		
		

		random=rs1.getString("First_Name");
		System.out.println(random);
		Map<String, String> emp_list = new HashMap<String, String>();
		
		if(rs.getBoolean("fin")==false && rs.getBoolean("request")==false && rs.getBoolean("hod")==false ) {
			
			
			emp_list.put("name", rs1.getString("First_Name")+" "+rs1.getString("Last_Name"));
		
			emp_list.put("EMPID", empid);
			emp_list.put("designation", rs3.getString("designation"));
			emp_list.put("Type", "Salary Certificate");
			emp_list.put("Certificate_id",rs.getString("salary_id"));
			emp_list.put("duration",rs.getString("from_date")+" - "+rs.getString("to_date"));

			
			
			System.out.println(rs3.getString("designation"));
			
			mymap.add(j,emp_list);
			j++;	
	}
	
	}
	return mymap;	
	}  

//PRINCIPAL
public List live_requestp() throws SQLException {//tested
	
	
 	//String false1="false";
	String sql1="SELECT * FROM public.salary where request=false and hod = true and fin = false ;";
	
	
	Statement st = db.connect().createStatement();
	ResultSet rs = st.executeQuery(sql1);
	
	String empid = null;
	String sql2=null;
	Statement st2 = null;
	ResultSet rs1 = null;
	String random=null;
	
	
	String sql3=null;
	Statement st3 = null;
	ResultSet rs3 = null;
	
	


List<Map<String, String>> mymap = new ArrayList<Map<String, String>>();
	
	int j=0;
	while(rs.next()){
		
		
		 empid=rs.getString("Employee_ID");
		 System.out.println(empid);

		 sql2="SELECT \"First_Name\", \"Last_Name\"\r\n" + 
				"	FROM public.\"Personal\" where \"Employee_ID\"='"+empid+"';";
		 st2 = db.connect().createStatement();
		 rs1 = st2.executeQuery(sql2);
		 rs1.next();
		
		
		
		sql3="SELECT designation FROM public.officeinfo where \"Employee_ID\"='"+empid+"';";
		st3 = db.connect().createStatement();
	    rs3 = st3.executeQuery(sql3);
		rs3.next();
		
		

		random=rs1.getString("First_Name");
		System.out.println(random);
		Map<String, String> emp_list = new HashMap<String, String>();
		
		if(rs.getBoolean("fin")==false && rs.getBoolean("request")==false &&rs.getBoolean("hod")==true) {
			
			
			emp_list.put("name", rs1.getString("First_Name")+" "+rs1.getString("Last_Name"));
			
			emp_list.put("EMPID", empid);
			emp_list.put("designation", rs3.getString("designation"));
			emp_list.put("Type", "Salary Certificate");
			emp_list.put("Certificate_id",rs.getString("salary_id"));
			emp_list.put("duration",rs.getString("from_date")+" - "+rs.getString("to_date"));

			System.out.println(rs3.getString("designation"));
			
			mymap.add(j,emp_list);
			j++;	
	}
	
	}
	return mymap;	
	}  
	
	
	
	
	
	


}
	
	
	
	
