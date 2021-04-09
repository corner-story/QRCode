package com.qrcode.mode;

import com.qrcode.tool.BinaryConvert;
import com.qrcode.tool.QRTable;

import java.util.HashMap;

public class AlphanumericMode extends DataMode {
    private static HashMap<Character, Integer> CHARACTERS = new HashMap<>();
    static {
        CHARACTERS.put('0', 0);
        CHARACTERS.put('1', 1);
        CHARACTERS.put('2', 2);
        CHARACTERS.put('3', 3);
        CHARACTERS.put('4', 4);
        CHARACTERS.put('5', 5);
        CHARACTERS.put('6', 6);
        CHARACTERS.put('7', 7);
        CHARACTERS.put('8', 8);
        CHARACTERS.put('9', 9);
        CHARACTERS.put('A', 10);
        CHARACTERS.put('B', 11);
        CHARACTERS.put('C', 12);
        CHARACTERS.put('D', 13);
        CHARACTERS.put('E', 14);
        CHARACTERS.put('F', 15);
        CHARACTERS.put('G', 16);
        CHARACTERS.put('H', 17);
        CHARACTERS.put('I', 18);
        CHARACTERS.put('J', 19);
        CHARACTERS.put('K', 20);
        CHARACTERS.put('L', 21);
        CHARACTERS.put('M', 22);
        CHARACTERS.put('N', 23);
        CHARACTERS.put('O', 24);
        CHARACTERS.put('P', 25);
        CHARACTERS.put('Q', 26);
        CHARACTERS.put('R', 27);
        CHARACTERS.put('S', 28);
        CHARACTERS.put('T', 29);
        CHARACTERS.put('U', 30);
        CHARACTERS.put('V', 31);
        CHARACTERS.put('W', 32);
        CHARACTERS.put('X', 33);
        CHARACTERS.put('Y', 34);
        CHARACTERS.put('Z', 35);
        CHARACTERS.put(' ', 36);
        CHARACTERS.put('$', 37);
        CHARACTERS.put('%', 38);
        CHARACTERS.put('*', 39);
        CHARACTERS.put('+', 40);
        CHARACTERS.put('-', 41);
        CHARACTERS.put('.', 42);
        CHARACTERS.put('/', 43);
        CHARACTERS.put(':', 44);
    }

    public AlphanumericMode(){
        this.ModeIndicator = "0010";
        this.CharacterCapacities = QRTable.CharacterCapacities_AlphanumericMode;
    }


    @Override
    protected boolean checkData(String data) {
        for (int i = 0; i < data.length(); i++) {
            if (!CHARACTERS.containsKey(data.charAt(i))){
                return false;
            }
        }
        return true;
    }

    @Override
    protected String encodeData(String data) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i + 1 < data.length() ; i = i + 2) {
            int tmp = CHARACTERS.get(data.charAt(i)) * 45 + CHARACTERS.get(data.charAt(i+1));
            sb.append(BinaryConvert.convertToBinary(tmp, 11));
        }
        if (data.length() % 2 == 1){
            int tmp = CHARACTERS.get(data.charAt(data.length() - 1));
            sb.append(BinaryConvert.convertToBinary(tmp, 6));
        }
        return sb.toString();
    }

    @Override
    protected int getCharacterCountIndicatorBits(int version) {
        if (version <= 9){
            return 9;
        }else if(version <= 26){
            return 11;
        }
        return 13;
    }
}
