package com.app.grocer;

import com.google.firebase.database.DataSnapshot;

public interface Callback {

    public abstract void onSuccessCallback(DataSnapshot snapshot);
}
