package org.example;


import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public interface DataAccessLayer {
    public default void insertRow(Boolean recent,Boolean voluntary,String word, String meaning, byte[] imageByte) {

    }

    public default void deleteRow(String word) {

    }

    public default String findSemantic(String word){
       return null;
    }

    public default void getList(String object) {

    }
    public default Connection createConnection(String url, String username, String password) {
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
        }

        return connection;
    }

}
