package fun.iweb.transport.command;


/**
 * @ClassName Header
 * @Descrition
 * @Author
 * @Date 2020/7/19 12:12
 * @Version 1.0
 **/
public class Header {
    private int requestId;
    private int version;
    private int type;
    public Header(){}

    public Header(int requestId, int version, int type) {
        this.requestId = requestId;
        this.version = version;
        this.type = type;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int length() {
        return Integer.BYTES * 3;
    }
}
