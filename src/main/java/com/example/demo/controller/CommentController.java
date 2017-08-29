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
 * Created by yancychan on 17-8-26.
 */
@RestController
@RequestMapping("/comment")
public class CommentController extends BaseController {
//
//    @Autowired
//    CommentDao commentDao;
//    @Autowired
//    OrdersDao ordersDao;
//    @Autowired
//    GoodDao goodDao;
//
//    @PostMapping("/set")
//    public JSONObject submitComment(@RequestBody JSONObject jsonObject){
//
//        JSONObject jo;
//
//        String orderId = jsonObject.getString("order_id");
//        String commentDate = jsonObject.getString("comment_date");
//        String commentTitle = jsonObject.getString("comment_title");
//        String commentContent = jsonObject.getString("comment_content");
//
//        if (MjStringUtil.isEmpty(orderId)){
//            jo = RespMsgUtil.getFailResponseJoWithErrMsg("订单号为空");
//        }else if(MjStringUtil.isEmpty(commentTitle)){
//            jo = RespMsgUtil.getFailResponseJoWithErrMsg("评论内容为空");
//        }else if(ordersDao.findById(orderId) == null){
//            jo = RespMsgUtil.getFailResponseJoWithErrMsg("订单不存在");
//        }else {
//            Orders order = ordersDao.findById(orderId);
//            Comment comment = order.getComment();
//            comment.setComment_date(commentDate);
//            comment.setComment_title(commentTitle);
//            comment.setComment_content(commentContent);
//            commentDao.save(comment);
//
//            jo = RespMsgUtil.getSuccessResponseJoWithData("感谢亲的评论");
//        }
//        return jo;
//    }
//
//    @PostMapping("/get")
//    public JSONObject getComment(@RequestBody JSONObject jsonObject){
//
//        JSONObject jo;
//
//        String goodId = jsonObject.getString("good_id");
//        if (MjStringUtil.isEmpty(goodId)){
//            jo = RespMsgUtil.getFailResponseJoWithErrMsg("商品号为空");
//        }else if(goodDao.findById(goodId) == null){
//            jo = RespMsgUtil.getFailResponseJoWithErrMsg("商品不存在或已下架");
//        }else {
//            List<Orders> orders;
//            JSONArray array = new JSONArray();
//            Comment comment;
//            orders = ordersDao.findByGoodId(goodId);
//            for (int i=0; i<orders.size(); i++){
//                Orders order = ordersDao.findById(orders.get(i).getOrder_id());
//                comment = order.getComment();
//                array.add(comment);
//            }
//            jo = RespMsgUtil.getSuccessResponseJoWithData(array);
//        }
//
//        return jo;
//    }
}
