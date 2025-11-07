package com.myapp.app.util;

import com.myapp.app.entity.User;

public class RequestContext {
    private static final ThreadLocal<User> currentUser = new ThreadLocal<>();

    public static void setUser(User user) { currentUser.set(user); }
    public static User getUser() { return currentUser.get(); }
    public static void clear() { currentUser.remove(); }
}
