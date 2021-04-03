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
}
