import com.sun.net.httpserver.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class http_server_thread_pool {
    private final static String FILENAME = "src/165kb.png";
    private final static int WORKING_TIME = 20 * 1000000;// ms2ns

    public static void main(String[] arg) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(3245), 1024);
        server.createContext("/png", new ImageHandler());
        // server.setExecutor(Executors.newScheduledThreadPool(32));
        server.setExecutor(Executors.newFixedThreadPool(128));
        server.start();
    }

    static class ImageHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange data) throws IOException {
            var png = new FileInputStream(FILENAME);
            var header = data.getResponseHeaders();
            var pngdata = png.readAllBytes();
            png.close();
            var start = System.nanoTime();
            while ((System.nanoTime() - start) < WORKING_TIME) {
                int sum = 0;
                for (int i = 0; i < 10; i++) {
                    sum += i;
                }
            }
            header.add("Content-Type", "image/png");
            data.sendResponseHeaders(200, 0);
            OutputStream os = data.getResponseBody();
            os.write(pngdata);
            os.close();
        }
    }
}