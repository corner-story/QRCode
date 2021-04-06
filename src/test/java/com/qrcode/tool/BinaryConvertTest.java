package com.qrcode.tool;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BinaryConvertTest {

    @Test
    public void testConvertToBinary() {
        assertEquals(BinaryConvert.convertToBinary(867, 10), "1101100011");
        assertEquals(BinaryConvert.convertToBinary(530, 10), "1000010010");
        assertEquals(BinaryConvert.convertToBinary(9, 4), "1001");
        assertEquals(BinaryConvert.convertToBinary(9, 5), "01001");
    }

    @Test
    public void testConvertToInt() {
        assertEquals(BinaryConvert.convertToInt("1101100011"), 867);
        assertEquals(BinaryConvert.convertToInt("1000010010"), 530);
        assertEquals(BinaryConvert.convertToInt("1001"), 9);
        assertEquals(BinaryConvert.convertToInt("01001"), 9);
    }

    @Test
    public void testSplitStringToIntArray() {
        String bits = "010000110101010101000110100001100101011100100110010101011100001001110111001100100000011000010010000001100110011100100110";
        int[] expected = new int[]{67, 85, 70, 134, 87, 38, 85, 194, 119, 50, 6, 18, 6, 103, 38};
        int[] result = BinaryConvert.splitStringToIntArray(bits, 8);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(result[i], expected[i]);
        }
    }
}
