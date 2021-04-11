package com.qrcode.mode;

import com.qrcode.tool.BinaryConvert;
import com.qrcode.tool.QRTable;

public class KanjiMode extends DataMode {

    public KanjiMode(){
        this.ModeIndicator = "1000";
        this.CharacterCapacities = QRTable.CharacterCapacities_KanjiMode;
    }

    @Override
    protected boolean checkData(String data) {
        try {
            byte[] bytes = data.getBytes("SJIS");
            if (bytes.length % 2 != 0){
                return false;
            }
            for (int i = 0; i < bytes.length; i = i + 2) {
                int value = ((bytes[i] & 0xff) << 8) | (bytes[i+1] & 0xff);
                if (value < 0x8140 || value > 0xebbf || (value > 0x9ffc && value < 0xe040)){
                    // return false;
                }
            }
        }catch(Exception e){
            System.out.println(e);
            return false;
        }
        return true;
    }

    @Override
    protected String encodeData(String data) {
        StringBuilder sb = new StringBuilder();
        try{
            byte[] bytes = data.getBytes("SJIS");
            for (int i = 0; i < bytes.length; i = i + 2) {
                int value = ((bytes[i] & 0xff) << 8) | (bytes[i+1] & 0xff);
                sb.append(getStringBits(value));
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return sb.toString();
    }

    private String getStringBits(int value){
        int tmp = value - getSubtract(value);
        int most = tmp >> 8, least = tmp & 0xff;
        int data = most * 0xc0 + least;
        return BinaryConvert.convertToBinary(data, 13);
    }

    private int getSubtract(int value){
        // 0x8140 to 0x9FFC
        if (value >= 0x8140 && value <= 0x9ffc){
            return 0x8140;
        }
        return 0xc140;
    }

    @Override
    protected int getCharacterCountIndicatorBits(int version) {
        if (version <= 9){
            return 8;
        }else if (version <= 26){
            return 10;
        }
        return 12;
    }
}
