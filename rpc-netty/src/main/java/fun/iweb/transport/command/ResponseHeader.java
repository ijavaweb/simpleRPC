package fun.iweb.transport.command;

import java.nio.charset.StandardCharsets;

/**
 * @ClassName ResponseHeader
 * @Descrition
 * @Author
 * @Date 2020/7/19 12:22
 * @Version 1.0
 **/
public class ResponseHeader extends Header{
    private int code;
    private String error;

    public ResponseHeader(int type, int version, int requestId,  Throwable throwable) {
        this(type, version, requestId, Code.UNKNOW_ERROR.getCode(), throwable.getMessage());
    }
    public ResponseHeader(int type, int version, int requestId) {
        this(type, version, requestId, Code.SUCCESS.getCode(), null);
    }
    public ResponseHeader(int type, int version, int requestId, byte[] serialize) {
        this(type, version, requestId, Code.SUCCESS.getCode(), null);
    }

    public ResponseHeader(int requestId, int version, int type, int code, String error) {
        super(requestId, version, type);
        this.code = code;
        this.error = error;
    }
    @Override
    public int length() {
        return Integer.BYTES + Integer.BYTES + Integer.BYTES + Integer.BYTES +
                Integer.BYTES +
                (error == null ? 0 : error.getBytes(StandardCharsets.UTF_8).length);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
