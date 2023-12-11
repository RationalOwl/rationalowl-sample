package com.rationalowl.umsdemo.domain;

public class UserRepository extends JsonRepository<User> {
    private static final String FILE_NAME = "user.json";

    private static UserRepository instance;

    public static synchronized UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }

        return instance;
    }

    private UserRepository() {
        super(User.class, FILE_NAME);
    }

    public User getUser() {
        return getValue();
    }

    public void setUser(User user) {
        setValue(user);
    }
}
