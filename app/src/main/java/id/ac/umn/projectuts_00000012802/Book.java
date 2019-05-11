package id.ac.umn.projectuts_00000012802;

public class Book {
    private String asin, group, format, title, author, publisher;
    private int favorite;

    public Book(){}
    public Book(String asin, String group, String format, String title, String author, String publisher, int favorite){
        this.asin = asin;
        this.group = group;
        this.format = format;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.favorite = favorite;
    }

    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }
}
