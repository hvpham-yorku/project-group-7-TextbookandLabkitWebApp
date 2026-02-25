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
    public String getTitle() { return title; }
    public String getCourseCode() { return courseCode; }
    public int getPrice() { return price; }
    public String getSellerEmail() { return sellerEmail; }
    public boolean isSold() { return sold; }

    public void setTitle(String title) { this.title = title; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
    public void setPrice(int price) { this.price = price; }
    public void markSold() { this.sold = true; }
}
