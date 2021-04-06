package com.qrcode.reedsolo;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RSCodeTest {

//    public static void main(String[] args) {
//        int[] toEncode = new int[]{
//                32, 91, 11, 120, 209, 114, 220, 77, 67, 64, 236, 17, 236, 17, 236, 17
//                ,0, 0, 0, 0, 0, 0, 0, 0, 0, 0
//        };
//        ReedSolomonEncoder reedSolomonEncoder = new ReedSolomonEncoder(GenericGF.QR_CODE_FIELD_256);
//        reedSolomonEncoder.encode(toEncode, 10);
//        for (int i = 0; i < 16; i++) {
//            System.out.print(toEncode[i] + ", ");
//        }
//        System.out.println("");
//        for (int i = 16; i < toEncode.length; i++) {
//            System.out.print(toEncode[i] + ", ");
//        }
//    }

    @Test
    public void testEncode() {
        int[] excepeted = new int[]{213, 199, 11, 45, 115, 247, 241, 223, 229, 248, 154, 117, 154, 111, 86, 161, 111, 39};
        int[] testData = new int[]{67, 85, 70, 134, 87, 38, 85, 194, 119, 50, 6, 18, 6, 103, 38};
        int[] result = RSCode.encode(testData, 18);
        for (int i = 0; i < excepeted.length; i++) {
            assertEquals(result[i], excepeted[i]);
        }
    }

}
