package com.qrcode.mode;

import com.qrcode.tool.BinaryConvert;

public class NumericMode extends DataMode {

    public NumericMode() {
        this.ModeIndicator = "0001";
        this.CharacterCapacities = new int[]{
                41, 34, 27, 17, 77, 63, 48, 34, 127, 101, 77, 58, 187, 149, 111, 82
                ,255, 202, 144, 106, 322, 255, 178, 139, 370, 293, 207, 154, 461, 365, 259, 202
                ,552, 432, 312, 235, 652, 513, 364, 288, 772, 604, 427, 331, 883, 691, 489, 374
                ,1022, 796, 580, 427, 1101, 871, 621, 468, 1250, 991, 703, 530, 1408, 1082, 775, 602
                ,1548, 1212, 876, 674, 1725, 1346, 948, 746, 1903, 1500, 1063, 813, 2061, 1600, 1159, 919
                ,2232, 1708, 1224, 969, 2409, 1872, 1358, 1056, 2620, 2059, 1468, 1108, 2812, 2188, 1588, 1228
                ,3057, 2395, 1718, 1286, 3283, 2544, 1804, 1425, 3517, 2701, 1933, 1501, 3669, 2857, 2085, 1581
                ,3909, 3035, 2181, 1677, 4158, 3289, 2358, 1782, 4417, 3486, 2473, 1897, 4686, 3693, 2670, 2022
                ,4965, 3909, 2805, 2157, 5253, 4134, 2949, 2301, 5529, 4343, 3081, 2361, 5836, 4588, 3244, 2524
                ,6153, 4775, 3417, 2625, 6479, 5039, 3599, 2735, 6743, 5313, 3791, 2927, 7089, 5596, 3993, 3057
        };
    }

    @Override
    protected boolean checkData(String data) {
        for (int i = 0; i < data.length(); i++) {
            if (!Character.isDigit(data.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected String encodeData(String data) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length(); i = i + 3) {
            String tmp = data.substring(i, Math.min(i + 3, data.length()));
            int bit = tmp.length() == 3 ? 10: (tmp.length() == 2 ? 7: 4);
            sb.append(BinaryConvert.convertToBinary(Integer.valueOf(tmp), bit));
        }
        return sb.toString();
    }

    @Override
    protected int getCharacterCountIndicatorBits(int version) {
        if (version <= 9){
            return 10;
        }else if (version <= 26){
            return 12;
        }
        return 14;
    }
}