package com.qrcode.mode;

import com.qrcode.tool.BinaryConvert;
import com.qrcode.tool.QRTable;

public class ByteMode extends DataMode {

    public ByteMode(){
        this.ModeIndicator = "0100";
        this.CharacterCapacities = QRTable.CharacterCapacities_ByteMode;
        this.ByteLength = true;
    }

    @Override
    protected boolean checkData(String data) {
        return true;
    }

    @Override
    protected String encodeData(String data) {
        StringBuilder sb = new StringBuilder();
        try{
            byte[] bytes = data.getBytes("UTF-8");
            for (int i = 0; i < bytes.length; i++) {
                int tmp = bytes[i] & 0xff;
                sb.append(BinaryConvert.convertToBinary(tmp, 8));
            }
        } catch(Exception e){
            System.out.println(e);
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
