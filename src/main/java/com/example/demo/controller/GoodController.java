package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.*;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Good;
import com.example.demo.util.MjStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by yancychan on 17-9-1.
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/good")
public class GoodController extends BaseController{

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

    @PostMapping("/get")
    public JSONObject showGood(@RequestBody JSONObject jsonObject,
                               @CookieValue(value = "token", defaultValue = "null")
                                       String token){

//        String token = jsonObject.getString("token");
        JSONObject jo = new JSONObject();

        if (MjStringUtil.isEmpty(token)) {
            jo.put("status", 1);
            jo.put("err", "您尚未登录");
        } else {
            Customer customer = customerDao.findByToken(token);
            if (customer == null) {
                jo.put("status", 1);
                jo.put("err", "登录状态异常, 请重新登录");
            } else {
                JSONArray goodArray = new JSONArray();
                List<Good> goodList = goodDao.findAll();
                for (int i=0; i<goodList.size(); i++){
                    goodArray.add(goodList.get(i));
                }
                jo.put("status", 0);
                jo.put("good", goodArray);
            }
        }
        return jo;
    }
}
