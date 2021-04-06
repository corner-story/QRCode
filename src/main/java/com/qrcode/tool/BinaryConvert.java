package com.qrcode.tool;

public class BinaryConvert {

    // 将data转换为 bit位的0/1字符串
    public static String convertToBinary(int data, int bit) {
        StringBuilder sb = new StringBuilder();
        while (bit-- != 0) {
            sb.append(data & 1);
            data = data >> 1;
        }
        return sb.reverse().toString();
    }

    public static String convertToBinary(int[] data, int bit) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            sb.append(convertToBinary(data[i], bit));
        }
        return sb.toString();
    }

    // 将0/1字符串 转化为 int
    public static int convertToInt(String bits) {
        int data = 0;
        for (int i = 0; i < bits.length(); i++) {
            data = data << 1;
            if (bits.charAt(i) == '1') {
                data++;
            }
        }
        return data;
    }

    // 将一个0/1字符串以K位为一组，生成 int[]
    public static int[] splitStringToIntArray(String bits, int k) {
        assert bits.length() % k == 0;
        int[] data = new int[bits.length() / k];
        for (int i = 0; i < data.length; i++) {
            String tmp = bits.substring(i * k, i * k + k);
            int value = convertToInt(tmp);
            data[i] = value;
        }
        return data;
    }
}
