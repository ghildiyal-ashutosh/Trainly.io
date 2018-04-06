
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ashu
 */
public class Faculty {
    private static Connection conn;
    private static PreparedStatement stmt;
    private static int professor_ID;
    private static Statement createStat;
    public static void facultyLogIn()
    {
   
         int t = 3;
        Scanner scanner = null;
        while(t > 0){
            scanner = new Scanner(System.in); 
            System.out.println("Please enter your email");
            String email = scanner.next();
            System.out.println("Enter your password");
            String pass = scanner.next();
            // save his id here
            String password = null;
            
            try{
                conn = ConnectionManager.getConnection();
                System.out.println(email);
                String pass2 = "select Password from login where Email = ?;";
                String student_ID = "SELECT Student_ID FROM login where Email =?;";
                String faculty_ID = "SELECT f.Faculty_ID from faculty f INNER JOIN student s ON f.Faculty_ID = s.Student_ID WHERE f.Appointer_User_ID IS NOT NULL AND f.Faculty_ID = ?;";
                stmt = conn.prepareStatement(pass2);
                stmt.setString(1, email);
                ResultSet rs = stmt.executeQuery();
                stmt = conn.prepareStatement(student_ID);
                stmt.setString(1, email);
                ResultSet rs1 = stmt.executeQuery();
                stmt = conn.prepareStatement(faculty_ID);
                stmt.setInt(1,rs1.getInt("Student_ID"));
                ResultSet rs2 = stmt.executeQuery();
                if(rs.next()){
                    password = rs.getString("Password");
                }else{
                    System.out.println("Email ID not found");
                }
                
                if(rs1.next()){
                    professor_ID = rs1.getInt("Student_ID");
                }
                
            }catch(SQLException e){
                System.out.println("error on checking the login process" + e);
            }finally{
                conn.close();
            }
            try
            {
                boolean status = Password.check(pass,password);
                if (status)
                {
                    if(rs2.next())
                    {
                    System.out.println("You have logged In successfully Professor");
                    facultyHomePage(professor_ID);// send his id here
                    break;
                    
                }
                    else
                    {
                        System.out.println("Not a professor yet, Loggin In as Student");
                        Students.studentHomePage(professor_ID);
                        
                    }
                    
                }
                else{
                    System.out.println("You have entered the wrong password, you have " + t + " chances left");
                    t--;
                }
            }
            catch(Exception e)
            {
                System.out.println("Empty password entered");
            }
                if(t < 0){
                    System.out.println("Chances exceeded");
                    System.exit(0);
                }
               
        }
        scanner.close();
       
}
    
    public static facultyHomePage
            {
            System.out.println("Enter one of the choices given below");
            System.out.println("1.Create a Course\n 2.Account History\n3.Modify Question\n4.Go Back"
                    + "")
                   Scanner scan = new Scanner(System.in);
                   int choice = scan.nextInt();
                   switch(choice)
                   {
                       case 1:
                           System.out.println("Welcome Professor, Lets create !!!");
                           createCourse(professor_ID);
                           break;
                           
                       case 2:
                           System.out.println("Your Account History tells you all about the Courses that you have created");
                           accountHistory(professor_ID);
                           break;
                           
                       case 3:
                           System.out.println("Here is a list of Question ordered by maximum likes");
                           modifyQuestion(professor_ID);
                           break;
                           
                       case 4:
                           System.out.println("GoodBye Professor");
                           LogIn.menu();
                           break;
                           
                       default:
                           Faculty.facultyLogIn();
                           break;
                           
                    }
                   scan.close();
}
            public static void createCourse(int id)
            {
                System.out.println("Press 1 to create a new Course\nPress 2 to go back")
                        Scanner scan = new Scanner(System.in);
                        Random rand = new Random();
                        
                        int courseId =0;
                        int choice = scan.nextInt();
                        if (choice ==1)
                        {
                            courseId = rand.nextInt(500);
                           
                            scanner.nextLine();
                             System.out.println("Enter the name of your course");
                            String cName = scan.nextLine();
                            System.out.println("Enter the icon for your course");
                            int icon = scan.nextInt();
                            scan.nextLine();
                            System.out.println("Enter the Primary Topic");
                            String primary = scan.nextLine();
                            System.out.println("Enter the suggested cost fo your course");
                         
                            int cost = scan.nextInt());
                            scan.nextLine();
                            System.out.println("Enter some description for your course");
                            String desc = scan.nextLine();
                            System.out.println("Now you have to enter the secondary topics of the this course ");
                            
                            INSERT INTO courses...Course_ID,Creation_Date,Description,Name,Icon,Primary_Topic,cost;
                            INSERT INTO CREATOR OF course_creation Faculty_ID, Course_ID,Time_Stamp;
                            secondaryTopic(courseId);
                                       scan.close();                         
                            
                        }
                         else
                        {
                            facultyHomePage();
                        }
                        
            }
            public static void secondaryTopic(courseId)
            {
                System.out.println("Press 1 to Enter Secondary Topic\n2.Press 2 to Go Back);
                Scanner scan = new Scanner(System.in);
                int choice = scan.nextInt();
                if (choice == 1)
                {
                scan.nextLine();
                String secondary = scan.nextLine();
                INSERT INTO secondary_topics....Secondary_topic,Course_ID;
                System.out.println("Secondary Topic added successfully");
                System.out.println("Press 1 to to add course_material\nPress 2 to add more secondary topic ");
                int choice 2 = scan.nextInt();
                if (choice2 == 1)
                    addCourseMaterial (courseId);
                    else
                    secondaryTopic(courseId);
                
                
            }
                else
                    createCourse(int id);
                    scan.close();
                    
               }
            public static void addCourseMaterial(courseId)
            {
                Random rand = new Random(500)*10;
                int material_id = rand.nextInt();
                Scanner scan = new Scanner (System.in);
                System.out.println("Enter the name of the course_material");
                String courseName= scan.nextString();
                System.out.println("Course_material added successfully");
                System.out.println("Course creation successfull");
                System.out.println("Press 1 to add more course material, Press 2 to go back ");
                int choice = scan.nextInt();
                INSERT INTO  course_material .Course_ID,.Material_ID,Name
                if (choice = 1)
                {
                    addCourseMaterial(courseId);
                }
                else
                    secondaryTopic(courseId);
                
            }
            scan.close();
}

    
   
    

