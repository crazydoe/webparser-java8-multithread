package com.michal.downloader;

import com.michal.encoder.Encoder;
import com.michal.model.Product;
import me.tongfei.progressbar.ProgressBar;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class ImageDownloader implements Runnable {

    public static int errCounter = 0;
    private Encoder encoder;
    private Product product;
    private ProgressBar bar;

    public ImageDownloader(final Product product, final Encoder encoder, final ProgressBar bar) {
        this.encoder = encoder;
        this.product = product;
        this.bar = bar;
    }

    public Product downloadAndEncode(final Product product) {
        BufferedImage image;
        try {
            image = ImageIO.read(new URL(product.getImage()));
            product.setImage(encoder.encode(image, "jpg"));
        } catch (IOException e) {
            errCounter++;
        }
        return product;
    }

    @Override
    public void run() {
        downloadAndEncode(product);
        bar.step();
    }
}
