package com.wsep202.TradingSystem.service.user_service.BuyerRegisteredServiceTest;

import com.github.rozidan.springboot.modelmapper.WithModelMapper;
import com.wsep202.TradingSystem.config.ObjectMapperConfig;
import com.wsep202.TradingSystem.config.TradingSystemConfiguration;
import com.wsep202.TradingSystem.domain.trading_system_management.UserSystem;
import com.wsep202.TradingSystem.service.user_service.BuyerRegisteredService;
import com.wsep202.TradingSystem.service.user_service.GuestService;
import com.wsep202.TradingSystem.service.user_service.SellerOwnerService;
import com.wsep202.TradingSystem.dto.*;
import com.wsep202.TradingSystem.service.user_service.ServiceTestsHelper;
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
@ContextConfiguration(classes = {TradingSystemConfiguration.class, ObjectMapperConfig.class, GuestService.class, BuyerRegisteredService.class, SellerOwnerService.class})
@SpringBootTest(args = {"admin","admin"})
@WithModelMapper

// *********** UC 3.7 - viewing personal purchase history ***********
public class ViewPurchaseHistoryTest {
    @Autowired
    GuestService guestService;
    @Autowired
    BuyerRegisteredService buyerRegisteredService;
    @Autowired
    SellerOwnerService sellerOwnerService;
    ServiceTestsHelper helper;
    UserSystemDto user = new UserSystemDto("username","name","lname");
    String userPassword = "password";
    MultipartFile image = null;
    UUID uuid;

    @BeforeEach
    void setUp() {
        if (this.helper == null || this.helper.getGuestService() == null ) {
            this.helper = new ServiceTestsHelper(this.guestService, this.buyerRegisteredService, this.sellerOwnerService);
        }
        this.helper.registerUser(this.user.getUserName(), this.userPassword,
                this.user.getFirstName(), this.user.getLastName(), image);
        Pair<UUID, Boolean> returnedValue = this.helper.loginUser(this.user.getUserName(),
                this.userPassword);
        if (returnedValue != null){
            this.uuid = returnedValue.getKey();
        }
    }

    @AfterEach
    void tearDown(){
        this.helper.logoutUser(this.user.getUserName(), this.uuid);
    }

    /**
     * view the purchase history of a user that isn't registered
     */
    @Test
    void viewPurchaseHistoryNotRegisteredUser() {
        try{
            Assertions.assertNull(this.buyerRegisteredService.viewPurchaseHistory("NotRegistered", uuid));
        } catch (Exception e){

        }
    }

    /**
     * view the purchase history of a user that isn't registered and his username is ""
     */
    @Test
    void viewPurchaseHistoryNotRegisteredEmptyUsername() {
        try{
            Assertions.assertNull(this.buyerRegisteredService.viewPurchaseHistory("", uuid));
        } catch (Exception e){

        }
    }

    /**
     * view the purchase history of a user that is registered, but his history is empty
     */
    @Test
    void viewEmptyPurchaseHistoryCartRegisteredUser() {
        List<ReceiptDto> receiptDtoList = new LinkedList<>();
        Assertions.assertEquals(receiptDtoList,
                this.buyerRegisteredService.viewPurchaseHistory(this.user.getUserName(), uuid));
    }

    /**
     * view the purchase history of a user that is registered, but his history is empty
     */
    @Test
    void viewPurchaseHistoryCartRegisteredUser() {
        this.helper.openStoreAddProductsAndPurchaseShoppingCart(this.user.getUserName(), this.uuid);
        Assertions.assertNotNull(this.buyerRegisteredService.viewPurchaseHistory(this.user.getUserName(), uuid));
    }
}
