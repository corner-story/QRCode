package com.qrcode.mode;

import java.util.ArrayList;
import java.util.List;

public class DataAnalysis {
    private static List<DataMode> dataModes = new ArrayList<DataMode>();
    static {
        dataModes.add(new NumericMode());
        dataModes.add(new AlphanumericMode());
    }

    public static DataMode selectMode(String data) throws Exception{
        for (DataMode dataMode: dataModes) {
            if (dataMode.checkData(data)){
                return dataMode;
            }
        }
        throw new Exception("select mode error");
    }
}
