public class Book {


    public int ISBN;
    public String title;
    public String author;
    public String description;

    public boolean available;
    public boolean isAvailable() {
        return available;
    }




    public Book(int ISBN, String title, String author, String description, boolean available) {
        this.ISBN = ISBN;
        this.title = title;
        this.author = author;
        this.description = description;
        this.available = available;
    }

    public int getISBN() {
        return ISBN;
    }

    public void setISBN(int ISBN) {
        this.ISBN = ISBN;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}

