// src/main/java/data/models/bookstore/Book.java
package data.models.bookstore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Book {
    public String isbn;
    public String title;
    public String subTitle;
    public String author;
    public String publish_date;
    public String publisher;
    public int pages;
    public String description;
    public String website;
}
