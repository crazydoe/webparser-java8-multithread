package com.michal.parser.input;

import com.michal.ParsingException;
import com.michal.model.Product;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CeneoParser implements HtmlParser {

    @Override
    public Wrapper parseHTMLDocument(final Document htmlDocument) throws ParsingException {
        if (!htmlDocument.head().select("meta[property=og:site_name]").attr("content").equals("Ceneo.pl")) {
            throw new ParsingException("Website not supported by Ceneo parser.");
        }

        Elements itemDivs = htmlDocument.select("div.cat-prod-row-body");
        if (itemDivs.isEmpty()) {
            itemDivs = htmlDocument.select("div.js_category-list-item");
        }

        List<Product> products = itemDivs.stream().map(singleDiv -> {
            Element titleElement = singleDiv.select("strong.cat-prod-row-name").first();
            if (titleElement == null) {
                titleElement = singleDiv.select("strong.category-item-box-name").first();
            }
            Element priceElement = singleDiv.select("span.price").first();
            Element imageElement = singleDiv.select("div.cat-prod-row-foto").first();
            if (imageElement == null) {
                imageElement = singleDiv.select("div.category-item-box-picture").first();
            }
            String imageURL = imageElement.select("img").first().attr("data-original");
            if (imageURL.isEmpty()) {
                imageURL = imageElement.select("img").first().attr("src");
            }
            return new Product()
                    .setName(titleElement.text())
                    .setPriceInt(priceElement.select("span.value").first().text())
                    .setPriceFraction(priceElement.select("span.penny").first().text())
                    .setImage("https:" + imageURL);
        }).collect(Collectors.toList());

        Wrapper wrapper = new Wrapper(products);
        parseNextPageURL(htmlDocument).ifPresent(wrapper::setNextPageURL);
        return wrapper;
    }

    @Override
    public Optional<String> parseNextPageURL(final Document htmlDocument) {
        Element next = htmlDocument
                .select("footer.category-list-footer").select("li.arrow-next")
                .first();
        if (next != null) {
            return Optional.of("https://www.ceneo.pl/" + next.select("a").first().attr("href"));
        } else {
            return Optional.empty();
        }
    }
}