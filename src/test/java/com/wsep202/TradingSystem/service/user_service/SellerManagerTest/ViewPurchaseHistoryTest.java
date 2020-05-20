package com.wsep202.TradingSystem.service.user_service.SellerManagerTest;

import com.github.rozidan.springboot.modelmapper.WithModelMapper;
import com.wsep202.TradingSystem.config.ObjectMapperConfig;
import com.wsep202.TradingSystem.config.TradingSystemConfiguration;
import com.wsep202.TradingSystem.service.user_service.*;
import com.wsep202.TradingSystem.dto.*;
import javafx.util.Pair;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TradingSystemConfiguration.class, ObjectMapperConfig.class, GuestService.class, BuyerRegisteredService.class, SellerOwnerService.class, SellerManagerService.class})
@SpringBootTest(args = {"admin","admin"})
@WithModelMapper

// *********** UC 5.1.1 - viewing purchase history ***********
public class ViewPurchaseHistoryTest {
    @Autowired
    GuestService guestService;
    @Autowired
    BuyerRegisteredService buyerRegisteredService;
    @Autowired
    SellerOwnerService sellerOwnerService;
    @Autowired
    SellerManagerService sellerManagerService;

    ServiceTestsHelper helper;
    UserSystemDto user = new UserSystemDto("username","name","lname");
    UserSystemDto manager = new UserSystemDto("manager","name","lname");
    String userPassword = "password";
    MultipartFile image = null;
    UUID uuid;
    int storeId = 0;
    private ProductDto productDto;

    @BeforeEach
    void setUp() {
        if (this.helper == null || this.helper.getGuestService() == null ) {
            this.helper = new ServiceTestsHelper(this.guestService, this.buyerRegisteredService, this.sellerOwnerService);
        }
        this.helper.registerUser(this.manager.getUserName(), this.userPassword,
                this.manager.getFirstName(), this.manager.getLastName(), image);
        this.helper.registerUser(this.user.getUserName(), this.userPassword,
                this.user.getFirstName(), this.user.getLastName(), image);
        Pair<UUID, Boolean> returnedValue = this.helper.loginUser(this.user.getUserName(),
                this.userPassword);
        if (returnedValue != null){
            this.uuid = returnedValue.getKey();
        }
        this.productDto = this.helper.openStoreAndAddProducts(this.user, this.userPassword, this.uuid);
    }

    @AfterEach
    void tearDown(){
        this.helper.logoutUser(this.user.getUserName(), this.uuid);
    }


    /**
     * view the history purchase of a valid store
     * no purchases
     */
    @Test
    void ViewHistoryNoPurchases() {
        List<ReceiptDto> returnedHistory = this.sellerManagerService.viewPurchaseHistoryOfManager(
                this.user.getUserName(), this.storeId, this.uuid);
        Assertions.assertEquals(new LinkedList<>(), returnedHistory);
    }

    /**
     * view the history purchase of a valid store
     * invalid owner
     */
    @Test
    void ViewHistoryNoPurchasesInvalidOwner() {
        try{
            Assertions.assertNull(this.sellerManagerService.viewPurchaseHistoryOfManager(
                    this.user.getUserName()+"Not", this.storeId, this.uuid));
        } catch (Exception e) {

        }
    }

    /**
     * view the history purchase of an invalid store
     */
    @Test
    void ViewHistoryNoPurchasesInvalidStore() {
        Assertions.assertNull(this.sellerManagerService.viewPurchaseHistoryOfManager(
                this.user.getUserName(), this.storeId+5, this.uuid));
    }

    /**
     * view the history purchase of an invalid store
     * invalid owner
     */
    @Test
    void ViewHistoryNoPurchasesInvalidStoreInvalidOwner() {
        try{
            Assertions.assertNull(this.sellerManagerService.viewPurchaseHistoryOfManager(
                    this.user.getUserName()+"Not", this.storeId+5, this.uuid));
        } catch (Exception e) {

        }
    }

    /**
     * view the history purchase of a valid store
     */
    @Test
    void ViewHistoryPurchases() {
        this.helper.openStoreAddProductsAndBuyProduct(this.user, this.uuid, this.userPassword);
        Assertions.assertNotNull(this.sellerManagerService.viewPurchaseHistoryOfManager(
                this.user.getUserName(), this.storeId, this.uuid));
    }
}
