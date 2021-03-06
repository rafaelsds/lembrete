package net.unesc.banco;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import net.unesc.exceptions.BancoException;

public class Conexao {
        
    public static Connection getConnection()throws BancoException {;
        Connection conn = null;
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            conn = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/agendadb", "sa", "");
        } catch (SQLException e) {
            throw new BancoException("Problemas ao conectar no banco de dados");
        } catch (ClassNotFoundException e) {
            throw new BancoException("O driver não foi configurado corretamente");
        }

        return conn;
    }
}
