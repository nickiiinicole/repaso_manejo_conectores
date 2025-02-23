
package ejercicios;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBC {

  public static Connection connection;

  /**
   * 
   * @param bd
   * @param server
   * @param user
   * @param password
   */
  public static void openConnection(String bd, String server, String user, String password) {

    try {
      Class.forName("org.mysql.jdbc.Driver");
      String url = String.format("jbdc:mysql://%s:3306/%s", server, bd);
      connection = DriverManager.getConnection(url, user, password);
      if (connection != null) {
        System.out.println("conected to" + bd);
      } else {
        System.out.println("Can't connected to" + bd);
      }
    } catch (SQLException | ClassNotFoundException e) {
      e.printStackTrace();
    }

  }

}
