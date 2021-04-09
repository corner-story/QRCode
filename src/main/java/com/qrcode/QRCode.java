package com.qrcode;

import com.qrcode.image.QRImage;
import com.qrcode.mode.DataAnalysis;
import com.qrcode.mode.DataMode;
import com.qrcode.reedsolo.RSCode;
import com.qrcode.tool.BinaryConvert;
import com.qrcode.tool.Image;
import com.qrcode.tool.QRTable;

public class QRCode {
    public String make(String data, int errorCorrectionLevel) throws Exception {
        DataMode dataMode = DataAnalysis.selectMode(data);
        int version = dataMode.getBestVersion(errorCorrectionLevel, data.length());
        System.out.println("二维码版本: " + version);
        String codeWords = dataMode.getDataCodewords(data, version, errorCorrectionLevel);
        // codeWords to blocks
        int[] dataCodeWords = BinaryConvert.splitStringToIntArray(codeWords, 8);
        int[][] blocks = QRTable.getAllBlocks(dataCodeWords, version, errorCorrectionLevel);
        int[][] rsBlocks = new int[blocks.length][];
        int rsbytes = QRTable.getECCodeWords(version, errorCorrectionLevel);
        for (int i = 0; i < blocks.length; i++) {
            rsBlocks[i] = RSCode.encode(blocks[i], rsbytes);
        }
        // 对blocks, rsBlocks进行交错处理
        int[] InterleavedBlocks = QRTable.InterleaveBlocks(blocks);
        int[] InterleavedRSBlocks = QRTable.InterleaveBlocks(rsBlocks);
        // 将blocks转换位String, 并连接blocks 和 rsBlocks
        StringBuilder sb = new StringBuilder();
        sb.append(BinaryConvert.convertToBinary(InterleavedBlocks, 8));
        sb.append(BinaryConvert.convertToBinary(InterleavedRSBlocks, 8));
        // 添加Remainder Bits
        int remainderBits = QRTable.getRemainderBits(version);
        for (int i = 0; i < remainderBits; i++) {
            sb.append("0");
        }
        // 绘制二维码
        String encodeData = sb.toString();
        QRImage qrImage = new QRImage(version);
        int[][] matrix = qrImage.fillData(encodeData);
        Image.writeImageFromArray("./images/QRCode.png", "png", matrix);
        return null;
    }


    public static void main(String[] args) throws Exception {
        int errorLevel = 0;
        String data = "1234";

    }
}
