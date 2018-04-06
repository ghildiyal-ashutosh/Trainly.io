/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author rishw
 */

import java.util.*;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
public class Quiz {
    private static Connection conn;
    private static PreparedStatement stmt;
    public static void startQuiz(int id, int course_id, int material_id){
        Scanner scanner =  new Scanner(System.in);
        System.out.println("Press 1 to start the quiz\nPress 2 to Go Back");
        int choice = scanner.nextInt();
        if(choice == 1){
        int rating = 0;
        String query = "SELECT q.SNo as serial_no,q.Correct as correct_option,q.Text,q.Feedback from Question q where q.Course_ID = ? and q.Material_ID = ?;";
        String queryAnswer = "SELECT a.answer_sno,a.answers from answers a where a.SNo = ? and a.Course_ID = ?  and a.Material_ID = ?;";
        String updateStudentCourseMaterial = "update student_course_material set status = 1 where Student_ID = ? and Material_ID = ? and Course_ID = ?;";
        String updateStudentCourses = "update student_courses set enrollment = 1 where Course_ID = ? and Student_ID = ?";
        String updateCompletedCourses = "insert into completed_courses values(?,?,?,?,?,?,now())";
        System.out.println("Please enter the correct answer no after each question");
        int question_no = 0;
        int question_correct = 0;
        String Feedback = null;
        int score = 0;
        try{
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, course_id);
            stmt.setInt(2, material_id);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                question_no = rs.getInt("serial_no");
                question_correct = rs.getInt("correct_option");
                Feedback = rs.getString("Feedback");
                System.out.println(question_no + ". "+ rs.getString("Text"));
                stmt = conn.prepareStatement(queryAnswer);
                stmt.setInt(1,question_no);
                stmt.setInt(2, course_id);
                stmt.setInt(3, material_id);
                ResultSet rs1 = stmt.executeQuery();
                while(rs1.next()){
                    System.out.println(rs1.getInt("answer_sno")+ ". "+ rs1.getString("answers"));
                }
                
                int userAnswer = scanner.nextInt();
                if(userAnswer == question_correct){
                    score += 15;
                }else{
                    System.out.println(Feedback);
                }
                
                
            }
            
            if(score >= 30){
                System.out.println("You have the passed the course");
                System.out.println("Please enter the comment");
                scanner.nextLine();
                String comment = scanner.nextLine();
                                
                System.out.println("Please give a rating for this course on the scale of 1 to 5");
                rating = scanner.nextInt();
                if(rating < 0){
                    rating = 0;
                }else if(rating > 5){
                    rating = 5;
                }else{
                    rating = rating;
                }
                
                try{
                    
                    stmt = conn.prepareStatement(updateStudentCourseMaterial);
                    stmt.setInt(1, id);
                    stmt.setInt(2, material_id);
                    stmt.setInt(3, course_id);
                    stmt.execute();
                    stmt = conn.prepareStatement(updateStudentCourses);
                    stmt.setInt(1, course_id);
                    stmt.setInt(2, id);
                    stmt.execute();
                    stmt = conn.prepareStatement(updateCompletedCourses);
                    stmt.setInt(1, course_id);
                    stmt.setInt(2, id);
                    stmt.setInt(3,rating);
                    stmt.setString(4, comment);
                    stmt.setInt(5,score);
                    stmt.setString(6,"You have completed the course successfully here is the course");
                    stmt.execute();
                    
                }catch(SQLException e){
                    System.out.println(e);
                }
                
                startQuiz(id,course_id,material_id);
            }else{
                startQuiz(id,course_id,material_id);
            }
            
        }catch(SQLException e){
                    System.out.println(e);
                    }finally{
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Quiz.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        }else{
            Students.courseMaterial(id);
        }
        scanner.close();
    }
}
