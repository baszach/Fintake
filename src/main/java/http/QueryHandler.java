package http;

public class QueryHandler {

    private final String[] urls;

    public QueryHandler(String... urls) {
        this.urls = urls;
    }

    public String[] requestData() {
        HttpHandler httpHandler = new HttpHandler();
        String[] data = new String[urls.length];
        for(int i = 0; i < data.length; i++) {
            data[i] = httpHandler.makeServiceCall(urls[i]);
        }
        return data;
    }
}
