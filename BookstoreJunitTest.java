import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.*;
import java.sql.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BookstoreJunitTest {

    private static final Logger logger = LogManager.getLogger(BookstoreJunitTest.class);

    @Test
    public void addBookTest(){

        BookStore bookStore = new BookStore();

        int testISBN = 112233; // 6 integer
        String testTitle = "Database";
        String testAuthor = "Kalle";
        String testDescription = "everything about database ";
        boolean testAvailable = true;

        bookStore.addBooks(testISBN, testTitle, testAuthor, testDescription, testAvailable);

        // Kontrollera att boken lades till i databasen
        try (Connection conn = MysqlConnection.connectToMysql();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Book WHERE ISBN = 112233")) {

            pstmt.setInt(1, testISBN);
            ResultSet rs = pstmt.executeQuery();
            assertTrue(rs.next());
            assertEquals("TestTitle1", rs.getString("testTitle"));

        } catch (SQLException e) {
            logger.warn("SQLException: " + e.getMessage());
        }
    }
    @Test
    public void deleteBooksTest(){

        BookStore bookStore = new BookStore();
        int testISBN = 112233;
        bookStore.deleteBooks(testISBN);


        try (Connection conn = MysqlConnection.connectToMysql();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Book WHERE ISBN = testISBN")) {

            pstmt.setInt(1, testISBN);
            ResultSet rs = pstmt.executeQuery();

            assertFalse(rs.next(), "Book should be deleted");
        }
        catch (SQLException e) {
            logger.warn("SQLException: " + e.getMessage());
        }

    }

    @Test
    public void displayTEST(){ // denna metod är egentligen onödig då jag har seach-metoden som gör samma sak

        BookStore bookStore = new BookStore();
        bookStore.display();


        try (Connection conn = MysqlConnection.connectToMysql();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Book");
             ResultSet rs = pstmt.executeQuery()) {

            assertTrue(rs.next(), "The database should contain at least one entry.");


        }
        catch (SQLException e) {
            logger.warn("SQLException: " + e.getMessage());
        }
    }
}
