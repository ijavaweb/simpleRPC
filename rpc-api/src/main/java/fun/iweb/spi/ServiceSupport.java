package fun.iweb.spi;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @ClassName ServiceSupport
 * @Descrition
 * @Author
 * @Date 2020/7/19 9:07
 * @Version 1.0
 **/
public class ServiceSupport {
    private final static Map<String ,Object> singletonServices = new HashMap<>();

    @SuppressWarnings("unchecked")
    public synchronized static <S> S load(Class<S> service) {
        return StreamSupport.stream(ServiceLoader.load(service).spliterator(),false)
                .map(ServiceSupport::singletonFilter)
                .findFirst().orElseThrow(RuntimeException::new);
    }

    public synchronized static <S> Collection<S> loadAll(Class<S> service) {
        return StreamSupport.stream(ServiceLoader.load(service).spliterator(),false)
                .map(ServiceSupport::singletonFilter)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private static <S> S singletonFilter(S service) {
        if (service.getClass().isAnnotationPresent(Singleton.class)) {
            String className = service.getClass().getCanonicalName();
            Object singletonInstance = singletonServices.putIfAbsent(className,service);
            return singletonInstance == null ? service: (S)singletonInstance;
        } else {
            return service;
        }
    }
}
