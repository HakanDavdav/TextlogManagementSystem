package org.example;

import util.Tool;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class TextLogFactory extends Thread implements DataAccessLayer{
    Tool tool = new Tool();
    Queue<String> queue = new LinkedList<>();
    Queue<String> textLog = new LinkedList<>();
    int[] cordinates;
    TextLogFactory(int[] cordinates){
        this.cordinates = cordinates;
    }

    @Override
    public void run() {
        Statement statement;
        try {
            User.continiousconnection = createConnection(User.personalListUrl,User.username,User.password);
            statement = User.continiousconnection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        while (true) {
           queue.add(tool.doOcr(tool.takeInstance(cordinates)));
         if (!textLog.contains(queue.peek())) {
             textLog.add(queue.peek());
             try {
                 statement.executeUpdate("insert textlog values (" + textLog.peek() + ")");
             } catch (SQLException e) {
                 throw new RuntimeException(e);
             }
         }
         if(queue.size()>40){
             queue.poll();
         }
         if (textLog.size()>40){
             textLog.poll();
         }

       }

    }

}
