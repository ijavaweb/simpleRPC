package fun.iweb.nameservice;

import fun.iweb.NameService;
import fun.iweb.serialize.SerializerSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @ClassName LocalFileNameService
 * @Descrition
 * @Author
 * @Date 2020/7/19 11:43
 * @Version 1.0
 **/
public class LocalFileNameService implements NameService {
    private static final Logger logger = LoggerFactory.getLogger(LocalFileNameService.class);
    private static final Collection<String> schemas = Collections.singleton("file");
    private File file;

    @Override
    public Collection<String> supportProtocol() {
        return schemas;
    }

    @Override
    public void connect(URI nameServiceUri) {

        if ( schemas.contains(nameServiceUri.getScheme())) {
            file = new File(nameServiceUri);
        } else {
            throw new RuntimeException("Unsupported schema");
        }
    }

    @Override
    public synchronized void registerService(String serviceName, URI uri) {
        logger.info("Register service:{},uri:{}",serviceName,uri);

        try(RandomAccessFile randomAccessFile = new RandomAccessFile(file,"rw");
            FileChannel fileChannel = randomAccessFile.getChannel()){
            FileLock lock = fileChannel.lock();
            try{
                int fileLength = (int) randomAccessFile.length();
                Metadata metadata;
                byte[] bytes;
                if(fileLength > 0) {
                    bytes = new byte[(int) randomAccessFile.length()];
                    ByteBuffer buffer = ByteBuffer.wrap(bytes);
                    while (buffer.hasRemaining()) {
                        fileChannel.read(buffer);
                    }
                    metadata = SerializerSupport.parse(bytes);
                } else {
                    metadata = new Metadata();
                }
                List<URI> uriList = metadata.computeIfAbsent(serviceName,k->new ArrayList<>());
                if (!uriList.contains(uri)) {
                    uriList.add(uri);
                }
                logger.info(metadata.toString());
                bytes = SerializerSupport.serialize(metadata);
                fileChannel.truncate(bytes.length);
                fileChannel.position(0L);
                fileChannel.write(ByteBuffer.wrap(bytes));
                fileChannel.force(true);
            } finally {
                lock.release();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public URI lookupService(String serviceName) throws IOException {
        Metadata metadata;
        try(RandomAccessFile randomAccessFile = new RandomAccessFile(file,"rw");
            FileChannel fileChannel = randomAccessFile.getChannel()) {
            FileLock lock = fileChannel.lock();
            try{
                byte[] bytes = new byte[(int) randomAccessFile.length()];
                ByteBuffer buffer = ByteBuffer.wrap(bytes);
                while (buffer.hasRemaining()) {
                    fileChannel.read(buffer);
                }
                metadata = bytes.length == 0 ? new Metadata(): SerializerSupport.parse(bytes);
                logger.info(metadata.toString());
            } finally {
                lock.release();
            }
        }
        List<URI> uris = metadata.get(serviceName);

        if (null == uris || uris.isEmpty()) {
            return null;
        } else {
            return uris.get(ThreadLocalRandom.current().nextInt(uris.size()));
        }
    }
}
