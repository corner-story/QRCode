package com.qrcode.demo;

import com.qrcode.QRCode;
import com.qrcode.tool.BinaryConvert;

public class QRImageDemo {
    public static void main(String[] args) throws Exception {
        // test Alpha num mode
        /*
        String data = "0 1 2 3 4 5 6 7 8 9 A B C D E F G H I J K L M N O P Q R S T U V W X Y Z   $ % * + - . / :";
        */
        // test byte mode
        String data = "河北工ｙｅ大学";
        StringBuilder sb = new StringBuilder();
        int time = 1;
        for (int i = 0; i < time; i++) {
            sb.append(data);
        }
        int error = 0;
        QRCode qrCode = new QRCode();
        qrCode.make(sb.toString(), error);


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

        String test = "河北工ｙｅ大学";
        System.out.println(test);
        byte[] bytes = test.getBytes("SJIS");
        for (int i = 0; i < bytes.length; i++) {
            System.out.println(BinaryConvert.convertToBinary(bytes[i] & 0xff, 8));
        }
    }
}
