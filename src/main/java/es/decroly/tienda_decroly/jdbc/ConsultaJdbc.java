package es.decroly.tienda_decroly.jdbc;

import java.math.BigDecimal;
import java.sql.*;

public class ConsultaJdbc {

    public static void main(String[] args) throws SQLException {
        String url = "jdbc:mysql://localhost:3306/tienda_decroly";
        String sql = "SELECT id, nombre, precio, stock FROM prueba_jdbc WHERE precio < ?";

        // 1. Abrir la conexion
        try (Connection con = DriverManager.getConnection(url, "tienda", "Tienda2026!");
             // 2. Preparar la consulta
             PreparedStatement ps = con.prepareStatement(sql)) {

            // 3. Rellenar los parametros
            ps.setBigDecimal(1, new BigDecimal("50"));

            // 4. Ejecutar y recorrer los resultados
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    long id = rs.getLong("id");
                    String nombre = rs.getString("nombre");
                    BigDecimal precio = rs.getBigDecimal("precio");
                    int stock = rs.getInt("stock");
                    System.out.println(id + " - " + nombre + " - " + precio + " EUR - " + stock + " uds");
                }
            }
        }   // 5. try-with-resources cierra todo, tambien si hay error


    }
}
