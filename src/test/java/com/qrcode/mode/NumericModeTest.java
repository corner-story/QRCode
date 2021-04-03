package com.qrcode.mode;


import org.junit.Test;

import static org.junit.Assert.*;


public class NumericModeTest {
    private DataMode dataMode = new NumericMode();

    @Test
    public void testCheck() {
        assertTrue(dataMode.check("012345"));
        assertFalse(dataMode.check("123abc"));
        assertTrue(dataMode.check(""));
    }

    @Test
    public void testEncode(){
        assertEquals(dataMode.encode("8675309"), "110110001110000100101001");
        assertEquals(dataMode.encode("1"), "0001");
        assertEquals(dataMode.encode("12"), "0001100");
        assertEquals(dataMode.encode("123"), "0001111011");
    }
}
