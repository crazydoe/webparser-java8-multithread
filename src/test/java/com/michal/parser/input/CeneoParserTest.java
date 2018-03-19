package com.michal.parser.input;

import com.michal.ParsingException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.testng.Assert;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@RunWith(JUnit4.class)
public class CeneoParserTest {

    @Test
    public void parseHTMLDocumentForListTest() throws IOException, ParsingException {
        //......given
        ClassLoader classLoader = getClass().getClassLoader();
        File input = new File(classLoader.getResource("testCeneoHtmlList.html").getFile());
        Document webpageList = Jsoup.parse(input, "UTF-8", "https://www.ceneo.pl/");

        //......when
        HtmlParser.Wrapper products = new CeneoParser().parseHTMLDocument(webpageList);

        //......then
        Assert.assertEquals(products.getDataSet().size(), 30);
        products.getDataSet().forEach(product -> {
            Assert.assertNotNull(product);
            Assert.assertNotNull(product.getName());
            Assert.assertNotNull(product.getPriceFraction());
            Assert.assertNotNull(product.getPriceInt());
            Assert.assertNotNull(product.getImage());
        });
    }

    @Test
    public void parseHTMLDocumentForGridTest() throws IOException, ParsingException {
        //......given
        ClassLoader classLoader = getClass().getClassLoader();
        File input = new File(classLoader.getResource("testCeneoHtmlGrid.html").getFile());
        Document webpageGrid = Jsoup.parse(input, "UTF-8", "https://www.ceneo.pl/");

        //......when
        HtmlParser.Wrapper products = new CeneoParser().parseHTMLDocument(webpageGrid);

        //......then
        Assert.assertEquals(products.getDataSet().size(), 30);
        products.getDataSet().forEach(product -> {
            Assert.assertNotNull(product);
            Assert.assertNotNull(product.getName());
            Assert.assertNotNull(product.getPriceInt());
            Assert.assertNotNull(product.getPriceFraction());
            Assert.assertNotNull(product.getImage());
        });
    }

    @Test(expected = ParsingException.class)
    public void parseHTMLDocumentForOtherPageThanCeneoTest() throws IOException, ParsingException {
        //......given
        ClassLoader classLoader = getClass().getClassLoader();
        File input = new File(classLoader.getResource("notCeneoPage.html").getFile());
        Document webpageGrid = Jsoup.parse(input, "UTF-8", "https://www.test.pl/");

        //......when
        CeneoParser ceneoParser = new CeneoParser();

        //......then
        ceneoParser.parseHTMLDocument(webpageGrid);

    }

    @Test
    public void parseNextPageURLTest() throws IOException, ParsingException {
        //......given
        ClassLoader classLoader = getClass().getClassLoader();
        File input = new File(classLoader.getResource("testCeneoHtmlList.html").getFile());
        Document webpageList = Jsoup.parse(input, "UTF-8", "https://www.ceneo.pl/");

        //......when
        Optional<String> nextUrl = new CeneoParser().parseNextPageURL(webpageList);

        //......then
        Assert.assertTrue(nextUrl.isPresent());
        nextUrl.ifPresent(s ->
                Assert.assertEquals(s, "https://www.ceneo.pl//Filmy_Blu-ray/Gatunek:Sensacyjne;0020-30-0-0-1.htm")
        );
    }

}