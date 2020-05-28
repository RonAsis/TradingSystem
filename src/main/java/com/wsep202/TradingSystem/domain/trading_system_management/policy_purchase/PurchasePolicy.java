/**
 * this class determines the rules of purchase the user and his bag should stands in.
 */
package com.wsep202.TradingSystem.domain.trading_system_management.policy_purchase;
/**
 * defines the purchase policy interface
 */

import com.wsep202.TradingSystem.domain.trading_system_management.purchase.BillingAddress;
import com.wsep202.TradingSystem.domain.trading_system_management.Product;
import lombok.Data;

import javax.persistence.*;
import java.util.Map;

@Data
@Entity
public abstract class PurchasePolicy {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
    /**
     * check if the purchase details stands in the purchase policy of the store
     * purchase details: user details/shopping bag details
     * @param purchase the purchase policy to check
     * @param products in bag to purchase
     * @param userAddress the shippment details of the user
     * @return true in case the purchase in the store is legal for this policy
     */
    public abstract boolean isApproved(Purchase purchase, Map<Product,Integer> products, BillingAddress userAddress);

}

