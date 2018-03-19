package com.michal.model;

public class Product {

    private String name;
    private String priceInt;
    private String priceFraction;
    private String image;

    public String getImage() {
        return image;
    }

    public Product setImage(String image) {
        this.image = image;
        return this;
    }

    public String getName() {
        return name;
    }

    public Product setName(String name) {
        this.name = name;
        return this;
    }

    public String getPriceInt() {
        return priceInt;
    }

    public Product setPriceInt(String priceInt) {
        this.priceInt = priceInt;
        return this;
    }

    public String getPriceFraction() {
        return priceFraction;
    }

    public Product setPriceFraction(String priceFraction) {
        this.priceFraction = priceFraction;
        return this;
    }

    public String toString() {
        return "\n\tname : " + name + "\n\tprice : " + priceInt + priceFraction + " zł,\n\timgUrl : " + image;
    }
}
