/*
Name: Viviana Luna
Course: CNT 4714 Fall 2024
Assignment title: Project 3 – A Two-tier Client-Server Application
Date: October 20, 2024
Class: DisplayQueryResults (GUI)
*/

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.*;
import java.awt.*;
import java.util.Properties;
import java.io.IOException;
import java.io.FileNotFoundException;
import javax.swing.table.DefaultTableModel;
import java.sql.Statement;




public class DisplayQueryResults extends JFrame 
{
   // default query retrieves all data from bikes table
   static final String DEFAULT_QUERY = "SELECT * FROM bikes";
   
   private ResultSetTableModel tableModel;
   private JTextArea queryArea;

   private  Connection connection;
   private JLabel connectionStatusLabel;


   // create ResultSetTableModel and GUI
   public DisplayQueryResults() 
   {   
      super( "SQL CLIENT APPLICATION" );

      // create ResultSetTableModel and display database table
      try
      {
         // create TableModel for results of query SELECT * FROM bikes
         tableModel = new ResultSetTableModel( DEFAULT_QUERY );

         // set up JTextArea in which user types queries
//			queryArea = new JTextArea( 3, 100);
         queryArea = new JTextArea( DEFAULT_QUERY, 3, 100 );
         queryArea.setWrapStyleWord( true );
         queryArea.setLineWrap( true );
         
         JScrollPane scrollPane = new JScrollPane( queryArea,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
         
         // set up JButton for submitting queries
         JButton submitButton = new JButton( "Submit Query" );
         submitButton.setBackground(Color.PINK);
         submitButton.setForeground(Color.MAGENTA);
         submitButton.setBorderPainted(false);
         submitButton.setOpaque(true);

         // create Box to manage placement of queryArea and 
         // submitButton in GUI
         Box box = Box.createHorizontalBox();
         box.add( scrollPane );
         //box.add( submitButton );

         // create JTable delegate for tableModel 
         JTable resultTable = new JTable( tableModel );
         resultTable.setGridColor(Color.BLACK);

         // This is to adjust the size of the result panel
//         JScrollPane resultScrollPane = new JScrollPane(resultTable);
//
//         resultScrollPane.setPreferredSize(new Dimension(800, 300));
//         resultTable.setRowHeight(25);
//         add(resultScrollPane, BorderLayout.CENTER);

         // Connection section
         JPanel connectionDetails = new JPanel();
         connectionDetails.setLayout(new BoxLayout(connectionDetails, BoxLayout.Y_AXIS));

         // Dropdown lists (prop file)
         String[] propertyFiles = {"project3.properties", "bikedb.properties", "newdb.properties", "modeldb.properites"};
         JComboBox<String> propertyFileDropdown = new JComboBox<>(propertyFiles);
         connectionDetails.add(new JLabel("Select Property File:"));
         connectionDetails.add(propertyFileDropdown);

         // Dropdown for user props
         String[] userProperties = {"root.properties", "client1.properties", "client2.properties"};
         JComboBox<String> userPropertyDropdown = new JComboBox<>(userProperties);
         connectionDetails.add(new JLabel("Select User:"));
         connectionDetails.add(userPropertyDropdown);

         connectionStatusLabel = new JLabel("NO CONNECTION ESTABLISHED");
         connectionStatusLabel.setForeground(Color.RED);
         connectionStatusLabel.setHorizontalAlignment(SwingConstants.CENTER);

         JPanel bottomPanel = new JPanel();
         bottomPanel.setLayout(new BorderLayout());

         // Username and pass text fields
         JTextField usernameField = new JTextField(15);
         JPasswordField passwordField = new JPasswordField(15);
         connectionDetails.add(new JLabel("Username:"));
         connectionDetails.add(usernameField);
         connectionDetails.add(new JLabel("Password:"));
         connectionDetails.add(passwordField);

         // Buttons for database connection
         JButton connectButton = new JButton("Connect to Database");
         connectButton.setBackground(Color.green);
         connectButton.setForeground(Color.white);
         JButton disconnectButton = new JButton("Disconnect from Database");
         disconnectButton.setBackground(Color.red);
         disconnectButton.setForeground(Color.white);

         // Buttons for SQL command
         JButton clearCommand = new JButton("Clear SQL Command");
         clearCommand.setBackground(Color.cyan);
         clearCommand.setForeground(Color.white);
         JButton executeCommand = new JButton("Execute SQL Command");
         executeCommand.setBackground(Color.blue);
         executeCommand.setForeground(Color.white);

//         JPanel bottomPanel = new JPanel();
//         bottomPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));

         // Clear results window button
         JButton clearResultsButton = new JButton("Clear Results");
         clearResultsButton.setBackground(Color.orange);
         clearResultsButton.setForeground(Color.white);

         // Close button
         JButton closeButton = new JButton("Close Application");
         closeButton.setBackground(Color.red);
         closeButton.setForeground(Color.white);

         add(connectionStatusLabel, BorderLayout.NORTH);


         // Action listeners for database connection buttons
         connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               // Connection to database butt
               String currentPropFile = (String) propertyFileDropdown.getSelectedItem();

               // Userand pass
               String username = usernameField.getText();
//               String password = passwordField.getText();
               String password = new String(passwordField.getPassword());

               Properties properties = new Properties();
               try(FileInputStream inFile = new FileInputStream(currentPropFile)){
                  properties.load(inFile);

                  String db = properties.getProperty("MYSQL_DB_URL");

                  connection = DriverManager.getConnection(db, username, password);

                  JOptionPane.showMessageDialog(null, "Connection to database successful", "Connection Success", JOptionPane.INFORMATION_MESSAGE);

                  connectionStatusLabel.setText("CONNECTED TO [" + db + "]");
                  connectionStatusLabel.setForeground(Color.GREEN);

                  // NEW CHANGE
                  // Create new ResultSetTableModel with the new connection
                  tableModel = new ResultSetTableModel(connection, queryArea.getText());

                  // Refresh the table with the new data
                  resultTable.setModel(tableModel);
               }
               catch (SQLException sqlException){
                  JOptionPane.showMessageDialog(null, "Failed to connect to the database: " + sqlException.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
               } catch (IOException ioException) {
                  JOptionPane.showMessageDialog(null, "Failed to load the properties file: " + ioException.getMessage(), "File Error", JOptionPane.ERROR_MESSAGE);
               }

            }
         });

         disconnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               // disconnection from database butt
               if (connection != null) {
                  try {
                     connection.close();
                     tableModel.disconnectFromDatabase();
                     JOptionPane.showMessageDialog(null, "Disconnected from the database", "Disconnection Success", JOptionPane.INFORMATION_MESSAGE);
                     connection = null; // Reset the connection object
                     connectionStatusLabel.setText("NO CONNECTION ESTABLISHED");
                     connectionStatusLabel.setForeground(Color.RED);

                  } catch (SQLException sqlException) {
                     JOptionPane.showMessageDialog(null, "Failed to disconnect from the database: " + sqlException.getMessage(), "Disconnection Error", JOptionPane.ERROR_MESSAGE);
                  }
               } else {
                  JOptionPane.showMessageDialog(null, "No active database connection to disconnect", "No Connection", JOptionPane.WARNING_MESSAGE);
               }

            }
         });

         // Action listeners for SQL command buttons
         clearCommand.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               // Clear command
               queryArea.setText("");
            }
         });

         executeCommand.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               // Execute the command
               String sqlQuery = queryArea.getText();
