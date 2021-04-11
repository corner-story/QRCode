package com.qrcode;

import com.qrcode.image.QRImage;
import com.qrcode.mode.DataAnalysis;
import com.qrcode.mode.DataMode;
import com.qrcode.reedsolo.RSCode;
import com.qrcode.tool.BinaryConvert;
import com.qrcode.tool.Image;
import com.qrcode.tool.QRTable;

public class QRCode {
    private int version = 1;

    public String makeQRCode(String data, int errorCorrectionLevel) throws Exception {
        // 选择合适的编码模式, 获取version 和 最后的0、1字符串
        DataMode dataMode = DataAnalysis.selectMode(data);
        version = dataMode.getBestVersion(data, errorCorrectionLevel);
        String codeWords = dataMode.getFinalBits(data, errorCorrectionLevel, version);

        System.out.println("编码数据: " + data);
        System.out.println("二维码版本: " + version);
        System.out.println("编码模式: " + dataMode.getClass().getName());
        System.out.println("\n");

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
        return sb.toString();
    }

    public void saveQRCode(String data, String path) {
        try {
            QRImage qrImage = new QRImage(version);
            int[][] matrix = qrImage.fillData(data);
            Image.writeImageFromArray(path, "png", matrix);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) throws Exception {
        String data = "0 1 2 3 4 5 6 7 8 9 A B C D E F G H I J K L M N O P Q R S T U V W X Y Z   $ % * + - . / :";
        String[] test = new String[]{
                "0123456789"                      // test NumericMode
                ,data          // test AlphanumericMode
                ,"河北工ｙｅ大学"                    // test Kanji mode
                ,"河北工业大学, Hello, World! --lambdafate" // test byte mode(UTF-8)
                ,"http://www.baidu.com/"
        };
        int error = 0;
        for (int i = 0; i < test.length; i++) {
            QRCode qrCode = new QRCode();
            String bits = qrCode.makeQRCode(test[i], error);
            System.out.println(bits);
            qrCode.saveQRCode(bits, "./images/QRCode-" + i + ".png");
        }
    }
}
