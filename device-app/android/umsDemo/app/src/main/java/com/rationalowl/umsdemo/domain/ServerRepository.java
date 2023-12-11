package com.rationalowl.umsdemo.domain;

public class ServerRepository extends JsonRepository<Server> {
    private static final String FILE_NAME = "server.json";

    private static ServerRepository instance;

    public static synchronized ServerRepository getInstance() {
        if (instance == null) {
            instance = new ServerRepository();
        }

        return instance;
    }

    private ServerRepository() {
        super(Server.class, FILE_NAME);
    }

    public Server getServer() {
        return getValue();
    }

    public void setServer(Server server) {
        setValue(server);
    }
}
