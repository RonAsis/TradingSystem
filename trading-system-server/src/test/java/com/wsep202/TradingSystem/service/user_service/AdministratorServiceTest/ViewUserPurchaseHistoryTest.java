package com.wsep202.TradingSystem.service.user_service.AdministratorServiceTest;

import com.github.rozidan.springboot.modelmapper.WithModelMapper;
import com.wsep202.TradingSystem.config.ObjectMapperConfig;
import com.wsep202.TradingSystem.config.TradingSystemConfiguration;
import com.wsep202.TradingSystem.config.httpSecurity.HttpSecurityConfig;
import com.wsep202.TradingSystem.service.user_service.*;
import com.wsep202.TradingSystem.dto.*;
import javafx.util.Pair;
import org.junit.jupiter.api.*;
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
@ContextConfiguration(classes = {TradingSystemConfiguration.class, HttpSecurityConfig.class, ObjectMapperConfig.class, GuestService.class, BuyerRegisteredService.class, SellerOwnerService.class, AdministratorService.class})
@SpringBootTest(args = {"admin","admin"})
@WithModelMapper

// *********** UC 6.4.2 - viewing user's purchase history ***********
public class ViewUserPurchaseHistoryTest {
    @Autowired
    GuestService guestService;
    @Autowired
    BuyerRegisteredService buyerRegisteredService;
    @Autowired
    SellerOwnerService sellerOwnerService;
    @Autowired
    AdministratorService administratorService;
    ServiceTestsHelper helper;

    String adminUsername = "admin";
    String adminPassword = "admin";
    UUID uuid;
    UserSystemDto user = new UserSystemDto("username","name","lname");
    String userPassword = "password";
    MultipartFile image = null;

    @BeforeEach
    void setUp() {
        if (this.helper == null || this.helper.getGuestService() == null ) {
            this.helper = new ServiceTestsHelper(this.guestService, this.buyerRegisteredService, this.sellerOwnerService);
        }
        this.helper.registerUser(this.user.getUserName(), this.userPassword,
                this.user.getFirstName(), this.user.getLastName(), image);
        Pair<UUID, Boolean> returnedValue = this.helper.loginUser(this.adminUsername,
                this.adminPassword);
        if (returnedValue != null){
            this.uuid = returnedValue.getKey();
        }
    }

    @AfterEach
    void tearDown(){
        this.helper.logoutUser(this.adminUsername, this.uuid);
    }

    /**
     * view the history purchase of a valid user
     * no purchases
     */
    @Test
    void viewHistoryNoPurchases() {
        List<ReceiptDto> returnedHistory = this.administratorService.viewPurchaseHistory(
                this.adminUsername, this.user.getUserName(), this.uuid);
        Assertions.assertEquals(new LinkedList<>(), returnedHistory);
    }

    /**
     * view the history purchase of a valid user
     * invalid admin
     */
    @Test
    void viewHistoryNoPurchasesInvalidAdmin() {
        Assertions.assertThrows(Exception.class, ()-> {
            this.administratorService.viewPurchaseHistory(
                    "NotAdmin", this.user.getUserName(), this.uuid);
        });
    }

    /**
     * view the history purchase of an invalid user
     */
    @Test
    void viewHistoryNoPurchasesInvalidUser() {
        Assertions.assertNull(this.administratorService.viewPurchaseHistory(
                this.adminUsername, "NotRegistered", this.uuid));
    }

    /**
     * view the history purchase of an invalid user
     * invalid admin
     */
    @Test
    void viewHistoryNoPurchasesInvalidUserInvalidAdmin() {
        Assertions.assertThrows(Exception.class, ()->
                {this.administratorService.viewPurchaseHistory(
                        "NotAdmin", this.user.getUserName(), this.uuid);}
        );
    }

    /**
     * view the history purchase of a valid user
     */
    @Test
    void viewHistoryPurchases() {
        Pair<Integer, List<ReceiptDto>> returnedValue =
                this.helper.createOwnerOpenStoreAddProductAddAndPurchaseShoppingCart(this.adminUsername, this.uuid);
        Assertions.assertNotNull(this.administratorService.viewPurchaseHistory(
                this.adminUsername, this.adminUsername, this.uuid));
    }
}
