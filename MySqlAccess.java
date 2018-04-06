package mySql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.spi.DirStateFactory.Result;

public class MySqlAccess {
	   private Connection connect = null;
	    private Statement statement = null;
	    private PreparedStatement preparedStatement = null;
	    private ResultSet resultSet = null;

	    public void readDataBase() throws Exception {
	        try {
	            // This will load the MySQL driver, each DB has its own driver
	            Class.forName("com.mysql.jdbc.Driver");
	            // Setup the connection with the DB
	            connect = DriverManager
	                    .getConnection("jdbc:mysql://localhost/test?"
	                            + "user= root password= password");

	            // Statements allow to issue SQL queries to the database
	            statement = connect.createStatement();
	            // Result set get the result of the SQL query
	            resultSet = statement
	                    .executeQuery("select * from test.student");
	            writeResultSet(resultSet);
	            /*String query = " insert into student (Student_ID,`First Name`,`Last Name`, University)"
	                    + " values ( ?, ?, ?, ?)";

	            // PreparedStatements can use variables and are more efficient
	            preparedStatement = connect
	                    .prepareStatement(query);
	            // "myuser, webpage, datum, summary, COMMENTS from feedback.comments");
	            // Parameters start with 1
	            preparedStatement.setInt(1, 17);
	            preparedStatement.setString(2, "Animeshuuu");
	            preparedStatement.setString(3, "Maneeeeeeee");
	            preparedStatement.setString(4, "Pune University");
	            preparedStatement.executeLargeUpdate();
	            preparedStatement = connect
	                    .prepareStatement("SELECT Student_ID, `First Name`, `Last Name`, University from test.student");
	            resultSet = preparedStatement.executeQuery();
	            writeResultSet(resultSet);
	            resultSet = statement
	            .executeQuery("select * from student");
	            writeMetaData(resultSet);*/
	            String query = "Select student1.University from test.student1 inner join test.student on student1.id = student.Student_ID ";
	            resultSet =statement.executeQuery(query);
	            while ( resultSet.next() ) {
	                String University = resultSet.getString("University");
	                
	                System.out.println( University );
	            }
	            
	        } catch (Exception e) {
	            throw e;
	        } finally {
	            close();
	        }

	    }

	    private void writeMetaData(ResultSet resultSet) throws SQLException {
	        //  Now get some metadata from the database
	        // Result set get the result of the SQL query

	        System.out.println("The columns in the table are: ");

	        System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
	        for  (int i = 1; i<= resultSet.getMetaData().getColumnCount(); i++){
	            System.out.println("Column " +i  + " "+ resultSet.getMetaData().getColumnName(i));
	        }
	    }

	    private void writeResultSet(ResultSet resultSet) throws SQLException {
	        // ResultSet is initially before the first data set
	        while (resultSet.next()) {
	            // It is possible to get the columns via name
	            // also possible to get the columns via the column number
	            // which starts at 1
	            // e.g. resultSet.getSTring(2);
	            Integer Student_ID = resultSet.getInt("STUDENT_ID");
	            String FirstName = resultSet.getString("First Name");
	            String LastName = resultSet.getString("Last Name");
	            String University = resultSet.getString("University");
	            System.out.println("Student_ID: " + Student_ID);
	            System.out.println("FirstName: " + FirstName);
	            System.out.println("LastName: " + LastName);
	            }
	    }

	    // You need to close the resultSet
	    private void close() {
	        try {
	            if (resultSet != null) {
	                resultSet.close();
	            }

	            if (statement != null) {
	                statement.close();
	            }

	            if (connect != null) {
	                connect.close();
	            }
	        } catch (Exception e) {

	        }
	    }
}
