package fun.iweb.transport;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @ClassName InFlightRequests
 * @Descrition 背压，防止后端处理请求压力过大
 * @Author
 * @Date 2020/7/19 12:42
 * @Version 1.0
 **/
public class InFlightRequests implements Closeable {
   private final static long TIMEOUT_SEC = 10L;
   private final Semaphore semaphore = new Semaphore(10);
   private final Map<Integer, ResponseFuture> futureMap = new ConcurrentHashMap<>();
   private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
   private final ScheduledFuture scheduledFuture;
   public InFlightRequests() {
       scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(this::removeTimeoutFutures,TIMEOUT_SEC,TIMEOUT_SEC,TimeUnit.SECONDS);
   }
   public void put(ResponseFuture responseFuture) throws InterruptedException,TimeoutException{

       if (semaphore.tryAcquire(TIMEOUT_SEC,TimeUnit.SECONDS)) {
           futureMap.put(responseFuture.getRequestId(),responseFuture);
       } else {
           throw  new TimeoutException();
       }
   }

   private void removeTimeoutFutures(){
       futureMap.entrySet().removeIf(entry-> {
           if (System.nanoTime() - entry.getValue().getTimestamp() > TIMEOUT_SEC * 1000000000l) {
               semaphore.release();
               return true;
           } else {
               return false;
           }
       });
   }
   public ResponseFuture remove (int requestId) {
       ResponseFuture future = futureMap.remove(requestId);

       if (null == future) {
           semaphore.release();
       }
       return future;
   }


    @Override
    public void close() throws IOException {
        scheduledFuture.cancel(true);
        scheduledExecutorService.shutdown();
    }
}
