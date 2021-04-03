package com.qrcode.mode;

import com.qrcode.tool.BinaryConvert;

public abstract class DataMode {

    protected String ModeIndicator;
    protected int[] CharacterCapacities;

    protected abstract boolean checkData(String data);

    protected abstract String encodeData(String data);

    protected abstract int getCharacterCountIndicatorBits(int version);


    public int getBestVersion(int errorCorrectionLevel, int dataLength){
        return 40;
    }


    public String encode(String data, int version){
        int bit = getCharacterCountIndicatorBits(version);
        String cci = BinaryConvert.convertToBinary(data.length(), bit);
        String ans = ModeIndicator + cci + encodeData(data);
        return ans;
    }
}