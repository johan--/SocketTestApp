package com.surinov.alexander.sockettestapp.data.source;

public interface ConnectionControl {
    
    void openConnection();

    void closeConnection();

    boolean isConnectionOpened();
}
