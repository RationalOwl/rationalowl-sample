package com.rationalowl.umsdemo.data.datasource;

import com.rationalowl.umsdemo.data.DataDef;

public class ServerLocalDataSource extends JsonLocalDataSource<DataDef.Server> {
    private static final String FILE_NAME = "server.json";

    private static ServerLocalDataSource instance;

    public static synchronized ServerLocalDataSource getInstance() {
        if (instance == null) {
            instance = new ServerLocalDataSource();
        }

        return instance;
    }

    private ServerLocalDataSource() {
        super(DataDef.Server.class, FILE_NAME);
    }

    public DataDef.Server getServer() {
        return getValue();
    }

    public void setServer(DataDef.Server server) {
        setValue(server);
    }
}
