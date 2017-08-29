package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.CommentDao;
import com.example.demo.dao.GoodDao;
import com.example.demo.dao.OrdersDao;
import com.example.demo.entity.Comment;
import com.example.demo.entity.Orders;
import com.example.demo.util.MjStringUtil;
import com.example.demo.util.RespMsgUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by yancychan on 17-8-27.
 */
@RestController
@RequestMapping("/score")
public class ScoreController extends BaseController{

//    @Autowired
//    CommentDao commentDao;
//    @Autowired
//    OrdersDao ordersDao;
//    @Autowired
//    GoodDao goodDao;
//
//    @PostMapping("/get")
//    public JSONObject getScore(@RequestBody JSONObject jsonObject){
//
//        JSONObject jo;
//
//        String goodId = jsonObject.getString("goodId");
//
//        if (MjStringUtil.isEmpty(goodId)){
//            jo = RespMsgUtil.getFailResponseJoWithErrMsg("商品号为空");
//        }else if(goodDao.findById(goodId) == null){
//            jo = RespMsgUtil.getFailResponseJoWithErrMsg("商品不存在或已下架");
//        }else {
//            List<Orders> orders;
//            int avarage = 0;
//            Comment comment;
//            orders = ordersDao.findByGoodId(goodId);
//            for (int i=0; i<orders.size(); i++){
//                Orders order = ordersDao.findById(orders.get(i).getOrder_id());
//                comment = order.getComment();
//                int score1 = Integer.parseInt(comment.getComment_score());
//                avarage += score1;
//                avarage /= (i+1);
//            }
//            jo = RespMsgUtil.getSuccessResponseJoWithData(avarage);
//        }
//        return jo;
//    }
//
//    @PostMapping("/set")
//    public JSONObject setScore(@RequestBody JSONObject jsonObject){
//
//        JSONObject jo;
//
//        String goodId = jsonObject.getString("goodId");
//        String orderId = jsonObject.getString("orderId");
//        String score = jsonObject.getString("score");
//
//
//        if (MjStringUtil.isEmpty(orderId)){
//            jo = RespMsgUtil.getFailResponseJoWithErrMsg("订单号为空");
//        }else if (MjStringUtil.isEmpty(goodId)){
//            jo = RespMsgUtil.getFailResponseJoWithErrMsg("商品号为空");
//        }else if(goodDao.findById(goodId) == null){
//            jo = RespMsgUtil.getFailResponseJoWithErrMsg("商品不存在或已下架");
//        }else if(ordersDao.findById(orderId) == null){
//            jo = RespMsgUtil.getFailResponseJoWithErrMsg("订单不存在");
//        }else {
//            Orders order = ordersDao.findById(orderId);
//            Comment comment = order.getComment();
//            comment.setComment_score(score);
//            List<Orders> orders;
//            int avarage = 0;
//            orders = ordersDao.findByGoodId(goodId);
//            for (int i=0; i<orders.size(); i++){
//                Orders order1 = ordersDao.findById(orders.get(i).getOrder_id());
//                comment = order1.getComment();
//                int score1 = Integer.parseInt(comment.getComment_score());
//                avarage += score1;
//                avarage /= (i+1);
//            }
//            jo = RespMsgUtil.getSuccessResponseJoWithData(avarage);
//        }
//        return jo;
//    }


}
