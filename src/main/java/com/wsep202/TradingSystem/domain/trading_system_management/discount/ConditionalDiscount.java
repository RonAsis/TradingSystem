package com.wsep202.TradingSystem.domain.trading_system_management.discount;
/**
 * discount which applied on related products under some conditions
 */

import com.wsep202.TradingSystem.domain.trading_system_management.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public abstract class ConditionalDiscount extends DiscountPolicy{

    protected String conditionDescription;
    //products with this discount with their required purchase amount to activate discount



    public ConditionalDiscount(Calendar endTime, double discountPercentage, String description) {
        this.endTime = endTime;
        this.discountPercentage = discountPercentage;
        //contains all products in store that are under this discount
        this.productsUnderThisDiscount = new HashMap<>();
        discountIdAcc = getdiscountIdAcc();
        this.id = discountIdAcc;
        this.conditionDescription = description;
    }



    /**
     * in case the condition is approved update products prices
     * @param products to update by discount
     */
    public abstract void applyDiscount(Map<Product,Integer> products);

    /**
     * this method will be called in case the discount expired and has to be undone
     * @param products to update
     */
    public abstract void undoDiscount(Map<Product,Integer> products);

    public String toString(){
        return "Discount details: discount percentage: "+discountPercentage+" ,valid util: "+endTime+"" +
                " condition: "+this.conditionDescription;
    }

    /**
     * add products to apply the discount on.
     * @param products
     * @return
     */
    public boolean addProductToThisDiscount(Map<Product,Integer> products) {
        try{
            this.productsUnderThisDiscount.putAll(products);
            return true;
        }catch (NullPointerException e){
            return false;
        }
    }
}
