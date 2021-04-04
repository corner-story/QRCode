package com.qrcode;

import com.qrcode.mode.DataAnalysis;
import com.qrcode.mode.DataMode;

public class QRCode {
    public void make(String data, int errorCorrectionLevel) throws Exception{
        DataMode dataMode = DataAnalysis.selectMode(data);
        int version = dataMode.getBestVersion(errorCorrectionLevel, data.length());
        dataMode.getDataCodewords(data, version, errorCorrectionLevel);
    }


    public static void main(String[] args) throws Exception{
        int errorLevel = 0;
        String data = "1234";

    }
}