//               String username = usernameField.getText().trim();
               String selectedPropertyFile = (String) propertyFileDropdown.getSelectedItem();

               if (selectedPropertyFile.equals("client1.properties") &&
                       (sqlQuery.trim().toUpperCase().startsWith("DELETE") ||
                               sqlQuery.trim().toUpperCase().startsWith("INSERT") ||
                               sqlQuery.trim().toUpperCase().startsWith("UPDATE"))) {
                  JOptionPane.showMessageDialog(null,
                          "Permission Denied: You are not allowed to execute this type of command.",
                          "Permission Error", JOptionPane.ERROR_MESSAGE);
                  return;  // Do not execute the query
               }
               if (selectedPropertyFile.equals("client2.properties") &&
                       (sqlQuery.trim().toUpperCase().startsWith("DELETE") ||
                               sqlQuery.trim().toUpperCase().startsWith("INSERT"))) {
                  JOptionPane.showMessageDialog(null,
                          "Permission Denied: You are not allowed to execute DELETE or INSERT commands.",
                          "Permission Error", JOptionPane.ERROR_MESSAGE);
                  return;  // Do not execute the query
               }
               try {
                  // Pass the query to the table model to execute
                  //tableModel.setQuery(sqlQuery);
                  if (sqlQuery.toUpperCase().startsWith("SELECT")) {
                     tableModel.setQuery(sqlQuery);  // Execute SELECT query and display results
                  } else {
                     // For UPDATE, DELETE, or INSERT, use executeUpdate, which does not return a ResultSet
                     try (Statement statement = connection.createStatement()) {
                        int rowsAffected = statement.executeUpdate(sqlQuery);  // Execute update query

                        JOptionPane.showMessageDialog(null,
                                "Query executed successfully. Rows affected: " + rowsAffected,
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                     }
                  }
               } catch (SQLException sqlException) {
                  JOptionPane.showMessageDialog(null,
                          "Error executing query: " + sqlException.getMessage(),
                          "Query Execution Error", JOptionPane.ERROR_MESSAGE);

                  // Optionally reset to default query
                  try {
                     tableModel.setQuery(DEFAULT_QUERY);
                     queryArea.setText(DEFAULT_QUERY);
                  } catch (SQLException ex) {
                     JOptionPane.showMessageDialog(null,
                             "Error executing default query: " + ex.getMessage(),
                             "Query Execution Error", JOptionPane.ERROR_MESSAGE);
                  }
               }
            }
         });

         // Action listener for Clear Results button
         clearResultsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               // Clear the JTable and the query area
               resultTable.setModel(new DefaultTableModel());
               queryArea.setText("");
            }
         });

         // Action listener for Close Application button
         closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               // Safely close the application and ensure database disconnection
               if (connection != null) {
                  try {
                     connection.close();
                     tableModel.disconnectFromDatabase();
                  } catch (SQLException sqlException) {
                     JOptionPane.showMessageDialog(null, "Error closing database connection: " + sqlException.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                  }
               }
               System.exit(0);
            }
         });

         // CHanging the layout of the buttons
         // Note for me: this didnt do anything lmfao
