package org.example;

import com.tulskiy.keymaster.common.Provider;

import javax.swing.*;
import java.io.*;
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
        continiousconnection = DataAccessLayer.createConnection(personalListUrl, username, password);
        connection = DataAccessLayer.createConnection(personalListUrl, username, password);
        ScreenCapture screenCapture = new ScreenCapture(false);
        Provider provider = Provider.getCurrentProvider(false);
        provider.register(KeyStroke.getKeyStroke("control Q"), screenCapture);


    }
}
