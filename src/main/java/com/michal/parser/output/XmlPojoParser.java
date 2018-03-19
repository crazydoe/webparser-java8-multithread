package com.michal.parser.output;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.michal.ParsingException;
import com.michal.model.Product;

import java.util.List;

public class XmlPojoParser implements PojoParser {

    public String parsePOJO(final List<Product> products) throws ParsingException {

        XmlMapper xmlMapper = new XmlMapper();
        try {
            return xmlMapper.writeValueAsString(new XMLWrapper(products));
        } catch (JsonProcessingException e) {
            throw new ParsingException(e.getMessage());
        }
    }


    @JacksonXmlRootElement(localName = "products")
    private class XMLWrapper {
        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "product")
        private List<Product> products;

        public XMLWrapper(List<Product> products) {
            this.products = products;
        }

        public List<Product> getProducts() {
            return products;
        }

        public XMLWrapper setProducts(List<Product> products) {
            this.products = products;
            return this;
        }
    }
}
