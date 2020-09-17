package fun.iweb;

import fun.iweb.spi.ServiceSupport;

import java.io.Closeable;
import java.net.URI;
import java.util.Collection;
import java.util.Collections;

/*
* @Description 对外提供服务的接口
* @Date  2020/7/19
* @Param
* @Return
**/
public interface RpcAccessPoint extends Closeable {
    /*
    * @Description 客户端获取远程服务的引用
    * @Date  2020/7/19
    * @Param [uri 服务的地址, serviceClass服务的接口类型]
    * @Return T
    **/
    <T> T getRemoteService(URI uri,Class<T> serviceClass);

    /*
    * @Description 服务端注册服务的实例
    * @Date  2020/7/19
    * @Param [service 服务实例, serviceClass 服务接口类型]
    * @Return java.net.URI
    **/
    <T> URI addServiceProvider(T service,Class<T> serviceClass);


    /*
    * @Description 获取注册中心的引用
    * @Date  2020/7/19
    * @Param [nameServiceUri 注册中心的URI]
    * @Return fun.iweb.NameService
    **/
    default NameService getNameService(URI nameServiceUri) {
        Collection<NameService> nameServices = ServiceSupport.loadAll(NameService.class);
        for (NameService nameService : nameServices) {
            if (nameService.supportProtocol().contains(nameService)) {
                nameService.connect(nameServiceUri);
                return nameService;
            }
        }
        return null;
    }


    /*
    * @Description 服务启动，监听接口并提供远程服务
    * @Date  2020/7/19
    * @Param []
    * @Return java.io.Closeable
    **/
    Closeable startServer() throws Exception;

}
