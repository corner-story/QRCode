package com.qrcode.demo;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import java.awt.image.BufferedImage;
import java.io.File;

public class ThbDemo {
    public static void testWaterMark(){
        String origin = "./images/" + "QRCode-" + "3" + ".png";
        String logo = "./images/logo.png";
        String output = "./images/output.png";
        try {
            BufferedImage logoImage = Thumbnails.of(new File(logo))
                    .size(60, 60)
                    .asBufferedImage();

            Thumbnails.of(new File(origin))
                    .size(300, 300)
                    .watermark(Positions.CENTER, logoImage, 1.0f)
                    .outputQuality(0.8)
                    .toFile(output);

        }catch (Exception e){
            System.out.println(e);
        }
    }


    public static void main(String[] args) {
        testWaterMark();
    }

}
