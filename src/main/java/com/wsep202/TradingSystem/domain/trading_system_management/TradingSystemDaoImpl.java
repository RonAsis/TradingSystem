package com.wsep202.TradingSystem.domain.trading_system_management;

import com.wsep202.TradingSystem.domain.exception.UserDontExistInTheSystemException;
import com.wsep202.TradingSystem.domain.image.ImagePath;
import com.wsep202.TradingSystem.domain.image.ImageUtil;
import com.wsep202.TradingSystem.domain.trading_system_management.discount.Discount;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.*;


public class TradingSystemDaoImpl extends TradingSystemDao {

    private static int idAcc = 0;
    private Set<Store> stores;
    private Set<UserSystem> users;
    private Set<UserSystem> administrators;

    public TradingSystemDaoImpl() {
        super();
        this.stores = new HashSet<>();
        this.users = new HashSet<>();
        this.administrators = new HashSet<>();
    }

    @Override
    public void registerAdmin(@NotNull UserSystem admin) {
        administrators.add(admin);
    }

    @Override
    public boolean isRegistered(UserSystem userSystem) {
        return users.stream()
                .anyMatch(userSystemReg -> userSystemReg.getUserName().equals(userSystem.getUserName())) ||
                administrators.stream()
                        .anyMatch(userSystemReg -> userSystemReg.getUserName().equals(userSystem.getUserName()));
    }

    @Override
    public void addUserSystem(UserSystem userToRegister, MultipartFile image) {
        String urlImage = null;
        if (Objects.nonNull(image)) {
            urlImage = ImageUtil.saveImage(ImagePath.ROOT_IMAGE_DIC + ImagePath.USER_IMAGE_DIC + image.getOriginalFilename(), image);
        }
        userToRegister.setImageUrl(urlImage);
        users.add(userToRegister);
    }

    @Override
    public Optional<UserSystem> getUserSystem(String username) {
        Optional<UserSystem> userSystem = findUserSystem(users, username);
        return userSystem.isPresent() ? userSystem : findUserSystem(administrators, username);
    }

    private Optional<UserSystem> findUserSystem(Set<UserSystem> userSystems, String username){
        return userSystems.stream()
                .filter(user -> user.getUserName().equals(username))
                .findFirst();
    }

    @Override
    public boolean isAdmin(String username) {
        return administrators.stream()
                .anyMatch(userSystem -> userSystem.getUserName().equals(username));
    }

    /**
     * returns the User system object match the received string in case its an admin user
     * otherwise throw exception
     *
     * @param administratorUsername - admins user name
     * @return administrator type user
     */
    public Optional<UserSystem> getAdministratorUser(String administratorUsername) {
        return administrators.stream()
                .filter(user -> user.getUserName().equals(administratorUsername))
                .findFirst();
    }

    @Override
    public Optional<Store> getStore(int storeId) {
        return stores.stream()
                .filter(store -> store.getStoreId() == storeId).findFirst();
    }

    @Override
    public void addStore(Store newStore) {
        newStore.setStoreId(getNewId());
        this.stores.add(newStore);
    }

    @Override
    public Set<Store> getStores() {
        return stores;
    }

    @Override
    public Set<Product> getProducts() {
        return stores.stream()
                .map(Store::getProducts)
                .reduce(new HashSet<>(), (productsAcc, products) -> {
                    productsAcc.addAll(products);
                    return productsAcc;
                });
    }

    @Override
    public Set<UserSystem> getUsers() {
        return this.users;
    }

    @Override
    public Product addProductToStore(Store store, UserSystem owner, Product product) {
        product.setProductSn(getNewId());
        return store.addNewProduct(owner, product)? product : null;
    }

    @Override
    public boolean removeDiscount(Store store, UserSystem user, int discountId) {
        return store.removeDiscount(user, discountId);
    }

    @Override
    public Discount addEditDiscount(Store store, UserSystem user, Discount discount) {
        discount.setDiscountId(getNewId());
        return store.addEditDiscount(user, discount);
    }

    @Override
    public boolean deleteProductFromStore(Store ownerStore, UserSystem user, int productSn) {
        return ownerStore.removeProductFromStore(user, productSn);
    }

    @Override
    public boolean editProduct(Store ownerStore, UserSystem user, int productSn, String productName, String category, int amount, double cost) {
        return ownerStore.editProduct(user, productSn, productName, category, amount, cost);
    }

    @Override
    public boolean addPermissionToManager(Store ownedStore, UserSystem ownerUser, UserSystem managerStore, StorePermission storePermission) {
        return ownedStore.addPermissionToManager(ownerUser, managerStore, storePermission);
    }

    @Override
    public boolean removePermission(Store ownedStore, UserSystem ownerUser, UserSystem managerStore, StorePermission storePermission) {
        return ownedStore.removePermission(ownerUser, managerStore, storePermission);
    }

    @Override
    public boolean saveProductInShoppingBag(String username, ShoppingCart shoppingCart, Store store, Product product, int amount) {
        return shoppingCart.addProductToCart(store, product.cloneProduct(), amount);
    }

    @Override
    public boolean removeProductInShoppingBag(String username, ShoppingCart shoppingCart, Store store, Product product) {
        return shoppingCart.removeProductInCart(store, product);
    }

    @Override
    public boolean changeProductAmountInShoppingBag(String username, ShoppingCart shoppingCart, int storeId, int amount, int productSn) {
        return shoppingCart.changeProductAmountInShoppingBag(storeId, amount, productSn);
    }

    @Override
    public void login(String username, ShoppingCart shoppingCart) {
        // Its need to be empty
    }

    @Override
    public void saveShoppingCart(String username) {
        // Its need to be empty
    }

    @Override
    public ShoppingCart getShoppingCart(String username, UUID uuid) {
        if(isValidUuid(username, uuid)){
            return users.stream()
                    .filter(userSystem -> userSystem.getUserName().equals(username))
                    .findFirst()
                    .map(UserSystem::getShoppingCart)
                    .orElseThrow(()-> new UserDontExistInTheSystemException(username));
        }
        throw new UserDontExistInTheSystemException(username);
    }

    @Override
    public void loadShoppingCart(UserSystem user) {
        // Its need to be empty
    }


    private int getNewId(){
        return idAcc++;
    }

}
