package com.michal.encoder;

import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Base64Encoder implements Encoder {

    @Override
    public String encode(BufferedImage image, String type) throws IOException {
        String imageString;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(image, type, bos);
        byte[] imageBytes = bos.toByteArray();
        BASE64Encoder encoder = new BASE64Encoder();
        imageString = encoder.encode(imageBytes);
        bos.close();
        return imageString;
    }
}
