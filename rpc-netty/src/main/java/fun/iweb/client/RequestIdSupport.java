package fun.iweb.client;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName RequestIdSupport
 * @Descrition
 * @Author
 * @Date 2020/7/19 18:01
 * @Version 1.0
 **/
public class RequestIdSupport {
    private final static AtomicInteger nextRequestId = new AtomicInteger(0);
    public static int next() {
        return nextRequestId.getAndIncrement();
    }

}
