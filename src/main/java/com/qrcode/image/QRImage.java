package com.qrcode.image;

import com.qrcode.tool.QRTable;

public class QRImage {
    private static int BLOCK = 0;
    private static int WHITE = 0xffffff;

    /*
    *   该位置为空，没有放置任何东西
    * */
    private static int EMPTY = 0x0000ff;

    /*
    *   该位置用做保留位
    * */
    private static int RESERVE = 0xff0000;

    private int version = 1;
    private int[][] matrix;

    public QRImage(int version){
        this.version = version;
        int size = (version - 1) * 4 + 21;
        this.matrix = new int[size][size];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = EMPTY;
            }
        }

    }

    public int[][] fillData(String encodeData){
        drawFinderPatterns();
        drawSeparators();
        drawAlignmentPatterns();
        drawTimingPatterns();
        drawDarkModuleAndReservedAreas(BLOCK);
        drawDataBits(encodeData);
        return matrix;
    }


    /*
    *   step1  添加 Finder Patterns
    * */
    private void drawFinderPatterns(){
        int[][] pos = new int[][]{
                {0, 0}                         // 左上角
                ,{0, matrix[0].length - 7}     // 右上角
                ,{matrix.length - 7, 0}        // 左下角
        };
        for (int[] xy: pos) {
            int x = xy[0], y = xy[1];
            drawArea(x, y, 7, 7, BLOCK);
            drawArea(x + 1, y + 1, 5, 5, WHITE);
            drawArea(x + 2, y + 2, 3, 3, BLOCK);
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
    private void drawAlignmentPatterns(){
        int[] locations = QRTable.getAlignmentLocations(version);
        for (int i = 0; i < locations.length; i++) {
            for (int j = 0; j < locations.length; j++) {
                if (checkArea(locations[i] - 2, locations[j] - 2, 5, 5)){
                    drawArea(locations[i] - 2, locations[j] - 2, 5, 5, BLOCK);
                    drawArea(locations[i] - 1, locations[j] - 1, 3, 3, WHITE);
                    drawArea(locations[i], locations[j], 1, 1, BLOCK);
                }
            }
        }
    }

    /*
    *   Step 4: Add the Timing Patterns
    * */
    public void drawTimingPatterns(){
        int x = 8, y = 6;
        int count = matrix.length - 16;
        for (int i = 0; i < count; i++, x++) {
            int color = i % 2 == 0 ? BLOCK: WHITE;
            drawArea(x, y, color);
            drawArea(y, x, color);
        }
    }

    /*
    *   Step 5: Add the Dark Module and Reserved Areas
    * */
    private void drawDarkModuleAndReservedAreas(int color){
        // 1. add dark module
        drawArea(matrix.length - 8, 8, BLOCK);
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
    private void drawDataBits(String dataBits){
        int dataIndex = 0;
        int direction = -1;
        int x = matrix.length - 1, y = x;
        int verticalTiming = 6;
        while(y > 0){
            while(x >= 0 && x < matrix.length){
                if (matrix[x][y] == EMPTY){
                    matrix[x][y] = dataBits.charAt(dataIndex++) == '0'? WHITE: BLOCK;
                }
                if (matrix[x][y-1] == EMPTY){
                    matrix[x][y-1] = dataBits.charAt(dataIndex++) == '0'? WHITE: BLOCK;
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

}
