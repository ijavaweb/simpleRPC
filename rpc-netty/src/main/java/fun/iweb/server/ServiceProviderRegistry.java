package fun.iweb.server;

public interface ServiceProviderRegistry {
    <T> void addServiceProvider(Class<? extends T> serviceClass,T serviceProvide);
}
