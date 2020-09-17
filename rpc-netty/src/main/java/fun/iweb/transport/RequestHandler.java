package fun.iweb.transport;

import fun.iweb.transport.command.Command;

/**
 * @ClassName RequestHandler
 * @Descrition
 * @Author
 * @Date 2020/7/19 12:08
 * @Version 1.0
 **/
public interface RequestHandler {
    /*
    * @Description 请求处理器
    * @Date  2020/7/19
    * @Param [requestCommand]
    * @Return Command
    **/
    Command handle(Command requestCommand);

    /*
    * @Description 支持的请求类型
    * @Date  2020/7/19
    * @Param []
    * @Return int
    **/
    int type();
}
