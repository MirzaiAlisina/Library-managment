import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

public class BookStoreMockito {

    @Test
    public void addBookMock() throws SQLException {

        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        MysqlConnection.setMockConnection(mockConnection); // Antagande att du har en metod för att sätta en mock connection

        BookStore bookStore = new BookStore();
        bookStore.addBooks(123451, "Test Book", "Test Author", "Description", true);

        verify(mockPreparedStatement).setInt(1, 123451);
        verify(mockPreparedStatement).setString(2, "Test Book");
        verify(mockPreparedStatement).setString(3,"Test Author");
        verify(mockPreparedStatement).setString(4,"Description");
        verify(mockPreparedStatement).setBoolean(5,true);

        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    public void deleteBookMock() throws SQLException {

        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        // Sätt mock-anslutningen
        MysqlConnection.setMockConnection(mockConnection);

        // Skapa ett BookStore-objekt och kör deleteBooks
        BookStore bookStore = new BookStore();
        int testISBN = 123456;
        bookStore.deleteBooks(testISBN);

        verify(mockPreparedStatement).setInt(1, testISBN);
        verify(mockPreparedStatement).executeUpdate();
    }
}
