public interface IBookStore {

    void  addBooks(int ISBN, String title, String author, String description, boolean available);
    void deleteBooks(int ISBN );
    void display();

}
