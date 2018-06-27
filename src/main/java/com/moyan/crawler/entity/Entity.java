package com.moyan.crawler.entity;

import javax.persistence.*;
import java.io.Serializable;

@javax.persistence.Entity
@Table(name = "products")
public class Entity {

    @Id
    @Column( name = "product_id" )
    private Long pid;

    @Column(name = "product_info")
    private String name;

    @Column( name = "price")
    private double price;

    @Column( name = "category")
    private String cate;

    @Column( name = "shipping")
    private boolean shipping;

    @Column( name = "coupon" )
    private boolean coupon;

    @Column( name = "special" )
    private boolean special;

    @Column( name = "discount" )
    private String discount;

    @Column( name = "producturl" )
    private String producturl;

    @Column( name = "linkedurl")
    private String linkedurl;

    @Column( name = "currency")
    private String currency;

    public Entity(){
        // Set the default values
        this.name = "";
        this.pid = 0L;
        this.cate = "";
        this.price = 0;
        this.shipping = false;
        this.coupon = false;
        this.special = false;
        this.discount = "";
        this.producturl = "";
        this.linkedurl = "";
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice(){
        return price;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public void setLinkedurl(String linkedurl) {
        this.linkedurl = linkedurl;
    }

    public String getLinkedurl() {
        return linkedurl;
    }


    public String getProducturl() {
        return producturl;
    }

    public void setProducturl(String url) {
        this.producturl = url;
    }

    public String getInfo(){
        return name;
    }

    public String getCate(){
        return cate;
    }

    public String getDiscount(){
        return discount;
    }

    //public Long getId(){return id;}

    public boolean isCoupon() {
        return coupon;
    }

    public boolean isShipping() {
        return shipping;
    }

    public boolean isSpecial() {
        return special;
    }

    public void setCate(String cate) {
        this.cate = cate;
    }

    public void setCoupon(boolean coupon) {
        this.coupon = coupon;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    //public void setId(Long id) {this.id = id;}

    public void setInfo(String name) {
        this.name = name;
    }

    public void setShipping(boolean shipping) {
        this.shipping = shipping;
    }

    public void setSpecial(boolean special) {
        this.special = special;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }
}
