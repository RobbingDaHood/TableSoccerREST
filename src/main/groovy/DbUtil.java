import groovy.sql.GroovyRowResult;
import groovy.sql.Sql;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by super on 30/10/2016.
 */
public class DbUtil {
    public static final String url = "jdbc:mysql://localhost:3306/NykreditBF?useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    public static final String user = "root";
    public static final String password = "";
    public static final String driver = "com.mysql.jdbc.Driver";

    public static String execute(String query) {
        Sql sql = null;
        try {
            sql = Sql.newInstance(url, user, password, driver);
            sql.execute(query);
            sql.close();
        } catch (ClassNotFoundException e) {
            return e.toString();
        } catch (SQLException e) {
            return e.toString();
        } finally {
            if (sql != null) {
                sql.close();
            }
        }
        
        return "OK";
    }

    public static List<GroovyRowResult> query(String query) throws SQLException, ClassNotFoundException {
        Sql sql = Sql.newInstance(url, user, password, driver);
        try {
            return sql.rows(query);
        } finally {
            sql.close();
        }
    }
}
