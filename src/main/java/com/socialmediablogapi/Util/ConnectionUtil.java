package com.socialmediablogapi.Util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {

  private static String url = "jdbc:h2:./h2/db";
  private static String username = "sa";
  private static String password = "sa";

  private static Connection connection = null;

  public static Connection getConnection() {
    if (connection == null) {
      try {
        connection = DriverManager.getConnection(url, username, password);
        resetTestDatabase();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }

    return connection;
  }

  public static void resetTestDatabase() {
    if (connection == null) {
      getConnection();
    } else {
      try {
        FileReader sqlReader = new FileReader("src/main/resources/SocialMedia.sql");
        RunScript.execute(connection, sqlReader);
      } catch (SQLException | FileNotFoundException e) {
        e.printStackTrace();
      }
    }
  }
}