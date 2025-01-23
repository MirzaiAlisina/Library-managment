import java.sql.*;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BookStore implements IBookStore {

    private static final Logger logger = LogManager.getLogger(BookStore.class);



    @Override
    public void addBooks(int ISBN, String title, String author, String description, boolean available) {

        String sql = "insert into Book(ISBN, title, author, description, available  ) values (?,?,?,?,?)";

        try (Connection conn = MysqlConnection.connectToMysql();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, ISBN);
            pstmt.setString(2, title);
            pstmt.setString(3, author);
            pstmt.setString(4, description);
            pstmt.setBoolean(5,available);

            pstmt.executeUpdate();

        }
        catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    @Override
    public void deleteBooks(int ISBN) {
        String sql = "delete from Book WHERE ISBN = ?";

        try (Connection conn = MysqlConnection.connectToMysql();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, ISBN);
            pstmt.executeUpdate();
        }
        catch (SQLException e) {
            logger.warn(e.getMessage());
        }

    }

    @Override
    public void display() {

        String sql = "select *from Book";
        try (Connection conn = MysqlConnection.connectToMysql();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while(rs.next()){
                int ISBN = rs.getInt("ISBN");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String description = rs.getString("description");
                boolean available = rs.getBoolean("available");

                logger.error("ISNB: " + ISBN + ", author: " + author + ", title: " + title + ", description: " + description + "available: " + available);
            }

        }

        catch (SQLException e) {
        logger.warn(e.getMessage());
    }





    }
}
