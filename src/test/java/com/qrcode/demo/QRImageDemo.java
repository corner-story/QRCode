package com.qrcode.demo;

import com.qrcode.QRCode;

public class QRImageDemo {
    public static void main(String[] args) throws Exception {
        String data = "0 1 2 3 4 5 6 7 8 9 A B C D E F G H I J K L M N O P Q R S T U V W X Y Z   $ % * + - . / :";
        String[] test = new String[]{
            "0123456789"                      // test NumericMode
            ,data          // test AlphanumericMode
            ,"河北工ｙｅ大学"                    // test Kanji mode
            ,"河北工业大学, Hello, World! --lambdafate" // test byte mode(UTF-8)
        };
        int error = 1;
        for (int i = 0; i < test.length; i++) {
            QRCode qrCode = new QRCode();
            String bits = qrCode.makeQRCode(test[i], error);
            System.out.println(bits);
            qrCode.saveQRCode(bits, "./images/QRCode-" + i + ".png");
        }

//        String test = "abc";
//        for (int i = 0; i < test.length(); i++) {
//            int tmp = test.charAt(i);
//            if (tmp > 126){
//                System.out.println("not ascii");
//            }
//
//        }
//        byte[] bytes = test.getBytes("ISO-8859-1");
//        for (int i = 0; i < bytes.length; i++) {
//            System.out.print((bytes[i]) + ", ");
//        }

//        String test = "河北工ｙｅ大学";
//        System.out.println(test);
//        byte[] bytes = test.getBytes("SJIS");
//        for (int i = 0; i < bytes.length; i++) {
//            System.out.println(BinaryConvert.convertToBinary(bytes[i] & 0xff, 8));
//        }

        // System.out.println(String.valueOf(0xef));
    }
}
