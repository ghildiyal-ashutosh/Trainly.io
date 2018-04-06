/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ashu
 */

import java.util.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
class LogIn
{
    public static Statement stmt;
    public static Connection conn;
    public static ResultSet rs = null;
    public static int last_insert_id = 0;
    public static PreparedStatement prepare;
public static void main(String args[])
{
    
        try {
            menu();
        } catch (Exception ex) {
            Logger.getLogger(LogIn.class.getName()).log(Level.SEVERE, null, ex);
        }
      
}
	
	public static void menu() throws SQLException, Exception
	{
           int choice;
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Welcome To Trainly.Io, Learn From The Best");
            
            while (true) {
                System.out.println("Please enter one of the numbers");
                System.out.println(" 1. LogIn as Student \n 2. LogIn as Admin \n 3. LogIn as Faculty \n 4. SignUp \n 5. Exit");
                System.out.println("************************************************************************");
                choice = scanner.nextInt();
                if ((choice  < 0) || (choice > 5))
                    System.out.println("ERROR Please enter a valid choice\n");
                else
                    break;
            }
            switch(choice)
            {
                case 1:
                    System.out.println("Welcome to Trainly.io");
                    
                            Students.studentLogIn();
                    break;
                    
                case 2:
                    System.out.println("Welcome Admin");
                    //adminLogIn();
                    break;
                    
                case 3:
                    System.out.println("Welcome Professor X");
	            Faculty.facultyLogIn();
                    break;
                    
                case 4:
                    System.out.println("Welcome new User");
                    signUp();
                    break;
                    
                case 5:
                    System.exit(0);
                    break;
                    
                default:
                    System.out.println("Invalid input");
                    break;
            }       }
	}
	
	public static void signUp() throws SQLException, Exception
	{
		Scanner scanner = new Scanner(System.in);
		//System.out.println("Welcome new Student, Trainly.Io is happy to have you");
		
		System.out.println(" 1.Go back \n 2.Continue \n 3.Exit");
		 try {
          
        int x = scanner.nextInt();
        switch(x)
        {
            case 1:
                menu();
                break;
                
            case 3:
                System.exit(0);
                break;
                
            default:
           
                System.out.println("Please enter the details asked below ,press enter after you enter the detail");
        
		System.out.println("Enter your first name");
		String firstName = scanner.next();
		
		System.out.println("Enter your second name");
		String secondName = scanner.next();
		
		System.out.println("Enter your postalCode");
		int postalCode = scanner.nextInt();
				 scanner.nextLine();
		
	        System.out.println("Enter your street");
		String street = scanner.nextLine();
				 scanner.nextLine();
		
		System.out.println("Enter your city");
		String city = scanner.next();
				 scanner.nextLine();
		
		System.out.println("Enter your country");
		String country = scanner.next();
		
		System.out.println("Enter your emailId");
		String emailId = scanner.next();
		
		System.out.println("Enter your Home-Contact Number, should be of 10 digits");
		long hContact = scanner.nextLong();
		
		System.out.println("Enter your Work-Contact Number, should be of 10 digits");
	    long wContact = scanner.nextLong();
			 
		System.out.println("Enter your password, length of the password should be 8 and 12 characters");
			 String password = scanner.next();
			 
	        System.out.println("Enter your password again");
			 String password2 = scanner.next();
			 
	        System.out.println("Do you want to work as a faculty, Enter Yes if yes, No if no");
			 String faculty = scanner.next();
			 
			 
			 checkInput(firstName, secondName,postalCode,street, city,country,emailId,hContact,wContact,password,password2,faculty);
                        
                         break;
			 }
		 }
		catch (InputMismatchException e) {
                System.out.println("You have entered invalid data" +e);
			signUp();
	 }
		scanner.close();
                
        }
		
    public static void checkInput(String fn,String sn, int pc, String st, String ct, 
            String coun, String eid, long hc, long wc, String pass1, String pass2, String faculty  ) throws SQLException, Exception
		{
			Scanner scanner = new Scanner(System.in);
			if((fn.length() >=2) && (fn.length() < 20)) 
			{
				if((sn.length()>=2) && (sn.length() < 20))
				{
					if((st.length() >=5) && (st.length() < 20))
					{
						if((ct.length() >= 2) && (ct.length () < 20))
						{
							if ((eid.length() >=2 && eid.length() < 20) && (eid.indexOf('@')!=-1) && ((eid.indexOf('.')) >                                                 (eid.indexOf('@'))))
							{
								if(Long.toString(hc).length() == 10)
								{
									if(Long.toString(wc).length() == 10)
									{
										if(pass1.equals(pass2) && (pass1.length() > 6 && pass1.length() < 12))
										{
											if (faculty.equalsIgnoreCase ("Yes"))
											{
                                           System.out.println("First Name \t"+ fn + " \n Second Name \t"+ sn +"\n Postal Code \t"+
                                                   pc + " \n Street \t"+ st + "\n City \t"+ ct + " \n Country \t"+ coun + "\n Email Id \t"+ eid + 
                                                   " \n Home-Contact \t"+ hc + "\n Work-Contact \t" +wc +"\n Password \t *****" + 
                                                           "\n Apply for Faculty \t" + faculty);
                                           
                                           try{
                                           conn = ConnectionManager.getConnection();
                                           String insertQuery = "insert into student(First_Name,Last_Name,Postal_Code,Country,City,Email,Street) values"
                                                   + "(?, ?, ? , ?, ?, ?, ?)";
                                           prepare = conn.prepareStatement(insertQuery,Statement.RETURN_GENERATED_KEYS);
                                           prepare.setString(1,fn);
                                           prepare.setString(2,sn);
                                           prepare.setInt(3,pc);
                                           prepare.setString(4, coun);
                                           prepare.setString(5, ct);
                                           prepare.setString(6, eid);
                                           prepare.setString(7, st);
                                           prepare.execute();
                                           rs = prepare.getGeneratedKeys();
                                           
                                           if(rs.next()){
                                                last_insert_id = rs.getInt(1);
                                           }
                                           
                                           }catch(Exception e){
                                               System.out.println("Something wrong with inserting");
                                           }finally{
                                               conn.close();
                                               
                                           }
                                           
                                           
												
                                 System.out.println("You have to enter some more details if you want to be  a Faculty");
                                 System.out.println("Enter You Work Website");
                                 String work_website = scanner.next();
                                 System.out.println("Enter affiliation detail");
                                 String affiliation = scanner.next();
                                 System.out.println("Enter the Title you are applying for");
                                 String title = scanner.next();
                                 System.out.println("Your faculty application is in Progress, You can LogIn as student");
                                 
                                 try{
                                     conn = ConnectionManager.getConnection();
                                     String queryfaculty = "insert into faculty(Faculty_ID,Work_Website,Affliation,Time_of_Verification,Appointer_User_ID)" + 
                                         "values(" + last_insert_id + ",?,?,null,null)";
                                 
                                 prepare = conn.prepareStatement(queryfaculty);
                                 prepare.setString(1, work_website);
                                 prepare.setString(2, affiliation);
                                 prepare.execute();
                                 String hashed = Password.getSaltedHash(pass2);
                                
                                 String loginInsertQuery = "insert into login(Email,Student_ID,Password) values(?,"+last_insert_id +",?);";
                                                              prepare = conn.prepareStatement(loginInsertQuery);
                                                              
                                                              prepare.setString(1, eid);
                                                              prepare.setString(2, hashed);
                                                              prepare.execute();
                                 }catch(Exception e){
                                     System.out.println("Something wrong with inserting faculty");
                                 }finally{
                                     conn.close();
                                    
                                 }
                                 
                                 
                                 menu();
                                 
                                 
                                                                                        }
                                 
                                else if(faculty.equalsIgnoreCase("No"))
			         {
                                     System.out.println("You have entered the following details");
				     System.out.println("First Name \t"+ fn + " \n Second Name \t"+ sn +"\n Postal Code \t"+ pc
                                                        + " \n Street \t"+ st + "\n City \t"+ ct + " \n Country \t"+ coun + "\n Email Id \t"+
                                                        eid + " \n Home-Contact \t"+ hc + "\n Work-Contact \t" +wc +"\n Password \t *****" + 
                                                        "\n Apply for Faculty \t" + faculty);
                                     try{
                                         conn = ConnectionManager.getConnection();
                                     String insertQuery = "insert into student(First_Name,Last_Name,Postal_Code,Country,City,Email,Street) values"
                                                   + "(?, ?, ? , ?, ?, ?, ?)";
                                           PreparedStatement prepare = conn.prepareStatement(insertQuery,Statement.RETURN_GENERATED_KEYS);
                                           prepare.setString(1,fn);
                                           prepare.setString(2,sn);
                                           prepare.setInt(3,pc);
                                           prepare.setString(4, coun);
                                           prepare.setString(5, ct);
                                           prepare.setString(6, eid);
                                           prepare.setString(7, st);
                                           prepare.execute();
                                           rs = prepare.getGeneratedKeys();
                                           
                                           if(rs.next()){
                                                last_insert_id = rs.getInt(1);
                                           }
                                     }catch(SQLException e){
                                         System.out.println("Something wrong with inserting the student");
                                     }finally{
                                         
                                       
                                         conn.close();
                                     }
				      System.out.println("Enter Yes to continue , anything else to return to menu");
                                                         String choice = scanner.next();
							 if (choice.equalsIgnoreCase("Yes"))
							 {
						            try
                                                            {// Hash a password for the first time
                                                              String hashed = Password.getSaltedHash(pass2);
                                                              try{
                                                                  conn = ConnectionManager.getConnection();
                                                                  String loginInsertQuery = "insert into login(Email,Student_ID,Password) values(?,"+last_insert_id +",?);";
                                                              prepare = conn.prepareStatement(loginInsertQuery);
                                                              
                                                              prepare.setString(1, eid);
                                                              prepare.setString(2, hashed);
                                                              prepare.execute();
                                                              }catch(Exception e){
                                                                  System.out.println(e);
                                                              }finally{
                                                                  conn.close();
                                                                  
                                                              }
                                                               System.out.println("You are now registered  as a Student");
		                                                menu();
                                                             }
                                                catch( Exception e)
                                                {
                                                    System.out.println ("No password for empty" + e);
                                                }
                                                }
                                               
			          
                                                                                
				else 
				menu();
                                 }
                                                                                                                                                                                        								
				else
				{
			            System.out.println("Invalid choice,  fill the form again");
				     signUp();
				}
				}
				else{
			             System.out.println("Password didnt match");
				     signUp();
				    }
										
									}
									else
									{
										System.out.println("Incorrect Work-Contact");
										signUp();
									}
								}
								else
								{
									System.out.println("Incorrect Home-Contact");
									signUp();
								}
								
							}
							else
							{
								System.out.println("Incorrect email");
								signUp();
							}
						}
					
					else
					{
						System.out.println("Incorrect or too long Country Name");
						signUp();
					}
					}
					else
					{
						System.out.println("Incorrect or too long street name");
						signUp();
					}
				}
				else
				{
					System.out.println("Incorrect or too long Second Name");
					signUp();
				}
			}
			else
			{
				System.out.println("Incorrect or too long First Name");
				signUp();
			}
				
		
	scanner.close();
		}
	}

										 
							   
										   

										   


	
