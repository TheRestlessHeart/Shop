package com.example.demo.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by yancychan on 17-8-30.
 */
@Entity
@Table
public class Express {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "express_id", columnDefinition = "varchar(64) binary")
    private String express_id;

    @Column(name = "express_type")
    private String express_type;

    @Column(name = "express_price")
    private String express_price;

    public String getExpress_id() {
        return express_id;
    }

    public void setExpress_id(String express_id) {
        this.express_id = express_id;
    }

    public String getExpress_type() {
        return express_type;
    }

    public void setExpress_type(String epress_type) {
        this.express_type = epress_type;
    }

    public String getExpress_price() {
        return express_price;
    }

    public void setExpress_price(String express_price) {
        this.express_price = express_price;
    }
}
