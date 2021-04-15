package com.qrcode.image;

import com.qrcode.tool.QRTable;

import javax.xml.crypto.Data;

public class QRImage {
    private static int BLACK = 0x000000;

    /*
    *   白色
    * */
    public final static int WHITE = 0xffffff;

    /*
    *   该位置为空，没有放置任何东西
    * */
    private static int EMPTY = Integer.MAX_VALUE;

    /*
    *   该位置用做保留位
    * */
    private static int RESERVE = Integer.MIN_VALUE;

    private int version = 1;
    private int ECCLevel = 0;
    private int MaskPattern = 0;
    private int[][] matrix;

    public QRImage(int version, int ECCLevel){
        this.version = version;
        this.ECCLevel = ECCLevel;
        int size = (version - 1) * 4 + 21;
        this.matrix = new int[size][size];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = EMPTY;
            }
        }
    }

    public QRImage(int version){
        this(version, 0);
    }

    public int[][] fillData(String encodeData, Integer FinderColor, Integer DataColor){
        if (FinderColor == null)
            FinderColor = BLACK;
        if (DataColor == null)
            DataColor = BLACK;

        // 添加定位信息
        drawFinderPatterns(FinderColor);
        drawSeparators();
        drawAlignmentPatterns(DataColor);
        drawTimingPatterns(DataColor);
        drawDarkModuleAndReservedAreas(RESERVE, DataColor);
        // 填充数据
        drawDataBits(encodeData, DataColor);
        // 填充纠错，mask模式，版本等信息
        drawFormat(ECCLevel, MaskPattern, DataColor);
        drawVersion(version, DataColor);
        return matrix;
    }


    /*
    *   step1  添加 Finder Patterns
    * */
    private void drawFinderPatterns(int color){
        int[][] pos = new int[][]{
                {0, 0}                         // 左上角
                ,{0, matrix[0].length - 7}     // 右上角
                ,{matrix.length - 7, 0}        // 左下角
        };
        for (int[] xy: pos) {
            int x = xy[0], y = xy[1];
            drawArea(x, y, 7, 7, color);
            drawArea(x + 1, y + 1, 5, 5, WHITE);
            drawArea(x + 2, y + 2, 3, 3, color);
        }
    }

    /*
    *   step2 添加 Separators
    * */
    private void drawSeparators(){
        // 左上角
        drawArea(7, 0, 1, 7, WHITE);
        drawArea(0, 7, 8, 1, WHITE);
        // 右上角
        drawArea(0, matrix.length - 8, 8, 1, WHITE);
        drawArea(7, matrix.length - 7, 1, 7, WHITE);
        // 左下角
        drawArea(matrix.length - 8, 0, 1, 7, WHITE);
        drawArea(matrix.length - 8, 7, 8, 1, WHITE);
    }

    /*
    *   step3 添加 Alignment Patterns
    * */
    private void drawAlignmentPatterns(int color){
        int[] locations = QRTable.getAlignmentLocations(version);
        for (int i = 0; i < locations.length; i++) {
            for (int j = 0; j < locations.length; j++) {
                if (checkArea(locations[i] - 2, locations[j] - 2, 5, 5)){
                    drawArea(locations[i] - 2, locations[j] - 2, 5, 5, color);
                    drawArea(locations[i] - 1, locations[j] - 1, 3, 3, WHITE);
                    drawArea(locations[i], locations[j], 1, 1, color);
                }
            }
        }
    }

    /*
    *   Step 4: Add the Timing Patterns
    * */
    public void drawTimingPatterns(int color){
        int x = 8, y = 6;
        int count = matrix.length - 16;
        for (int i = 0; i < count; i++, x++) {
            int c = i % 2 == 0 ? color: WHITE;
            drawArea(x, y, c);
            drawArea(y, x, c);
        }
    }

    /*
    *   Step 5: Add the Dark Module and Reserved Areas
    * */
    private void drawDarkModuleAndReservedAreas(int color, int dataColor){
        // 1. add dark module
        drawArea(matrix.length - 8, 8, dataColor);
        // 2. add Format Information Area
        // 左上角
        drawArea(8,0, 1, 6, color);
        drawArea(0, 8,6,1, color);
        drawArea(8,7, color);
        drawArea(8, 8, color);
        drawArea(7,8, color);
        // 右上角
        drawArea(8, matrix.length - 8, 1, 8, color);
        // 左下角
        drawArea(matrix.length - 7, 8, 7, 1, color);
        // 3. add Version Information Area
        if (version >= 7){
            // 右上角
            drawArea(0, matrix.length - 11, 6, 3, color);
            // 左下角
            drawArea(matrix.length - 11, 0, 3, 6, color);
        }
    }


    /*
    *   Step 6: Place the Data Bits
    * */
    private void drawDataBits(String dataBits, int color){
        int dataIndex = 0;
        int direction = -1;
        int x = matrix.length - 1, y = x;
        int verticalTiming = 6;
        while(y > 0){
            while(x >= 0 && x < matrix.length){
                if (matrix[x][y] == EMPTY){
                    matrix[x][y] = dataBits.charAt(dataIndex++) == '0'? WHITE: color;
                    matrix[x][y] = checkMask(x, y, color);
                }
                if (matrix[x][y-1] == EMPTY){
                    matrix[x][y-1] = dataBits.charAt(dataIndex++) == '0'? WHITE: color;
                    matrix[x][y-1] = checkMask(x, y - 1, color);
                }
                x += direction;
            }
            direction = -direction;
            x += direction;
            y -= 2;
            if (y == verticalTiming){
                y--;
            }

        }
        assert dataIndex == dataBits.length();
    }


    /*
    *   add Format
    * */
    private void drawFormat(int ECCLevel, int maskPattern, int color){
        // 添加格式信息
        String format = QRTable.getFormatStringBits(ECCLevel, maskPattern);
        int dataIndex = 0;
        // 左上角
        int x = 8, y = 0;
        for (int i = 0; i < 8; i++, y++) {
            if (y == 6)
                continue;
            assert matrix[x][y] == RESERVE;
            drawArea(x, y, format.charAt(dataIndex++) == '0' ? WHITE: color);
        }
        for (int i = 0; i < 9; i++, x--) {
            if (x == 6)
                continue;
            assert matrix[x][y] == RESERVE;
            drawArea(x, y, format.charAt(dataIndex++) == '0' ? WHITE: color);
        }
        assert x == -1 && dataIndex == format.length();
        // 左下, 右上
        dataIndex = 0;
        x = matrix.length - 1; y = 8;
        for (int i = 0; i < 7; i++, x--) {
            assert matrix[x][y] == RESERVE;
            drawArea(x, y, format.charAt(dataIndex++) == '0' ? WHITE: color);
        }
        x = 8; y = matrix.length - 8;
        for (int i = 0; i < 8; i++, y++) {
            assert matrix[x][y] == RESERVE;
            drawArea(x, y, format.charAt(dataIndex++) == '0' ? WHITE: color);
        }
        assert y == matrix.length && dataIndex == format.length();
    }

    /*
    *   add version
    * */
    private void drawVersion(int version, int color){
        if(version < 7)
            return;
        String versionBits = new StringBuffer(QRTable.getVersionStringBits(version)).reverse().toString();
        // 右上
        int dataIndex = 0;
        int x = 0, y = matrix.length - 11;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 3; j++) {
                assert matrix[x+i][y+j] == RESERVE;
                drawArea(x+i, y+ j, versionBits.charAt(dataIndex++) == '0' ? WHITE: color);
            }
        }
        assert dataIndex == versionBits.length();
        // 左下
        x = matrix.length - 11; y = 0;
        dataIndex = 0;
        for (int j = 0; j < 6; j++) {
            for (int i = 0; i < 3; i++) {
                assert matrix[x+i][y+j] == RESERVE;
                drawArea(x+i, y+ j, versionBits.charAt(dataIndex++) == '0' ? WHITE: color);
            }
        }
        assert dataIndex == versionBits.length();
    }


    /*
    *   绘制一个区域
    * */
    private void drawArea(int x, int y, int rows, int clos, int color){
        for (int i = x; i < x + rows; i++) {
            for (int j = y; j < y + clos; j++) {
                matrix[i][j] = color;
            }
        }
    }

    /*
    *   绘制一个点
    * */
    private void drawArea(int x, int y, int color){
        matrix[x][y] = color;
    }

    /*
    *   检查一个区域是否全为空，没被其他元素占用
    * */
    private boolean checkArea(int x, int y, int rows, int clos){
        for (int i = x; i < x + rows; i++) {
            for (int j = y; j < y + clos; j++) {
                if (matrix[i][j] != EMPTY){
                    return false;
                }
            }
        }
        return true;
    }

    /*
    *   检查该位置是否应该进行masking
    * */
    private int checkMask(int x, int y, int msk, int color){
        assert matrix[x][y] == BLACK || matrix[x][y] == WHITE;
        boolean invert;
        switch (msk) {
            case 0:  invert = (x + y) % 2 == 0;                    break;
            case 1:  invert = x % 2 == 0;                          break;
            case 2:  invert = y % 3 == 0;                          break;
            case 3:  invert = (x + y) % 3 == 0;                    break;
            case 4:  invert = (x / 2 + y / 3) % 2 == 0;            break;
            case 5:  invert = x * y % 2 + x * y % 3 == 0;          break;
            case 6:  invert = (x * y % 2 + x * y % 3) % 2 == 0;    break;
            case 7:  invert = ((x + y) % 2 + x * y % 3) % 2 == 0;  break;
            default:  throw new AssertionError();
        }
        if (invert){
            return matrix[x][y] == color ? WHITE: color;
        }
        return matrix[x][y];
    }

    private int checkMask(int x, int y, int color){
        return checkMask(x, y, MaskPattern, color);
    }
}
