package org.example;

import com.tulskiy.keymaster.common.Provider;

import javax.swing.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class User implements DataAccessLayer {
    static public Connection connection;
    static public Connection continiousconnection;
    static public String username = "root";
    static public String password = "1234";
    static public String personalListUrl = "jdbc:mysql://localhost:3306/words";
    static public String dictionaryUrl = "jdbc:mysql://localhost:3306/wn_pro_mysql";



    User() {

        connection = createConnection(personalListUrl,username,password);
        ScreenCapture screenCapture = new ScreenCapture(false);
        Provider provider = Provider.getCurrentProvider(false);
        provider.register(KeyStroke.getKeyStroke("control Q"), screenCapture);


    Thread autoSaver = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                synchronized (screenCapture.getKey()) {
                    try {
                        screenCapture.getKey().wait();
                        System.out.println("continuing");
                        insertRow(false, false, screenCapture.word, screenCapture.meaning, screenCapture.byteImage);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    });
    autoSaver.start();



    }




    @Override
    public void insertRow(Boolean dialog,Boolean voluntary ,String word, String meaning, byte[] imageByte) {
        User.connection = createConnection(User.personalListUrl, User.username, User.password);
        if(!dialog&&voluntary) {
            try {
                Statement statement = User.connection.createStatement();
                statement.executeUpdate(
                        "INSERT INTO wordtable (word, meaning, imagebyte) VALUES ('" + word.trim() + "', '" + meaning + "', '" + imageByte + "')");
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        else if(!dialog){
            try {
                Statement statement = User.connection.createStatement();
                statement.executeUpdate(
                        "INSERT INTO recentwordtable (word, meaning, imagebyte) VALUES ('" + word.trim() + "', '" + meaning + "', '" + imageByte + "')");
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else if(dialog){

        }
    }


    @Override
    public void deleteRow(String word) {
        User.connection = createConnection(User.personalListUrl,User.username,User.password);
        try {
            Statement statement = User.connection.createStatement();
            statement.executeUpdate("delete from wordtable where word = " + word );
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
