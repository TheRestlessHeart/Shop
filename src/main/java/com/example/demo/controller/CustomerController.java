package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;

import com.example.demo.dao.CustomerDao;
import com.example.demo.entity.Customer;
import com.example.demo.util.RespMsgUtil;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

/**
 * Created by yancychan on 17-8-26.
 */
@RestController
@RequestMapping("/customer")
public class CustomerController extends BaseController {

    @Autowired
    CustomerDao customerDao;

    @PostMapping("/register")
    public JSONObject register(@RequestBody JSONObject jsonObject){

        JSONObject jo;

        String customerName = jsonObject.getString("customer_name");
        Customer customer = new Customer();
        customer.setCustomer_name(customerName);
        customer.setCustomer_password("123123123");
        customer.setAddress("北京科技大学");
        customer.setDegress("100");
        customer.setEmail("1@163.com");
        customer.setPhone("10086");
        customerDao.save(customer);

        jo = RespMsgUtil.getSuccessResponseJo();
        return jo;
    }
}
