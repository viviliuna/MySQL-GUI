// Example using the RowSet Interface
// Displaying the contents of the bikes table  in the bikedb using JdbcRowSet.
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.sql.rowset.JdbcRowSet;
import javax.sql.rowset.RowSetProvider;
//import com.sun.rowset.JdbcRowSetImpl; // Sun’s JdbcRowSet implementation - works only with Java 8 or earlier
import java.util.Properties;
import com.mysql.cj.jdbc.MysqlDataSource;

public class JDBCRowSetTest 
{
   // JDBC driver name and database URL    
   // use the values below if not loading from a properties file
   static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";        
   static final String DATABASE_URL = "jdbc:mysql://localhost:3306/bikedb?useTimezone=true&serverTimezone=UTC";
   static final String USERNAME = "root";
   static final String PASSWORD = "rootMAC1$";
   
   // constructor connects to database, queries database, processes 
   // results and displays results in window
   public JDBCRowSetTest() 
   {
      // connect to database bikes and query database
      try 
      {
    	  //using a properties file  
    	 
  	      Properties properties = new Properties();
  	      FileInputStream filein = null;
  	      MysqlDataSource dataSource = null;
  	      //read properties file
  	      try {
  	    	filein = new FileInputStream("db.properties");
  	    	properties.load(filein);
  	    	dataSource = new MysqlDataSource();
  	    	dataSource.setURL(properties.getProperty("MYSQL_DB_URL"));
  	    	dataSource.setUser(properties.getProperty("MYSQL_DB_USERNAME"));
  	    	dataSource.setPassword(properties.getProperty("MYSQL_DB_PASSWORD"));	 	
  	      } catch (IOException e) {
  	    	  e.printStackTrace();
  	      }
  	     
            // connect to database bikes and query database
     	    // establish connection to database
      	   Connection connection = dataSource.getConnection();
    	  
      	System.out.println("Output from JDBCRowSetTest:   Using a JdbcRowSet connection object example. A connected RowSet. \n");
     	java.util.Date date = new java.util.Date();
     	System.out.println(date);  System.out.println(); 
     	
    	 // Class.forName( JDBC_DRIVER ); // load database driver class

         // specify properties of JdbcRowSet
         //JdbcRowSet jrowSet = new JdbcRowSetImpl();  
    	 JdbcRowSet jrowSet = RowSetProvider.newFactory(). createJdbcRowSet();
         jrowSet.setUrl( DATABASE_URL ); // set database URL
         jrowSet.setUsername( USERNAME ); // set username
         jrowSet.setPassword( PASSWORD ); // set password
    	 
         jrowSet.setCommand( "SELECT bikename, cost, purchased, mileage FROM bikes" ); // set query
         jrowSet.execute(); // execute query

         // process query results
         ResultSetMetaData metaData = jrowSet.getMetaData();
         int numberOfColumns = metaData.getColumnCount();
         //System.out.println( "The bikes Table from the bikedb database: \n" );
        // System.out.println("Uses a JdbcRowSet connection object. \n" );;
         
         System.out.println("Results of the Query: . . . . . . . . . . . . . . . . . . . . . . . . . . . . .\n");
         

         // display rowset header
         for ( int i = 1; i <= numberOfColumns; i++ )
            System.out.printf( "%-20s\t", metaData.getColumnName( i ) );
         System.out.println();
         
         for ( int i = 1; i <= numberOfColumns; i++ )
             System.out.printf( "%-20s\t", "-------------------" );
          System.out.println();
         
         // display each row
         while ( jrowSet.next() ) 
         {
            for ( int i = 1; i <= numberOfColumns; i++ )
               System.out.printf( "%-20s\t", jrowSet.getObject( i ) );
            System.out.println();
         } // end while
      } // end try
      catch ( SQLException sqlException ) 
      {
         sqlException.printStackTrace();
         System.exit( 1 );
      } // end catch
//      catch ( ClassNotFoundException classNotFound ) 
//      {
//        classNotFound.printStackTrace();            
//         System.exit( 1 );
//      } // end catch    
   } // end JDBCRowSetTest constructor
   
   // launch the application
   public static void main( String args[] )
   {
      JDBCRowSetTest window = new JDBCRowSetTest();      
   } // end main
} // end class JDBCRowSetTest
