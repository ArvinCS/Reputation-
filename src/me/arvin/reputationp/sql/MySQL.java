package me.arvin.reputationp.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

import me.arvin.reputationp.Main;

public class MySQL extends Database {
    private String SQLiteCreateTokensTable = "CREATE TABLE IF NOT EXISTS reputation (" + // make sure to put your table name in here too.
            "`player` varchar(255) NOT NULL," + // This creates the different colums you will save data too. varchar(32) Is a string, int = integer
            "`likes` int(32) NOT NULL," +
            "`dislikes` int(32) NOT NULL," +
            "`reputation` int(32) NOT NULL," +
            "`point` int(32) NOT NULL," +
            "`follower` varchar(32) NOT NULL," +
            "`ignorer` varchar(32) NOT NULL," +
            "PRIMARY KEY (`player`)" +  // This is creating 3 colums Player, Kills, Total. Primary key is what you are going to use as your indexer. Here we want to use player so
            ");";
    
    private String host;
    private int port;
    private String database;
    private String user;
    private String password;

    private Connection connection;
    
    private String getHost() {
        return this.host;
    }

    private int getPort() {
        return this.port;
    }

    private String getDataBase() {
        return this.database;
    }

    private String getUser() {
        return this.user;
    }

    private String getPassword() {
        return this.password;
    }

    public Connection getSQLConnection() {
        try {
            if(this.connection!=null&&!this.connection.isClosed()){
                return connection;
            }
            this.connection = DriverManager.getConnection("jdbc:mysql://" + getHost() + ":" + getPort() + "/" + getDataBase() + "?user=" + getUser() + "&password=" + getPassword());
            return this.connection;
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE,"MySQL exception on initialize", ex);
        }
        return null;
    }

    private void setHost(String host) {
        this.host = host;
    }

    private void setPort(int port) {
        this.port = port;
    }

    private void setDataBase(String datebase) {
        this.database = datebase;
    }

    private void setUser(String user) {
        this.user = user;
    }

    private void setPassword(String password) {
        this.password = password;
    }

    public MySQL(Main instance, String host, int port, String datebase, String user, String password) {
    	super(instance);
        setHost(host);
        setPort(port);
        setDataBase(datebase);
        setUser(user);
        setPassword(password);
    }

    public void load() {
        connection = getSQLConnection();
        try {
            Statement s = getSQLConnection().createStatement();
            s.executeUpdate(SQLiteCreateTokensTable);
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        initialize();
    }
    
    /*private boolean openConnection() {
        if (!isConnected()) {
            try {
                setConnection(DriverManager.getConnection("jdbc:mysql://" + getHost() + ":" + getPort() + "/" + getDataBase() + "?user=" + getUser() + "&password=" + getPassword()));
            }
            catch (SQLException e) {
                e.printStackTrace();

                return false;
            }
        }
        return true;
    }


    private boolean closeConnection() {
        if (isConnected()) {
            try {
                getSQLConnection().close();
            }
            catch (SQLException e) {
                e.printStackTrace();

                return false;
            }
        }
        return true;
    }

    public boolean isConnected() {
        try {
            if (getSQLConnection() == null) {
                return false;
            }
            if (getSQLConnection().isClosed()) {
                return false;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }*/
}
