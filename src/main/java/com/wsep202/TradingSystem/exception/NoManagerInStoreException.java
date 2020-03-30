package com.wsep202.TradingSystem.exception;

public class NoManagerInStoreException extends RuntimeException {

    public NoManagerInStoreException(String userName, int storeId){
        super("The user name '" + userName + "' is not manager in the store number: '"+ storeId + "'");
    }
}
