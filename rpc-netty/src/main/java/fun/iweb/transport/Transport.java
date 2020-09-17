package fun.iweb.transport;

import fun.iweb.transport.command.Command;

import java.util.concurrent.CompletableFuture;

/*
* @Description 发送请求命令
* @Date  2020/7/19
* @Param
* @Return
**/
public interface Transport {
    CompletableFuture<Command> send (Command request);
}
