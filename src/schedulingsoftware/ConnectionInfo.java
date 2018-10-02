/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulingsoftware;

/**
 *
 * @author max.randolph
 */
public class ConnectionInfo {

    private String database;
    private String server;
    private String username;
    private String password;

    public ConnectionInfo(String database, String server, String username, String password) {
        this.server = server;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    String getDatabase() {
        return database;
    }

    void setDatabase(String value) {
        database = value;
    }

    String getServer() {
        return server;
    }

    void setServer(String value) {
        server = value;
    }

    String getUsername() {
        return username;
    }

    void setUsername(String value) {
        username = value;
    }

    String getPassword() {
        return password;
    }

    void setPassword(String value) {
        password = value;
    }

}
