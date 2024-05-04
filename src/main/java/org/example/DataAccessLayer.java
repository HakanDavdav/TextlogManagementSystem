package org.example;


import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public interface DataAccessLayer {
    public default void insertRow(Boolean dialog,Boolean voluntary ,String word, String meaning, byte[] imageByte) {
        User.connection = createConnection(User.personalListUrl, User.username, User.password);
        if(!dialog&&voluntary) {
            try {
                Statement statement = User.connection.createStatement();
                statement.executeUpdate(
                        "INSERT INTO wordtable (word,meaning,imagebyte) VALUES ('" + word.trim() + "', '" + meaning + "', '" + imageByte + "')");
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        else if(!dialog){
            try {

                Statement statement = User.connection.createStatement();
                CallableStatement callableStatement = User.connection.prepareCall("{call remover()}");
                statement.executeUpdate(
                        "INSERT INTO recentwordtable (word,meaning,imageByte) VALUES ('" + word.trim() + "', '" + meaning + "', '" + imageByte + "')");
                callableStatement.executeUpdate();

                statement.close();
                callableStatement.close();
                callableStatement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else if(dialog){
            try {

                Statement statement = User.continiousconnection.createStatement();
                CallableStatement callableStatement = User.continiousconnection.prepareCall("{call textlogremover()}");
                statement.executeUpdate(
                        "INSERT INTO textlog (dialog) VALUES ('" + word + "')");
                callableStatement.executeUpdate();

                statement.close();
                callableStatement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }

    }

    public default void deleteRow(String word) {
        User.connection = createConnection(User.personalListUrl,User.username,User.password);
        try {
            Statement statement = User.connection.createStatement();
            statement.executeUpdate("delete from wordtable where word = " + "'"+word+"'" );
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }



    public default String findSemantic(String word){
       return null;
    }



    public static Connection createConnection(String url, String username, String password) {
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
        }

        return connection;
    }

}
