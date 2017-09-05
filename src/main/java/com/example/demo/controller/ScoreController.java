package com.example.demo.controller;

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

import java.util.List;

/**
 * Created by yancychan on 17-8-27.
 */
@CrossOrigin(origins = "*")
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
                } else if (MjStringUtil.isEmpty(score)) {
                    jo.put("status", 1);
                    jo.put("err", "评分不能为空");
                } else if (score.equals("1") || score.equals("2") || score.equals("3")
                        || score.equals("4") || score.equals("5")) {
                    Orders order = ordersDao.findById(orderId);
                    try {
                        String orders_score = order.getComment().getComment_score();
                        System.out.println(orders_score);
                        if (orders_score == null) {
                            Good good = order.getGood();
                            double originalScore = Double.parseDouble(good.getGood_score());
                            System.out.println("该物品原来的评分是:" + originalScore);
                            String goodId = good.getGood_id();
                            Comment comment = order.getComment();
                            comment.setComment_score(score);
                            System.out.println("本次订单的评分为:" + score);
                            commentDao.save(comment);
                            List<Orders> orders;
                            orders = ordersDao.findByGoodId(goodId);
                            if (originalScore == 0){
                                good.setGood_score(score);
                                goodDao.save(good);
                                System.out.println("1该物品现在的评分是:" + good.getGood_score());
                            }else {
                                double currentScore = (originalScore * (orders.size() - 1) + Integer.parseInt(score)) / orders.size();
                                good.setGood_score(Double.toString(currentScore));
                                goodDao.save(good);
                                System.out.println("2该物品现在的评分是:" + currentScore);
                            }
                            System.out.println("存入到数据库的评分是" + good.getGood_score());
                            jo.put("status", 0);
                            jo.put("score", good.getGood_score());
                        } else {
                            jo.put("status", 1);
                            jo.put("err", "您已给该订单评过分");
                        }
                    } catch (NullPointerException e) {
                        System.out.println(e);
                    }
                } else {
                    jo.put("status", 1);
                    jo.put("err", "评分错误");
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
//        String token = jsonObject.getString("token");

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
                    jo.put("err", "订单号不存在");
                } else if (ordersDao.findById(orderId).getGood() == null) {
                    jo.put("status", 1);
                    jo.put("err", "商品不存在或已下架");
                } else {
                    Orders thisOrder = ordersDao.findById(orderId);
                    Good good = thisOrder.getGood();
                    List<Orders> orders = ordersDao.findByGoodId(good.getGood_id());
                    String score = good.getGood_score();
                    System.out.println("从数据库中取出的评分是" + score);
                    jo.put("status", 0);
                    jo.put("score", score);
                    jo.put("count", orders.size());
                }
            }
        }
        return jo;
    }
}
