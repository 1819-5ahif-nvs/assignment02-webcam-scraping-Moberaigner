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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    private static ScheduledExecutorService exec =
            Executors.newSingleThreadScheduledExecutor();
    private static String link;

    public static void main(String[] args) {
        try {
            //create and start Webserver
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0); //runs on port 8000
            server.createContext("/", new MyHandler());
            server.setExecutor(null);
            server.start();

            //update link and write to file every 60 seconds
            exec.scheduleAtFixedRate(() -> {
                link = update();
                writeToFile(link);
            }, 0, 60, TimeUnit.SECONDS);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String update() {
        //get actual link for the video
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

    public static void writeToFile(String text) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("myFile.txt",true));
            writer.append("\n");
            writer.append(text + "     "+LocalDateTime.now());

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    static class MyHandler implements HttpHandler {  //Webserver
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = "<video controls>\n" +
                    "  <source src=\"" + link + "\" type=\"video/mp4\">\n" +
                    "</video>";
            Headers h = t.getResponseHeaders();
            h.set("Content-Type", "text/html");      //send as html text
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
