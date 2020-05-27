/**
 * this class represents a conditional discount to apply at the store level.
 * apply the discount on the sum of the purchase cost.
 */
package com.wsep202.TradingSystem.domain.trading_system_management.discount;
import com.wsep202.TradingSystem.domain.exception.IllegalProductPriceException;
import com.wsep202.TradingSystem.domain.exception.NotValidEndTime;
import com.wsep202.TradingSystem.domain.trading_system_management.Product;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Calendar;
import java.util.Map;

@Setter
@Getter
@Builder
@Slf4j
public class ConditionalStoreDiscount extends DiscountPolicy {

    /**
     * the minimal price of purchase to apply the discount from
     */
    private double minPrice;
    @Override
    public void applyDiscount(Discount discount, Map<Product, Integer> products) {
        //The discount time is not expired yet
        if (!isExpired(discount) && !discount.isApplied()) {
            double totalPurchasedCost = getTotalPurchasedCost(products);
            if (minPrice <= totalPurchasedCost) {
                products.keySet().forEach(product -> {
                    setCostAfterDiscount(discount, product, calculateDiscount(discount, product.getOriginalCost()));
                });
            }
            discount.setApplied(true);
        } else {  //check if needs to update back the price
            undoDiscount(discount, products);
        }
    }

    @Override
    public boolean isApprovedProducts(Discount discount, Map<Product, Integer> products) {
        return !isExpired(discount) && minPrice <= getTotalPurchasedCost(products);
    }

    @Override
    public void undoDiscount(Discount discount, Map<Product, Integer> products) {
        if (discount.isApplied()) {
            products.keySet().forEach(product -> {
                double discountCost = calculateDiscount(discount, product.getOriginalCost());
                product.setCost(product.getCost() + discountCost);    //update price
            });
        }
    }
}
