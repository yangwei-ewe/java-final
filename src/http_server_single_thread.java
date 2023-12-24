import com.sun.net.httpserver.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class http_server_single_thread {
    public static void main(String[] arg) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(3245), 0);
        server.createContext("/png", new ImageHandler());
        server.start();
    }

    static class ImageHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange data) throws IOException {
            var png = new FileInputStream("src/super_small_image.png");
            var header = data.getResponseHeaders();
            var pngdata = png.readAllBytes();
            png.close();
            header.add("Content-Type", "image/png");
            data.sendResponseHeaders(200, 0);
            OutputStream os = data.getResponseBody();
            os.write(pngdata);
            os.close();
        }
    }
}