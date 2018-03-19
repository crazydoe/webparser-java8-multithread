package com.michal.parser.input;

import com.michal.ParsingException;
import com.michal.model.Product;
import org.jsoup.nodes.Document;

import java.util.List;
import java.util.Optional;

public interface HtmlParser {

    Wrapper parseHTMLDocument(Document htmlDocument) throws ParsingException;

    Optional<String> parseNextPageURL(Document htmlDocument);

    class Wrapper {
        private String nextPageURL;
        private List<Product> dataSet;

        public Wrapper(List<Product> dataSet) {
            this.dataSet = dataSet;
        }

        public String getNextPageURL() {
            return nextPageURL;
        }

        public Wrapper setNextPageURL(String nextPageURL) {
            this.nextPageURL = nextPageURL;
            return this;
        }

        public List<Product> getDataSet() {
            return dataSet;
        }
    }
}
