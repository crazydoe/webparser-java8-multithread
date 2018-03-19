package com.michal.parser.output;

import com.michal.ParsingException;
import com.michal.model.Product;

import java.util.List;

/**
 * Created by michal on 16.03.2018.
 */
public interface PojoParser {

    String parsePOJO(List<Product> products) throws ParsingException;

}
