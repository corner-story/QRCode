package com.qrcode.mode;

import com.qrcode.tool.BinaryConvert;
import com.qrcode.tool.QRTable;

public class ByteMode extends DataMode {

    public ByteMode(){
        this.ModeIndicator = "0100";
        this.CharacterCapacities = QRTable.CharacterCapacities_ByteMode;
    }

    @Override
    protected boolean checkData(String data) {
        for (int i = 0; i < data.length(); i++) {
            int tmp = data.charAt(i);
            if (tmp > 126){
                return false;
            }
        }
        return true;
    }

    @Override
    protected String encodeData(String data) {
        StringBuilder sb = new StringBuilder();
        // byte[] bytes = data.getBytes("ISO-8859-1");
        for (int i = 0; i < data.length(); i++) {
            int tmp = data.charAt(i);
            sb.append(BinaryConvert.convertToBinary(tmp, 8));
        }
        return sb.toString();
    }

    @Override
    protected int getCharacterCountIndicatorBits(int version) {
        if (version <= 9){
            return 8;
        }else if(version <= 26){
            return 16;
        }
        return 16;
    }
}
