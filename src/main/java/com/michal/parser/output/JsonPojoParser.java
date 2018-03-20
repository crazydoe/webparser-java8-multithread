package com.michal.parser.output;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.michal.ParsingException;
import com.michal.model.Product;

import java.util.List;

public class JsonPojoParser implements PojoParser {

    @Override
    public String parsePOJO(final List<Product> products) throws ParsingException {
        String json;
        try {
            json = new ObjectMapper().writeValueAsString(products);
        } catch (JsonProcessingException e) {
            throw new ParsingException(e.getMessage());
        }
        return json;
    }

}
