package fun.iweb.serialize;

import fun.iweb.spi.ServiceSupport;
import fun.iweb.serialize.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName SerializerSupport
 * @Descrition 支持序列化的一系列操作
 * @Author
 * @Date 2020/7/19 8:44
 * @Version 1.0
 **/
@SuppressWarnings("unchecked")
public class  SerializerSupport {
    private static final Logger logger = LoggerFactory.getLogger(SerializerSupport.class);
    private static Map<Class<?>, Serializer<?>> serializerMap = new HashMap<>();
    private static Map<Byte, Class<?>> typeMap = new HashMap<>();

    static {
        for (Serializer serializer: ServiceSupport.loadAll(Serializer.class)) {
            registerType(serializer.type(),serializer.getSerialiazeClass(),serializer);
            logger.info("Found serializer,class:{},type:{}.",serializer.getSerialiazeClass().getCanonicalName(),
                    serializer.type());
        }
    }


    /*
    * @Description 获取序列化对象的类型
    * @Date  2020/7/19
    * @Param [bytes]
    * @Return byte
    **/
    private static byte parseEntryType(byte[] bytes) {
        return bytes[0];
    }

    /*
    * @Description 注册序列化的类型和方法
    * @Date  2020/7/19
    * @Param [type, eClass, serializer]
    * @Return void
    **/
    private static <E> void registerType(byte type,Class<E> eClass,Serializer<E> serializer) {
        serializerMap.put(eClass,serializer);
        typeMap.put(type,eClass);
    }

    /*
    * @Description 发序列化方法及重载形式
    * @Date  2020/7/19
    * @Param [bytes, offset, length, eClass]
    * @Return E
    **/
    private static <E> E parse(byte[] bytes,int offset,int length,Class<E> eClass) {
        Object entry = serializerMap.get(eClass).parse(bytes,offset,length);
        if (eClass.isAssignableFrom(entry.getClass())) {
            return (E) entry;
        } else {
            throw new SerializerException("Type mismatch");
        }
    }

    public static <E> E parse(byte[] bytes) {
        return parse(bytes,0,bytes.length);
    }
    private static <E> E parse(byte[] bytes,int offset,int length) {
        byte type = parseEntryType(bytes);
        Class<E> eClass = (Class<E>)typeMap.get(type);

        if (null == eClass) {
            throw new SerializerException(String.format("Unkonw entry type :%d!",type));
        } else {
            return parse(bytes,offset+1,length-1,eClass);
        }
    }

    /*
    * @Description 序列化方法
    * @Date  2020/7/19
    * @Param [entry]
    * @Return byte[]
    **/
    public static <E> byte[] serialize(E entry) {

        Serializer<E> serializer = (Serializer<E>) serializerMap.get(entry.getClass());
        if ( null == serializer) {
            throw new SerializerException(String.format("Unknow entry class type:%s!",entry.getClass().toString()));
        }
        byte[] bytes = new byte[serializer.size(entry) +1];

        bytes[0] = serializer.type();

        serializer.serialize(entry,bytes,1,bytes.length-1);
        return bytes;
    }
}
