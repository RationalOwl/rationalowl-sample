package com.rationalowl.umsdemo.data;

import java.io.IOException;

public interface DataCallback<T> {
    void onResponse(T response);

    void onFailure(IOException e);
}
