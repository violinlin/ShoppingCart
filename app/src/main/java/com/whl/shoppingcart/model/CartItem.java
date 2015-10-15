package com.whl.shoppingcart.model;

/**
 * Author:whl
 * Email:294084532@qq.com
 * 2015/10/14
 */

/**
 * 购物车条目数据，用于描述购物车一个item，也是ListView加载时候的数据对象
 */
public class CartItem {
    private long productID;
    private String productName;
    private  float productPrice;
    private String producuIcon;

//    ------------------------------------
    /**
     * 购物车条目相关
     *
     */
    private int count;
//    TODO 考虑增加其他字段
    /**
     * 代表在购物车中，当前条目是否被选中
     */
    private boolean checked;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public long getProductID() {
        return productID;
    }

    public void setProductID(long productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public float getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(float productPrice) {
        this.productPrice = productPrice;
    }

    public String getProducuIcon() {
        return producuIcon;
    }

    public void setProducuIcon(String producuIcon) {
        this.producuIcon = producuIcon;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
