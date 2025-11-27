package app;

import java.sql.Connection;
import java.sql.DriverManager;

public class DB {

    private static final String URL  = "jdbc:mysql://localhost:3306/rpg_game2?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "root";

    public static Connection conectar() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
