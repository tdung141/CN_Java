package lab05.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBHelper {
    private static final String URL =
            "jdbc:mysql://localhost:3306/minishop_db?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Ho_Chi_Minh";
    private static final String USER = "root";
    private static final String PASSWORD = ""; 
    
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Khong tim thay MySQL JDBC Driver. Kiem tra pom.xml!", e);
        }
    }

    private DBHelper() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            boolean ok = conn != null && !conn.isClosed();
            System.out.println(ok ? "Ket noi CSDL thanh cong!" : "Ket noi CSDL that bai!");
            return ok;
        } catch (SQLException e) {
            System.out.println("Ket noi CSDL that bai: " + e.getMessage());
            return false;
        }
    }
}
