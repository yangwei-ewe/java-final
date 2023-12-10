import com.sun.net.httpserver.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class http_server_multi_thread {
    static int curr_thread_num = 0;

    public static void main(String[] arg) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(3245), 0);
        server.createContext("/png", new ImageHandler());
        // server.createContext("/test", new TestHandler());
        server.start();
    }

    /*
     * static class TestHandler implements HttpHandler {
     * 
     * @Override
     * public void handle(HttpExchange exchange) throws IOException {
     * var header = exchange.getResponseHeaders();
     * header.add("Content-Type", "text/html");
     * exchange.sendResponseHeaders(200, 0);
     * OutputStream os = exchange.getResponseBody();
     * os.write(
     * "<!DOCTYPE html>\r\n<head>\r\n<meta charset=\"UTF-8\">\r\n<title>ewe</title>\r\n</head>\r\n\r\n<body>\r\n<img src=\"./png\" />\r\n</body>"
     * .getBytes());
     * os.close();
     * }
     * }
     */

    static class ImageHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange data) throws IOException {
            // while(curr_thread_num<15)
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        var png = new FileInputStream("src/IMG_4791.PNG");
                        var header = data.getResponseHeaders();
                        var pngdata = png.readAllBytes();
                        png.close();
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
                }
            }).start();
        }
    }
}