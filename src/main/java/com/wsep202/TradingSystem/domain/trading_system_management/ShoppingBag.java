package com.wsep202.TradingSystem.domain.trading_system_management;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;


@Data
@Slf4j
public class ShoppingBag {

    //private Map<Integer, Integer> mapProductSnToAmount;
    NumberFormat formatter = new DecimalFormat("#.##");

    /**
     * the store of the products
     */
    private Store storeOfProduct;
    /**
     * list of all of the products and the amount of each product
     */
    private Map<Product, Integer> productListFromStore;
    /**
     * shows the cost of all of the products in bag
     */
    private double totalCostOfBag;
    /**
     * shows how many types of products in the bag
     */
    private int numOfProductsInBag;

    public ShoppingBag(Store storeOfProduct){
        this.storeOfProduct = storeOfProduct;
        productListFromStore = new HashMap<>();
        totalCostOfBag = 0;
        numOfProductsInBag = 0;
    }

    /**
     * This method is used for adding products to the bag.
     * In case if the product is null, the method won't do anything.
     * @param productToAdd - the product that needs to be added.
     * @return true if the product was added successfully to the bag, false if the product was null.
     */
    public boolean addProductToBag(Product productToAdd, int amountOfProduct){
        if (productToAdd == null) {
            log.error("A null product was trying to be added to the bag");
            return false;
        }
        if(productToAdd.getStoreId() != storeOfProduct.getStoreId()){
            log.error("Store id and product store id does not mach");
            return false;
        }
        else {
            if (productListFromStore.containsKey(productToAdd)) {
                log.info("Calls changeAmountOfProductInBag to update the amount of the exciting product");
                return changeAmountOfProductInBag(productToAdd, amountOfProduct);
            }
            if (amountOfProduct <= 0){
                log.error("The amount of the product needs to be greater than zero");
                return false;
            }
            log.info("Add new product to the bag");
            productListFromStore.put(productToAdd, amountOfProduct);
            numOfProductsInBag += 1;
            totalCostOfBag += (productToAdd.getCost() * amountOfProduct);
            totalCostOfBag = Double.parseDouble(formatter.format(totalCostOfBag));
            return true;
        }
    }

    /**
     * This method is used for deleting a product from the bag.
     * In case if the product does not exists or if it's null, the method won't do anything.
     * @param productToRemove - the product that needs to be removed.
     * @return true if the product was in the bag and removed from list, else false.
     */
    public boolean removeProductFromBag(Product productToRemove){
        if (productToRemove == null){
            log.error("A null product was trying to be deleted");
            return false;
        }
        if(productToRemove.getStoreId() != storeOfProduct.getStoreId()){
            log.error("Store id and product store id does not mach");
            return false;
        }
        if (productListFromStore.containsKey(productToRemove)){
            log.info("A product was removed from the bag");
            totalCostOfBag -= (productListFromStore.get(productToRemove)*productToRemove.getCost());
            totalCostOfBag = Double.parseDouble(formatter.format(totalCostOfBag));
            numOfProductsInBag -= 1;
            productListFromStore.remove(productToRemove);
            return true;
        }
        log.error("The product was not found");
        return false;
    }

    /**
     * This method used to change the amount of an exciting product.
     * In case if the calculation of amount of the product is less than 0, the method will return false.
     * @param product - the product that is already in the bag
     * @param amountOfProduct - the amount that needs to be added
     * @return true if the amount can be changed, false if it can't.
     */
    private boolean changeAmountOfProductInBag(Product product, int amountOfProduct){
        if(amountOfProduct < 0 && amountOfProduct + productListFromStore.get(product) < 0){
            log.error("The amount of product cannot be less than 0");
            return false;
        }
        log.info("Update the amount of an exciting product");
        totalCostOfBag += (amountOfProduct*product.getCost());
        totalCostOfBag = Double.parseDouble(formatter.format(totalCostOfBag));
        productListFromStore.replace(product,productListFromStore.get(product)+amountOfProduct);
        if (productListFromStore.get(product)==0) {
            productListFromStore.remove(product);
            numOfProductsInBag --;
        }
        return true;
    }
}