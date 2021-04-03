package com.qrcode;

import com.qrcode.mode.DataAnalysis;
import com.qrcode.mode.DataMode;

public class QRCode {
    public static void main(String[] args) throws Exception{
        DataMode dataMode = DataAnalysis.selectMode("123");

    }
}
