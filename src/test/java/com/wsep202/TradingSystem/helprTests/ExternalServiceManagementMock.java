package com.wsep202.TradingSystem.helprTests;

import com.wsep202.TradingSystem.domain.trading_system_management.*;
import externals.ChargeSystem;
import externals.SecuritySystem;
import externals.SupplySystem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ExternalServiceManagementMock  extends ExternalServiceManagement {

    public static boolean isAuthenticatedUserPassword = true;
    public static  List<Integer> storesIdsCanBuy = new LinkedList<>();
    public static boolean cancelCharge = true;
    public static boolean deliver = true;
    public static boolean cancelDeliver = true;

    @Override
    public void connect() {
    }

    @Override
    public PasswordSaltPair getEncryptedPasswordAndSalt(String password) {
        return new PasswordSaltPair(password,"1");
    }

    @Override
    public boolean isAuthenticatedUserPassword(String password, UserSystem user){
        return isAuthenticatedUserPassword;
    }

    @Override
    public List<Integer> charge(PaymentDetails paymentDetails, ShoppingCart cart){
        return storesIdsCanBuy;
    }

    public boolean cancelCharge(PaymentDetails paymentDetails, ShoppingCart cart){
        return cancelCharge;
    }


    public boolean deliver(BillingAddress addressInfo,ShoppingCart cart){
        return deliver;
    }

    public boolean cancelDelivery(BillingAddress addressInfo, ShoppingCart cart){
      return cancelDeliver;
    }
}
