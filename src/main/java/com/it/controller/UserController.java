package com.it.controller;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.it.entity.User;
import com.it.mapper.UserMapper;
import com.it.service.UserService;
import com.it.utli.SMSUtils;
import com.it.utli.SystemJsonResponse;
import com.it.utli.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 用户模块
 * @since  2022-10-15
 * @version  1.0
 * @author  hyj
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    /**
     * 接受手机号发送验证码
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public SystemJsonResponse sendMsg(@RequestBody User user, HttpServletRequest request){
        //获取手机号
        String phone = user.getPhone();
        if(!StringUtils.isEmpty(phone)){
            //生成验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            //调用阿里云的api
            System.out.println(code);
            //SMSUtils.sendMessage("阿里云短信测试","SMS_154950909",phone,code);
            //将验证码保存到session
            HttpSession session = request.getSession();
            session.setAttribute(phone,code);
            return SystemJsonResponse.success(200,"手机验证成功");
        }
        return SystemJsonResponse.fail();
    }



    /**
     * 移动端用户登录
     * @param map
     * @param request
     * @return
     */
    @PostMapping("/login")
    public SystemJsonResponse login(@RequestBody Map map, HttpServletRequest request){
        //获取手机号
        String phone = map.get("phone").toString();
        //获取验证码
        String code = map.get("code").toString();
        //获取session保存的验证码
        HttpSession session = request.getSession();
        Object codeInSession = session.getAttribute(phone);
        //通过验证码比较
        if(codeInSession != null &&  code.equals(codeInSession) ){
            LambdaQueryWrapper<User> lq = new LambdaQueryWrapper<>();
            lq.eq(User::getPhone,phone);
            User user = userMapper.selectOne(lq);
            if(user == null){
                user = new User();
                user.setPhone(phone);
                userMapper.insert(user);
            }
            return SystemJsonResponse.success(user);
        }
        return SystemJsonResponse.fail(999,"验证码错误");
    }
}
