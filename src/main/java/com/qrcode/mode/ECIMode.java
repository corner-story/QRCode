package com.qrcode.mode;

import com.qrcode.tool.BinaryConvert;
import com.qrcode.tool.QRTable;

public class ECIMode extends DataMode {
    public ECIMode(){
        this.ModeIndicator = "0111" +                                           // ECI mode indicator
                             BinaryConvert.convertToBinary(26, 8) +     // UTF-8 ID
                             "0100";                                            // mode indicator
        this.CharacterCapacities = null;
    }


    @Override
    protected boolean checkData(String data) {
        return true;
    }

    @Override
    protected String encodeData(String data) {
        StringBuilder sb = new StringBuilder();
        try {
            byte[] bytes = data.getBytes("UTF-8");
            for (int i = 0; i < bytes.length; i++) {
                sb.append(BinaryConvert.convertToBinary(bytes[i] & 0xff, 8));
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return sb.toString();
    }

    @Override
    protected int getCharacterCountIndicatorBits(int version) {
        if (version <= 9){
            return 8;
        }else if(version <= 26){
            return 10;
        }
        return 12;
    }

    @Override
    public int getBestVersion(String data, int errorCorrectionLevel) throws Exception {
        int version = -1;
        int length = getDataLength(data);
        for (int i = 1; i <= 40 ; i++) {
            int tmp = length + getCharacterCountIndicatorBits(i);
            if (tmp <= QRTable.getMaxBits(i, errorCorrectionLevel) && (version == -1 || QRTable.getMaxBits(i, errorCorrectionLevel) < QRTable.getMaxBits(version, errorCorrectionLevel))){
                version = i;
            }
        }
        if (version < 0){
            throw new Exception("select version error");
        }
        return 2;
    }
}
