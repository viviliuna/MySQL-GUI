// Very basic JDBC example showing loading of JDBC driver, establishing connection
// creating a statement, executing a simple SQL query, and displaying the results.
// uses an explicit loading of the driver
import java.sql.*;

public class SimpleJDBC_StaticLoad {
	
  public static void main(String[] args)  throws SQLException, ClassNotFoundException {
	    //dump out some heading details
		System.out.println("Output from SimpleJDBC_StaticLoad:  Using an explicit driver load.");
		java.util.Date date = new java.util.Date();
		System.out.println(date);  System.out.println();
		
	    // Load the JDBC driver
	    Class.forName("com.mysql.cj.jdbc.Driver");
	    System.out.println("Driver loaded");
	    
	    // Create a connection object and establish a connection to the database - using all parameters
	    // Note: some older MacOS versions will require the useTimeZone and serverTimezone parameters.  These are not required on Big Sur or later.
	    //Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bikedb?useTimezone=true&serverTimezone=UTC", "root", "rootMAC1$");
	    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bikedb", "root", "rootMAC1$");
	    System.out.println("Database connected");
		//print meta data from connection details
		DatabaseMetaData dbMetaData = connection.getMetaData();
		System.out.println("JDBC Driver name " + dbMetaData.getDriverName() );
		System.out.println("JDBC Driver version " + dbMetaData.getDriverVersion());
		System.out.println("Driver Major version " +dbMetaData.getDriverMajorVersion());
		System.out.println("Driver Minor version " +dbMetaData.getDriverMinorVersion() );
		System.out.println();
	
	    // Create a statement object
	    Statement statement = connection.createStatement();
	
	    // Execute a query using the statement
	    ResultSet resultSet = statement.executeQuery ("select bikename,cost,mileage from bikes");
	
	    // Iterate through the result set that was returned and print the results
	    System.out.println("Results of the Query: . . . . . . . . . . . . . . . . . . . . . . . . . . . . .\n");
	    while (resultSet.next())
	      System.out.println(resultSet.getString("bikename") + "         \t" +
	           resultSet.getString("cost") + "         \t" + resultSet.getString("mileage"));
		  //the following print statement works exactly the same  
	      //System.out.println(resultSet.getString(1) + "         \t" +
	      //  resultSet.getString(2) + "         \t" + resultSet.getString(3));
	    System.out.println();
	    System.out.println();
	    // Close the connection object
	    connection.close();
  }
}
