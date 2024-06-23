package com.lishuo.test;

import com.lishuo.HelloObject;
import com.lishuo.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloServiceImpl2 implements HelloService {
    private static final Logger logger =
            LoggerFactory.getLogger(HelloServiceImpl.class);
    @Override
    public String hello(HelloObject object) {
        logger.info("二号实现类接收到：{}", object.getMessage());
        return "这是调用的返回值，id=" + object.getId();
    }
}
