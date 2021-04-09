package com.qrcode.mode;

import com.qrcode.tool.BinaryConvert;
import com.qrcode.tool.QRTable;

public class NumericMode extends DataMode {

    public NumericMode() {
        this.ModeIndicator = "0001";
        this.CharacterCapacities = QRTable.CharacterCapacities_NumericMode;
    }

    @Override
    protected boolean checkData(String data) {
        for (int i = 0; i < data.length(); i++) {
            if (!Character.isDigit(data.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected String encodeData(String data) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length(); i = i + 3) {
            String tmp = data.substring(i, Math.min(i + 3, data.length()));
            int bit = tmp.length() == 3 ? 10: (tmp.length() == 2 ? 7: 4);
            sb.append(BinaryConvert.convertToBinary(Integer.valueOf(tmp), bit));
        }
        return sb.toString();
    }

    @Override
    protected int getCharacterCountIndicatorBits(int version) {
        if (version <= 9){
            return 10;
        }else if (version <= 26){
            return 12;
        }
        return 14;
    }
}
