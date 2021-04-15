package com.qrcode.tool;


import com.qrcode.image.QRImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

public class Image {


    /*
     *   原来的matrix太小, 要进行扩大, 并添加border
     * */
    public static int[][] addScalaAndBorder(int[][] matrix, int scale, int borderSize) {
        int version = (matrix.length - 17) / 4;
        assert version >= 1 && version <= 40;
        assert scale >= 1 && scale <= 50;
        int qrLength = matrix.length * scale;
        int[][] ans = new int[qrLength + borderSize * 2][qrLength + borderSize * 2];
        int edge = borderSize + qrLength;
        for (int i = 0; i < ans.length; i++) {
            for (int j = 0; j < ans.length; j++) {
                if (i >= borderSize && i < edge && j >= borderSize && j < edge)
                    ans[i][j] = matrix[(i - borderSize) / scale][(j - borderSize) / scale];
                else
                    ans[i][j] = QRImage.WHITE;
            }
        }
        return ans;
    }

    /*
     *   在中间添加logo
     * */
    public static int[][] addLogo(int[][] matrix, String logoPath, int logoSize) {
        int[][] logo = getLogoMatrix(logoPath, logoSize);
        assert logo.length != 0 && logo.length < matrix.length && logo[0].length < matrix.length;
        for (int i = 0; i < logo.length; i++) {
            for (int j = 0; j < logo[i].length; j++) {
                matrix[(matrix.length - logo.length) / 2 + i][(matrix[0].length - logo[0].length) / 2 + j] = logo[i][j];
            }
        }
        return matrix;
    }

    /*
     *   logo预处理, 防止logo太大, 二维码无法读取
     * */
    private static int[][] getLogoMatrix(String logoPath, int logoSize) {
        BufferedImage logo = reSize(logoPath, logoSize, logoSize, true);
        return convertImageToArray(logo);
    }


    public static BufferedImage reSize(String path, int width, int height, boolean equalScale) {
        File srcImg = new File(path);
        String type = getImageType(srcImg);
        if (type == null) {
            return null;
        }
        if (width < 0 || height < 0) {
            return null;
        }

        BufferedImage srcImage = null;
        try {
            srcImage = ImageIO.read(srcImg);
            System.out.println("srcImg size=" + srcImage.getWidth() + "X" + srcImage.getHeight());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        // targetW，targetH分别表示目标长和宽
        BufferedImage target = null;
        if (srcImage != null) {
            double sx = (double) width / srcImage.getWidth();
            double sy = (double) height / srcImage.getHeight();
            // 等比缩放
            if (equalScale) {
                if (sx > sy) {
                    sx = sy;
                    width = (int) (sx * srcImage.getWidth());
                } else {
                    sy = sx;
                    height = (int) (sy * srcImage.getHeight());
                }
            }
            System.out.println("destImg size=" + width + "X" + height);
            ColorModel cm = srcImage.getColorModel();
            WritableRaster raster = cm.createCompatibleWritableRaster(width, height);
            boolean alphaPremultiplied = cm.isAlphaPremultiplied();

            target = new BufferedImage(cm, raster, alphaPremultiplied, null);
            Graphics2D g = target.createGraphics();
            // smoother than exlax:
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g.drawRenderedImage(srcImage, AffineTransform.getScaleInstance(sx, sy));
            g.dispose();
        }
        return target;
    }


    public static String getImageType(File file) {
        if (file != null && file.exists() && file.isFile()) {
            String fileName = file.getName();
            int index = fileName.lastIndexOf(".");
            if (index != -1 && index < fileName.length() - 1) {
                return fileName.substring(index + 1);
            }
        }
        return null;
    }

    public static String getImageType(String path){
        return getImageType(new File(path));
    }

    private static BufferedImage readImage(String imageFile) {
        File file = new File(imageFile);
        BufferedImage bf = null;
        try {
            bf = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bf;
    }

    public static int[][] convertImageToArray(BufferedImage bf) {
        // 获取图片宽度和高度
        int width = bf.getWidth();
        int height = bf.getHeight();
        // 将图片sRGB数据写入一维数组
        int[] data = new int[width * height];
        bf.getRGB(0, 0, width, height, data, 0, width);
        // 将一维数组转换为为二维数组
        int[][] rgbArray = new int[height][width];
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                rgbArray[i][j] = data[i * width + j];
        return rgbArray;
    }

    public static void writeImageFromArray(String imageFile, String type, int[][] rgbArray) {
        // 获取数组宽度和高度
        int width = rgbArray[0].length;
        int height = rgbArray.length;
        // 将二维数组转换为一维数组
        int[] data = new int[width * height];
        for (int i = 0; i < height; i++)
            for (int j = 0; j < width; j++)
                data[i * width + j] = rgbArray[i][j];
        // 将数据写入BufferedImage
        BufferedImage bf = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        bf.setRGB(0, 0, width, height, data, 0, width);
        // 输出图片
        try {
            File file = new File(imageFile);
            ImageIO.write((RenderedImage) bf, type, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
