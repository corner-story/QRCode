package com.qrcode.mode;

import com.qrcode.tool.BinaryConvert;
import com.qrcode.tool.QRTable;

public abstract class DataMode {

    /*
    *   编码模式指示符, 数字编码, 字符编码
    * */
    protected String ModeIndicator;

    /*
    *   数据编码模式的 字符容量
    * */
    protected int[] CharacterCapacities;

    /*
    *   检查数据是否满足该模式要求
    * */
    protected abstract boolean checkData(String data);

    /*
    *   根据特定的编码模式编码数据
    * */
    protected abstract String encodeData(String data);

    /*
    *   获取编码模式的 cci长度
    * */
    protected abstract int getCharacterCountIndicatorBits(int version);

    /*
    *   编码模式确定后，可以确定一个最符合要求的二维码版本
    * */
    public int getBestVersion(int errorCorrectionLevel, int dataLength) throws Exception{
        int index = -1;
        for (int i = errorCorrectionLevel; i < CharacterCapacities.length; i = i + 4) {
            if (dataLength <= CharacterCapacities[i] && (index == -1 || CharacterCapacities[i] < CharacterCapacities[index])){
                index = i;
            }
        }
        if (index == -1){
            throw new Exception("select version error, data length: " + dataLength);
        }
        return (index / 4) + 1;
    }


    /*
    *   获取 mode indicator + cci + data encode
    * */
    public String getDataCodewords(String data, int version, int errorCorrectionLevel) throws Exception{
        StringBuilder sb = new StringBuilder();
        int bit = getCharacterCountIndicatorBits(version);
        String cci = BinaryConvert.convertToBinary(data.length(), bit);
        sb.append(ModeIndicator);
        sb.append(cci);
        sb.append(encodeData(data));
        int maxBits = QRTable.getMaxBits(version, errorCorrectionLevel);
        if (maxBits < sb.length()){
            throw new Exception("encode data error");
        }
        // add Terminator
        int term = Math.min(4, maxBits - sb.length());
        for (int i = 0; i < term; i++) {
            sb.append("0");
        }
        // 让编码的长度成为8的倍数
        if (sb.length() % 8 != 0) {
            term = 8 - (sb.length() % 8);
            for (int i = 0; i < term; i++) {
                sb.append("0");
            }
        }
        // 添加末尾字符串凑够MaxBits长度
        String fill = "1110110000010001";
        int j = 0;
        while(sb.length() != maxBits){
            sb.append(fill.charAt(j));
            j = (j + 1) % fill.length();
        }
        return sb.toString();
    }
}