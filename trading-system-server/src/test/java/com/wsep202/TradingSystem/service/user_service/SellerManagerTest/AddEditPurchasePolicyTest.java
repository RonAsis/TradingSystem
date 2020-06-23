package com.wsep202.TradingSystem.service.user_service.SellerManagerTest;

import com.github.rozidan.springboot.modelmapper.WithModelMapper;
import com.wsep202.TradingSystem.config.ObjectMapperConfig;
import com.wsep202.TradingSystem.config.TradingSystemConfiguration;
import com.wsep202.TradingSystem.config.httpSecurity.HttpSecurityConfig;
import com.wsep202.TradingSystem.service.user_service.*;
import com.wsep202.TradingSystem.dto.*;
import io.swagger.models.auth.In;
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

import java.util.UUID;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TradingSystemConfiguration.class, HttpSecurityConfig.class, ObjectMapperConfig.class, GuestService.class, BuyerRegisteredService.class, SellerOwnerService.class, SellerManagerService.class})
@SpringBootTest(args = {"admin","admin"})
@WithModelMapper

// *********** UC 4.2 (inherited from owner) - adding/ editing the store's purchase's policies ***********
public class AddEditPurchasePolicyTest {
    @Autowired
    GuestService guestService;
    @Autowired
    BuyerRegisteredService buyerRegisteredService;
    @Autowired
    SellerOwnerService sellerOwnerService;
    @Autowired
    SellerManagerService sellerManagerService;

    ServiceTestsHelper helper;
    UserSystemDto owner = new UserSystemDto("username","name","lname");
    UserSystemDto manager = new UserSystemDto("manager","name","lname");
    UserSystemDto manager1 = new UserSystemDto("manager1","name","lname");
    String userPassword = "password";
    MultipartFile image = null;
    UUID uuid;
    private Integer storeId;
    int counter = 0;
    private PurchasePolicyDto purchasePolicyDto = new PurchasePolicyDto();

    @BeforeEach
    void setUp() {
        if (this.helper == null || this.helper.getGuestService() == null ) {
            this.helper = new ServiceTestsHelper(this.guestService, this.buyerRegisteredService, this.sellerOwnerService);
        }
        this.manager.setUserName(this.manager.getUserName()+counter);
        this.helper.registerUser(this.manager.getUserName(), this.userPassword,
                this.manager.getFirstName(), this.manager.getLastName(), image);
        this.manager1.setUserName(this.manager1.getUserName()+counter);
        this.helper.registerUser(this.manager1.getUserName(), this.userPassword,
                this.manager1.getFirstName(), this.manager1.getLastName(), image);
        this.owner.setUserName(this.owner.getUserName()+counter);
        this.helper.registerUser(this.owner.getUserName(), this.userPassword,
                this.owner.getFirstName(), this.owner.getLastName(), image);
        Pair<UUID, Boolean> returnedValueLogin = this.helper.loginUser(this.owner.getUserName(),
                this.userPassword);
        if (returnedValueLogin != null){
            this.uuid = returnedValueLogin.getKey();
        }
        Integer storeId = this.helper.openStoreAddProductAndAddManager(this.owner, this.uuid, this.manager.getUserName());
        if (storeId != null){
            this.storeId = storeId;
        }
        this.sellerOwnerService.addManager(this.owner.getUserName(),this.storeId,this.manager1.getUserName(),this.uuid);
        this.helper.logoutUser(this.owner.getUserName(), this.uuid);
        returnedValueLogin = this.helper.loginUser(this.manager.getUserName(),
                this.userPassword);
        if (returnedValueLogin != null){
            this.uuid = returnedValueLogin.getKey();
        }
    }

    @AfterEach
    void tearDown(){
        this.helper.logoutUser(this.manager.getUserName(), this.uuid);
    }


    /**
     * a manager who's not permitted to add policies, adding a new purchase policy
     */
    @Test
    void notPermittedManagerAddingNewPurchasePolicy() {
        Assertions.assertThrows(Exception.class, () ->
                {this.sellerManagerService.addEditPurchasePolicy(this.manager1.getUserName(),
                        this.storeId, this.purchasePolicyDto, this.uuid);}
        );
    }

    /**
     * adding a new purchase policy, invalid store
     */
    @Test
    void addingNewPurchasePolicyInvalidStore() {
        Assertions.assertThrows(Exception.class, ()->
                {this.sellerManagerService.addEditPurchasePolicy(this.manager.getUserName(),
                        this.storeId+15, this.purchasePolicyDto, this.uuid);}
        );
    }

    /**
     * adding a new purchase policy, invalid manager
     */
    @Test
    void addingNewPurchasePolicyInvalidManager() {
        Assertions.assertThrows(Exception.class, ()->
                {this.sellerManagerService.addEditPurchasePolicy(this.manager.getUserName()+"notManager",
                        this.storeId, this.purchasePolicyDto, this.uuid); }
        );
    }

    /**
     * adding a new null purchase policy
     */
    @Test
    void addingNullPurchasePolicy() {
        Assertions.assertThrows(Exception.class, ()->
                {this.sellerManagerService.addEditPurchasePolicy(this.manager.getUserName(),
                this.storeId, null, this.uuid); }
        );
    }

    /**
     * adding a new null purchase policy
     */
   @Test
    void permittedManagerAddingNewPurchasePolicy() {
        this.helper.logoutUser(this.manager.getUserName(), this.uuid);
        UUID ownerUuid = this.helper.loginUser(this.owner.getUserName(), this.userPassword).getKey();
        this.helper.addPermission(this.owner.getUserName(), this.storeId, this.manager.getUserName(), "edit purchase policy", ownerUuid);
        this.purchasePolicyDto.setMax(200);

        this.helper.logoutUser(this.owner.getUserName(), ownerUuid);
        this.uuid = this.helper.loginUser(this.manager.getUserName(), this.userPassword).getKey();
        this.purchasePolicyDto.setPurchaseId(-1);
      Assertions.assertNotNull(this.sellerManagerService.addEditPurchasePolicy(this.manager.getUserName(),
                this.storeId, this.purchasePolicyDto, this.uuid));
    }
}
