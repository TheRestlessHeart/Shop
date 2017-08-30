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

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yancychan on 17-8-26.
 */
@CrossOrigin("*")
@RestController
@RequestMapping("/comment")
public class CommentController extends BaseController {

    @Autowired
    CommentDao commentDao;
    @Autowired
    OrdersDao ordersDao;
    @Autowired
    GoodDao goodDao;
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
            jo.put("status", 1);
            jo.put("err", "您尚未登录");
        } else {
            Customer customer = customerDao.findByToken(token);
            if (customer == null) {
                jo.put("status", 1);
                jo.put("err", "登录异常");
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
                    Orders order = ordersDao.findById(orderId);
                    Comment comment = order.getComment();
                    comment.setComment_content(commentContent);
                    commentDao.save(comment);
                    jo.put("status", 0);
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

        String orderId = jsonObject.getString("order_id");

        if (MjStringUtil.isEmpty(token)) {
            jo.put("status", 1);
            jo.put("err", "您尚未登录");
        } else {
            Customer customer = customerDao.findByToken(token);
            if (customer == null) {
                jo.put("status", 1);
                jo.put("err", "登录异常");
            } else {
                if (MjStringUtil.isEmpty(orderId)) {
                    jo.put("status", 1);
                    jo.put("err", "订单号为空");
                } else if (goodDao.findById(orderId) == null) {
                    jo.put("status", 1);
                    jo.put("err", "订单不存在");
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
                        array.add(comment);
                    }
                    jo.put("status", 0);
                    jo.put("comment", array);
                }
            }
        }
        return jo;
    }
}
