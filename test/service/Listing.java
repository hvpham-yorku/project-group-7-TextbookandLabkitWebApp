package domain;

public class Listing {

    private final String id;
    private String title;
    private String courseCode;
    private int price;
    private final String sellerEmail;
    private boolean sold;

    public Listing(String id, String title, String courseCode, int price, String sellerEmail) {
        this.id = id;
        this.title = title;
        this.courseCode = courseCode;
        this.price = price;
        this.sellerEmail = sellerEmail;
        this.sold = false;
    }

    public String getId() { return id; }
    public String getSellerEmail() { return sellerEmail; }

    public void edit(String title, String courseCode, int price) {
        if (sold) return;
        this.title = title;
        this.courseCode = courseCode;
        this.price = price;
    }

    public void markSold() {
        sold = true;
    }
}
