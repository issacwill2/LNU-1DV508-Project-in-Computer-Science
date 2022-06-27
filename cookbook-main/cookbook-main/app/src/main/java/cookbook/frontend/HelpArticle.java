package cookbook.frontend;

public class HelpArticle {
    private final String title;
    private final String body;

    public HelpArticle(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
}