//         setLayout(new GridLayout(2,1));
//         add(clearCommand);

         connectionDetails.add(connectButton);
         connectionDetails.add(disconnectButton);
         connectionDetails.add(clearCommand);
         connectionDetails.add(executeCommand);
         connectionDetails.add(clearResultsButton);
         connectionDetails.add(closeButton);

         bottomPanel.add(clearResultsButton);
         bottomPanel.add(closeButton);

         add(bottomPanel, BorderLayout.SOUTH);

         JPanel buttonPanel = new JPanel();
         buttonPanel.add(clearResultsButton);
         buttonPanel.add(closeButton);
         bottomPanel.add(buttonPanel, BorderLayout.CENTER);
         bottomPanel.add(connectionStatusLabel, BorderLayout.SOUTH);
         add(bottomPanel, BorderLayout.SOUTH);


         // Moving all of the connection details up top left
         Box upBox = Box.createVerticalBox();
         upBox.add(connectionDetails);
         box.add(upBox);
         
         // place GUI components on content pane
         add( box, BorderLayout.NORTH );
         add( new JScrollPane( resultTable ), BorderLayout.CENTER );

         // create event listener for submitButton
         submitButton.addActionListener( 
         
            new ActionListener() 
            {
               // pass query to table model
               public void actionPerformed( ActionEvent event )
               {
                  // perform a new query
                  try 
                  {
                     tableModel.setQuery( queryArea.getText() );
                  } // end try
                  catch ( SQLException sqlException ) 
                  {
                     JOptionPane.showMessageDialog( null, 
                        sqlException.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE );
                     
                     // try to recover from invalid user query 
                     // by executing default query
                     try 
                     {
                        tableModel.setQuery( DEFAULT_QUERY );
                        queryArea.setText( DEFAULT_QUERY );
                     } // end try
                     catch ( SQLException sqlException2 ) 
                     {
                        JOptionPane.showMessageDialog( null, 
                           sqlException2.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE );
         
                        // ensure database connection is closed
                        //tableModel.disconnectFromDatabase();
         
                        System.exit( 1 ); // terminate application
                     } // end inner catch                   
                  } // end outer catch
               } // end actionPerformed
            }  // end ActionListener inner class          
         ); // end call to addActionListener

         setSize( 1000, 800 ); // set window size - formerly 600x300
         setVisible( true ); // display window  
      } // end try
      catch ( ClassNotFoundException classNotFound )
      {
         JOptionPane.showMessageDialog( null,
            "MySQL driver not found", "Driver not found",
            JOptionPane.ERROR_MESSAGE );

         System.exit( 1 ); // terminate application
      } // end catch
      catch ( SQLException sqlException )
      {
         JOptionPane.showMessageDialog( null, sqlException.getMessage(), "Database error", JOptionPane.ERROR_MESSAGE );

         // ensure database connection is closed
         tableModel.disconnectFromDatabase();

         System.exit( 1 );
      } // end catch
      
      // dispose of window when user quits application (this overrides
      // the default of HIDE_ON_CLOSE)
      setDefaultCloseOperation( DISPOSE_ON_CLOSE );
      
      // ensure database connection is closed when user quits application
      addWindowListener(new WindowAdapter() 
         {
            // disconnect from database and exit when window has closed
            public void windowClosed( WindowEvent event )
            {
               //tableModel.disconnectFromDatabase();
               System.exit( 0 );
            } // end method windowClosed
         } // end WindowAdapter inner class
      ); // end call to addWindowListener
   } // end DisplayQueryResults constructor
   
   // execute application
   public static void main( String args[] ) 
   {
      new DisplayQueryResults();     
   } // end main
} // end class DisplayQueryResults




