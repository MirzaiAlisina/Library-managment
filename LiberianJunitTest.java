import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.*;
import java.sql.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LiberianJunitTest {

    private static final Logger logger = LogManager.getLogger(LiberianJunitTest.class);

    @Test
    public void registerMemberTest(){

        Liberian liberian = new Liberian();

        int studentID = 2023;
        String firstname = "Emma";
        String lastname = "Gustavsson";
        String email = "Emma812@gmail.com";
        boolean registered = true;
        String level= "Bachelor";
        int limitNr = 3;
        int borrowedNr = 0;
        int warning = 3;
        boolean active = true;

        liberian.registerMember(studentID,firstname,lastname,email,registered,level,limitNr,borrowedNr,warning,active);
        Student updatedStudent = liberian.getStudentById(2023);

        try (Connection conn = MysqlConnection.connectToMysql();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Student WHERE studentID = 2021")) {

            pstmt.setInt(1, studentID);
            ResultSet rs = pstmt.executeQuery();
            assertTrue(rs.next());

        } catch (SQLException e) {
            logger.warn("SQLException: " + e.getMessage());
        }

    }

    @Test
    public void UnRegisterMemberTest(){

        Liberian liberian = new Liberian();
        int testStudentID = 2023; //fyra integer
        liberian.UnRegisterMember(testStudentID);

        try (Connection conn = MysqlConnection.connectToMysql();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Student WHERE studentID = 2023")) {

            pstmt.setInt(1, testStudentID);
            ResultSet rs = pstmt.executeQuery();

            assertFalse(rs.next(), "Account should be deleted");
        }
        catch (SQLException e) {
            logger.warn("SQLException: " + e.getMessage());
        }

    }

    @Test

    public void suspendTest(){
        Liberian liberian = new Liberian();

        int testStudentID = 2023;
        liberian.suspend(testStudentID);

        try (Connection conn = MysqlConnection.connectToMysql();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Student WHERE studentID = 2023")) {

            pstmt.setInt(1, testStudentID);
            ResultSet rs = pstmt.executeQuery();

            assertFalse(rs.next(), "Account should be suspended");
        }
        catch (SQLException e) {
            logger.warn("SQLException: " + e.getMessage());
        }
    }
}
