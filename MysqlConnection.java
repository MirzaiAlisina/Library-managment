import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlConnection {
    private static final Logger logger = LogManager.getLogger(MysqlConnection.class);


       private static final String url = "jdbc:mysql://194.47.179.74: 23306/ 1IK173-VT24-A";
       private static final String user = "am224xm";
       private static final String password = "am224xm";

       private static Connection mockConnection = null;



       // Metod för att sätta en mock-anslutning
       public static void setMockConnection(Connection mockConn) {
           mockConnection = mockConn;

       }

       public static Connection connectToMysql (){

           if (mockConnection != null) {
               return mockConnection; // Returnera mock-anslutningen om den är inställd
           }

        Connection connection = null;
        logger.warn("Database connecting...");

        try {
            connection = DriverManager.getConnection (url, user, password);
            logger.warn("Database connected");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }

}

