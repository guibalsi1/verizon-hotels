package control;

import java.io.File;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBanco {

    private static String appDir() {
        try {
            return new File(
                    ConexaoBanco.class.getProtectionDomain().getCodeSource().
                            getLocation().toURI()).getParentFile().getParent();
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }
    private static final String URL = "jdbc:sqlite:"+ConexaoBanco.appDir()+"/data/banco_hotel.db";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            throw new RuntimeException("Erro na conexão com o banco de dados: " + e.getMessage());
        }
    }
}
