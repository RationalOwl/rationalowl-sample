package com.rationalowl.umsdemo.data.datasource;

import com.rationalowl.umsdemo.data.DataDef;

public class UserLocalDataSource extends JsonLocalDataSource<DataDef.User> {
    private static final String FILE_NAME = "user.json";

    private static UserLocalDataSource instance;

    public static synchronized UserLocalDataSource getInstance() {
        if (instance == null) {
            instance = new UserLocalDataSource();
        }

        return instance;
    }

    private UserLocalDataSource() {
        super(DataDef.User.class, FILE_NAME);
    }

    public DataDef.User getUser() {
        return getValue();
    }

    public void setUser(DataDef.User user) {
        setValue(user);
    }
}
