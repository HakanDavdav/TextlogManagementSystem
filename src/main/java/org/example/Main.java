package org.example;

import ApplicationInterface.Saved;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.CLAHE;
import org.opencv.imgproc.Imgproc;
import util.Tool;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args){
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println(Core.VERSION);
        Tool tool = new Tool();

        File file = new File("C:/Users/Administrator/Desktop/Untitled.png");
        Mat src = Imgcodecs.imread(file.getAbsolutePath(), Imgcodecs.IMREAD_GRAYSCALE);

        // Resize the image to a desired size
        int targetWidth = 640*2; // Adjust as needed
        int targetHeight = 480*2; // Adjust as needed
        Mat resizedSrc = new Mat();
        Imgproc.resize(src, resizedSrc, new org.opencv.core.Size(targetWidth, targetHeight));

        Mat dst = new Mat();

        // Apply Gaussian blur to the resized image


        // Apply adaptive thresholding to the blurred image
        Imgproc.adaptiveThreshold(resizedSrc,dst, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 7, 10);

        // Save the processed image
        Imgcodecs.imwrite("C:/Users/Administrator/Desktop/output.jpg", dst);

        try {
            BufferedImage bufferedImage = ImageIO.read(new File("C:/Users/Administrator/Desktop/output.jpg"));
            
            String x = tool.doOcr(bufferedImage);
            System.out.println(x);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}