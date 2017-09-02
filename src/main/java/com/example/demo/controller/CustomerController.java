package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;

import com.example.demo.dao.CustomerDao;
import com.example.demo.entity.Customer;
import com.example.demo.util.MjStringUtil;
import com.example.demo.util.Token;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by yancychan on 17-8-26.
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/customer")
public class CustomerController extends BaseController {

    @Autowired
    CustomerDao customerDao;

    //注册时发送过来用户名密码, 先检查用户名的合法性以及是否被注册过
    @PostMapping("/signup")
    public JSONObject signup(@RequestBody JSONObject jsonObject){

        JSONObject jo = new JSONObject();

        String customerName = jsonObject.getString("username");
        String customer_password = jsonObject.getString("password");

        if (MjStringUtil.isEmpty(customerName) ||
                MjStringUtil.isEmpty(customer_password)){
            jo.put("status", 1);
            jo.put("err", "用户名或密码为空");
        }else {

            if (customerDao.findByCustomerName(customerName) != null) {
                jo.put("status", 1);
                jo.put("err", "用户名已被注册");
            } else {
                Customer customer = new Customer();
                customer.setCustomer_name(customerName);
                customer.setCustomer_password(customer_password);
                customerDao.save(customer);
                jo.put("status", 0);
            }
        }
        return jo;
    }

    //登录时接收账号密码, 先查询是否有此用户, 然后再验证用户的合法性, 如果合法就返回一个自定义token给客户端
    @PostMapping("/login")
    public JSONObject login(HttpServletResponse response,
                            @RequestBody JSONObject jsonObject){
        JSONObject jo = new JSONObject();

        String customerName = jsonObject.getString("username");
        String customer_password = jsonObject.getString("password");

        if (MjStringUtil.isEmpty(customerName) ||
                MjStringUtil.isEmpty(customer_password)){
            jo.put("status", 1);
            jo.put("err", "用户名或密码为空");
        }else {
            if (customerDao.findByCustomerName(customerName) == null) {
                jo.put("status", 1);
                jo.put("err", "该用户不存在");
            } else {
                Customer customer = customerDao.findByCustomerName(customerName);
                if (customer.getCustomer_password().equals(customer_password)) {
                    String token = Token.generateToken(customerName);
                    customer.setToken(token);
                    customerDao.save(customer);
                    Cookie cookie = new Cookie("token", token);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                    jo.put("status", 0);
                } else {
                    jo.put("status", 1);
                    jo.put("err", "密码错误");
                }
            }
        }
        return jo;
    }

    //退出当前账号
    @PostMapping("/logout")
    public JSONObject logout(@CookieValue(value = "token",defaultValue = "null") String token,
                              @RequestBody JSONObject jsonObject){
        JSONObject jo = new JSONObject();

//        String token = jsonObject.getString("token");
        if (MjStringUtil.isEmpty(token)){
            jo.put("status", 1);
            jo.put("err","您尚未登录");
        }else {
            Customer customer = customerDao.findByToken(token);
            if (customer == null){
                jo.put("status", 1);
                jo.put("err", "登录状态异常, 请重新登录");
            }else {
                customer.setToken(null);
                customerDao.save(customer);
//                cookieToken = null;
                jo.put("status", 0);
            }
        }
        return jo;
    }

    //编辑个人信息
    @PostMapping("/edit")
    public JSONObject editMessage(@CookieValue(value = "token",defaultValue = "null") String token,
                                  @RequestBody JSONObject jsonObject){
        JSONObject jo = new JSONObject();

//        String token = jsonObject.getString("token");
        String email = jsonObject.getString("email");
        String phone = jsonObject.getString("phone");
        String address = jsonObject.getString("address");

        if (MjStringUtil.isEmpty(token)){
            jo.put("status", 1);
            jo.put("err","您尚未登录");
        }else {
            Customer customer = customerDao.findByToken(token);
            if (customer == null){
                jo.put("status", 1);
                jo.put("err", "登录状态异常, 请重新登录");
            }else {
                if (MjStringUtil.isEmpty(email)){
                    jo.put("status", 1);
                    jo.put("err", "邮箱不能为空");
                }else if (MjStringUtil.isEmpty(phone)){
                    jo.put("status", 1);
                    jo.put("err", "电话号码不能为空");
                }else if (MjStringUtil.isEmpty(address)){
                    jo.put("status", 1);
                    jo.put("err", "地址不能为空");
                }else {
                    customer.setEmail(email);
                    customer.setPhone(phone);
                    customer.setAddress(address);
                    customerDao.save(customer);

                    jo.put("status", 0);
                    jo.put("customer_id",customer.getCustomer_id());
                    jo.put("customer_name",customer.getCustomer_name());
                    jo.put("address",customer.getAddress());
                    jo.put("email",customer.getEmail());
                    jo.put("phone",customer.getPhone());
                }
            }
        }
        return jo;
    }

    @PostMapping("/reset")
    public JSONObject resetPassword(@CookieValue(value = "token",defaultValue = "null") String token,
                                    @RequestBody JSONObject jsonObject){
        JSONObject jo =new JSONObject();

//        String token = jsonObject.getString("token");
        String password = jsonObject.getString("password");
        String new_password = jsonObject.getString("new_password");
        String confirm_new_password = jsonObject.getString("confirm_password");

        if (MjStringUtil.isEmpty(token)){
            jo.put("status", 1);
            jo.put("err","您尚未登录");
        }else {
            Customer customer = customerDao.findByToken(token);
            if (customer == null){
                jo.put("status", 1);
                jo.put("err", "登录状态异常, 请重新登录");
            }else {
                if (!customer.getCustomer_password().equals(password)){
                    jo.put("status", 1);
                    jo.put("err", "原密码错误，请重新输入");
                }else if (MjStringUtil.isEmpty(new_password) ||
                        MjStringUtil.isEmpty(confirm_new_password)){
                    jo.put("status", 1);
                    jo.put("err", "新密码不能为空");
                }else if (!new_password.equals(confirm_new_password)){
                    jo.put("status", 1);
                    jo.put("err", "两次输入的新密码不同，请重新输入");
                }else {
                    customer.setCustomer_password(new_password);
                    customerDao.save(customer);
                    jo.put("status", 0);
                }
            }
        }
        return jo;
    }

    @PostMapping("/message")
    public JSONObject getCustomerMessage(@CookieValue(value = "token",defaultValue = "null") String token,
                                         @RequestBody JSONObject jsonObject){
        JSONObject jo = new JSONObject();
//        String token = jsonObject.getString("token");

        if (MjStringUtil.isEmpty(token)){
            jo.put("status", 1);
            jo.put("err","您尚未登录");
        }else {
            Customer customer = customerDao.findByToken(token);
            if (customer == null){
                jo.put("status", 1);
                jo.put("err", "登录状态异常, 请重新登录");
            }else {
                jo.put("status", 0);
                jo.put("customer_id",customer.getCustomer_id());
                jo.put("customer_name",customer.getCustomer_name());
                jo.put("address",customer.getAddress());
                jo.put("email",customer.getEmail());
                jo.put("phone",customer.getPhone());
            }
        }
        return jo;
    }
}
