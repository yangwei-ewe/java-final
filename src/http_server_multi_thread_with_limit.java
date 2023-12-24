import com.sun.net.httpserver.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class http_server_multi_thread_with_limit {
    private final static int THREAD_LIMIT = 16;
    private final static int SLEEP_TIME = 13;
    private static int curr_thread_num = 0;
    public static Lock lock;

    public static void main(String[] arg) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(3245), 0);
        lock = new ReentrantLock(true);
        server.createContext("/png", new ImageHandler());
        // server.createContext("/test", new TestHandler());
        server.start();
        while (true) {
            System.out.println("curr thread: " + curr_thread_num);
            Thread.sleep(500);
        }
    }

    static class ImageHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange data) throws IOException {
            // System.out.println("in");
            while (curr_thread_num >= THREAD_LIMIT) {
                try {
                    Thread.sleep(SLEEP_TIME);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            lock.lock();
            curr_thread_num++;
            lock.unlock();
            new Thread(() -> {
                try {
                    var png = new FileInputStream("src/super_small_image.png");
                    var header = data.getResponseHeaders();
                    var pngdata = png.readAllBytes();
                    png.close();
                    header.add("Content-Type", "image/png");
                    data.sendResponseHeaders(200, 0);
                    OutputStream os = data.getResponseBody();
                    os.write(pngdata);
                    lock.lock();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    try {
                        data.sendResponseHeaders(500, 0);
                        data.getResponseBody().close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                } finally {
                    curr_thread_num--;
                    // System.out.println("release");
                    lock.unlock();
                }
            }).start();
        }
    }
}