package com.qrcode.demo;

import com.qrcode.QRCode;

public class QRImageDemo {
    public static void main(String[] args) throws Exception{
        String data = "0 1 2 3 4 5 6 7 8 9 A B C D E F G H I J K L M N O P Q R S T U V W X Y Z   $ % * + - . / :";
        data = "HELLO WORLD " + data;
        StringBuilder sb = new StringBuilder();
        int time = 10;
        for (int i = 0; i < time; i++) {
            sb.append(data);
        }
        int error = 0;
        QRCode qrCode = new QRCode();
        qrCode.make(sb.toString(), error);
//        System.out.println(sb.toString());
    }

}
