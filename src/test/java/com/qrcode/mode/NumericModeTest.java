package com.qrcode.mode;


import org.junit.Test;

import static org.junit.Assert.*;


public class NumericModeTest {
    private DataMode dataMode = new NumericMode();

    @Test
    public void testCheck() {
        assertTrue(dataMode.checkData("012345"));
        assertFalse(dataMode.checkData("123abc"));
        assertTrue(dataMode.checkData(""));
    }

    @Test
    public void testEncodeData() {
        assertEquals(dataMode.encodeData("8675309"), "110110001110000100101001");
        assertEquals(dataMode.encodeData("1"), "0001");
        assertEquals(dataMode.encodeData("12"), "0001100");
        assertEquals(dataMode.encodeData("123"), "0001111011");
    }

    @Test
    public void testGetBestVersion() throws Exception {
        assertEquals(dataMode.getBestVersion(0, 101), 3);
        assertEquals(dataMode.getBestVersion(0, 76), 2);
        assertEquals(dataMode.getBestVersion(3, 2700), 38);
    }

    @Test
    public void testGetDataCodewords() throws Exception {
        assertEquals(dataMode.getDataCodewords("01234567", 1, 3), "000100000010000000001100010101100110000110000000111011000001000111101100");
    }
}
