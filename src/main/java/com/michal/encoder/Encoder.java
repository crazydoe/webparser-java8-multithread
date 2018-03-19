package com.michal.encoder;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface Encoder {

    String encode(BufferedImage image, String type) throws IOException;

}
