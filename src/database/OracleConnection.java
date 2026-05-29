// conexion a oracle
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OracleConnection {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:orcl";
    private static final String USER = "ESTUDIANTE";  // Cambia por tu usuario de la VM
    private static final String PASSWORD = "DKK1989";  // Cambia por tu contraseña
    
    private static Connection connection = null;
    
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Conexion a Oracle establecida"); /// conexion esitosa
            } catch (ClassNotFoundException e) {
                System.err.println("Driver JDBC no encontrado: " + e.getMessage());
                throw new SQLException(e);
            }
        }
        return connection;
    }
    
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Conexion cerrada");
            } catch (SQLException e) {
                System.err.println("Error al cerrar conexion: " + e.getMessage());
            }
        }
    }
}
