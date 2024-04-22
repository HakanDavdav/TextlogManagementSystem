package org.example;

import com.tulskiy.keymaster.common.HotKey;
import com.tulskiy.keymaster.common.HotKeyListener;
import util.Tool;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Queue;


public class ScreenCapture extends JFrame implements MouseListener , KeyListener , DataAccessLayer, HotKeyListener, Runnable {
    double firstMoment;
    double lastMoment;

    boolean keyControl;
    boolean mouseControl;

    static Point firstPoint;
    static Point lastPoint;

    Object key = new Object();
    Tool tool = new Tool();



    String meaning;
    String word;
    byte[] byteImage;




    public ScreenCapture(boolean dialogLogOrNot) {

        lastPoint = null;
        firstPoint = null;
        keyControl = false;
        mouseControl = false;
        firstMoment = 0;



        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setUndecorated(true);
        this.setOpacity(0.01f);
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.setVisible(true);
        this.setAlwaysOnTop(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBackground(Color.black);

    }



    public void keyPressed(KeyEvent e) {
        if ('e'==e.getKeyChar()&&!keyControl){
            firstMoment = System.currentTimeMillis();
            System.out.println(firstMoment);
            keyControl = true;
        }
    }



    public synchronized void keyReleased(KeyEvent e) {
        lastMoment = System.currentTimeMillis();

        if ((lastMoment- firstMoment)/1000>2) {
            System.out.println((lastMoment- firstMoment)/1000);
            keyControl = false;
            mouseControl = true;
            System.out.println("y");
        }
    }




    @Override
    public void mousePressed(MouseEvent e) {
        System.out.println("sa");
        if (mouseControl==true) {
            firstPoint = MouseInfo.getPointerInfo().getLocation();
            System.out.println("as");
        }
    }



    @Override
    public void mouseReleased(MouseEvent e) {
       synchronized (key) {
           if (mouseControl == true&&e.getButton()==3) {

           }
           else if (mouseControl == true) {
               configureCordinates();
               meaning = tool.findSemantic(tool.doOcr(tool.takeInstance(configureCordinates())));
               byteImage = tool.imageToByte(tool.takeInstance(configureCordinates()));
               word = tool.doOcr(tool.takeInstance(configureCordinates()));
               mouseControl = false;
               if(meaning!=null&&byteImage!=null&&word!=null) {
                   key.notify();
               }
           }
       }
    }

    public Object getKey(){
        return this.key;
    }



   static public int[] configureCordinates() {
        lastPoint = MouseInfo.getPointerInfo().getLocation();
        int minX = Math.min(firstPoint.x, lastPoint.x);
        int minY = Math.min(firstPoint.y, lastPoint.y);
        int width = Math.abs(firstPoint.x - lastPoint.x);
        int height = Math.abs(firstPoint.y - lastPoint.y);
        int[] values = {minX,minY,width,height};
        return values;
    }




    @Override
    public void onHotKey(HotKey hotKey) {
        if( this.isVisible() ){
           this.setVisible(false);
       }
        else {
           this.setVisible(true);
       }
    }





    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }


    @Override
    public void run() {

    }
}
