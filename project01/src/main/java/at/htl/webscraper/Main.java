package at.htl.webscraper;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.jsoup.Jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
            server.createContext("/", new MyHandler());
            server.setExecutor(null);
            server.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String output() {
        Document doc;
        String output = "";
        try {
            doc = Jsoup.connect("https://webtv.feratel.com/webtv/?cam=5132&design=v3&c0=0&c2=1&lg=en&s=0").get();
            output = doc.getElementById("fer_video").select("source").attr("src");
            System.out.println(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = "<video controls>\n" +
                    "  <source src=\"" + output() + "\" type=\"video/mp4\">\n" +
                    "</video>";
            Headers h = t.getResponseHeaders();
            h.set("Content-Type","text/html");
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
