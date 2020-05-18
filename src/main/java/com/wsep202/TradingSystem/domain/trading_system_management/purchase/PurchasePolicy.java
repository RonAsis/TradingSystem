/**
 * this class determines the rules of purchase the user and his bag should stands in.
 */
package com.wsep202.TradingSystem.domain.trading_system_management.purchase;

import com.wsep202.TradingSystem.domain.trading_system_management.BillingAddress;
import com.wsep202.TradingSystem.domain.trading_system_management.Product;
import com.wsep202.TradingSystem.domain.trading_system_management.UserSystem;
import lombok.Data;
import lombok.Synchronized;

import java.util.Map;

@Data
public abstract class PurchasePolicy {
    protected static int purchaseIdAcc = 0;
    protected int id;
    /**
     * check if the purchase details stands in the purchase policy of the store
     * purchase details: user details/shopping bag details
     * @param products in bag to purchase
     * @param user the purchasing user
     * @param userAddress the shippment details of the user
     * @return true in case the purchase in the store is legal for this policy
     */
    public abstract boolean isApproved(Map<Product,Integer> products,UserSystem user, BillingAddress userAddress);
    @Synchronized
    protected int getPurchaseIdAcc(){
        return purchaseIdAcc++;
    }
}

