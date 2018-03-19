package com.michal.encoder;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.testng.Assert;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@RunWith(JUnit4.class)
public class Base64EncoderTest {

    @Test
    public void encoderTest() throws IOException {
        //.....given
        ClassLoader classLoader = getClass().getClassLoader();
        File input = new File(classLoader.getResource("encoderTestImage.jpeg").getFile());
        BufferedImage image = ImageIO.read(input);
        String expected = FileUtils.readFileToString(
                new File(classLoader.getResource("encoderExpectedHash").getFile()),
                "utf-8"
        );

        //......when
        String base64Coded = new Base64Encoder().encode(image, "jpg");

        //......then
        Assert.assertEquals(base64Coded, expected);
    }
}