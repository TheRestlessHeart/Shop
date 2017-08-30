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
import java.util.Date;
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
    @Autowired
    GoodDao goodDao;
    @Autowired
    MerchantDao merchantDao;
    @Autowired
    ExpressDao expressDao;

    @PostMapping("/paid")
    public JSONObject paidOrders(@CookieValue(value = "token", defaultValue = "null")
                                             String token) {

        System.out.println(token);
        JSONObject jo = new JSONObject();

        if (MjStringUtil.isEmpty(token)) {
            jo.put("status", 1);
            jo.put("err", "您尚未登录");
        } else {
            Customer customer = customerDao.findByToken(token);
            if (customer == null) {
                jo.put("status", 1);
                jo.put("err", "登录异常");
            } else {
                JSONArray ordersArray = new JSONArray();
                String customer_id = customer.getCustomer_id();
                String customer_name = customer.getCustomer_name();
                List<Orders> ordersList = new ArrayList<>();
                ordersList = ordersDao.findByCustomerId(customer_id);

                for (Orders order : ordersList) {
                    //state为"1"表示订单已支付, 为"0"表示未支付
                    if (order.getOrder_state().equals("1")) {
                        JSONObject obj = new JSONObject();

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                        Good good = order.getGood();
                        Express express = order.getExpress();
                        Merchant merchant = order.getMerchant();
                        String order_id = order.getOrder_id();
                        String order_price = order.getOrder_price();
                        String send_type = express.getExpress_type();
                        String send_price = express.getExpress_price();
                        String merchant_name = merchant.getMerchant_name();
                        String good_name = good.getGood_name();
                        String order_date = sdf.format(order.getOrder_date());

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
                }
                jo.put("status", 0);
                jo.put("orderList", ordersArray);
            }
        }
        return jo;
    }

    @PostMapping("/generate")
    public JSONObject generateOrder(@RequestBody JSONObject jsonObject,
                                    @CookieValue(value = "token", defaultValue = "null")
                                            String token){

        JSONObject jo = new JSONObject();

        System.out.println(token);

        String counts = jsonObject.getString("good_count");
        String send_id = jsonObject.getString("send_id");
        String merchant_id = jsonObject.getString("merchant_id");
        String good_id = jsonObject.getString("good_id");
        Good good = goodDao.findById(good_id);
        Merchant merchant = merchantDao.findById(merchant_id);
        if (MjStringUtil.isEmpty(token)) {
            jo.put("status", 1);
            jo.put("err", "您尚未登录");
        } else {
            Customer customer = customerDao.findByToken(token);
            if (customer == null) {
                jo.put("status", 1);
                jo.put("err", "登录异常");
            } else {
                if (MjStringUtil.isEmpty(good_id) || good == null){
                    jo.put("status", 1);
                    jo.put("err", "商品不存在");
                }else if (MjStringUtil.isEmpty(merchant_id) || merchant == null){
                    jo.put("status", 1);
                    jo.put("err", "商店不存在");
                }else if(MjStringUtil.isEmpty(counts) || Integer.parseInt(counts) <= 0){
                    jo.put("status", 1);
                    jo.put("err", "请您选择购买数量");
                }else if (MjStringUtil.isEmpty(send_id)){
                    jo.put("status", 1);
                    jo.put("err", "请您选择运输方式");
                }
                else {
                    int count = Integer.parseInt(counts);
                    String good_count = good.getGood_count();
                    int current_count = Integer.parseInt(good_count);
                    if (good_count.equals(0)){
                        jo.put("status", 1);
                        jo.put("err", "商品已售光");
                    }else {
                        Date date = new Date();
                        Express express = expressDao.findById(send_id);
                        float price;
                        if (Float.parseFloat(good.getGood_price()) * count < 100){
                            price = Float.parseFloat(good.getGood_price()) * count +
                                    Float.parseFloat(express.getExpress_price());
                        }else {
                            price = Float.parseFloat(good.getGood_price()) * count;
                        }
                        Orders order = new Orders();
                        order.setOrder_date(date);
                        order.setCustomer(customer);
                        order.setGood(good);
                        order.setMerchant(merchant);
                        order.setExpress(express);
                        order.setOrder_price(Float.toString(price));
                        order.setOrder_state("0");
                        ordersDao.save(order);
                        int restCount = current_count - count;
                        good.setGood_count(Integer.toString(restCount));

                        jo.put("status", 0);
                        jo.put("order", order);
                    }
                }
            }
        }
        return jo;
    }
}
