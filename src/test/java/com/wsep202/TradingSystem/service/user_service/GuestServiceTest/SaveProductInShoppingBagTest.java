package com.wsep202.TradingSystem.service.user_service.GuestServiceTest;

import com.github.rozidan.springboot.modelmapper.WithModelMapper;
import com.wsep202.TradingSystem.domain.config.TradingSystemConfiguration;
import com.wsep202.TradingSystem.domain.trading_system_management.CardAction;
import com.wsep202.TradingSystem.domain.trading_system_management.UserSystem;
import com.wsep202.TradingSystem.service.user_service.BuyerRegisteredService;
import com.wsep202.TradingSystem.service.user_service.GuestService;
import com.wsep202.TradingSystem.service.user_service.SellerOwnerService;
import com.wsep202.TradingSystem.service.user_service.dto.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TradingSystemConfiguration.class, GuestService.class, BuyerRegisteredService.class, SellerOwnerService.class})
@SpringBootTest(args = {"admin","admin"})
@WithModelMapper

public class SaveProductInShoppingBagTest {
    @Autowired
    GuestService guestService;
    @Autowired
    BuyerRegisteredService buyerRegisteredService;
    @Autowired
    SellerOwnerService sellerOwnerService;

    StoreDto storeDto;
    UserSystem userSystem;
    ProductDto productDto;
    ShoppingCartDto shoppingCartDto;
    PaymentDetailsDto paymentDetailsDto;
    BillingAddressDto billingAddressDto;
    private ReceiptDto receiptDto;
    private UserSystem owner;

    @BeforeEach
    void setUp() {
        userSystem = new UserSystem("username","name","lname","pass");
        this.shoppingCartDto = new ShoppingCartDto();
        this.billingAddressDto = new BillingAddressDto(this.userSystem.getFirstName()+" "+this.userSystem.getLastName(),
                "address", "city", "country", "1234567");
        this.paymentDetailsDto = new PaymentDetailsDto(CardAction.PAY, "123456789", "month",
                "year", "Cardholder", 798, "id");

    }

    @AfterEach
    void tearDown() {
        this.guestService.clearDS();
    }

    /**
     * invalid store
     */
    @Test
    void SaveProductInShoppingCartInvalidStore() {
        openStoreAndAddProducts();
        Assertions.assertFalse(this.guestService.saveProductInShoppingBag("notUser",
                this.storeDto.getStoreId()+5, this.productDto.getProductSn(), 1));
    }

    /**
     * invalid store
     * invalid product
     */
    @Test
    void SaveProductInShoppingCartInvalidStoreInvalidProduct() {
        openStoreAndAddProducts();
        Assertions.assertFalse(this.guestService.saveProductInShoppingBag("notUser",
                this.storeDto.getStoreId()+5, this.productDto.getProductSn()+5, 1));
    }


    /**
     * invalid product
     */
    @Test
    void SaveProductInShoppingCartInvalidProduct() {
        openStoreAndAddProducts();
        Assertions.assertFalse(this.guestService.saveProductInShoppingBag("notUser",
                this.storeDto.getStoreId(), this.productDto.getProductSn()+5, 1));
    }


    /**
     * opening a new store and adding a product to it
     */
    void openStoreAndAddProducts(){
        this.owner = new UserSystem("owner","name","lname","pass");
        // registering the owner
        Assertions.assertTrue(this.guestService.registerUser(owner.getUserName(), owner.getPassword(),
                owner.getFirstName(), owner.getLastName()));

        // opening a new store, owned by owner
        Assertions.assertTrue(this.buyerRegisteredService.openStore(owner.getUserName(),
                new PurchasePolicyDto(), new DiscountPolicyDto(), "storeName"));

        // getting the storeDto of the store the owner opened
        this.storeDto = this.buyerRegisteredService.getStoresDtos().get(0);

        // adding a product to the owner's store
        Assertions.assertTrue(this.sellerOwnerService.addProduct(owner.getUserName(), storeDto.getStoreId(),
                "motor", "motors", 20, 20));
        // getting the productDto of the added product

        this.productDto = (ProductDto) this.buyerRegisteredService.getStoresDtos().get(0).getProducts().toArray()[0];
    }

}
