import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

public class LiberianMockito {

    @Test
    public void registerMemberMock() throws SQLException {

        Connection mockConnection = Mockito.mock(Connection.class);
        PreparedStatement mockPreparedStatement = Mockito.mock(PreparedStatement.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        MysqlConnection.setMockConnection(mockConnection);

        // Skapa instans av Liberian och anropa registerMember
        Liberian liberian = new Liberian();
        liberian.registerMember(1234, "Förnamn", "Efternamn", "email@example.com", true, "Level", 5, 0, 0, true);

        verify(mockPreparedStatement).setInt(1, 1234);
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    public void unRegisterMemberMock() throws Exception {

        Connection mockConnection = Mockito.mock(Connection.class);
        PreparedStatement mockPreparedStatement = Mockito.mock(PreparedStatement.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        MysqlConnection.setMockConnection(mockConnection);

        Liberian liberian = new Liberian();
        liberian.UnRegisterMember(1234);

        verify(mockPreparedStatement).setInt(1, 1234);
        verify(mockPreparedStatement).executeUpdate();
    }

    @Test
    public void getStudentByIdMOck() throws Exception {
        Connection mockConnection = Mockito.mock(Connection.class);
        PreparedStatement mockPreparedStatement = Mockito.mock(PreparedStatement.class);
        ResultSet mockResultSet = Mockito.mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);

        MysqlConnection.setMockConnection(mockConnection);

        Liberian liberian = new Liberian();
        Student result = liberian.getStudentById(2021);

        verify(mockPreparedStatement).setInt(1, 2021);
        verify(mockPreparedStatement).executeQuery();
        assertNotNull(result);
    }

    @Test
    public void suspendTest() throws Exception {
        Connection mockConnection = Mockito.mock(Connection.class);
        PreparedStatement mockPreparedStatement = Mockito.mock(PreparedStatement.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        MysqlConnection.setMockConnection(mockConnection);

        Liberian liberian = new Liberian();
        int studentID = 2021;
        liberian.suspend(studentID);

        verify(mockPreparedStatement).setInt(1, studentID);
        verify(mockPreparedStatement).executeUpdate();
    }
}

