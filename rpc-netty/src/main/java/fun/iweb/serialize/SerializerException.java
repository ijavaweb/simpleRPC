package fun.iweb.serialize;

/**
 * @ClassName SerializerException
 * @Descrition
 * @Author
 * @Date 2020/7/19 8:42
 * @Version 1.0
 **/
public class SerializerException extends RuntimeException{
    public  SerializerException(String msg) {
        super(msg);
    }
    public SerializerException(Throwable throwable) {
        super(throwable);
    }
}
