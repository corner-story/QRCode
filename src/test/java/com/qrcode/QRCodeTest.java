package com.qrcode;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class QRCodeTest {
    @Test
    public void testDecoder() throws Exception {
        String data = "0 1 2 3 4 5 6 7 8 9 A B C D E F G H I J K L M N O P Q R S T U V W X Y Z   $ % * + - . / :";
        String[] test = new String[]{
                "0123456789"                        // test NumericMode
                , data                               // test AlphanumericMode
                , "河北工ｙｅ大学"                      // test Kanji mode
                , "河北工业大学, Hello, World! --lambdafate" // test byte mode(UTF-8)
                , "http://www.baidu.com/"
        };
        int error = 0;
        for (int i = 0; i < test.length; i++) {
            String savePath = "./images/QRCode-" + i + ".png";
            QRCode.of()
                    .ecl(error)
                    .borderSize(0)
                    .encode(test[i])
                    .toFile(savePath, "./images/logo.png");
            assertEquals(QRCode.decode(savePath), test[i]);
        }
    }
}
