package com.wsep202.TradingSystem.service.user_service;


import com.wsep202.TradingSystem.domain.trading_system_management.*;
import com.wsep202.TradingSystem.dto.*;
import javafx.util.Pair;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class GuestService {

    private final TradingSystemFacade tradingSystemFacade;

    /**
     * register user to the system
     * @param username user to register - unique
     * @param password
     * @param firstName
     * @param lastName
     * @return
     */
    public boolean registerUser(String username,
                                String password,
                                String firstName,
                                String lastName){
        return tradingSystemFacade.registerUser(username, password, firstName, lastName);
    }

    /**
     * login user to the system
     * @param username
     * @param password
     * @return
     */
    public Pair<UUID, Boolean> login(String username,
                                     String password){
        return tradingSystemFacade.login(username, password);
    }

    /**
     * view store info by store id
     * @param storeId
     * @return
     */
    public StoreDto viewStoreInfo(int storeId){
        return tradingSystemFacade.viewStoreInfo(storeId);
    }

    /**
     * view product in store with store id info.
     * @param storeId belongs to the product to view
     * @param productId - product to see
     * @return
     */
    public ProductDto viewProduct(int storeId, int productId){
        return tradingSystemFacade.viewProduct(storeId, productId);
    }

    /**
     *      * search product by productName
     * @param productName
     * @return
     */
    public List<ProductDto> searchProductByName(String productName){
        return tradingSystemFacade.searchProductByName(productName);
    }

    /**
     *      * search product by category
     * @param category
     * @return
     */
    public List<ProductDto> searchProductByCategory(String category){
        return tradingSystemFacade.searchProductByCategory(category);
    }

    /**
     *      * search product by KeyWords
     * @param keyWords
     * @return
     */
    public List<ProductDto> searchProductByKeyWords(List<String> keyWords){
        return tradingSystemFacade.searchProductByKeyWords(keyWords);
    }

    /**
     *      * filter products by range price
     * @param products to filter
     * thresholds:
     * @param min
     * @param max
     * @return
     */
    public List<ProductDto> filterByRangePrice(List<ProductDto> products,
                                               double min,
                                               double max){
        return tradingSystemFacade.filterByRangePrice(products, min, max);
    }

    /**
     *      * filter products by product rank
     * @param products to filter
     * @param rank of product
     * @return
     */
    public List<ProductDto> filterByProductRank(List<ProductDto> products,
                                                int rank){
        return tradingSystemFacade.filterByProductRank(products, rank);
    }

    /**
     *      * filter products by store rank
     * @param products
     * @param rank - store rank to filter by
     * @return
     */
    public List<ProductDto> filterByStoreRank(List<ProductDto> products,
                                              int rank){
        return tradingSystemFacade.filterByStoreRank(products, rank);
    }

    /**
     *      * purchase shopping cart
     * @param shoppingCart includes the bags of each store the user selected
     * @param paymentDetails    - charging info of the user
     * @param billingAddressDto - the destination to deliver the purchases
     * @return
     */
    public List<ReceiptDto> purchaseShoppingCartGuest(ShoppingCartDto shoppingCart,
                                                      PaymentDetailsDto paymentDetails,
                                                      BillingAddressDto billingAddressDto){
        return tradingSystemFacade.purchaseShoppingCart(shoppingCart, paymentDetails, billingAddressDto);
    }

    public List<StoreDto> getStores() {
        return tradingSystemFacade.getStores();
    }

    public List<ProductDto> getProducts() {
        return tradingSystemFacade.getProducts();
    }

    public List<String> getCategories() {
        return tradingSystemFacade.getCategories();
    }
}
