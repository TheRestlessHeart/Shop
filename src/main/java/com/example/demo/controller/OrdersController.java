package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.CustomerDao;
import com.example.demo.dao.GoodDao;
import com.example.demo.dao.MerchantDao;
import com.example.demo.dao.OrdersDao;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Good;
import com.example.demo.entity.Merchant;
import com.example.demo.entity.Orders;
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
                        Merchant merchant = order.getMerchant();
                        String order_id = order.getOrder_id();
                        String order_price = order.getOrder_price();
                        String send_type = order.getSend_type();
                        String send_price = order.getSend_price();
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
        System.out.println(token);
        JSONObject jo = new JSONObject();

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
                }else {
                    String good_count = good.getGood_count();
                    if (good_count.equals(0)){
                        jo.put("status", 1);
                        jo.put("err", "商品已售光");
                    }else {
                        Date date = new Date();
                        Orders order = new Orders();
                        order.setOrder_date(date);
                        order.setCustomer(customer);
                        order.setGood(good);
                        order.setMerchant(merchant);

                    }
                }
            }
        }
        return jo;
    }
}
