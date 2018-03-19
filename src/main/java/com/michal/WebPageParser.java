package com.michal;

import com.michal.downloader.ImageDownloader;
import com.michal.downloader.ProductsDownloader;
import com.michal.encoder.Base64Encoder;
import com.michal.encoder.Encoder;
import com.michal.enums.EncoderType;
import com.michal.enums.InputType;
import com.michal.enums.OutputType;
import com.michal.model.Product;
import com.michal.parser.input.CeneoParser;
import com.michal.parser.input.HtmlParser;
import com.michal.parser.output.JsonPojoParser;
import com.michal.parser.output.PojoParser;
import com.michal.parser.output.XmlPojoParser;
import me.tongfei.progressbar.ProgressBar;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WebPageParser {

    private ProductsDownloader productsDownloader;
    private PojoParser outputParser;
    private Encoder encoder;

    private WebPageParser(final Encoder encoder, final HtmlParser inputParser, final PojoParser outputParser) {
        this.outputParser = outputParser;
        this.encoder = encoder;
        productsDownloader = new ProductsDownloader(inputParser);
    }

    public String getParsedProducts(final String sourceUrl, final Integer pageTimeout,
                                    final Integer searchDepth) throws Exception {

        List<Product> products = productsDownloader.downloadAllProducts(sourceUrl, pageTimeout, searchDepth);

        ProgressBar bar = new ProgressBar("Downloading images", products.size());

        bar.start();

        Set<Thread> threads = new HashSet<>();
        products.forEach(product -> {
            Thread thread = new Thread(new ImageDownloader(product, encoder, bar));
            thread.start();
            threads.add(thread);

            if (threads.size() == 50) {
                // why? because 50 running threads at one time gives the best results..
                // at least for me...
                joinThreads(threads);
            }
        });

        bar.stop();
        return outputParser.parsePOJO(products);
    }

    private void joinThreads(final Set<Thread> threads) {
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        threads.clear();
    }

    public static class WebPageParserBuilder {
        public static WebPageParser buildWebPageParser(final InputType inType, final OutputType outType,
                                                       final EncoderType encoderType) {
            Encoder encoder = null;
            HtmlParser inputParser = null;
            PojoParser outputParser = null;

            switch (inType) {
                case CENEO:
                    inputParser = new CeneoParser();
                    break;
            }

            switch (outType) {
                case XML:
                    outputParser = new XmlPojoParser();
                    break;
                case JSON:
                    outputParser = new JsonPojoParser();
                    break;
            }

            switch (encoderType) {
                case BASE64:
                    encoder = new Base64Encoder();
                    break;
            }

            return new WebPageParser(encoder, inputParser, outputParser);
        }
    }


}
