package database;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private static DatabaseConnection instancia;


    private Connection connection;
    private static Properties reader = new Properties();

    static {
        try {
            InputStream in = DatabaseConnection.class.getResourceAsStream("/db.config");
            reader.load(in);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private DatabaseConnection() throws SQLException {

        try{
            this.connection = DriverManager.getConnection(reader.getProperty("db.url"), reader.getProperty("db.user"), reader.getProperty("db.password"));

        }catch(SQLException e){
            throw new SQLException("Error al conectarnos a la base de datos -" + e.getMessage());
        }
    }
    public static DatabaseConnection getInstancia() throws SQLException {
        if(instancia != null){
            return instancia;
        }else{
            instancia = new DatabaseConnection();
            return instancia;
        }
    }

    public Connection getConnection() {
        return connection;
    }
}

