package com.qrcode.reedsolo;

public final class RSCode {
    private static final ReedSolomonEncoder RSEncoder = new ReedSolomonEncoder(GenericGF.QR_CODE_FIELD_256);

    // 对data进行编码, 获取rsbytes位纠错码
    public static int[] encode(int[] data, int rsbytes){
        int[] toEncode = new int[data.length + rsbytes];
        System.arraycopy(data, 0, toEncode, 0, data.length);
        RSEncoder.encode(toEncode, rsbytes);
        int[] rsData = new int[rsbytes];
        System.arraycopy(toEncode, data.length, rsData, 0, rsbytes);
        return rsData;
    }
}
