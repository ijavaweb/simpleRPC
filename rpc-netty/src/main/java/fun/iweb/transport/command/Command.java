package fun.iweb.transport.command;

/**
 * @ClassName Command
 * @Descrition
 * @Author
 * @Date 2020/7/19 12:10
 * @Version 1.0
 **/
public class Command {
    protected Header header;
    private byte[] payload;

    public Command(Header header, byte[] payload) {
        this.header = header;
        this.payload = payload;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }
}
