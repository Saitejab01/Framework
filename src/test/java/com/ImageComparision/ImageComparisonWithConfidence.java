package com.ImageComparision;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class ImageComparisonWithConfidence {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Take image paths from user
        System.out.print("Enter path of first image: ");
        String imagePath1 = scanner.nextLine();

        System.out.print("Enter path of second image: ");
        String imagePath2 = scanner.nextLine();

        // Take confidence score
        System.out.print("Enter confidence percentage (0-100): ");
        double confidenceThreshold = scanner.nextDouble();

        try {
            // Load images
            BufferedImage img1 = ImageIO.read(new File(imagePath1));
            BufferedImage img2 = ImageIO.read(new File(imagePath2));

            // Compare dimensions
            if (img1.getWidth() != img2.getWidth() || img1.getHeight() != img2.getHeight()) {
                System.out.println("Images are NOT identical (different dimensions).");
                return;
            }

            int width = img1.getWidth();
            int height = img1.getHeight();
            int diffCount = 0;

            // Track bounding box of differences
            int minX = width, minY = height, maxX = 0, maxY = 0;

            // Compare pixel by pixel
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (img1.getRGB(x, y) != img2.getRGB(x, y)) {
                        diffCount++;
                        if (x < minX) minX = x;
                        if (y < minY) minY = y;
                        if (x > maxX) maxX = x;
                        if (y > maxY) maxY = y;
                    }
                }
            }

            // Calculate similarity
            double totalPixels = width * height;
            double similarity = 100.0 - ((diffCount * 100.0) / totalPixels);

            System.out.println("Similarity: " + String.format("%.2f", similarity) + "%");

            if (similarity >= confidenceThreshold) {
                System.out.println("✅ Images MATCH with required confidence (" + confidenceThreshold + "%).");
            } else {
                System.out.println("❌ Images DO NOT MATCH. Confidence below " + confidenceThreshold + "%.");
            }

            // Create output image
            BufferedImage diffImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = diffImage.createGraphics();
            g.drawImage(img1, 0, 0, null);

            // Draw red rectangle if differences found
            if (diffCount > 0) {
                g.setColor(Color.RED);
                g.setStroke(new BasicStroke(3)); // thickness of rectangle
                g.drawRect(minX, minY, (maxX - minX), (maxY - minY));
            }

            g.dispose();

            // Save difference image
            String diffPath = "./src/test/resources/Testdata/difference.png";
            ImageIO.write(diffImage, "png", new File(diffPath));
            System.out.println("🖼️ Difference image saved at: " + diffPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
