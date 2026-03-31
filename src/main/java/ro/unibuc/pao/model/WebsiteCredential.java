package ro.unibuc.pao.model;

public class WebsiteCredential extends Credential {
    private String websiteUrl;
    private String email;

    public WebsiteCredential() {
    }

    public WebsiteCredential(int id, String title, String username, String secret, Category category,
                             String websiteUrl, String email) {
        super(id, title, username, secret, category);
        this.websiteUrl = websiteUrl;
        this.email = email;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getType() {
        return "WEBSITE";
    }

    @Override
    public String getDisplayText() {
        return super.getDisplayText() + ", site=" + websiteUrl + ", email=" + email;
    }
}
