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
import com.example.demo.util.RespMsgUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by yancychan on 17-8-27.
 */
@RestController
@RequestMapping("/score")
public class ScoreController extends BaseController {

    @Autowired
    CommentDao commentDao;
    @Autowired
    OrdersDao ordersDao;
    @Autowired
    GoodDao goodDao;
    @Autowired
    CustomerDao customerDao;

    @PostMapping("/set")
    public JSONObject setScore(@RequestBody JSONObject jsonObject,
                               @CookieValue(value = "token", defaultValue = "null")
                                       String token) {

        JSONObject jo = new JSONObject();

        String orderId = jsonObject.getString("order_id");
        String score = jsonObject.getString("score");

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
                } else if (MjStringUtil.isEmpty(score)) {
                    jo.put("status", 1);
                    jo.put("err", "评分为空");
                } else {
                    Orders order = ordersDao.findById(orderId);
                    Good good = order.getGood();
                    String goodId = good.getGood_id();
                    Comment comment = order.getComment();
                    comment.setComment_score(score);
                    commentDao.save(comment);
                    List<Orders> orders;
                    int avarage = 0;
                    orders = ordersDao.findByGoodId(goodId);
                    System.out.println(orders.size());
                    for (int i = 0; i < orders.size(); i++) {
                        Orders order1 = ordersDao.findById(orders.get(i).getOrder_id());
                        comment = order1.getComment();
                        int score1 = Integer.parseInt(comment.getComment_score());
                        avarage += score1;
                        System.out.println(avarage);
                    }
                    avarage /= orders.size();
                    System.out.println(avarage);
                    jo.put("status", 0);
                    jo.put("score", avarage);
                }
            }
        }
        return jo;
    }

    @PostMapping("/get")
    public JSONObject getScore(@RequestBody JSONObject jsonObject,
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
                    jo.put("err", "商品号为空");
                } else if (goodDao.findById(orderId) == null) {
                    jo.put("status", 1);
                    jo.put("err", "商品不存在或已下架");
                } else {
                    Orders thisOrder = ordersDao.findById(orderId);
                    Good good = thisOrder.getGood();
                    List<Orders> orders;
                    int avarage = 0;
                    Comment comment;
                    orders = ordersDao.findByGoodId(good.getGood_id());
                    for (int i = 0; i < orders.size(); i++) {
                        Orders order = ordersDao.findById(orders.get(i).getOrder_id());
                        comment = order.getComment();
                        int score1 = Integer.parseInt(comment.getComment_score());
                        avarage += score1;
                    }
                    avarage /= orders.size();
                    System.out.println(avarage);
                    jo.put("status", 0);
                    jo.put("score", avarage);
                }
            }
        }
        return jo;
    }
}
