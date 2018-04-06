import java.util.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
public class Students {
    private static Connection conn;
    private static PreparedStatement stmt;
    private static int student_ID1;
    private static Statement createStat;
     
    
    public static void studentLogIn() throws SQLException
    {   int t = 3;
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
                stmt = conn.prepareStatement(pass2);
                stmt.setString(1, email);
                ResultSet rs = stmt.executeQuery();
                stmt = conn.prepareStatement(student_ID);
                stmt.setString(1, email);
                ResultSet rs1 = stmt.executeQuery();
                if(rs.next()){
                    password = rs.getString("Password");
                }else{
                    System.out.println("Email ID not found");
                }
                
                if(rs1.next()){
                    student_ID1 = rs1.getInt("Student_ID");
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
                    System.out.println("You have logged In successfully");
                    studentHomePage(student_ID1);// send his id here
                    break;
                    
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
   // public static void studentHomePage()
    //{
        //System.out.println("1.Account History \n 2.My Courses \n 3.Search Course By Professor \n");
                // 4.Search Course By Name);
        
    //}

    private static void studentHomePage(int id) {
        //To change body of generated methods, choose Tools | Templates.
        Scanner scanner = new Scanner(System.in);
        int choice;
        while(true){
        System.out.println("Please enter the one of the numbers");
        System.out.println("1. Account History \n2.My Courses \n3.Search queries by professor \n4. Search course by name \n5.Enroll Course \n6.PlayList"+
                "\n7.Forum Questions \n8.Course Material Status \n9.Go Back \n10.Exit");
        
        System.out.println("***********************************************************************************************************");
        choice = scanner.nextInt();
        if(choice < 0 || choice > 8){
            System.out.println("Please enter a valid choice");
        }else{
            break;
        }
        }
        
        switch(choice){
            case 1:
            {
                System.out.println("Your account history is as follows:");
                accountHistory(id);
                break;
            }
            
            case 2:
            {
                System.out.println("My courses");
                myCourses(id);
                break;
            }
            case 3:
            {
                System.out.println("Search course by prof name");
                searchByProfName(id);
                break;
            }
            
            case 4:
            {
                System.out.println("Search course by name");
                searchCourseByName(id);
                break;
            }
            case 5:
            {
                System.out.println("Enroll for a new course");
                enrollCourse(id);
                break;
            }
            case 6:
            {
                System.out.println("Playlist");
                playList(id);
                break;
            }
            case 7:
            {
                System.out.println("Forum Questions");
                forumQuestions(id);
                break;
            }
            
            case 8:
            {
                System.out.println("View Course Material");
                courseMaterial(id);
                break;
            
            }
            /*
            case 6:
            {
                System.out.println("Course material status");
                courseMaterialStatus(id);
                break;
            }
            case 7:
            {
                System.out.println("Redirecting to menu");
            try {
                LogIn.menu();
            } catch (Exception ex) {
                Logger.getLogger(Students.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
            case 8:
            {
                System.out.println("Exit");
                System.exit(0);
            }
           
            default:
            {
                System.out.println("Please try again");
                studentHomePage(id);
            }
             
            
         */   
        }
            scanner.close();
    }
    
    public static void accountHistory(int id){
        try{
            conn = ConnectionManager.getConnection();
            String queryAccountHistory = "SELECT c.Course_ID AS Course_Id, c.Description AS Course_Description, c.Name AS Courses_Name,c.cost AS Course_Cost,sc.Date_of_Payment AS Course_Started_On, " +
                 "cc.Score AS Marks_Obtained ,cc.Date_of_Completion AS Date_Of_Completion FROM (courses c INNER JOIN student_courses sc ON c.Course_ID = sc.Course_ID )  INNER JOIN completed_courses cc " +
                        "ON sc.Course_ID = cc.Course_ID WHERE sc.Student_ID = ? GROUP BY c.Course_ID;"; 
                                stmt = conn.prepareStatement(queryAccountHistory);
                                stmt.setInt(1, id);
                                ResultSet rs = stmt.executeQuery();
                                while(rs.next()){
                                    System.out.println(rs.getInt("Course_ID" + " | " + rs.getString("Course_Description") + " | " + rs.getString("Course_Name") + " | " + rs.getDouble("Course_Cost") + " | "
                                     + rs.getString("Course_Started_On")));
                                }
                               String total = "SELECT SUM(c.Cost) AS Total_Amount FROM courses c INNER JOIN student_courses sc ON c.Course_ID=sc.Course_ID " +
                                       "WHERE sc.Student_ID = ?";
                               stmt = conn.prepareStatement(total);
                               stmt.setInt(1, id);
                               ResultSet rs1 = stmt.executeQuery();
                               if(rs1.next()){
                                   System.out.println("Total: " + rs1.getDouble("Total_Amount"));
                               }
                               
        }catch(Exception e){
            System.out.println(e);
        }finally{
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Students.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    public static void enrollCourse(int id){
        Random rand = new Random();
        int value = rand.nextInt(500)*1000;
        String facultyCheck = "select f.Faculty_ID from Faculty f where f.Faculty_ID = ?;";
        String adminCheck = "select a.Admin_ID from Admin a where a.Admin_ID = ?;";
        String studentCheck = " select s.Student_ID from Student s inner join completed_Courses cc on s.Student_ID = cc.Student_ID where cc.Student_ID = ? group by s.Student_ID having AVG(cc.Score) > 25 ;";
        //String costQueryMinus = "select Name as course_name, cost from courses where Name = ?";
        try{
            conn = ConnectionManager.getConnection();
        String courseQuery = "SELECT * FROM COURSES";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(courseQuery);
        System.out.println("Course Name     Cost");
        while(rs.next()){
            
            System.out.println(rs.getString("Name") + "              " + rs.getDouble("cost"));
        }
        
        }catch(Exception e){
            
        }finally{
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Students.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("Enter the name of the course you want to enroll");
        Scanner scanner= new Scanner(System.in);
        String cName = scanner.next();
        int Course_ID = 0 ;
        double cost;
        try{
            conn = ConnectionManager.getConnection();
        String course2Query = "SELECT c.Course_ID, c.cost FROM courses c where c.Name = ?";
            stmt = conn.prepareStatement(course2Query);
            stmt.setString(1, cName);
            ResultSet rs1 = stmt.executeQuery();
            stmt = conn.prepareStatement(facultyCheck);
            stmt.setInt(1, id);
            ResultSet rs2 = stmt.executeQuery();
            stmt = conn.prepareStatement(adminCheck);
            stmt.setInt(1, id);
            ResultSet rs3 = stmt.executeQuery();
            stmt = conn.prepareStatement(studentCheck);
            stmt.setInt(1, id);
            ResultSet rs4 = stmt.executeQuery();
            if(rs1.next() && (rs2.next() || rs3.next() || rs4.next())){
                
                //stmt = conn.prepareStatement(costQueryMinus);
                //stmt.setString(1, cName);
                //ResultSet rs5 = stmt.executeQuery();
                System.out.println("You have to awarded a discount of 50% and the new cost is");
                //1DBTablePrinter.printResultSet(rs5);
                double discounted_cost;
                discounted_cost = rs1.getDouble("cost") / 2;
                String queryCourseInsert = "INSERT INTO student_courses(Course_ID,Student_ID,Payment_Confirmation,Date_of_Payment,Enrollment,Amount_per_course) values(?,?,?,now(),1,?)";
                stmt = conn.prepareStatement(queryCourseInsert);
                stmt.setInt(1,rs1.getInt("Course_ID"));
                stmt.setInt(2, id);
                stmt.setInt(3, value);
                stmt.setDouble(4, discounted_cost);
                stmt.execute();
                System.out.println("You have been successfully enrolled please note the transaction id " + value );
            }
            else if(rs1.next()){
                Course_ID = rs1.getInt("Course_ID");
                cost = rs1.getDouble("cost");
                String queryInsert = "INSERT INTO student_courses(Course_ID,Student_ID,Payment_Confirmation,Date_of_Payment,Enrollment,Amount_per_course) values(?,?,?,now(),1,?)";
                stmt = conn.prepareStatement(queryInsert);
                stmt.setInt(1, Course_ID);
                stmt.setInt(2, id);
                stmt.setInt(3, value);
                stmt.setDouble(4, cost);
                stmt.execute();
                System.out.println("You have been successfully enrolled please note the transaction id " + value );
            }else{
                System.out.println("Wrong course name, please enter again");
                enrollCourse(id);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Students.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Students.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static void searchByProfName(int id){
        
        /*System.out.println("Faculties in our website are as follow");
        try{
            conn = ConnectionManager.getConnection();
        String queryFacultyName = "SELECT CONCAT(First_Name,\" \",Last_Name) FROM Student INNER JOIN faculty ON Student.Student_ID = Faculty.Faculty_ID;";
         createStat = conn.createStatement();
         ResultSet rs = createStat.executeQuery(queryFacultyName);
         while(rs.next()){
             System.out.println();
         }
        }catch(Exception e){
            System.out.println(e);
        }finally{
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Students.class.getName()).log(Level.SEVERE, null, ex);
            }
        }*/
     
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type in the professor name"+ " Format: Firstname Lastname");
        
        String facultyName = scanner.nextLine();
        
        String querySearchCourse = "SELECT c.Name as Course_Name, c.Primary_Topic, CONCAT(s.First_Name , \" \",s.Last_Name) as Name FROM "
                + " ((courses c inner join course_creation cc ON c.Course_ID = cc.Course_ID) inner join faculty f on cc.Faculty_ID = f.Faculty_ID)"
                + " inner join student s on f.Faculty_ID = s.Student_ID WHERE concat(s.First_Name, \" \", s.Last_Name) = ?;";
        try{
            conn = ConnectionManager.getConnection();
        stmt = conn.prepareStatement(querySearchCourse);
        stmt.setString(1,facultyName);
        ResultSet rs1 = stmt.executeQuery();
        System.out.println(facultyName);
        DBTablePrinter.printResultSet(rs1);
        }catch(SQLException e){
           System.out.println(e);
        }finally{
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Students.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        scanner.close();
    }
    
    public static void searchCourseByName(int id){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the course name");
        String courseName = scanner.nextLine();
        
        String queryCourse = "SELECT c.Name as course_name, c.Primary_Topic as primary_topic, AVG(cc.Rating) as AVG_Course_Rating FROM "
                + " (courses c inner join secondary_topics sc on c.Course_ID = sc.Course_ID) inner join "+
                "completed_courses cc on sc.Course_ID = cc.Course_ID WHERE Name = ?;";
        
        try{
            //System.out.println(courseName);
            conn  = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(queryCourse);
            stmt.setString(1, courseName);
            ResultSet rs = stmt.executeQuery();
            //while(rs.next()){
              //  System.out.println("aafafa");
            //}
          
          DBTablePrinter.printResultSet(rs);
        }catch(SQLException e){
            System.out.println(e);
        }finally{
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Students.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        scanner.close();
    }
    
    public static void myCourses(int id){
        String subjectOfInterestQuery = "SELECT c.Course_ID AS Course_ID, c.Name AS Course_Name," +
        "c.Primary_Topic as Primary_Topic,sc.Secondary_topic as Secondary_Topics FROM (courses c INNER JOIN subject_of_interest soi ON c.Course_ID = soi.Course_ID) " +
               "INNER JOIN secondary_topics sc ON soi.Course_ID = sc.Course_ID WHERE soi.Student_ID = ? GROUP BY sc.Secondary_topic ORDER BY c.Name;"; 
        String enrolledQuery = "SELECT c.Course_ID, c.Name, c.Primary_Topic, st.Secondary_topic from courses c inner join (SELECT * FROM student_courses sc where sc.Course_ID not in (select cc.Course_ID from completed_courses cc)) q on c.Course_ID = q.Course_ID inner join secondary_topics st on q.Course_ID = st.Course_ID where q.Student_ID = ? GROUP BY c.Course_ID order by c.Name;";
        
        String completedQuery = "select c.Course_ID,c.Name,c.Primary_Topic,AVG(cc.Rating), st.secondary_topic from courses c inner join secondary_topics st on c.course_ID = st.Course_ID inner join completed_courses cc on st.Course_ID = cc.Course_ID where cc.Student_ID = ? group by st.secondary_topic order by AVG(cc.Rating);";
        
        try{
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(subjectOfInterestQuery);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            System.out.println("Course_WishList");
            DBTablePrinter.printResultSet(rs);
            stmt = conn.prepareStatement(enrolledQuery);
            stmt.setInt(1,id);
            rs = stmt.executeQuery();
            System.out.println("Enrolled Course");
            DBTablePrinter.printResultSet(rs);
            stmt = conn.prepareStatement(completedQuery);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            
            System.out.println("Completed Courses");
            DBTablePrinter.printResultSet(rs);
        }catch(SQLException e){
            System.out.println(e);
        }
        finally{
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Students.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static void playList(int id){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Press 1 to create playlist \nPress 2 to edit a playlist \npress 3 toAccess a playlist \npress 4 to delete a playlist");
        int choice = scanner.nextInt();
        switch(choice){
            case 1:
            {
                createNewPlayList(id);
                break;
            }
            case 2:
            {
                editPlayList(id);
                break;
            }
            case 3:
            {
                accessPlayList(id);
                break;
            }
            
            case 4:
            {
                deletePlayList(id);
                break;
            }
            default:
            {
                System.out.println("Incorrect choice, PLEASE try again");
                playList(id);
            }
        }
        scanner.close();
    }
    
    public static void createNewPlayList(int id){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Give a name to your playlist");
        String playListName = scanner.nextLine();
        System.out.println("Here is the list of the course material and their status");
        String playListQuery = "select cm.Name, cm.Course_ID, cm.Material_ID, scm.Status from course_material cm inner join student_course_material scm on cm.Course_ID = scm.Course_ID and cm.Material_ID = scm.Material_ID where scm.Student_ID = ?;";
        try{
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(playListQuery);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            DBTablePrinter.printResultSet(rs);
        }catch(SQLException e){
            System.out.println(e);
        }finally{
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Students.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("Enter the course material name you want to insert");
        String courseMaterial = scanner.nextLine();
       
        String Query = "select cm.Course_ID, cm.Material_ID from course_material cm where cm.Name = ?;"; 
        String insertQuery = "insert into playlist(P_Name,Student_ID) values(? ,?)";
        String insertVideoQuery = "select video from link l where l.Course_ID = ? and l.Material_ID = ?";
        String insertPlayListContent = "insert into playlist_content(P_Name,User_ID,Course_ID,Material_ID,Video) values(?,?,?,?,?)";
        int course_id = 0;
        int material_id = 0;
        int video = 0;
        try{
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(Query);
            stmt.setString(1, courseMaterial);
            ResultSet rs =stmt.executeQuery();
            
            
            while(true){
                if(rs.next()){
                    course_id = rs.getInt("Course_ID");
                    material_id = rs.getInt("Material_ID");
                    stmt = conn.prepareStatement(insertQuery);
                    stmt.setString(1, playListName);
                    stmt.setInt(2, id);
                    stmt.execute();
                }else{
                    break;
                }
            }
            
            stmt = conn.prepareStatement(insertVideoQuery);
            stmt.setInt(1, course_id);
            stmt.setInt(2, material_id);
            rs = stmt.executeQuery();
            if(rs.next()){
                video = rs.getInt("video");
            }
            
            stmt = conn.prepareStatement(insertPlayListContent);
            stmt.setString(1, playListName);
            stmt.setInt(2, id);
            stmt.setInt(3, course_id);
            stmt.setInt(4, material_id);
            stmt.setInt(5, video);
            stmt.execute();
            
            
        }catch(SQLException e){
            System.out.println(e);
        }finally{
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Students.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        scanner.close();
    }
    
    public static void accessPlayList(int id){
        System.out.println("Here is the list of your playList");
        String playListContentName = "SELECT p.P_Name, p.video from playList_Content p where p.User_ID = ? group by video;";
        try{
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(playListContentName);
            stmt.setInt(1,id);
            ResultSet rs = stmt.executeQuery();
            DBTablePrinter.printResultSet(rs);
        }catch(Exception e){
            System.out.println(e);
        }finally{
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Students.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the playlist name");
        String playListName = scanner.nextLine();
        
        String playListContentBasedOnName = "SELECT p.P_Name,p.video from playlist_content p where p.User_ID = ? and P.P_Name = ?";
        try{
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(playListContentBasedOnName);
            stmt.setInt(1, id);
            stmt.setString(2, playListName);
            ResultSet rs = stmt.executeQuery();
            DBTablePrinter.printResultSet(rs);
        }catch(Exception e){
            System.out.println(e);
        }finally{
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Students.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
        scanner.close();
    }
    
    public static void editPlayList(int id){
        String listofCoursespecial = null;
        String playListContentName = "SELECT p.P_Name from playList_Content p where p.User_ID = ? group by p.P_Name;";
        System.out.println("Here is the list of your playlist");
        try{
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(playListContentName);
            stmt.setInt(1,id);
            ResultSet rs = stmt.executeQuery();
            DBTablePrinter.printResultSet(rs);
        }catch(Exception e){
            System.out.println(e);
        }finally{
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Students.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        Scanner scanner = new Scanner(System.in);
        //int choice = scanner.nextInt();
        
        System.out.println("Enter the playlist name you want to add or delete");
        String playListName = scanner.nextLine();
        
        String playListContentBasedOnName = "select pc.P_Name, cm.Name from playlist_Content pc inner join course_material cm on pc.Course_ID = cm.Course_ID and pc.Material_ID = cm.Material_ID where pc.P_Name = ? and pc.User_ID = ? group by cm.Name;";
        try{
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(playListContentBasedOnName);
            stmt.setString(1, playListName);
            stmt.setInt(2, id);
            ResultSet rs = stmt.executeQuery();
            DBTablePrinter.printResultSet(rs);
        }catch(Exception e){
            System.out.println(e);
        }finally{
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Students.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        System.out.println("Press 1 to Add to PlayList \nPress 2 delete from playlist\nPress 3 to Go back");
        String listOfCourseMaterial = "select cm.Name, scm.status from course_material cm inner join student_course_material scm on cm.Course_ID = scm.Course_ID and cm.Material_ID = scm.Material_ID where scm.Student_ID = ?;";
        int choice = scanner.nextInt();
        String materialName = null;
        if(choice == 1){
            System.out.println("Here is list of videos from course material");
            try{
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(listOfCourseMaterial);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            DBTablePrinter.printResultSet(rs);
        }catch(Exception e){
            System.out.println(e);
        }finally{
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Students.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
            System.out.println("Enter the name of the content you want to enter in the present playlist");
            materialName = scanner.next();
            listofCoursespecial = "select l.video, scm.Material_ID, scm.Course_ID from link l inner join course_material cm on l.Course_ID = cm.Course_ID and l.Material_ID = cm.Material_ID inner join student_course_material scm on cm.Material_ID = scm.Material_ID and cm.Course_ID = scm.Course_ID where scm.Student_ID = ? and cm.Name = ?";
            String insertPlayList = "insert into playlist_content values(?,?,?,?,?)";
            int course_ID = 0;
            int material_ID = 0;
            int video = 0;
            try{
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(listofCoursespecial);
            stmt.setInt(1, id);
            stmt.setString(2,materialName);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                course_ID = rs.getInt("Course_ID");
                material_ID = rs.getInt("Material_ID");
                video = rs.getInt("video");
            }
            stmt = conn.prepareStatement(insertPlayList);
            stmt.setString(1,playListName);
            stmt.setInt(2, id);
            stmt.setInt(3, course_ID);
            stmt.setInt(4, material_ID);
            stmt.setInt(5, video);
            
            stmt.execute();
            
        }catch(Exception e){
            System.out.println(e);
        }finally{
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Students.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        }else if(choice == 2){
         /*   listofCoursespecial = "select l.video, scm.Material_ID, scm.Course_ID from link l inner join course_material cm on l.Course_ID = cm.Course_ID and l.Material_ID = cm.Material_ID inner join student_course_material scm on cm.Material_ID = scm.Material_ID and cm.Course_ID = scm.Course_ID where scm.Student_ID = ? and cm.Name = ?";
            System.out.println("Here is list of videos from course material");
            materialName = scanner.next();
            try{
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(listofCoursespecial);
            
            stmt.setInt(1, id);
            stmt.setString(2, materialName);
            ResultSet rs = stmt.executeQuery();
            DBTablePrinter.printResultSet(rs);
        }catch(Exception e){
            System.out.println(e);
        }finally{
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Students.class.getName()).log(Level.SEVERE, null, ex);
            }
        }*/
            System.out.println("Name the content you want to delete");
            
            String course_material = scanner.next();
            
            String query2 = "select cm.Course_ID, cm.Material_ID FROM course_material cm where cm.Name = ?;";
            String delete = "DELETE FROM Playlist_content where P_Name = ? and User_ID = ? and Course_ID = ? and Material_ID = ?";
            int course_ID = 0;
            int material_ID = 0;
            
            
               try{
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(query2);
            stmt.setString(1, course_material);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                course_ID = rs.getInt("Course_ID");
                material_ID = rs.getInt("Material_ID");
                stmt = conn.prepareStatement(delete);
                stmt.setString(1, playListName);
                stmt.setInt(2, id);
                stmt.setInt(3, course_ID);
                stmt.setInt(4, material_ID);
                stmt.execute();
            }
        }catch(Exception e){
            System.out.println(e);
        }finally{
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Students.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
            
        }else{
            editPlayList(id);
        }
        
        scanner.close();
        
    }
    
    public static void deletePlayList(int id){
        String playListContentName = "SELECT p.P_Name from playList_Content p where p.User_ID = ? group by p.P_Name;";
        System.out.println("Here is the list of your playlist");
        try{
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(playListContentName);
            stmt.setInt(1,id);
            ResultSet rs = stmt.executeQuery();
            DBTablePrinter.printResultSet(rs);
        }catch(Exception e){
            System.out.println(e);
        }finally{
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Students.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        Scanner scanner = new Scanner(System.in);
        //int choice = scanner.nextInt();
        
        System.out.println("Enter the playlist name you want to delete");
        String playListName = scanner.nextLine();
        
        String playListContentBasedOnName = "select pc.P_Name, cm.Name from playlist_Content pc inner join course_material cm on pc.Course_ID = cm.Course_ID and pc.Material_ID = cm.Material_ID where pc.P_Name = ? and pc.User_ID = ? group by cm.Name;";
        try{
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(playListContentBasedOnName);
            stmt.setString(1, playListName);
            stmt.setInt(2, id);
            ResultSet rs = stmt.executeQuery();
            DBTablePrinter.printResultSet(rs);
        }catch(Exception e){
            System.out.println(e);
        }finally{
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Students.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       
        String deletePlayList = "DELETE FROM playlist where P_Name = ? and Student_ID = ?";
        String deletePlayListContent = "DELETE FROM playlist_content where P_Name = ? and User_ID = ?";
        try{
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(deletePlayListContent);
            stmt.setString(1, playListName);
            stmt.setInt(2, id);
            stmt.execute();
            stmt = conn.prepareStatement(deletePlayList);
            stmt.setString(1, playListName);
            stmt.setInt(2, id);
            stmt.execute();
        }catch(Exception e){
            System.out.println(e);
        }finally{
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Students.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static void forumQuestions(int id){
        System.out.println("Press 1 for asking question\nPress 2 for viewing questions\nPress 3 to Go Back");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        
        if(choice == 2){
            String questionQuery = "SELECT  Question_ID as Question_No,Student_ID as Question_Part,Question as Question_Asked from question_asked";
            String answerQuery = "SELECT answer,CONCAT(First_Name,\" \",Last_Name) as Prof_Name from Questions_answer qa inner join student s on qa.Faculty_id = s.Student_ID where qa.Question_ID = ? and qa.Student_ID = ? and qa.visibilty = 1;";
            String count = "SELECT COUNT(*) as count FROM Question_Asked";
            int question_id = 0;
            int student_id = 0;
            //int countofrows = 0;
            try{
                conn = ConnectionManager.getConnection();
                createStat = conn.createStatement();
                ResultSet rs = createStat.executeQuery(questionQuery);
                //ResultSet rs2 = createStat.executeQuery(count);
                //countofrows = lrs2.getInt("count");
                while(rs.next()){
                    question_id = rs.getInt("Question_No");
                    student_id = rs.getInt("Question_Part");
                    System.out.println("Question No" + "  " + "Question Part" + " Question");
                    System.out.println(question_id +"              " +student_id +"        " +rs.getString("Question_Asked"));
                    //DBTablePrinter.printResultSet(rs);
                    stmt = conn.prepareStatement(answerQuery);
                    stmt.setInt(1, question_id);
                    stmt.setInt(2,student_id);
                    ResultSet rs1 = stmt.executeQuery();
                    //System.out.println("Answer");
                    DBTablePrinter.printResultSet(rs1);
                    
                }
                        }
            catch(SQLException e){
                System.out.println(e);
            }finally{
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Students.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            System.out.println("Press 1 to like a question\nPress 2 to go back");
            String likeQuery = "INSERT INTO likes_for_question values(?,?,?)";
            int choice2 = scanner.nextInt();
            if(choice2 == 1){
                System.out.println("Enter the question number you want to like");
                int questionNo = scanner.nextInt();
                System.out.println("Enter the question part you want to like");
                int questionPart = scanner.nextInt();
                
                try{
                    conn = ConnectionManager.getConnection();
                    stmt = conn.prepareStatement(likeQuery);
                    stmt.setInt(1, questionNo);
                    stmt.setInt(2, questionPart);
                    stmt.setInt(3, id);
                    stmt.execute();
                }catch(SQLException e){
                    System.out.println(e);
                }finally{
                    try {
                        conn.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Students.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
            }else{
                forumQuestions(id);
            }
         scanner.close();
        }
        else if(choice == 1){
        System.out.println("Please enter the question you want to ask");
       
        Scanner scanner1 = new Scanner(System.in);
        String Question = scanner1.nextLine();
        
        Random rand = new Random();
        int randint= rand.nextInt(500);
        String InsertQuestionAsked = "INSERT INTO Question_Asked values(?,?,?)";
        try{
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(InsertQuestionAsked);
            stmt.setInt(1,randint);
            stmt.setInt(2, id);
            stmt.setString(3,Question);
            stmt.execute();
        }catch(SQLException e){
            System.out.println(e);
        }finally{
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Students.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        scanner1.close();
               
    }else{
            forumQuestions(id);
        }
    }
    
    public static void courseMaterial(int id){
        System.out.println("Press 1 to see the course material\nPress 2 to Go Back");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        if(choice == 1){
            //System.out.println("Enter the course name for which you want to create the course material");
            //String courseName = scanner.next();
            //int Course_ID = 0;
            //String query = "Select c.Course_ID from courses c inner join student_courses sc on c.Course_ID = sc.Course_ID where sc.Student_ID = ? and c.Name = ?";
            String query1 = "Select cm.Name as course_material_name, q.Min_Score as min_score, scm.status as material_status from (course_material cm inner join student_course_material scm on cm.Course_ID = scm.Course_ID and cm.Material_ID = scm.Material_ID) inner join quiz q on scm.Course_ID = q.Course_ID and scm.Material_ID = q.Material_ID where scm.Student_ID = ?";
            String query2 = "Select cm.Name as course_material_name, p.Text as post, scm.status as material_status from (course_material cm inner join student_course_material scm on cm.Course_ID = scm.Course_ID and cm.Material_ID = scm.Material_ID) inner join post p on scm.Course_ID = p.Course_ID and scm.Material_ID = p.Material_ID where scm.Student_ID = ?";
            String query3 = "Select cm.Name as course_material_name, df.Path as path_of_file, df.Size as size_of_File, df.Type as Type_of_file, scm.status as material_status from (course_material cm inner join student_course_material scm on cm.Course_ID = scm.Course_ID and cm.Material_ID = scm.Material_ID) inner join download_file df on scm.Course_ID = df.Course_ID and scm.Material_ID = df.Material_ID where scm.Student_ID = ?";
            String query4 = "Select cm.Name as course_material_name, l.Video as video_File, l.URL as url_of_link, scm.status as material_status from (course_material cm inner join student_course_material scm on cm.Course_ID = scm.Course_ID and cm.Material_ID = scm.Material_ID) inner join link l on scm.Course_ID = l.Course_ID and scm.Material_ID = l.Material_ID where scm.Student_ID = ?";
            try{
                conn = ConnectionManager.getConnection();
               // stmt = conn.prepareStatement(query);
                //stmt.setInt(1, id);
                //stmt.setString(2, courseName);
                //ResultSet rs = stmt.executeQuery();
                //if(rs.next()){
                  //  Course_ID = rs.getInt("Course_ID");
                //}
                stmt = conn.prepareStatement(query1);
                stmt.setInt(1, id);
                ResultSet rs1 = stmt.executeQuery();
                stmt = conn.prepareStatement(query2);
                stmt.setInt(1, id);
                ResultSet rs2 = stmt.executeQuery();
                stmt = conn.prepareStatement(query3);
                stmt.setInt(1, id);
                ResultSet rs3 = stmt.executeQuery();
                stmt = conn.prepareStatement(query4);
                stmt.setInt(1, id);
                ResultSet rs4 = stmt.executeQuery();
                
                DBTablePrinter.printResultSet(rs1);
                DBTablePrinter.printResultSet(rs2);
                DBTablePrinter.printResultSet(rs3);
                DBTablePrinter.printResultSet(rs4);
                    
            }catch(SQLException e){
                System.out.println(e);
            }finally{
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(Students.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            System.out.println("Press 1 to access the course material and 2 to Go back");
            int choice2 = scanner.nextInt();
            if(choice2 == 1){
                System.out.println("Enter the course material name you want to access");
                String courseMaterialName = scanner.next();
                String query5 = "select  scm.Material_ID as material_id, scm.Course_ID as course_id from student_course_material scm  inner join course_material cm on scm.Material_ID = cm.Material_ID and scm.Course_ID = cm.Course_ID where scm.Student_ID = ? and cm.Name = ?;";
                String query6 = "select p.Text as post from post p where p.Course_ID = ? and p.Material_ID = ?";
                String query7 = "select q.Min_Score as min_score from quiz q where q.Course_ID = ? and q.Material_ID = ?";
                String query8 = "select l.URL as video_Url, l.Video as Video from link l where l.Course_ID = ? and l.Material_ID = ?";
                String query9 = "select df.Path as file_path, df.Size as file_Size, df.Type as file_type from download_file df where df.Course_ID = ? and df.Material_ID = ?";
                int course_id1 = 0;
                int material_id1 = 0;
                try{
                    conn = ConnectionManager.getConnection();
                    stmt = conn.prepareStatement(query5);
                    stmt.setInt(1,id);
                    stmt.setString( 2, courseMaterialName);
                    ResultSet rs = stmt.executeQuery();
                    if(rs.next()){
                    course_id1 = rs.getInt("course_id");
                    material_id1 = rs.getInt("material_id");
                    }
                    stmt = conn.prepareStatement(query6);
                    stmt.setInt(1,course_id1);
                    stmt.setInt( 2, material_id1);
                    ResultSet rs1 = stmt.executeQuery();
                    
                    
                        DBTablePrinter.printResultSet(rs1);
                    
                    stmt = conn.prepareStatement(query8);
                    stmt.setInt(1,course_id1);
                    stmt.setInt( 2, material_id1);
                    ResultSet rs2 = stmt.executeQuery();
                    
                    DBTablePrinter.printResultSet(rs2);
                        
                    stmt = conn.prepareStatement(query9);
                    stmt.setInt(1,course_id1);
                    stmt.setInt( 2, material_id1);
                    ResultSet rs3 = stmt.executeQuery();
                    
                    
                        DBTablePrinter.printResultSet(rs3);
                    
                    stmt = conn.prepareStatement(query7);
                    stmt.setInt(1,course_id1);
                    stmt.setInt( 2,material_id1);
                    ResultSet rs4 = stmt.executeQuery();
                    
                    while(rs4.next()){
                    
                        Quiz.startQuiz(id,course_id1,material_id1);
                    }
                    
                        
                    
                            
                    
                }catch(SQLException e){
                    System.out.println(e);
                }finally{
                    try {
                        conn.close();
                    } catch (SQLException ex) {
                        Logger.getLogger(Students.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }else{
                courseMaterial(id);
            }
        }else{
            courseMaterial(id);
        }
        scanner.close();
    }
    
    
    
        }
        
    
    




