package grapevin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Scanner;

public class SQLConnection  {
    private static String URL;
    private static String USERNAME;
    private static String PASSWORD;
    protected Connection conn;

    public SQLConnection (String url, String username, String password) throws ClassNotFoundException {
        URL = url;
        USERNAME = username;
        PASSWORD = password;
        conn = getConnection();
    }

    public Connection getConnection() throws ClassNotFoundException {
        Connection connection = null;
        try {
            // load and register JDBC driver for MySQL 
            //Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            //System.out.println("Connected to the database!"); // Check
        } catch (SQLException e) {
            System.err.println("Connection error: " + e.getMessage());
        }
        return connection;
    }

    public String closeConection() {
        if (conn != null) {
            try {
                conn.close();
                return "Connection closed";
            } catch (SQLException e) {
                return "Error closing connection: " + e.getMessage();
            }
        }
        return "Error";      
    }

    // Put the wines of the gpt response in the ArrayLists
    public void saveWinesInDB(ArrayList<String> gptWineList) throws SQLException {
        for (String wine : gptWineList) {
            String[] owine = wine.split(",");
            if (owine[1].toLowerCase().contains("white".toLowerCase())){
                putWineInTable("WhiteWines", owine);
            } else if (owine[1].toLowerCase().contains("red".toLowerCase())){
                putWineInTable("RedWines", owine);
            } else if (owine[1].toLowerCase().contains("ros".toLowerCase())){
                putWineInTable("RoseWines", owine);
            } else {
                System.out.println("Unknown Wine Category");
            }
        }
    }

     // Check if wine exists and then put in DB
     public void putWineInTable(String tablename, String[] owine) throws SQLException {
        PreparedStatement a = null;
        boolean yn = valueExists(tablename, owine[0], "w_name");
        if (yn == false){
            a = makeSQLStatementInsert(tablename, owine[0], owine[1], owine[2]);
            a.executeUpdate();
        } else if (yn == true){
            System.out.println("Wine " + "'" + owine[0] + "'" + " allready exists");
        }
    }

    // Check if value exists in DB
    public boolean valueExists(String tableName, String value, String column) throws SQLException {
        String sql = "SELECT COUNT(*) FROM " 
                    + tableName + 
                    " WHERE "
                    + column + 
                    " = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, value);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            int count = resultSet.getInt(1);
            return count > 0;
        }
        return false;
    }

    // Make INSERT PreparedStatement in SQL
    public PreparedStatement makeSQLStatementInsert (String tablename, String wname, 
                            String wcolor, String wflavor) throws SQLException {
        String sql = " insert into " + tablename + "(w_name, w_color, w_flavor)"
            + " values (?, ?, ?)";

        PreparedStatement preparedStmt = conn.prepareStatement(sql);
        preparedStmt.setString (1, wname);
        preparedStmt.setString (2, wcolor);
        preparedStmt.setString (3, wflavor);
        return preparedStmt; 
    }

    // Get the String table from DB table
    public String getStringTable(String userResponse) throws SQLException {
        if (userResponse == "White") {
            return makeSQLStatementSelect("WhiteWines");
        } else if (userResponse == "Red") {
            return makeSQLStatementSelect("RedWines");
        } else if (userResponse == "Rose") {
            return makeSQLStatementSelect("RoseWines");
        } else {
            return null;
        }
    }
        
    // Get a String table using SELECT 
    public String makeSQLStatementSelect(String table) throws SQLException {
        String sql = "select w_name from  " + table;
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        
        ResultSet resultSet = preparedStatement.executeQuery();

        // Process the result set and store values in a StringBuilder
        StringBuilder resultString = new StringBuilder();
        while (resultSet.next()) {
            String columnValue = resultSet.getString("w_name");
            resultString.append(columnValue).append(",");
        }

        // Remove the trailing comma and space
        if (resultString.length() > 0) {
            resultString.setLength(resultString.length() - 2);
        }

        return resultString.toString();
    }

    // Add user answers in table
    public void addUserWines(ArrayList<String> gptWineList, User user) throws SQLException {
        String sql = "insert into QuizAnswers (username, w_name1, w_name2, w_name3, creation_time) values (?, ?, ?, ?, CURRENT_TIMESTAMP)";

        PreparedStatement preparedStmt = conn.prepareStatement(sql);
        preparedStmt.setString (1, user.getUsername());
        preparedStmt.setString (2, gptWineList.get(0));
        preparedStmt.setString (3, gptWineList.get(1));
        preparedStmt.setString (4, gptWineList.get(2));

        preparedStmt.executeUpdate();
    }

    public void saveUser(User user) throws SQLException {
        String sql = "insert into users (username, user_password)values (?, ?)";

        PreparedStatement preparedStmt = conn.prepareStatement(sql);
        preparedStmt.setString (1, user.getUsername());
        preparedStmt.setString (2, user.getPassword());
        preparedStmt.executeUpdate();
    }

    public User findByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        User user = new User();
        while (resultSet.next()) {
            String uname = resultSet.getString("username");
            String pswrd = resultSet.getString("user_password");
            user.setUsername(uname);
            user.setPassword(pswrd);
        }
        return user;
    }

    public String findPassword(String password) throws SQLException {
        String sql ="SELECT user_password FROM users WHERE user_password = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, password);
        ResultSet resultSet = preparedStatement.executeQuery();
        String user_password = resultSet.toString();
        return user_password;
        
    }

    public void register(User user, Scanner sc) throws SQLException {

        while (true) {
            System.out.println("Enter username (or type 'x' to cancel): ");
            String username = sc.nextLine();
            
            if (username.equalsIgnoreCase("x")) {
                System.out.println("Cancelled registration");
                System.exit(0);
            }
            
            user.setUsername(username);

            try {
                    System.out.println("Enter password: ");
                    String password = sc.nextLine();
                    user.setPassword(password);
                    saveUser(user);
                    System.out.println("Registration successful");
                    break;
            } catch (SQLIntegrityConstraintViolationException e) {
                 // Username exists, prompt user to try again
                 System.out.println("Username exists, please try again");
            }
        }
    }

    public void login(User user, Scanner sc) throws SQLException {

        while (true) {
            System.out.println("Enter username (or type 'x' to cancel): ");
            String username = sc.nextLine();
            
            if (username.equalsIgnoreCase("x")) {
                System.out.println("Cancelled login");
                System.exit(0);
            }
            
            System.out.println("Enter password: ");
            String password = sc.nextLine();
            
            user.setUsername(username);
            user.setPassword(password);
            
            User foundUser = findByUsername(user.getUsername());
            
            try {
                if (foundUser != null && foundUser.getPassword().equals(user.getPassword())) {
                    // Username exists and password matches, login successful
                    System.out.println("Login successful :)");
                    break;
                }
            } catch (NullPointerException e) {
                // Username doesn't exist or password doesn't match, prompt user to try again
                System.out.println("Wrong username or password, please try again");
            }
        }
    }
}
