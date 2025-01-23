import java.sql.*;

import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Liberian implements ILiberian {

    private static final Logger logger = LogManager.getLogger(Liberian.class);

    public int workID;
    public String name;

    public Liberian() {
        this.workID = workID;
        this.name = name;
    }


    @Override
    public void registerMember( int studentID,  String firstname, String lastname, String email,
                                boolean registered, String level, int limitNr, int borrowedNr, int warning, boolean active) {

        String sql = "insert into Student(studentID, firstname, lastname,email, registered, level, limitNr, borrowedNr, warning, active )"+ "values (?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = MysqlConnection.connectToMysql() ;PreparedStatement pstmt = conn.prepareStatement(sql)){



            pstmt.setInt(1,studentID);
            pstmt.setString(2,firstname);
            pstmt.setString(3, lastname);
            pstmt.setString(4,email);
            pstmt.setBoolean(5,registered);
            pstmt.setString(6, level);
            pstmt.setInt(7,limitNr );
            pstmt.setInt(8,borrowedNr);
            pstmt.setInt(9,warning);
            pstmt.setBoolean(10, active);
            pstmt.executeUpdate();

        }
        catch (Exception e) {
            logger.warn(e.getMessage());

        }

    }

    @Override
    public void UnRegisterMember( int studentID) {

        String sql = "delete from Student WHERE studentID = ?";

        try (Connection conn = MysqlConnection.connectToMysql();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.warn(e.getMessage());
        }

    }

    @Override
    public void suspend( int studentID) {

        String sql = "UPDATE Student SET active = false WHERE studentID = ? AND warning > 2";

        try (Connection conn = MysqlConnection.connectToMysql();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.warn(e.getMessage());
        }
    }

    public Student getStudentById(int studentId) {
        Student student = null;
        try (Connection conn = MysqlConnection.connectToMysql();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Student WHERE studentID = ?")) {
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                student = new Student(2021);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return student;
    }
}
