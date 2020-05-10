package com.wsep202.TradingSystem.web.controllers;

import com.wsep202.TradingSystem.dto.*;
import com.wsep202.TradingSystem.service.user_service.BuyerRegisteredService;
import com.wsep202.TradingSystem.web.controllers.api.PublicApiPaths;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javafx.util.Pair;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(PublicApiPaths.BUYER_REG_PATH)
@CrossOrigin(origins = "http://localhost:4200")
@Api(value = "API to buyer registered", produces = "application/json")
@RequiredArgsConstructor
public class BuyerRegisteredController {

    private final BuyerRegisteredService buyerRegisteredService;

    /**
     * logout username from the system
     *
     * @param username user to logout
     */
    @ApiOperation(value = "logout")
    @PutMapping("logout/{username}/{uuid}")
    public boolean logout(@PathVariable String username,
                          @PathVariable UUID uuid) {
        return buyerRegisteredService.logout(username, uuid);
    }

    /**
     * open store
     *
     * @param usernameOwner the opener and first owner of store
     * @param storeName     store name
     */
    @ApiOperation(value = "open store")
    @PostMapping("open-store/{usernameOwner}/{storeName}/{description}/{uuid}")
    public boolean openStore(@PathVariable String usernameOwner,
                              @PathVariable String storeName,
                              @PathVariable String description,
                              @PathVariable UUID uuid) {
        return buyerRegisteredService.openStore(usernameOwner, storeName, description, uuid);
    }

    /**
     * View buyer purchase history
     *
     * @param username of the user the history belongs to
     */
    @ApiOperation(value = "view purchase history")
    @GetMapping("view-purchase-history/{username}/{uuid}")
    public List<ReceiptDto> viewPurchaseHistory(@PathVariable String username,
                                                @PathVariable UUID uuid) {
        return buyerRegisteredService.viewPurchaseHistory(username, uuid);
    }

    /**
     * view product in shopping bag
     *
     * @param username the user the bag belongs to
     */
    @ApiOperation(value = "view products in shopping cart")
    @GetMapping("view-products-in-shopping-cart/{username}/{uuid}")
    public ShoppingCartDto viewProductsInShoppingCart(@PathVariable String username,
                                                      @PathVariable UUID uuid) {
        return buyerRegisteredService.watchShoppingCart(username, uuid);
    }

    /**
     * remove product in shopping bag (edit)
     *
     * @param username  the user which edit
     * @param storeId   the store belongs to the product
     * @param productSn identifier of product
     */
    @ApiOperation(value = "remove product in shopping bag")
    @PostMapping("remove-product-in-shopping-bag/{username}/{storeId}/{productSn}/{uuid}")
    public boolean removeProductInShoppingBag(@PathVariable String username,
                                              @PathVariable int storeId,
                                              @PathVariable int productSn,
                                              @PathVariable UUID uuid) {
        return buyerRegisteredService.removeProductInShoppingBag(username, storeId, productSn, uuid);
    }

    /**
     * remove product in shopping bag (edit)
     *
     * @param username  the user which edit
     * @param storeId   the store belongs to the product
     * @param productSn identifier of product
     */
    @ApiOperation(value = "change product-amount in shopping bag")
    @PostMapping("change-product-amount-in-shopping-bag/{username}/{storeId}/{productSn}/{uuid}")
    public boolean changeProductAmountInShoppingBag(@PathVariable String username,
                                                    @PathVariable int storeId,
                                                    @PathVariable int productSn,
                                                    @PathVariable UUID uuid) {
        return buyerRegisteredService.changeProductAmountInShoppingBag(username, storeId, productSn, uuid);
    }

    /**
     * purchase shopping cart
     *
     * @param username          user that purchase
     * @param paymentDetailsDto info to charge of the user
     * @param billingAddressDto the destination of the delivery
     */
    @ApiOperation(value = "purchase shopping cart buyer")
    @PutMapping("purchase-shopping-cart-buyer/{username}/{uuid}")
    public List<ReceiptDto> purchaseShoppingCartBuyer(@PathVariable String username,
                                                      @RequestBody PaymentDetailsDto paymentDetailsDto,
                                                      @RequestBody BillingAddressDto billingAddressDto,
                                                      @PathVariable UUID uuid) {
        return buyerRegisteredService.purchaseShoppingCartBuyer(username, paymentDetailsDto, billingAddressDto, uuid);
    }

    /**
     * add product to shopping cart
     */
    @ApiOperation(value = "add product to shopping cart")
    @PostMapping("add-product-to-shopping-cart/{username}/{amount}/{uuid}")
    public boolean addProductToShoppingCart(@PathVariable String username,
                                            @PathVariable int amount,
                                            @RequestBody ProductDto productDto,
                                            @PathVariable UUID uuid) {
        return buyerRegisteredService.addProductToShoppingCart(username, amount, productDto, uuid);
    }

    /**
     * get shopping cart
     */
    @ApiOperation(value = "get-shopping-cart")
    @GetMapping("get-shopping-cart/{username}/{uuid}")
    public ShoppingCartViewDto getShoppingCart(@PathVariable String username,
                                               @PathVariable UUID uuid) {
        return buyerRegisteredService.getShoppingCart(username, uuid);
    }

    /**
     * get Total Price Of ShoppingCart
     */
    @ApiOperation(value = " get Total Price Of ShoppingCart")
    @GetMapping(value = "get-total-price-of-shopping-cart/{username}/{uuid}")
    @ResponseBody
    public Pair<Double, Double> getTotalPriceOfShoppingCart(@PathVariable String username,
                                                            @PathVariable UUID uuid) {
        return buyerRegisteredService.getTotalPriceOfShoppingCart(username, uuid);
    }

}
