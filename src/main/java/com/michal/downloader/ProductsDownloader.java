package com.michal.downloader;

import com.michal.ParsingException;
import com.michal.model.Product;
import com.michal.parser.input.HtmlParser;
import me.tongfei.progressbar.ProgressBar;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ProductsDownloader {

    private int webpageCounter = 0;
    private HtmlParser parser;
    private final Logger log = LoggerFactory.getLogger(getClass());

    public ProductsDownloader(HtmlParser parser) {
        this.parser = parser;
    }

    public List<Product> downloadAllProducts(final String url, final int timeout, int depth) throws IOException {
        ProgressBar bar;

        if (depth == Integer.MAX_VALUE) {
            bar = new ProgressBar("Downloading page", 0);
        } else {
            bar = new ProgressBar("Downloading page", depth);
        }
        bar.start();

        String nextUrl = url;
        List<Product> products = new LinkedList<>();
        Connection.Response response;

        while (nextUrl != null && webpageCounter++ < depth) {
            try {
                response = Jsoup.connect(nextUrl)
                        .userAgent("Gecko/20070725 Firefox/2.0.0.6")
                        .timeout(timeout)
                        .execute();
                bar.step();
                if (response.statusCode() != 200) {
                    log.error("Could not connect to given URL, http status code: " + response.statusCode());
                    return Collections.emptyList();
                }
                Document document = response.parse();
                HtmlParser.Wrapper wrapper = parser.parseHTMLDocument(document);
                if (!wrapper.getDataSet().isEmpty()) {
                    products.addAll(wrapper.getDataSet());
                }
                nextUrl = wrapper.getNextPageURL();
            } catch (SocketTimeoutException ste) {
                log.warn("Website skipped due to timeout error, URL: " + nextUrl);
            } catch (ParsingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                bar.stop();
                throw e;
            }
        }
        bar.stop();
        return products;
    }
}