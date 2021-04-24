package com.qrcode;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.qrcode.image.QRImage;
import com.qrcode.mode.DataAnalysis;
import com.qrcode.mode.DataMode;
import com.qrcode.reedsolo.RSCode;
import com.qrcode.tool.BinaryConvert;
import com.qrcode.tool.Image;
import com.qrcode.tool.QRTable;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

/*
 *   QRCode: 二维码编码及生成二维码图片
 *
 *
 *
 * */
public class QRCode {
    public static QRCode of() {
        return new QRCode();
    }

    public static String decode(String filePath) {
        BufferedImage image;
        String data = null;
        try {
            image = ImageIO.read(new File(filePath));
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
            HashMap<DecodeHintType, Object> hints = new HashMap<>();
            hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
            Result result = new MultiFormatReader().decode(binaryBitmap, hints);// 对图像进行解码
            String content = result.getText();
            data = content;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    private int version = 1;
    private int errorCorrectionLevel = 0;

    private Integer selectVersion = null;
    private Integer FinderColor = null;
    private Integer DataColor = null;
    private Integer pixelSize = null;
    private Integer borderSize = null;

    /*
     *   调用makeQRCode之后得到的编码序列
     *   bits 将用于填充到二维数组中
     * */
    private String bits = "";

    private QRCode() {
    }

    public QRCode version(Integer selectVersion) {
        if (selectVersion != null && 1 <= selectVersion && selectVersion <= 40)
            this.selectVersion = selectVersion;
        return this;
    }

    public QRCode ecl(Integer ecl) {
        if (ecl != null && 0 <= ecl && ecl <= 3)
            errorCorrectionLevel = ecl;
        return this;
    }

    public QRCode findColor(Integer finderColor) {
        this.FinderColor = finderColor;
        return this;
    }

    public QRCode dataColor(Integer dataColor) {
        this.DataColor = dataColor;
        return this;
    }

    public QRCode pixelSize(Integer pixelSize) {
        this.pixelSize = pixelSize;
        return this;
    }

    public QRCode borderSize(Integer borderSize) {
        this.borderSize = borderSize;
        return this;
    }

    public QRCode encode(String data) throws Exception {
        this.bits = getQRCodeBits(data, errorCorrectionLevel, selectVersion);
        return this;
    }

    public QRCode toFile(String savePath) {
        int[][] matrix = getQRCodeMatrix(bits, FinderColor, DataColor, pixelSize, borderSize);
        Image.writeImageFromArray(savePath, Image.getImageType(savePath), matrix);
        return this;
    }

    public QRCode toFile(String savePath, String logoPath) {
        toFile(savePath).addLogo(savePath, logoPath);
        return this;
    }

    private void addLogo(String origin, String logo) {
        if (logo == null)
            return;
        try {
            BufferedImage logoImage = Thumbnails.of(new File(logo))
                    .size(60, 60)
                    .asBufferedImage();

            Thumbnails.of(new File(origin))
                    .size(300, 300)
                    .watermark(Positions.CENTER, logoImage, 1.0f)
                    .outputQuality(0.8)
                    .toFile(origin);
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    /*
     *   data: 要编码的数据
     *   errorCorrectionLevel: 0~3, 纠错级别
     * */
    private String getQRCodeBits(String data, Integer errorCorrectionLevel, Integer selectVersion) throws Exception {
        if (errorCorrectionLevel == null)
            errorCorrectionLevel = 0;
        if (selectVersion == null)
            selectVersion = 1;
        // 选择合适的编码模式, 获取version 和 最后的0、1字符串
        DataMode dataMode = DataAnalysis.selectMode(data);
        /*记录这里的version和ecl, saveQRCode 中要用到ecl*/
        this.version = dataMode.getBestVersion(data, errorCorrectionLevel);
        this.version = Math.max(this.version, selectVersion);
        this.errorCorrectionLevel = errorCorrectionLevel;
        String codeWords = dataMode.getFinalBits(data, errorCorrectionLevel, version);

        System.out.println("编码数据: " + data);
        System.out.println("二维码版本: " + version);
        System.out.println("编码模式: " + dataMode.getClass().getName());
        System.out.println("纠错级别: " + this.errorCorrectionLevel);

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

    private int[][] getQRCodeMatrix(String bits
            , Integer FinderColor
            , Integer DataColor
            , Integer pixelSize
            , Integer borderSize) {
        /*
         *   这里要使用errorCorrectionLevel初始化QRImage, 否则只能使用ecl为0的纠错级别
         *   ecl设置为其他时生成的二维码无法识别
         * */
        QRImage qrImage = new QRImage(version, errorCorrectionLevel);
        int[][] matrix = qrImage.fillData(bits, FinderColor, DataColor);
        matrix = Image.addScaleAndBorder(matrix, pixelSize, borderSize);
        return matrix;
    }
}
