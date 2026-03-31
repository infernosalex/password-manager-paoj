package ro.unibuc.pao.model;

public class CardCredential extends Credential {
    private String cardHolder;
    private String cardNumber;
    private String expirationDate;

    public CardCredential() {
    }

    public CardCredential(int id, String title, String username, String secret, Category category,
                          String cardHolder, String cardNumber, String expirationDate) {
        super(id, title, username, secret, category);
        this.cardHolder = cardHolder;
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public String getType() {
        return "CARD";
    }

    @Override
    public String getDisplayText() {
        return super.getDisplayText() + ", titular=" + cardHolder + ", expirare=" + expirationDate;
    }
}
