package ApplicationInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Dummy extends JFrame implements MouseListener {
    Object key;
    public Dummy(int[] cordinates,int[] cordinates2){
        this.setLocation(cordinates2[0],cordinates2[1]);
        this.setSize(cordinates[2],cordinates[3]);
        this.setUndecorated(true);
        this.setBackground(new Color(0, 255, 0, 0));

        JPanel jPanel = new JPanel(){

            @Override
            protected void paintComponent(Graphics g) {
                // Allow super to paint
                super.paintComponent(g);

                // Apply our own painting effect
                Graphics2D g2d = (Graphics2D) g.create();
                // 50% transparent Alphae
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.0f));

                g2d.setColor(getBackground());
                g2d.fill(getBounds());

                g2d.dispose();
            }
        };

        jPanel.addMouseListener(this);
        jPanel.setOpaque(false);
        jPanel.setLayout(new BorderLayout());
        this.setContentPane(jPanel);


        JLabel jLabel = new JLabel("DUMMY",SwingConstants.CENTER);
        jLabel.setFont(new Font(Font.DIALOG,Font.BOLD,30));
        jLabel.setForeground(Color.WHITE);
        jPanel.add(jLabel,BorderLayout.SOUTH);
        this.getContentPane().setBackground(Color.BLACK);
        this.setOpacity(0.3f);
        this.setVisible(true);

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        this.dispose();
        key = new Object();
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
}
