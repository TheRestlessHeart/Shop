package com.example.demo.entity;

import org.hibernate.annotations.GenericGenerator;


import javax.persistence.*;

/**
 * Created by yancychan on 17-8-26.
 */

@Entity
@Table(name = "good")
public class Good {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "good_id", columnDefinition = "varchar(64) binary")
    private String good_id;

    @Column(name = "good_name")
    private String good_name;

    @Column(name = "good_type")
    private String good_type;

    @Column(name = "good_price")
    private String good_price;

    @Column(name = "good_intro")
    private String good_intro;

    @Column(name = "good_image")
    private String good_image;

    @Column(name = "good_count")
    private String good_count;

    @Column(name = "good_sales")
    private String good_sales;

    public String getGood_id() {
        return good_id;
    }

    public void setGood_id(String good_id) {
        this.good_id = good_id;
    }

    public String getGood_name() {
        return good_name;
    }

    public void setGood_name(String good_name) {
        this.good_name = good_name;
    }

    public String getGood_type() {
        return good_type;
    }

    public void setGood_type(String good_type) {
        this.good_type = good_type;
    }

    public String getGood_price() {
        return good_price;
    }

    public void setGood_price(String good_price) {
        this.good_price = good_price;
    }

    public String getGood_intro() {
        return good_intro;
    }

    public void setGood_intro(String good_intro) {
        this.good_intro = good_intro;
    }

    public String getGood_image() {
        return good_image;
    }

    public void setGood_image(String good_image) {
        this.good_image = good_image;
    }

    public String getGood_count() {
        return good_count;
    }

    public void setGood_count(String good_count) {
        this.good_count = good_count;
    }

    public String getGood_sales() {
        return good_sales;
    }

    public void setGood_sales(String good_sales) {
        this.good_sales = good_sales;
    }
}
