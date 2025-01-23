import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.*;
import java.sql.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StudentJunitTest {

    private static final Logger logger = LogManager.getLogger(StudentJunitTest.class);


    @Test
    public void testBorrowBook() {
        // Antag att du skapar och sätter upp en Student-instans
        int studentID = 2021;
        String firstname = "Puntos";
        String lastname = "Davidsson";
        String email = "puntos12@gmail.com";
        boolean registered = true;
        String level = "PHD";
        int limitNr = 7;
        int borrowedNr = 0;
        int warning = 0;
        boolean active = true;

        Student student = new Student(studentID, firstname, lastname, email, registered, level, limitNr, borrowedNr, warning, active);

        int testISBN = 111155; // Ett giltigt ISBN från din Book-tabell
        student.borrowBook(testISBN);
        try (Connection conn = MysqlConnection.connectToMysql();
             PreparedStatement checkAvailable = conn.prepareStatement("SELECT available FROM Book WHERE ISBN = ?");
             PreparedStatement checkBorrowed = conn.prepareStatement("SELECT * FROM BorrowedBook WHERE ISBN = ?");
             PreparedStatement checkStudent = conn.prepareStatement("SELECT borrowedNr FROM Student WHERE studentID = ?")) {


            checkAvailable.setInt(1, testISBN);
            ResultSet rsAvailable = checkAvailable.executeQuery();
            assertTrue(rsAvailable.next());
            assertFalse(rsAvailable.getBoolean("available"));


            checkBorrowed.setInt(1, testISBN);
            ResultSet rsBorrowed = checkBorrowed.executeQuery();
            assertTrue(rsBorrowed.next());
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }

    }

    @Test
    public void returnBookTest()throws SQLException {
        // Antag att du ställer in en student och lånar ut en bok
        // ...
        int studentID = 2021;
        String firstname = "Puntos";
        String lastname = "Davidsson";
        String email = "puntos12@gmail.com";
        boolean registered = true;
        String level = "PHD";
        int limitNr = 7;
        int borrowedNr = 0;
        int warning = 0;
        boolean active = true;
        Student student = new Student(studentID, firstname, lastname, email, registered, level, limitNr, borrowedNr, warning, active);

        student.returnBook(111155, 2021);
        int testISBN = 111155;


        try (Connection conn = MysqlConnection.connectToMysql();
             PreparedStatement checkLoan = conn.prepareStatement("SELECT * FROM BorrowedBook WHERE ISBN = ? AND borrowedBy = ?");
             PreparedStatement checkStudent = conn.prepareStatement("SELECT borrowedNr, warning FROM Student WHERE studentID = ?");
             PreparedStatement checkBook = conn.prepareStatement("SELECT available FROM Book WHERE ISBN = ?")) {

            // Kontrollera om lånet har tagits bort
            checkLoan.setInt(1, testISBN);
            checkLoan.setInt(2, studentID);
            ResultSet rsLoan = checkLoan.executeQuery();
            assertFalse(rsLoan.next());

            // Kontrollera studentens uppdaterade data
            checkStudent.setInt(1, studentID);
            ResultSet rsStudent = checkStudent.executeQuery();
            assertTrue(rsStudent.next());
            // Kontrollera borrowedNr och warning här...

            // Kontrollera att boken är tillgänglig igen
            checkBook.setInt(1, testISBN);
            ResultSet rsBook = checkBook.executeQuery();
            assertTrue(rsBook.next());
            assertTrue(rsBook.getBoolean("available"));
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }

    }

    @Test
    public void searchBookTest() {
        Student student = new Student(2021);
        String testTitle = "Scrum";
        student.searchBook(111155, testTitle, "Albert"); // ISBN och författarenamn används inte i sökningen

        try (Connection conn = MysqlConnection.connectToMysql();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Book WHERE title LIKE ?")
        ) {
            pstmt.setString(1, "%" + testTitle + "%");
            ResultSet rs = pstmt.executeQuery();

            boolean found = false;
            while (rs.next()) {
                if (rs.getString("title").contains(testTitle)) {
                    found = true;
                }
            }
            assertTrue(found); // Bekräfta att minst en bok hittades med den angivna titeln
        } catch (SQLException e) {
            fail("SQL Exception: " + e.getMessage());
        }
    }
    @Test
    public void submitRequestTest(){

        Student student = new Student(2021);

        String firstname = "Pontus";
        String lastname =  "Davidsson";
        String email = "puntos12@gmail.com";
        String level = "PHD";
        String type = "deletion";
        int studentID = 2021;
        student.submitRequest(firstname,lastname,email,level,type,studentID);

        // Kontrollera att förfrågan lagts till i databasen
        try (Connection conn = MysqlConnection.connectToMysql();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Request WHERE studentID = ?")) {

            pstmt.setInt(1, 2021); // Använd samma studentID som i submitRequest
            ResultSet rs = pstmt.executeQuery();

            assertTrue(rs.next()); // Kontrollera att en post finns


        } catch (SQLException e) {
            fail(e.getMessage());
        }

    }

}
