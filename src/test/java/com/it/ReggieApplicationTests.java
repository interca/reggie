package com.it;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

@SpringBootTest

class ReggieApplicationTests {

    @Test
    void contextLoads() {
        String openid = DigestUtils.md5DigestAsHex("123456".getBytes());
        System.out.println(openid);
        //e10adc3949ba59abbe56e057f20f883e
    }

}
