package com.wsep202.TradingSystem.domain.trading_system_management;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TradingSystemDao {
    void registerAdmin(UserSystem admin);

    boolean isRegistered(UserSystem userSystem);

    void addUserSystem(UserSystem userToRegister, MultipartFile image);

    Optional<UserSystem> getUserSystem(String username);

    boolean isAdmin(String username);

    Optional<UserSystem> getAdministratorUser(String username);

    Optional<Store> getStore(int storeId);

    List<Product> searchProductByName(String productName);

    List<Product> searchProductByCategory(ProductCategory productCategory);

    List<Product> searchProductByKeyWords(List<String> keyWords);

    void addStore(Store newStore, UserSystem userSystem);

    Set<Store> getStores();

    Set<Product> getProducts();

    Set<UserSystem> getUsers();

}
