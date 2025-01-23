import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


public class StudentMockito {


    @Test
    public void borrowBookTest() throws Exception {

        Connection mockConnection = Mockito.mock(Connection.class);
        PreparedStatement mockCheckBook = Mockito.mock(PreparedStatement.class);
        PreparedStatement mockBorrowBook = Mockito.mock(PreparedStatement.class);
        PreparedStatement mockUpdateBook = Mockito.mock(PreparedStatement.class);
        PreparedStatement mockUpdateStudent = Mockito.mock(PreparedStatement.class);
        ResultSet mockResultSet = Mockito.mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockCheckBook).thenReturn(mockBorrowBook).thenReturn(mockUpdateBook).thenReturn(mockUpdateStudent);
        when(mockCheckBook.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true); // Simulerar att boken finns och är tillgänglig

        Student student = new Student();
        student.setStudentId(2021); // Exempelvärde
        student.setRegistered(true);
        student.setActive(true);
        student.setBorrowedNr(0);
        student.setLimitNr(5);

        int ISBN = 123456;
        student.borrowBook(ISBN);

        verify(mockCheckBook).setInt(1, ISBN);
        verify(mockBorrowBook, times(1)).setInt(anyInt(), anyInt());
        verify(mockBorrowBook).setDate(anyInt(), any());
        verify(mockUpdateBook).setInt(1, ISBN);
        verify(mockUpdateStudent).setInt(1, 2021);
        verify(mockBorrowBook).executeUpdate();
        verify(mockUpdateBook).executeUpdate();
        verify(mockUpdateStudent).executeUpdate();


        MysqlConnection.setMockConnection(null);
    }

    @Test
    public void  returnBookMock() throws SQLException {


        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);


        LocalDate mockEndDate = LocalDate.now(); // Eller vilket datum du nu önskar använda i testet
        when(mockResultSet.getObject("end", LocalDate.class)).thenReturn(mockEndDate);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);


        MysqlConnection.setMockConnection(mockConnection);

        int studentID = 2000;
        String firstname = "Kalle";
        String lastname = "Jhonsson";
        String email = "kalle12@gmail.com";
        boolean registered = true;
        String level = "PHD";
        int limitNr = 7;
        int borrowedNr = 0;
        int warning = 0;
        boolean active = true;
        Student student = new Student(studentID, firstname, lastname, email, registered, level, limitNr, borrowedNr, warning, active);

        int ISBN = 123459;

        student.returnBook(ISBN, studentID);

        // Verifiera interaktioner
        verify(mockPreparedStatement).setInt(1, ISBN);
        verify(mockPreparedStatement).setInt(2, studentID);
        verify(mockPreparedStatement, times(2)).executeUpdate(); // Justera 'times' baserat på hur många gånger update kallas

    }
    @Test
    public void searchBookMock() throws SQLException {
        // Skapa mock-objekt
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);


        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false); // Returnerar sant första gången, sedan falskt


        MysqlConnection.setMockConnection(mockConnection);

        Student instance = new Student(); // Ersätt med din klass som har searchBook-metoden
        instance.searchBook(123456, "Scrum", "Albert");


        verify(mockPreparedStatement).setString(1, "%Scrum%");
        verify(mockPreparedStatement).executeQuery();

        // Rensa mock-anslutningen efter testet
        MysqlConnection.setMockConnection(null);
    }

    @Test
    public void submitRequestMock() throws SQLException {

        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);

        // Konfigurera beteendet för mock-objekten
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        MysqlConnection.setMockConnection(mockConnection);

        // Skapa och använd objektet som har submitRequest-metoden
        Student student = new Student();
        student.submitRequest("Pontus", "Davidsson", "puntos12@gmail.com", "PHD", "deletion", 2021);

        // Verifiera att korrekta metoder anropas på PreparedStatement
        verify(mockPreparedStatement).setString(1, "Pontus");
        verify(mockPreparedStatement).setString(2, "Davidsson");
        verify(mockPreparedStatement).setString(3, "puntos12@gmail.com");
        verify(mockPreparedStatement).setString(4, "PHD");
        verify(mockPreparedStatement).setString(5, "deletion");
        verify(mockPreparedStatement).setInt(6, 2021);
        verify(mockPreparedStatement).executeUpdate();

        // Rensa mock-anslutningen efter testet
        MysqlConnection.setMockConnection(null);

    }
}
