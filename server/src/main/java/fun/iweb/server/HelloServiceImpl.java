package fun.iweb.server;

import fun.iweb.rpc.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName HelloServiceImpl
 * @Descrition
 * @Author
 * @Date 2020/7/24 22:28
 * @Version 1.0
 **/
public class HelloServiceImpl implements HelloService {
    private static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);

    @Override
    public String hello(String name) {
        logger.info("HelloServiceImpl收到:{}",name);
        String s = "Hello"+name;
        logger.info("HelloServiceImpl返回:{}",s);
        return s;
    }
}
