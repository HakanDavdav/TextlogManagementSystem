package ApplicationInterface;

import org.example.DataAccessLayer;
import org.example.User;
import util.Tool;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import javax.swing.text.Utilities;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Saved extends JFrame implements DataAccessLayer {
    public static JTextPane jTextPane = new JTextPane();
    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();


    public Saved(){
        jTextPane = new JTextPane();
        setText();
        JScrollPane jScrollPane = new JScrollPane(jTextPane);
        jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        this.setSize(300,450);
        this.add(jScrollPane);
        this.setLocation(dimension.width-this.getWidth(),0);
        this.setUndecorated(true);
        setText();
   jTextPane.addMouseListener(new MouseListener() {

       @Override
       public void mouseClicked(MouseEvent e) {
           try {
               String word=null;
               int pt= jTextPane.viewToModel(e.getPoint());
               int spt=Utilities.getWordStart(jTextPane,pt);
               int ept=Utilities.getWordEnd(jTextPane,pt);
               jTextPane.setSelectionStart(spt);
               jTextPane.setSelectionEnd(ept);
               word= jTextPane.getSelectedText();
               deleteRow(word);
               setText();

           } catch (BadLocationException ex) {
               throw new RuntimeException(ex);
           }
       }

       @Override
       public void mousePressed(MouseEvent e) {

       }

       @Override
       public void mouseReleased(MouseEvent e) {

       }

       @Override
       public void mouseEntered(MouseEvent e) {

       }

       @Override
       public void mouseExited(MouseEvent e) {

       }
   });

    }

    public static void setText(){
        try {
            Connection connection = DataAccessLayer.createConnection(User.personalListUrl,User.username,User.password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from wordtable");
            String word;
            String meaning;
            jTextPane.setText("");
            while (resultSet.next()){
                word = resultSet.getString("word");
                meaning = resultSet.getString("meaning");
                jTextPane.getDocument().insertString(jTextPane.getDocument().getLength(),"word : " + word + "\nmeaning : " + meaning+"\n\n", null);
            }
            resultSet.close();

        } catch (SQLException | BadLocationException e) {
            throw new RuntimeException(e);
        }
    }

}
