package com.wsep202.TradingSystem.domain.trading_system_management;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@Builder
@Slf4j
public class Product {

    @Min(value = 0, message = "Must be greater than or equal zero")
    private int productSn;

    private static int productSnAcc = 0;

    @NotBlank(message = "product name must not be empty or null")
    private String name;

    @NotNull(message = "Must be category")
    private ProductCategory category;

    @Min(value = 0, message = "Must be greater than or equal zero")
    @Builder.Default
    private int amount = 0;

    @Min(value = 0, message = "Must be greater than or equal zero")
    @Builder.Default
    private double cost = 0;

    @Min(value = 0, message = "Must be greater than or equal zero")
    @Max(value = 5, message = "Must be smaller than or equal 5")
    @Builder.Default
    private int rank = 0;

    private int createProductSkuAcc(){
        return productSnAcc++;
    }

    public Product(String name, ProductCategory category, int amount, double cost){
        this.productSn = createProductSkuAcc();
        this.name = name;
        this.category = category;
        this.amount = amount;
        this.cost = cost;
        this.rank = 0;
    }
    /**
     * Increases the amount of products
     */
    public void increasesProduct(){
        amount++;
    }

    /**
     * Reduces the amount of products
     */
    public boolean reducesProduct(){
        boolean canReduce = false;
        if(amount > 0){
            amount --;
            canReduce = true;
        }
        return canReduce;
    }
}
