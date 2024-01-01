import com.sun.net.httpserver.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class http_server_multi_thread {
    private final static String FILENAME = "src/165kb.png";
    private final static int WORKING_TIME = 20 * 1000000;// ms2ns

    public static void main(String[] arg) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(3245), 1024);
        server.createContext("/png", new ImageHandler());
        // server.createContext("/test", new TestHandler());
        server.start();
    }

    static class ImageHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange data) throws IOException {
            new Thread(() -> {
                try {
                    var header = data.getResponseHeaders();
                    var png = new FileInputStream(FILENAME);
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
                } catch (IOException e) {
                    e.printStackTrace();
                    try {
                        data.sendResponseHeaders(500, 0);
                        data.getResponseBody().close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }).start();
        }
    }
}