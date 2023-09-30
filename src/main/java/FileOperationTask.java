import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileOperationTask implements Runnable {

    private final long index;

    public FileOperationTask(long index) {
        this.index = index;
    }

    @Override
    public void run() {
//        writeLargeDataToDisk();
        doIoOperation();
    }


    private void writeLargeDataToDisk() {
        String filePath = "hello-" + index+ ".txt";
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            byte[] data = new byte[1024 * 1024 * 100]; // 100 MB of data
            long startOfWriting = System.currentTimeMillis();
            System.out.println(startOfWriting + " before writing big file with index " + index +" by " + Thread.currentThread());
            fos.write(data);
            long endOfWriting = System.currentTimeMillis();
            System.out.println(endOfWriting + " after writing big file with index " + index + " by " + Thread.currentThread());
            System.out.println(" writing time by thread " + Thread.currentThread().getName() + " is " + (endOfWriting - startOfWriting) + " ms");
            fos.close();
            Path path = Path.of(filePath);
            Files.deleteIfExists(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doIoOperation() {
        String fileName = "hello-" + index;
        Path filePath = Path.of(fileName + ".txt");
        try {
            Files.writeString(filePath, "hello world", StandardOpenOption.CREATE);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
