package fun.iweb.nameservice;

import java.net.URI;
import java.util.HashMap;
import java.util.List;

/**
 * @ClassName Metadata
 * @Descrition
 * @Author
 * @Date 2020/7/19 10:21
 * @Version 1.0
 **/
public class Metadata extends HashMap<String, List<URI>> {
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Metadata:").append("\n");
        for (Entry<String,List<URI>> entry:entrySet()) {
            sb.append("\t").append("Classname:")
                    .append(entry.getKey()).append("\n");
            sb.append("\t").append("URIs:").append("\n");

            for (URI uri:entry.getValue()) {
                sb.append("\t\t").append(uri).append("\n");
            }
        }
        return sb.toString();
    }
}
