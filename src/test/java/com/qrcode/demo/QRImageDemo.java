package com.qrcode.demo;

import com.qrcode.QRCode;

public class QRImageDemo {
    public static void main(String[] args) throws Exception{
        String data = "1234567890";
        StringBuilder sb = new StringBuilder();
        int time = 90;
        for (int i = 0; i < time; i++) {
            sb.append(data);
        }
        int error = 0;
        QRCode qrCode = new QRCode();
        qrCode.make(sb.toString(), error);
    }

}
