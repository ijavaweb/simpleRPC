package fun.iweb.client;

import fun.iweb.transport.Transport;

public interface StubFactory {
    <T> T createStub(Transport transport,Class<T> serviceClass);

}

