package com.wsep202.TradingSystem.domain.factory;

import com.wsep202.TradingSystem.domain.trading_system_management.Product;
import com.wsep202.TradingSystem.domain.trading_system_management.UserSystem;
import com.wsep202.TradingSystem.domain.trading_system_management.discount.VisibleDiscount;
import com.wsep202.TradingSystem.dto.VisibleDiscountDto;

import java.util.Calendar;
import java.util.HashMap;

/**
 * create all the new objects in the system
 */
public class FactoryObjects {
/*
create user in the system
 */
    public UserSystem createSystemUser(String userName, String firstName, String lastName, String password){
        return UserSystem.builder()
                .userName(userName)
                .password(password)
                .firstName(firstName)
                .lastName(lastName).build();
    }
/*
create visible discount
 */
    public VisibleDiscount createVisibleDiscount(Calendar endTime, double discount,
                                                 HashMap<Product, Integer> products) {
        return new VisibleDiscount(products,endTime,discount);
    }
}
