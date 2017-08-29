package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.CommentDao;
import com.example.demo.dao.GoodDao;
import com.example.demo.dao.OrdersDao;
import com.example.demo.entity.Comment;
import com.example.demo.entity.Good;
import com.example.demo.entity.Orders;
import com.example.demo.util.MjStringUtil;
import com.example.demo.util.RespMsgUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by yancychan on 17-8-27.
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("")
public class ImpartController extends BaseController{

    @Autowired
    CommentDao commentDao;
    @Autowired
    OrdersDao ordersDao;
    @Autowired
    GoodDao goodDao;

    @RequestMapping("")
    public JSONObject impartController(@RequestBody JSONObject jsonObject){

        JSONObject jo = null;

        String action = jsonObject.getString("action");
        if (action.equals("score/get")){

            String goodId = jsonObject.getString("goodId");

            if (MjStringUtil.isEmpty(goodId)){
                jo = RespMsgUtil.getFailResponseJoWithErrMsg("商品号为空");
            }else if(goodDao.findById(goodId) == null){
                jo = RespMsgUtil.getFailResponseJoWithErrMsg("商品不存在或已下架");
            }else {
                List<Orders> orders;
                int avarage = 0;
                Comment comment;
                orders = ordersDao.findByGoodId(goodId);
                for (int i=0; i<orders.size(); i++){
                    Orders order = ordersDao.findById(orders.get(i).getOrder_id());
                    comment = order.getComment();
                    int score1 = Integer.parseInt(comment.getComment_score());
                    avarage += score1;
                }
                avarage /= orders.size();
                System.out.println(avarage);
                JSONObject avarageScore = new JSONObject();
                avarageScore.put("score", avarage);
                jo = RespMsgUtil.getSuccessResponseJoWithData(avarageScore);
            }
            return jo;
        }else if (action.equals("score/set")){
//            String goodId = jsonObject.getString("goodId");
            String orderId = jsonObject.getString("orderId");
            String score = jsonObject.getString("score");

            if (MjStringUtil.isEmpty(orderId)){
                jo = RespMsgUtil.getFailResponseJoWithErrMsg("订单号为空");
            }else if(ordersDao.findById(orderId) == null){
                jo = RespMsgUtil.getFailResponseJoWithErrMsg("订单不存在");
            }else {
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
                for (int i=0; i<orders.size(); i++){
                    Orders order1 = ordersDao.findById(orders.get(i).getOrder_id());
                    comment = order1.getComment();
                    int score1 = Integer.parseInt(comment.getComment_score());
                    avarage += score1;
                    System.out.println(avarage);
                }
                avarage /= orders.size();
                System.out.println(avarage);
                JSONObject avarageScore = new JSONObject();
                avarageScore.put("score", avarage);
                jo = RespMsgUtil.getSuccessResponseJoWithData(avarageScore);
            }
            return jo;
        }else if (action.equals("comment/get")){

            String goodId = jsonObject.getString("goodId");
            if (MjStringUtil.isEmpty(goodId)){
                jo = RespMsgUtil.getFailResponseJoWithErrMsg("商品号为空");
            }else if(goodDao.findById(goodId) == null){
                jo = RespMsgUtil.getFailResponseJoWithErrMsg("商品不存在或已下架");
            }else {
                List<Orders> orders;
                JSONArray array = new JSONArray();
                Comment comment;
                orders = ordersDao.findByGoodId(goodId);
                for (int i=0; i<orders.size(); i++){
                    Orders order = ordersDao.findById(orders.get(i).getOrder_id());
                    comment = order.getComment();
                    array.add(comment);
                    System.out.println(comment.getComment_content());
                }
                jo = RespMsgUtil.getSuccessResponseJoWithData(array);
            }

            return jo;
        }else if (action.equals("comment/set")){

            String orderId = jsonObject.getString("orderId");
            String commentContent = jsonObject.getString("comment");
            System.out.println(commentContent);

            if (MjStringUtil.isEmpty(orderId)){
                jo = RespMsgUtil.getFailResponseJoWithErrMsg("订单号为空");
            }else if(ordersDao.findById(orderId) == null){
                jo = RespMsgUtil.getFailResponseJoWithErrMsg("订单不存在");
            }else {
                Orders order = ordersDao.findById(orderId);
                Comment comment = order.getComment();
                comment.setComment_content(commentContent);
                commentDao.save(comment);
                JSONObject reply = new JSONObject();
                reply.put("data", "感谢亲的评论");
                jo = RespMsgUtil.getSuccessResponseJoWithData(comment);
            }
            return jo;
        }else {
            jo = RespMsgUtil.getFailResponseJoWithErrMsg("action出错");
        }
        return jo;
    }
}
