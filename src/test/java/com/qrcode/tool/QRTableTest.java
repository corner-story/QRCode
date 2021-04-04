package com.qrcode.tool;

import org.junit.Test;
import static org.junit.Assert.*;

public class QRTableTest {

    @Test
    public void testGetMaxBits(){
        assertEquals(QRTable.getMaxBits(1, 2), 104);
        assertEquals(QRTable.getMaxBits(7, 1), 124 * 8);
    }
}
