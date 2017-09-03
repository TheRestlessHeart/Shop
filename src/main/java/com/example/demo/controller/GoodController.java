package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.*;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Good;
import com.example.demo.util.MjStringUtil;
import com.example.demo.util.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yancychan on 17-9-1.
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/good")
public class GoodController extends BaseController {

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
    public JSONObject showGood() {
        JSONObject jo = new JSONObject();

        JSONArray goodArray = new JSONArray();
        List<Good> goodList = goodDao.findAll();
        for (int i = 0; i < goodList.size(); i++) {
            goodArray.add(goodList.get(i));
        }
        jo.put("status", 0);
        jo.put("good", goodArray);

        return jo;
    }

    @PostMapping("/type")
    public JSONObject findByType(@RequestBody JSONObject jsonObject){

        JSONObject jo = null;
        String type = jsonObject.getString("type");
        List<Good> goodList = new ArrayList<>();

        if (MjStringUtil.isEmpty(type)){
            jo.put("status", 1);
            jo.put("err", "类型为空");
        }else if (!Type.isInclude(type)){
            jo.put("status", 1);
            jo.put("err", "该类型不存在");
        }else {
            goodList = goodDao.findByType(type);
            JSONArray bookArray = new JSONArray();
            for (Good good:goodList){
                bookArray.add(good);
            }
            jo.put("books",bookArray);
        }
        return jo;
    }

    @PostMapping("/detail")
    public JSONObject bookDetail(@RequestBody JSONObject jsonObject){
        JSONObject jo = new JSONObject();

        String book_id = jsonObject.getString("good_id");
        if (MjStringUtil.isEmpty(book_id)){
            jo.put("status", 1);
            jo.put("err", "book_id为空");
        }else if (goodDao.findById(book_id) == null){
            jo.put("status", 1);
            jo.put("err", "书目不存在或已下架");
        }else {
            Good good = goodDao.findById(book_id);
            jo.put("status", 0);
            jo.put("good", good);
        }
        return jo;
    }
}
