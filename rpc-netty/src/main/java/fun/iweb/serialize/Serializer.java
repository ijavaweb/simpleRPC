package fun.iweb.serialize;

//序列化接口
public interface Serializer<T> {

    /*
    * @Description
    * @Date  2020/7/19
    * @Param [entry] 需要序列化的对象
    * @Return int 序列化后的长度
    **/
    int size(T entry);

    /*
    * @Description 序列化对象
    * @Date  2020/7/19
    * @Param entry 待序列化对象,
    * @Param bytes 存放序列化对象的数组
    * @Param offset 数组的偏移量
    * @Param length 对象序列化后的长度
    * @Return void
    **/
    void serialize(T entry,byte[] bytes,int offset,int length);

    /*
    * @Description 反序列化
    * @Date  2020/7/19
    * @Param [bytes, offset, length]
    * @Return T
    **/
     T parse(byte[] bytes,int offset,int length);

     /*
     * @Description 表示序列化对象的数据类型
     * @Date  2020/7/19
     * @Param []
     * @Return byte
     **/
     byte type();

     /*
     * @Description 获取序列化对象的Class对象
     * @Date  2020/7/19
     * @Param []
     * @Return java.lang.Class<T>
     **/
     Class<T> getSerialiazeClass();

}
