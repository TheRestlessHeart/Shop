package com.example.demo.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.example.demo.dao.CommentDao;
import com.example.demo.dao.CustomerDao;
import com.example.demo.dao.GoodDao;
import com.example.demo.dao.OrdersDao;

import com.example.demo.entity.Comment;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Good;
import com.example.demo.entity.Orders;
import com.example.demo.util.MjStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by yancychan on 17-8-26.
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/comment")
public class CommentController extends BaseController {

    @Autowired
    CommentDao commentDao;
    @Autowired
    OrdersDao ordersDao;
    @Autowired
    CustomerDao customerDao;

    @PostMapping("/set")
    public JSONObject setComment(@RequestBody JSONObject jsonObject,
                                 @CookieValue(value = "token", defaultValue = "null")
                                         String token) {

        JSONObject jo = new JSONObject();

        String orderId = jsonObject.getString("order_id");
        String commentContent = jsonObject.getString("comment");
        System.out.println(commentContent);

        if (MjStringUtil.isEmpty(token)) {
            jo.put("status", 2);
            jo.put("err", "请重新登录");
        } else {
            Customer customer = customerDao.findByToken(token);
            if (customer == null) {
                jo.put("status", 2);
                jo.put("err", "请重新登录");
            } else {
                if (MjStringUtil.isEmpty(orderId)) {
                    jo.put("status", 1);
                    jo.put("err", "订单号为空");
                } else if (ordersDao.findById(orderId) == null) {
                    jo.put("status", 1);
                    jo.put("err", "订单不存在");
                } else if (MjStringUtil.isEmpty(commentContent)) {
                    jo.put("status", 1);
                    jo.put("err", "评论为空");
                } else {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date();
                        String dateForm = sdf.format(date);
                        Orders order = ordersDao.findById(orderId);
                        Comment comment = order.getComment();
                        if (comment.getComment_content() == null) {
                            comment.setComment_content(commentContent);
                            comment.setComment_date(dateForm);
                            commentDao.save(comment);
                            order.setComment(comment);
                            ordersDao.save(order);
                            jo.put("status", 0);
                        } else {
                            jo.put("status", 1);
                            jo.put("err", "你已评论过该订单");
                        }

                    } catch (NullPointerException e) {
                        System.out.println(e);
                    }
                }
            }
        }
        return jo;
    }

    @PostMapping("/get")
    public JSONObject getComment(@RequestBody JSONObject jsonObject,
                                 @CookieValue(value = "token", defaultValue = "null")
                                         String token) {

        JSONObject jo = new JSONObject();

//        String token = jsonObject.getString("token");
        String orderId = jsonObject.getString("order_id");

        if (MjStringUtil.isEmpty(token)) {
            jo.put("status", 2);
            jo.put("err", "请重新登录");
        } else {
            Customer customer = customerDao.findByToken(token);
            if (customer == null) {
                jo.put("status", 2);
                jo.put("err", "请重新登录");
            } else {
                if (MjStringUtil.isEmpty(orderId)) {
                    jo.put("status", 1);
                    jo.put("err", "订单号为空");
                } else if (ordersDao.findById(orderId) == null) {
                    jo.put("status", 1);
                    jo.put("err", "订单不存在");
                } else if (ordersDao.findById(orderId).getGood() == null) {
                    jo.put("status", 1);
                    jo.put("err", "商品不存在或已下架");
                } else {
                    Orders thisOrder = ordersDao.findById(orderId);
                    Good good = thisOrder.getGood();
                    List<Orders> orders = new ArrayList<>();
                    JSONArray array = new JSONArray();
                    Comment comment;
                    orders = ordersDao.findByGoodId(good.getGood_id());
                    for (int i = 0; i < orders.size(); i++) {
                        Orders order = ordersDao.findById(orders.get(i).getOrder_id());
                        comment = order.getComment();
                        System.out.println(comment.getComment_content());
                        try {
                            if (!comment.getComment_content().equals(null)) {
                                array.add(comment);
                            }
                        } catch (NullPointerException e) {
                            System.out.println(e);
                        }
                    }
                    jo.put("status", 0);
                    jo.put("comment", array);
                }
            }
        }
        return jo;
    }
}
