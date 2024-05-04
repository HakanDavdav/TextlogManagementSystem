package org.example;

import util.Tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
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
    Statement statement;


    @Override
    public void run() {
            try {
                statement = User.continiousconnection.createStatement();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            while (true) {
                if (ScreenCapture.dummy==null){
                    break;
                }

                queue.add(tool.doOcr(tool.takeInstance(cordinates)));
                System.out.println(queue.peek());
                if (!textLog.contains(queue.peek()) && !queue.peek().contains("DUMMY")) {
                    System.out.println(queue.peek());
                    textLog.add(queue.peek());
                    insertRow(true, false, textLog.peek(), null, null);
                }
                if (queue.size() > 40) {
                    queue.poll();
                }
                if (textLog.size() > 40) {
                    textLog.poll();
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }


            }


    }

}
