import com.sun.net.httpserver.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class http_server {
    public static void main(String[] arg) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(3245), 0);
        server.createContext("/test", new TestHandler());
        server.start();
    }

    static class TestHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            var header = exchange.getResponseHeaders();
            header.add("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, 0);
            OutputStream os = exchange.getResponseBody();
            os.write("{\"text\":\"ewe\"}".getBytes());
            os.close();
        }
    }

    static class ShutdownHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange data) throws IOException {

        }
    }
}