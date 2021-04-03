package com.qrcode.tool;

public class BinaryConvert {

    public static String convertToBinary(int data, int bit){
        StringBuilder sb = new StringBuilder();
        while(bit-- != 0){
            sb.append(data & 1);
            data = data >> 1;
        }
        return sb.reverse().toString();
    }
}
