package util;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.example.DataAccessLayer;
import org.example.User;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Tool implements DataAccessLayer {
    Robot robot;
    {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }
    Tesseract tesseract = new Tesseract();
    int filecounter;


    public BufferedImage takeInstance (int[] cordinates){

        BufferedImage wordImage = null;
        Rectangle screenSize = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        int minX = cordinates[0];
        int minY = cordinates[1];
        int width = cordinates[2];
        int height = cordinates[3];

        BufferedImage screenImage = robot.createScreenCapture(screenSize);
        try {
            wordImage = screenImage.getSubimage(minX, minY, width, height);

        } catch (RasterFormatException exception) {
            // Handle other potential exceptions (discussed later)
        }
        return wordImage;
    }


    public byte[] imageToByte(BufferedImage wordImage) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(wordImage,"jpg",byteArrayOutputStream);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        byte[] imageByte = byteArrayOutputStream.toByteArray();
        return imageByte;
    }



    public String doOcr (BufferedImage wordImage){
        String word;
        try {
            tesseract.setDatapath("src/main/resources/");
            word = tesseract.doOCR(wordImage);
        } catch (TesseractException ex) {
            throw new RuntimeException(ex);
        }return word;

    }



    @Override
    public String findSemantic(String word){
        ResultSet resultSet = null;
        Statement statement = null;
        String meaning = null;
        User.connection = createConnection(User.dictionaryUrl,User.username,User.password);

        try {
            statement = User.connection.createStatement();
            ArrayList<Long> synonims = new ArrayList<Long>();

            resultSet = statement.executeQuery("SELECT synset_id FROM wn_synset WHERE word = '" + word.trim() + "'");
            while (resultSet.next()){
                int counter = 0;
                synonims.add(resultSet.getLong("synset_id"));
                System.out.println(synonims.get(counter));
                counter ++;
            }

            resultSet.close();
            if (!synonims.isEmpty()) {
                resultSet = statement.executeQuery("SELECT gloss FROM wn_gloss WHERE synset_id = " + synonims.get(0));
                while (resultSet.next()) {
                    meaning = resultSet.getString("gloss");
                    System.out.println(meaning);
                }
            }
            else{
                System.out.println("Invalid word");
            }
        }

        catch (SQLException e) {
            throw new RuntimeException(e);
        }

        finally {
            try {
                resultSet.close();
                statement.close();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        User.connection = createConnection(User.personalListUrl,User.username,User.password);
        return meaning;
    }


    public void fileSaver(BufferedImage bufferedImage) throws SQLException, IOException {

        Statement statement= User.connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select indexx from index");
        while (resultSet.next()){
            filecounter= resultSet.getInt("indexx");
        }
        resultSet.close();
        String filename = "image "+filecounter;
        statement.executeUpdate("update indexx set indexx " + (filecounter+1) + " where indexx = " + filecounter );
        File file = new File("Images/Image",filename);
        ImageIO.write(bufferedImage,"jpg",file);

    }



}
