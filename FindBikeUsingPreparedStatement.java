//// This example uses the PreparedStatement Interface
//
//import javax.swing.*;
//import java.sql.*;
//import java.awt.*;
//import java.awt.event.*;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.util.Properties;
//import com.mysql.cj.jdbc.MysqlDataSource;
//
//public class FindBikeUsingPreparedStatement {
//  boolean isStandalone = false;
//  private JTextField jtfbike = new JTextField(25);
//  private JTextField jtfcost = new JTextField(6);
//  private JButton jbtShowCost = new JButton("Show Bike Cost Info");
//
//  // PreparedStatement - A Statement is an interface that represents an SQL statement.  You execute Statement objects and they
//  // generate either ResultSet objects (for queries) or integer values (for updating commands).  The PreparedStatement interface
//  // extends the Statement interface and is used for pre-compiling SQL statements that may contain input parameters.
//  private PreparedStatement pstmt;
//
//  /** Initialize the app */
//  public void init() {
//    // Initialize database connection and create a PreparedStatement object
//    initializeDB();
//
//    jbtShowCost.addActionListener(new java.awt.event.ActionListener() {
//          public void actionPerformed(ActionEvent e) {
//                 jbtShowCost_actionPerformed(e);
//          }
//    });
//
//    JPanel jPanel1 = new JPanel();
//    jPanel1.add(new JLabel("Bike Name"));
//    jPanel1.add(jtfbike);
//    jPanel1.add(jbtShowCost);
//
//    jbtShowCost.setBackground(Color.BLACK);
//    jbtShowCost.setForeground(Color.RED);
//
//  }
//
//  private void initializeDB() {
//	    //using a properties file
//	    Properties properties = new Properties();
//	    FileInputStream filein = null;
//	    MysqlDataSource dataSource = null;
//	    //read properties file
//	    try {
//	    	filein = new FileInputStream("db.properties");
//	    	properties.load(filein);
//	    	dataSource = new MysqlDataSource();
//	    	dataSource.setURL(properties.getProperty("MYSQL_DB_URL"));
//	    	dataSource.setUser(properties.getProperty("MYSQL_DB_USERNAME"));
//	    	dataSource.setPassword(properties.getProperty("MYSQL_DB_PASSWORD"));
//
//	    	// connect to database bikes and query database
//	      	// establish connection to database
//	       	Connection connection = dataSource.getConnection();
//	       	System.out.println("Database connected");
//
//            String queryString = "select cost from bikes where bikename = ?";
//
//            // Create a statement
//            pstmt = connection.prepareStatement(queryString);
//	    }  // end try
//	      catch ( SQLException sqlException )
//	      {
//	         sqlException.printStackTrace();
//	         System.exit( 1 );
//	      } // end catch
//	      catch (IOException e) {
//	   	     e.printStackTrace();
//	       }
//  } //end initializeDB
//
//  private void jbtShowCost_actionPerformed(ActionEvent e) {
//    String bikename = jtfbike.getText();
//    String cost = jtfcost.getText();
//    try {
//      pstmt.setString(1, bikename);
//      ResultSet rset = pstmt.executeQuery();
//
//      if (rset.next()) {
//        String price = rset.getString(1);
//
//        // Display result in a dialog box
//        JOptionPane.showMessageDialog(null, bikename + " cost $" + price);
//      }
//      else {
//        // Display result in a dialog box
//        JOptionPane.showMessageDialog(null, "Bike Not Found");
//      }
//    }
//    catch (SQLException ex) {
//      ex.printStackTrace();
//    }
//  }
//
//  /** Main method */
//  public static void main(String[] args) {
//    FindBikeUsingPreparedStatementOLD app = new FindBikeUsingPreparedStatementOLD();
//    JFrame frame = new JFrame();
//    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    frame.setTitle("Find Bike Cost");
//    frame.getContentPane().add(app, BorderLayout.CENTER);
//    app.init();
//    //app.start();
//    frame.setSize(580, 80);
//    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
//    frame.setLocation((d.width - frame.getSize().width) / 2,
//      (d.height - frame.getSize().height) / 2);
//    frame.setVisible(true);
//  }
//}
//
