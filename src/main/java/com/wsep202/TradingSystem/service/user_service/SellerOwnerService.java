package com.wsep202.TradingSystem.service.user_service;

import com.wsep202.TradingSystem.domain.trading_system_management.Receipt;
import com.wsep202.TradingSystem.domain.trading_system_management.TradingSystemFacade;
import com.wsep202.TradingSystem.service.user_service.dto.ReceiptDto;
import com.wsep202.TradingSystem.service.user_service.dto.StoreDto;
import com.wsep202.TradingSystem.service.user_service.dto.UserSystemDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SellerOwnerService {

    private final TradingSystemFacade tradingSystemFacade;

    /**
     * View store purchase history
     */
    public List<ReceiptDto> viewPurchaseHistory(String ownerUsername, int storeId){
        return tradingSystemFacade.viewPurchaseHistoryOfOwner(ownerUsername,storeId);
    }

    /**
     * add product
     */
    public boolean addProduct(String ownerUsername, int storeId, String productName, String category, int amount, double cost ){
         return tradingSystemFacade.addProduct(ownerUsername, storeId, productName, category, amount, cost);
    }

    /**
     * remove product
     */
    public boolean removeProduct(String ownerUsername, int storeId, int productSn){
        return tradingSystemFacade.deleteProductFromStore(ownerUsername, storeId, productSn);
    }

    /**
     * edit product
     */
    public boolean editProduct(String ownerUsername, int storeId,int productSn, String productName, String category, int amount, double cost ){
        return tradingSystemFacade.editProduct(ownerUsername, storeId, productSn, productName, category, amount, cost);
    }

    /**
     * add owner
     */
    public boolean addOwner(String ownerUsername, int storeId, String newOwnerUsername){
        return tradingSystemFacade.addOwner(ownerUsername, storeId, newOwnerUsername);
    }

    /**
     * remove manager
     */
    public boolean removeManager(String ownerUsername, int storeId, String newManagerUsername){
        return tradingSystemFacade.removeManager(ownerUsername, storeId, newManagerUsername);
    }

    /**
     * add permission
     */
    public boolean addPermission(String ownerUsername, int storeId, String newManagerUsername, String permission){
        return tradingSystemFacade.addPermission(ownerUsername, storeId, newManagerUsername, permission);
    }

    /**
     * add manager
     */
    public boolean addManager(String ownerUsername, int storeId, String newManagerUsername){
        return tradingSystemFacade.addManager(ownerUsername, storeId, newManagerUsername);
    }

    /**
     * a function that returns the list of stores that are saved in the system
     * @return - list of StoreDto's.
     */
    public List<StoreDto> getStoresDtos() {
        return this.tradingSystemFacade.getStoresDtos();
    }

    /**
     * a function that returns the list of users that are saved in the system
     * @return - list of UserSystemDto's.
     */
    public List<UserSystemDto> getUsersDtos() {
        return this.tradingSystemFacade.getUsersDtos();
    }

    /**
     * a function that returns the list of administrators that are saved in the system
     * @return - list of UserSystemDto's.
     */
    public List<UserSystemDto> getAdministratorsDtos() {
        return this.tradingSystemFacade.getAdministratorsDtos();
    }

    /**
     * a function to clear the data structures
     */
    public void clearDS(){
        this.tradingSystemFacade.clearDS();
    }

}
