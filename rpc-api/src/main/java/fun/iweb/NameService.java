package fun.iweb;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;

/*
* @Description 注册中心
* @Date  2020/7/19
* @Param
* @Return
**/
public interface NameService {
    /*
    * @Description 支持的所有协议
    * @Date  2020/7/19
    * @Param
    * @Return
    **/
    Collection<String> supportProtocol();

    /*
    * @Description 链接注册中心
    * @Date  2020/7/19
    * @Param [nameServiceUri] 注册中心的地址
    * @Return void
    **/
    void connect (URI nameServiceUri);

    /*
    * @Description 向注册中心注册服务
    * @Date  2020/7/19
    * @Param [serviceName 服务名称, uri 服务地址]
    * @Return void
    **/
    void registerService(String serviceName,URI uri);


    /*
    * @Description 查询服务地址
    * @Date  2020/7/19
    * @Param [serviceName，服务名称]
    * @Return java.net.URI
    **/
    URI lookupService(String serviceName) throws IOException;
}
