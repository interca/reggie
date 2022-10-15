package com.it.filter;

import com.alibaba.fastjson.JSON;
import com.it.entity.Employee;
import com.it.entity.User;
import com.it.service.EmployeeService;
import com.it.utli.SystemJsonResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

/**
 * 过滤器判断用户登录
 * @since 2022-9-14
 * @author hyj
 */
//所有请求拦截
@Component
@WebFilter(filterName = "LoginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER=new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request= (HttpServletRequest) servletRequest;
        HttpServletResponse response= (HttpServletResponse) servletResponse;
        String requestURI = request.getRequestURI();
        //不需要处理的请求
        String[]urs=new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",//短信发送
                "/user/login"//短信登录
        };
        boolean check = check(urs, requestURI);
        if(check==true) {
            filterChain.doFilter(request, response);
            return;
        }
        //请求要处理  后台
        Employee employee = (Employee) request.getSession().getAttribute("employee");
        if(employee!=null) {
            System.out.println("后台已经登录");
            filterChain.doFilter(request, response);
            return;
        }
        //请求要处理  手机端
        User user = (User) request.getSession().getAttribute("user");
        if(user!=null) {
            System.out.println("用户已经登录");
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("没有登录");
       //如果没有登录，返回登录结果
        response.getWriter().write(JSON.toJSONString(SystemJsonResponse.fail(0,"NOTLOGIN")));
    }

    /**
     * 路径匹配是否要放行
     * @param URL
     * @param RequestURI
     * @return
     */
    public boolean check(String[]URL,String RequestURI){
        for (String s : URL) {
            boolean match = PATH_MATCHER.match(s, RequestURI);
            if(match == true)return true;
        }
        return false;
    }


}
