package com.qrcode.tool;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class QRTableTest {

    @Test
    public void testGetMaxBits() {
        assertEquals(QRTable.getMaxBits(1, 2), 104);
        assertEquals(QRTable.getMaxBits(7, 1), 124 * 8);
    }

    @Test
    public void testGetAllBlocks() {
        int[][] expected = new int[][]{
                {67, 85, 70, 134, 87, 38, 85, 194, 119, 50, 6, 18, 6, 103, 38},
                {246, 246, 66, 7, 118, 134, 242, 7, 38, 86, 22, 198, 199, 146, 6},
                {182, 230, 247, 119, 50, 7, 118, 134, 87, 38, 82, 6, 134, 151, 50, 7},
                {70, 247, 118, 86, 194, 6, 151, 50, 16, 236, 17, 236, 17, 236, 17, 236}
        };
        int[] testData = new int[]{
                67, 85, 70, 134, 87, 38, 85, 194, 119, 50, 6, 18, 6, 103, 38
                , 246, 246, 66, 7, 118, 134, 242, 7, 38, 86, 22, 198, 199, 146, 6
                , 182, 230, 247, 119, 50, 7, 118, 134, 87, 38, 82, 6, 134, 151, 50, 7
                , 70, 247, 118, 86, 194, 6, 151, 50, 16, 236, 17, 236, 17, 236, 17, 236
        };
        int[][] result = QRTable.getAllBlocks(testData, 5, 2);
        for (int i = 0; i < expected.length; i++) {
            for (int j = 0; j < expected[i].length; j++) {
                assertEquals(result[i][j], expected[i][j]);
            }
        }
    }

    @Test
    public void testInterleaveBlocks() {
        int[] expected = new int[]{67, 246, 182, 70, 85, 246, 230, 247, 70, 66, 247, 118, 134, 7, 119, 86, 87, 118, 50, 194, 38, 134, 7, 6, 85, 242, 118, 151, 194, 7, 134, 50, 119, 38, 87, 16, 50, 86, 38, 236, 6, 22, 82, 17, 18, 198, 6, 236, 6, 199, 134, 17, 103, 146, 151, 236, 38, 6, 50, 17, 7, 236};
        int[][] testData = new int[][]{
                {67, 85, 70, 134, 87, 38, 85, 194, 119, 50, 6, 18, 6, 103, 38},
                {246, 246, 66, 7, 118, 134, 242, 7, 38, 86, 22, 198, 199, 146, 6},
                {182, 230, 247, 119, 50, 7, 118, 134, 87, 38, 82, 6, 134, 151, 50, 7},
                {70, 247, 118, 86, 194, 6, 151, 50, 16, 236, 17, 236, 17, 236, 17, 236}
        };
        int[] result = QRTable.InterleaveBlocks(testData);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], result[i]);
        }
    }

    @Test
    public void testConvertToBinaryByArray() {
        String expected = "0100001111110110101101100100011001010101111101101110011011110111010001100100001011110111011101101000011000000111011101110101011001010111011101100011001011000010001001101000011000000111000001100101010111110010011101101001011111000010000001111000011000110010011101110010011001010111000100000011001001010110001001101110110000000110000101100101001000010001000100101100011000000110111011000000011011000111100001100001000101100111100100101001011111101100001001100000011000110010000100010000011111101100110101010101011110010100111010111100011111001100011101001001111100001011011000001011000100000101001011010011110011010100101011010111001111001010010011000001100011110111101101101000010110010011111100010111110001001011001110111101111110011101111100100010000111100101110010001110111001101010111110001000011001001100001010001001101000011011110000111111111101110101100000011110011010101100100110101101000110111101010100100110111100010001000010100000001001010110101000110110110010000011101000011010001111110000001000000110111101111000110000001011001000100111100001011000110111101100";
        int[] testData = new int[]{
                67, 246, 182, 70, 85, 246, 230, 247, 70, 66, 247, 118, 134, 7, 119, 86, 87, 118, 50, 194, 38, 134, 7, 6, 85, 242, 118, 151, 194, 7, 134, 50, 119, 38, 87, 16, 50, 86, 38, 236, 6, 22, 82, 17, 18, 198, 6, 236, 6, 199, 134, 17, 103, 146, 151, 236, 38, 6, 50, 17, 7, 236, 213, 87, 148, 235, 199, 204, 116, 159, 11, 96, 177, 5, 45, 60, 212, 173, 115, 202, 76, 24, 247, 182, 133, 147, 241, 124, 75, 59, 223, 157, 242, 33, 229, 200, 238, 106, 248, 134, 76, 40, 154, 27, 195, 255, 117, 129, 230, 172, 154, 209, 189, 82, 111, 17, 10, 2, 86, 163, 108, 131, 161, 163, 240, 32, 111, 120, 192, 178, 39, 133, 141, 236
        };
        String result = BinaryConvert.convertToBinary(testData, 8);
        assertEquals(result, expected);
    }
}
