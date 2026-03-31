package ro.unibuc.pao.model;

public class NoteCredential extends Credential {
    private String content;

    public NoteCredential() {
    }

    public NoteCredential(int id, String title, String username, String secret, Category category, String content) {
        super(id, title, username, secret, category);
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String getType() {
        return "NOTE";
    }

    @Override
    public String getDisplayText() {
        return super.getDisplayText() + ", nota=" + content;
    }
}
