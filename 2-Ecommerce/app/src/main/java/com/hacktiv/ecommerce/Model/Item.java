package com.hacktiv.ecommerce.Model;

import java.io.Serializable;

public class Item implements Serializable {
    private String itemName;
    private String imgUrl;
    private String itemDescription;
    private int itemPrice;
    private int itemQuantity;

    public Item() {

    }

    public Item(String itemName, String imgUrl, String itemDescription, int itemPrice, int itemQuantity) {
        this.itemName = itemName;
        this.imgUrl = imgUrl;
        this.itemDescription = itemDescription;
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

}
