package api;

public class TitledSourceUrl {

    private final String title;
    private final String url;

    private TitledSourceUrl(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public static TitledSourceUrl from(String title, String url) {
        return new TitledSourceUrl(title, url);
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }
}
