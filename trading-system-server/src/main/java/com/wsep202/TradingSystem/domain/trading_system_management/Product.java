package com.wsep202.TradingSystem.domain.trading_system_management;

import com.google.common.base.Strings;
import com.wsep202.TradingSystem.domain.exception.IllegalProductCostOrAmountException;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
@Entity
public class Product {

    /**
     * the product serial number
     */
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int productSn;

    /**
     * the name of the product
     */
    @NotBlank(message = "product name must not be empty or null")
    private String name;

    /**
     * the category of the product
     */
    @NotNull(message = "Must be category")
    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    /**
     * the amount of this product in the store (=>storeId)
     */
    @Min(value = 0, message = "Must be greater than or equal zero")
    @Builder.Default
    private int amount = 0;

    /**
     * the cost of this product
     */
    @Min(value = 0, message = "Must be greater than or equal zero")
    @Builder.Default
    private double cost = 0;

    /**
     * the original price of this product before discount if applied one
     */
    @Min(value = 0, message = "Must be greater than or equal zero")
    private double originalCost = 0;

    /**
     * the rank of this product
     */
    @Min(value = 1, message = "Must be greater than or equal zero")
    @Max(value = 9, message = "Must be smaller than or equal 5")
    @Builder.Default
    private int rank = 5;

    /**
     * the storeId that connected to the store that the product exists in it.
     */
    private int storeId;

    public Product(String name, ProductCategory category, int amount, double cost, int storeId){
        if (cost<0 || amount<0) {
            log.error("cost and amount can't be negative");
            throw new IllegalProductCostOrAmountException(productSn);
        }
        this.name = name;
        this.category = category;
        this.amount = amount;
        this.cost = cost;
        this.originalCost = cost;
        this.rank = 5;
        this.storeId = storeId;
    }

    /**
     * Increases the amount of products with newAmount.
     * @param addedAmount - the amount to add to the original amount
     *                  (must be greater then zero).
     * @return - true if succeeded, else returns false.
     */
    public boolean increasesProductAmount(int addedAmount){
        boolean canIncrease = false;
        if (addedAmount > 0) { // can't increase with negative or zero newAmount
            log.info("The amount in product: " + productSn + " raised from: " + amount + " to: "+ amount+addedAmount + ".");
            amount += addedAmount;
            canIncrease = true;
        }
        log.error("The function 'increasesProductAmount(int addedAmount)' can't accept negative/zero amount!");
        return canIncrease;
    }

    /**
     * Reduces the amount of products with removalAmount.
     * @param removalAmount - the amount to reduce from the original amount
     *                  (must be greater then zero).
     * @return - true if succeeded, else returns false.
     */
    public boolean reducesProductAmount(int removalAmount){
        boolean canReduce = false;
        if(removalAmount > 0){ // can't reduce with negative or zero newAmount
            log.info("The amount in product: " + productSn + " raised from: " + amount + " to: "+ amount+removalAmount + ".");
            amount -= removalAmount;
            canReduce = true;
        }
        log.error("The function 'increasesProductAmount(int addedAmount)' can't accept negative/zero amount!");
        return canReduce;
    }

    public Product cloneProduct(){
        Product returnedProduct = new Product(name,category,amount,cost,storeId);
        returnedProduct.setProductSn(productSn);
        returnedProduct.setOriginalCost(originalCost);
        returnedProduct.setRank(rank);
        return returnedProduct;
    }
}
