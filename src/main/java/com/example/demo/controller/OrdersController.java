package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.*;
import com.example.demo.entity.*;
import com.example.demo.util.MjStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yancychan on 17-8-30.
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/order")
public class OrdersController extends BaseController {

    @Autowired
    CustomerDao customerDao;
    @Autowired
    OrdersDao ordersDao;

    @PostMapping("/paid")
    public JSONObject paidOrders(@CookieValue(value = "token", defaultValue = "null")
                                         String token,
                                 @RequestBody JSONObject jsonObject) {
//        String token = jsonObject.getString("token");

        System.out.println(token);
        JSONObject jo = new JSONObject();

        if (MjStringUtil.isEmpty(token)) {
            jo.put("status", 2);
            jo.put("err", "请重新登录");
        } else {
            Customer customer = customerDao.findByToken(token);
            if (customer == null) {
                jo.put("status", 2);
                jo.put("err", "请重新登录");
            } else {
                JSONArray ordersArray = new JSONArray();
                String customer_id = customer.getCustomer_id();
                String customer_name = customer.getCustomer_name();
                List<Orders> ordersList = new ArrayList<>();
                ordersList = ordersDao.findByCustomerId(customer_id);
//                double cost = 0;
                for (Orders order : ordersList) {
                    //state为"1"表示订单已支付, 为"0"表示未支付
                    if (order.getOrder_state().equals("1")) {
                        JSONObject obj = new JSONObject();

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                        Good good = order.getGood();
                        Express express = order.getExpress();
                        Merchant merchant = order.getMerchant();
                        String order_id = order.getOrder_id();
                        Double order_price = order.getOrder_price();
                        String send_type = express.getExpress_type();
                        String send_price = express.getExpress_price();
                        String merchant_name = merchant.getMerchant_name();
                        String good_name = good.getGood_name();
                        String order_date = sdf.format(order.getOrder_date());
//                        cost += order_price;

                        obj.put("customer_name", customer_name);
                        obj.put("merchant_name", merchant_name);
                        obj.put("good_name", good_name);
                        obj.put("order_id", order_id);
                        obj.put("order_price", order_price);
                        obj.put("order_date", order_date);
                        obj.put("send_price", send_price);
                        obj.put("send_type", send_type);
                        ordersArray.add(obj);
                    }
//                    if (customer.getDegress() == null){
//                        customer.setDegress("1");
//                        customerDao.save(customer);
//                    }
//                    System.out.println(customer.getDegress());
//                    int originalDegress = Integer.parseInt(customer.getDegress());
//                    if (originalDegress < 10) {
//                        int currentDegress = (int) cost / (1000 * originalDegress);
//                        customer.setDegress(Integer.toString(currentDegress));
//                        customerDao.save(customer);
//                    }
                }
                jo.put("status", 0);
                jo.put("orderList", ordersArray);
            }
        }
        return jo;
    }
}
