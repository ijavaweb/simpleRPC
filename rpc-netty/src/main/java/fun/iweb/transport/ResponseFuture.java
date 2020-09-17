package fun.iweb.transport;

import fun.iweb.transport.command.Command;

import java.util.concurrent.CompletableFuture;

/**
 * @ClassName ReponseFuture
 * @Descrition
 * @Author
 * @Date 2020/7/19 12:34
 * @Version 1.0
 **/
public class ResponseFuture {
    private final int requestId;
    private final CompletableFuture<Command> future;
    private final long timestamp;

    public ResponseFuture(int requestId, CompletableFuture<Command> future) {
        this.requestId = requestId;
        this.future = future;
        timestamp = System.nanoTime();
    }

    public int getRequestId() {
        return requestId;
    }

    public CompletableFuture<Command> getFuture() {
        return future;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
