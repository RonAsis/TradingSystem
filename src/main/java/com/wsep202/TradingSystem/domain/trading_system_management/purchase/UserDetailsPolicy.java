/**
 * this class represents policy on user details
 * and describes the conditions the user needs to stands at
 * in aim to perform purchase in the store
 */
package com.wsep202.TradingSystem.domain.trading_system_management.purchase;

import com.wsep202.TradingSystem.domain.trading_system_management.BillingAddress;
import com.wsep202.TradingSystem.domain.trading_system_management.Product;
import com.wsep202.TradingSystem.domain.trading_system_management.UserSystem;
import com.wsep202.TradingSystem.domain.trading_system_management.notification.Notification;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;
@Setter
@Getter
@Slf4j
@Builder
public class UserDetailsPolicy extends PurchasePolicy {


    @Override
    public boolean isApproved(Purchase purchase, Map<Product, Integer> products,UserSystem user, BillingAddress userAddress) {
        if(!purchase.getCountriesPermitted().contains(userAddress.getCountry())) {
            //the country of the user is not in the allowed countries for purchase in store
            user.newNotification(Notification.builder().
                    content("Sorry, but your user details are incompatible with the store policy: " +
                            "store doesn't make deliveries to: "+userAddress.getCountry()).build());
            log.info("The purchase policy failed because the country of user '"+user.getUserName()+"' " +
                    "is not in the permitted countries of the store due to purchase policy with ID:" +
                    " :" + purchase.purchaseId);
            return false;
        }
        log.info("The purchase policy passed for user '"+user.getUserName()+"' " +
                "his country is permitted. purchase policy with ID:" +
                " :" + purchase.purchaseId);
        return true;
    }

}
