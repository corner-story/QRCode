package com.qrcode.demo;

import com.qrcode.QRCode;
import com.qrcode.image.QRImage;

public class QRImageDemo {
    public static void main(String[] args) throws Exception{
        String data = "0123456999999";
        int error = 0;
        QRCode qrCode = new QRCode();
        qrCode.make(data, error);
    }
}
