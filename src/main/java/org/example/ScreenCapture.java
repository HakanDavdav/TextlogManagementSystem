package org.example;

import ApplicationInterface.Dummy;
import ApplicationInterface.Recent;
import ApplicationInterface.Saved;
import ApplicationInterface.TextLog;
import com.tulskiy.keymaster.common.HotKey;
import com.tulskiy.keymaster.common.HotKeyListener;
import util.Tool;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class ScreenCapture extends JFrame implements MouseListener , KeyListener , DataAccessLayer, HotKeyListener, Runnable {
    double firstMoment;
    double lastMoment;

    boolean keyControl;
    boolean mouseControl;
    MouseAdapter mouseAdapter;

    static Point startPoint;
    static Point endPoint;

    Object key = new Object();
    Tool tool = new Tool();

    Recent recent = new Recent();
    Saved saved = new Saved();
    TextLog textLog = new TextLog();
    static Dummy dummy;

    String meaning;
    String word;
    byte[] byteImage;
    JPanel drawing;




    public ScreenCapture(boolean dialogLogOrNot) {

        endPoint = null;
        startPoint = null;
        keyControl = false;
        mouseControl = false;
        firstMoment = 0;

        recent.setText();
        saved.setText();


        drawing = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(255, 0, 0));
                super.paintComponent(g);
                Graphics2D graphics2D = (Graphics2D) g;
                graphics2D.setStroke(new BasicStroke(4));
                if (startPoint != null && endPoint != null) {
                    int x = Math.min(startPoint.x, endPoint.x);
                    int y = Math.min(startPoint.y, endPoint.y);
                    int width = Math.abs(startPoint.x - endPoint.x);
                    int height = Math.abs(startPoint.y - endPoint.y);
                    graphics2D.drawRect(x, y, width, height);
                }

            }
        };

        MouseMotionListener mouseMotionListener = new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                endPoint = e.getPoint();
                drawing.repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }
        };


        this.addMouseMotionListener(mouseMotionListener);
        this.add(drawing);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.toFront();
        this.setUndecorated(true);
        this.setOpacity(0.05f);
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.setAlwaysOnTop(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


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

        if ((lastMoment- firstMoment)/1000>0.1) {
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
            startPoint = MouseInfo.getPointerInfo().getLocation();
            System.out.println("as");
            drawing.repaint();
        }
    }



    @Override
    public void mouseReleased(MouseEvent e) {
           if (mouseControl == true&&e.getButton()==3) {
               TextLogFactory textLogFactory = new TextLogFactory(configureCordinates());
               dummy = new Dummy(configureCordinates(),configureCordinates(true));
               textLogFactory.start();
               mouseControl = false;


           }
           else if (mouseControl == true) {
               configureCordinates();
               String word = tool.doOcr(tool.takeInstance(configureCordinates()));
               String meaning = tool.findSemantic(tool.doOcr(tool.takeInstance(configureCordinates())));
               byte[] byteImage = tool.imageToByte(tool.takeInstance(configureCordinates()));
               if((word != null)||(meaning != null)) {
                   insertRow(false, false, word, meaning, byteImage);
               }
               else{
                   System.out.println("Unidentified word");
               }
               Recent.setText();
               mouseControl = false;

           }
           drawing.repaint();
           endPoint = null;
           startPoint = null;

    }



   static public int[] configureCordinates() {
        endPoint = MouseInfo.getPointerInfo().getLocation();
        int minX = Math.min(startPoint.x, endPoint.x);
        int minY = Math.min(startPoint.y, endPoint.y);
        int width = Math.abs(startPoint.x - endPoint.x);
        int height = Math.abs(startPoint.y - endPoint.y);
        int[] values = {minX,minY,width,height};
        return values;
    }
    static public int[] configureCordinates(Boolean truth) {
        endPoint = MouseInfo.getPointerInfo().getLocation();
        int[] values = {startPoint.x,startPoint.y};
        return values;
    }



    @Override
    public void onHotKey(HotKey hotKey) {
        if(!this.isVisible()&&!(recent.isVisible()||saved.isVisible()||textLog.isVisible())){
           this.setVisible(true);
           recent.setVisible(false);
           saved.setVisible(false);
           textLog.setVisible(false);
       }
        else if (this.isVisible()) {
            this.setVisible(false);
            recent.setVisible(true);
            saved.setVisible(true);
            textLog.setVisible(true);
        }
        else if (!this.isVisible()&&(recent.isVisible()||saved.isVisible()||textLog.isVisible())) {
            recent.setVisible(false);
            saved.setVisible(false);
            textLog.setVisible(false);
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
