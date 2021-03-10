package main.java.http;

public class QueryHandler {

    private String[] urls;

    public QueryHandler(String... urls) {
        updateUrls(urls);
    }

    public String[] requestData() {
        HttpHandler httpHandler = new HttpHandler();
        String[] data = new String[urls.length];
        for(int i = 0; i < data.length; i++) {
            data[i] = httpHandler.makeServiceCall(urls[i]);
        }
        return data;
    }

    public void updateUrls(String... urls) {
        this.urls = urls;
    }
}
