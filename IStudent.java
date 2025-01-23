public interface IStudent {
     void borrowBook(int ISBN);
     void returnBook(int ISBN, int studentId);
     void searchBook(int ISBN, String title, String author);
     void submitRequest(String firstname, String lastname, String email, String level, String type, int studentID);

}
