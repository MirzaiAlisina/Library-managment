import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;

public class Student implements IStudent{


    private static final Logger logger = LogManager.getLogger(Student.class);

    public int studentID;
    public String firstname;
    public String lastname;
    public String email;
    public boolean registered;
    public String level;
    public int limitNr;
    public int borrowedNr;
    public int warning;
    public boolean active;


    public Student(int studentID, String firstname, String lastname, String email, boolean registered, String level, int limitNr, int borrowedNr, int warning, boolean active) {
        this.studentID = studentID;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.registered = registered;
        this.level = level;
        this.limitNr = limitNr;
        this.borrowedNr = borrowedNr;
        this.warning = warning;
        this.active = active;
    }

    public Student(int studentID) {
    }
    public Student(){

    }


    public int getStudentId() {
        return studentID;
    }
    public void setStudentId(int studentId) {
        this.studentID = studentId;
    }

    public String getFirstname() {
        return firstname;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        registered = registered;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getLimitNr() {
        return limitNr;
    }

    public void setLimitNr(int limitNr) {
        this.limitNr = limitNr;
    }

    public int getBorrowedNr() {
        return borrowedNr;
    }

    public void setBorrowedNr(int borrowedNr) {
        this.borrowedNr = borrowedNr;
    }

    public int getWarning() {
        return warning;
    }

    public void setWarning(int warning) {
        this.warning = warning;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }



    @Override
    public void borrowBook( int ISBN) {
        if (!registered || !active) {
            logger.error("Du måste vara registrerad och aktiv för att låna böcker.");
            return;
        }
        if (borrowedNr >= limitNr) {
            logger.error("Du har nått din lånegäns.");
            return;
        }

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(30);

        try (Connection conn = MysqlConnection.connectToMysql();
             PreparedStatement checkBook = conn.prepareStatement("SELECT ISBN FROM Book WHERE ISBN = ? AND available = true");
             PreparedStatement borrowBook = conn.prepareStatement("INSERT INTO BorrowedBook (ISBN, borrowedBy, startDate, endDate) VALUES (?, ?, ?, ?)");
             PreparedStatement updateBook = conn.prepareStatement("UPDATE Book SET available = false WHERE ISBN = ?");
             PreparedStatement updateStudent = conn.prepareStatement("Update Student set borrowedNr = borrowedNr+1 where studentID =?")

        ) {

            checkBook.setInt(1, ISBN);
            ResultSet rs = checkBook.executeQuery();
            if (!rs.next()) {
                logger.error("Boken är inte tillgänglig.");
                return;
            }

            borrowBook.setInt(1, ISBN);
            borrowBook.setInt(2, this.studentID);
            borrowBook.setDate(3, Date.valueOf(startDate));
            borrowBook.setDate(4, Date.valueOf(endDate));
            borrowBook.executeUpdate();

            updateBook.setInt(1, ISBN);
            updateBook.executeUpdate();

            updateStudent.setInt(1, studentID);
            updateStudent.executeUpdate();

            logger.info("Bok lånad!");
        } catch (SQLException e) {
            logger.error("SQL Exception: " + e.getMessage());
        }
    }

    @Override
    public void returnBook( int ISBN, int studentID) {
            LocalDate returnDate = LocalDate.now();

            try (Connection conn = MysqlConnection.connectToMysql();
                 PreparedStatement checkLoan = conn.prepareStatement("SELECT start, end FROM BorrowedBook WHERE ISBN = ? AND borrowedBy = ?");
                 PreparedStatement deleteLoan = conn.prepareStatement("DELETE FROM BorrowedBook WHERE ISBN = ? AND borrowedBy = ?");
                 PreparedStatement updateBook = conn.prepareStatement("UPDATE books SET available = true WHERE ISBN = ?");
                 PreparedStatement updateStudent = conn.prepareStatement("UPDATE Student SET borrowedNr = borrowedNr - 1, warning = warning + ? WHERE studentID = ?")) {

                // Kontrollera lån och förseningar
                checkLoan.setInt(1, ISBN);
                checkLoan.setInt(2, studentID);
                ResultSet rs = checkLoan.executeQuery();
                if (!rs.next()) {
                    logger.warn("Inget sådant lån hittades.");
                    return;
                }
                LocalDate endDate = rs.getObject("end", LocalDate.class);
                int warningUpdate = returnDate.isAfter(endDate) ? 1 : 0;

                // Ta bort lån från borrowedBooks
                deleteLoan.setInt(1, ISBN);
                deleteLoan.setInt(2, studentID);
                deleteLoan.executeUpdate();

                // Uppdatera bokens tillgänglighet i books-tabellen
                updateBook.setInt(1, ISBN);
                updateBook.executeUpdate();

                // Uppdatera studentens information
                updateStudent.setInt(1, warningUpdate);
                updateStudent.setInt(2, studentID);
                updateStudent.executeUpdate();

                if (warningUpdate == 1) {
                    logger.warn("Bok återlämnad med försening. En varning har lagts till.");
                } else {
                    logger.info("Bok återlämnad.");
                }
            } catch (SQLException e) {
                logger.warn("SQL Exception: " + e.getMessage());
            }
        }


    @Override
    public void searchBook( int ISBN, String title, String author) {
        String sql = "SELECT * FROM Book WHERE title LIKE ?";

        try (Connection conn = MysqlConnection.connectToMysql();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + title + "%");

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println("Title: " + title + ", Author: " + author + ", ISBN: " + ISBN);

            }
        } catch (SQLException e) {
            logger.warn("SQL Exception: " + e.getMessage());
        }

    }

    @Override
    public void submitRequest(String firstname, String lastname, String email, String level, String type, int studentID ) {

        String sql = "INSERT INTO Request (firstname, lastname, email, level, type, studentID ) VALUES (?, ?, ?, ?,?,?)";

        try (Connection conn = MysqlConnection.connectToMysql();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, firstname);
            pstmt.setString(2, lastname);
            pstmt.setString(3, email);
            pstmt.setString(4, level);
            pstmt.setString(5,type);
            pstmt.setInt(6,studentID);

            pstmt.executeUpdate();
            logger.info("Förfrågan om " + type + " skickad.");
        } catch (SQLException e) {
            logger.warn("SQL Exception: " + e.getMessage());
        }

    }


}
