package com.wsep202.TradingSystem.domain.trading_system_management;


import com.wsep202.TradingSystem.domain.exception.*;
import com.wsep202.TradingSystem.domain.factory.FactoryObjects;
import com.wsep202.TradingSystem.dto.*;
import javafx.util.Pair;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Type;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
public class TradingSystemFacade {

    /**
     * inorder talk with the system
     */
    private final TradingSystem tradingSystem;

    /**
     * inorder to convert dto to object
     */
    private final ModelMapper modelMapper;

    /**
     * inorder to create new objects
     */
    private final FactoryObjects factoryObjects;

    /**
     * view purchase history of user logged in
     *
     * @param userName - must be logged in
     * @return all the receipt of the user
     */
    public List<ReceiptDto> viewPurchaseHistory(@NotBlank String userName, UUID uuid) {
        try {
            UserSystem user = tradingSystem.getUser(userName, uuid);   //get registered user by his username
            List<Receipt> receipts = user.getReceipts(); //get user receipts
            return convertReceiptList(receipts);
        } catch (TradingSystemException e) {
            log.error("Tried to view his purchase history and failed", e);
            return null;
        }
    }

    /**
     * administrator view purchase history of store
     *
     * @param administratorUsername the user name of the admin
     * @param storeId               - the store id of the store that want view purchase history
     * @return all the receipt of the store
     */
    public List<ReceiptDto> viewPurchaseHistory(@NotBlank String administratorUsername, int storeId, UUID uuid) {
        try {
            //get the store if the user has admin permissions (it is admin)
            Store store = tradingSystem.getStoreByAdmin(administratorUsername, storeId, uuid);
            List<Receipt> receipts = store.getReceipts();
            return convertReceiptList(receipts);
        } catch (TradingSystemException e) {
            log.error("Tried to view purchase history of store failed", e);
            return null;
        }
    }

    /**
     * administrator view purchase history of user
     *
     * @param administratorUsername the user name of the admin
     * @param userName              - the userName that want view purchase history
     * @return all the receipt of the user
     */
    public List<ReceiptDto> viewPurchaseHistory(@NotBlank String administratorUsername, @NotBlank String userName, UUID uuid) {
        try {
            UserSystem userByAdmin = tradingSystem.getUserByAdmin(administratorUsername, userName, uuid);
            return convertReceiptList(userByAdmin.getReceipts());
        } catch (TradingSystemException e) {
            log.error("Tried to view purchase history of user and failed", e);
            return null;
        }
    }

    /**
     * Manager view purchase history of store
     *
     * @param managerUsername - the manager Username of the store manger that want view purchase history
     * @param storeId         - the store that want view the purchase history
     * @param uuid
     * @return all the receipt of the store
     */
    public List<ReceiptDto> viewPurchaseHistoryOfManager(@NotBlank String managerUsername, int storeId, UUID uuid) {
        try {
            //get the registered user with username
            UserSystem mangerStore = tradingSystem.getUser(managerUsername, uuid);
            return convertReceiptList(mangerStore.getManagerStore(storeId).getReceipts());
        } catch (TradingSystemException e) {
            log.error("The user is not a manager of the store ,so cant see purchase history of store", e);
            return null;
        }
    }

    /**
     * Owner view purchase history of store
     *
     * @param ownerUserName the owner Username of the store manger that want view purchase history
     * @param storeId       - the store that want view the purchase history
     * @return all the receipt of the store
     */
    public List<ReceiptDto> viewPurchaseHistoryOfOwner(@NotBlank String ownerUserName, int storeId, UUID uuid) {
        try {
            UserSystem user = tradingSystem.getUser(ownerUserName, uuid);
            return convertReceiptList(user.getOwnerStore(storeId).getReceipts());
        } catch (TradingSystemException e) {
            log.error("Can't see history of the store", e);
            return null;
        }
    }

    /**
     * add product to store
     *
     * @param ownerUsername the username of the owner store
     * @param storeId       - of the store that want add product
     * @param productName   - the name of the new product
     * @param category      - the category of the product
     * @param amount        - the amount of the product
     * @param cost          - the cost of the product
     * @return true if succeed
     */
    public boolean addProduct(@NotBlank String ownerUsername, int storeId,
                              @NotBlank String productName, @NotBlank String category,
                              int amount, double cost, UUID uuid) {
        try {
            UserSystem user = tradingSystem.getUser(ownerUsername, uuid); //get registered user with ownerUsername
            Store ownerStore = user.getOwnerStore(storeId); //verify he owns store with storeId
            //convert to a category we can add to the product
            ProductCategory productCategory = ProductCategory.getProductCategory(category);
            Product product = new Product(productName, productCategory, amount, cost, storeId);
            return ownerStore.addNewProduct(user, product);
        } catch (TradingSystemException e) {
            log.error("add product failed", e);
            return false;
        }
    }

    /**
     * delete product form store
     *
     * @param ownerUsername the username of the owner store
     * @param storeId       - of the store that want delete product
     * @param productSn     - the sn of the product
     * @return true if succeed
     */
    public boolean deleteProductFromStore(@NotBlank String ownerUsername, int storeId, int productSn, UUID uuid) {
        try {
            UserSystem user = tradingSystem.getUser(ownerUsername, uuid); //get the registered user
            Store ownerStore = user.getOwnerStore(storeId); //verify he is owner of the store
            return ownerStore.removeProductFromStore(user, productSn);
        } catch (TradingSystemException e) {
            log.error("deleteProduct from store failed", e);
            return false;
        }
    }

    /**
     * edit product
     *
     * @param ownerUsername the username of the owner store
     * @param storeId       - of the store that want edit product
     * @param productSn     - the sn of the product
     * @param productName   - the name of the product
     * @param category      - the category of the product
     * @param amount        - the amount of the product
     * @param cost          - the cost of the product
     * @param uuid
     * @return true if succeed
     */
    public boolean editProduct(@NotBlank String ownerUsername, int storeId, int productSn, @NotBlank String productName,
                               @NotBlank String category, int amount, double cost, UUID uuid) {
        try {
            UserSystem user = tradingSystem.getUser(ownerUsername, uuid);
            Store ownerStore = user.getOwnerStore(storeId);
            return ownerStore.editProduct(user, productSn, productName, category, amount, cost);
        } catch (TradingSystemException e) {
            log.error("editProduct failed", e);
            return false;
        }
    }

    /**
     * add new owner to store
     *
     * @param ownerUsername    the username of the owner store
     * @param storeId          - of the store that want add new owner
     * @param newOwnerUsername - the new owner
     * @param uuid
     * @return true if succeed
     */
    public boolean addOwner(@NotBlank String ownerUsername, int storeId, @NotBlank String newOwnerUsername, UUID uuid) {
        try {
            UserSystem ownerUser = tradingSystem.getUser(ownerUsername, uuid);
            UserSystem newOwnerUser = tradingSystem.getUser(newOwnerUsername, uuid);
            Store ownerStore = ownerUser.getOwnerStore(storeId);
            return tradingSystem.addOwnerToStore(ownerStore, ownerUser, newOwnerUser);
        } catch (TradingSystemException e) {
            log.error("Add owner failed", e);
            return false;
        }
    }

    /**
     * add manger to the store with the default permission
     *
     * @param ownerUsername      the username of the owner store
     * @param storeId            - of the store that want add new owner
     * @param newManagerUsername - the new manger
     * @param uuid
     * @return true if succeed
     */
    public boolean addManager(@NotBlank String ownerUsername, int storeId, @NotBlank String newManagerUsername, UUID uuid) {
        try {
            UserSystem ownerUser = tradingSystem.getUser(ownerUsername, uuid);
            UserSystem newManagerUser = tradingSystem.getUser(newManagerUsername, uuid);
            Store ownedStore = ownerUser.getOwnerStore(storeId);
            return tradingSystem.addMangerToStore(ownedStore, ownerUser, newManagerUser);
        } catch (TradingSystemException e) {
            log.error("Add manager failed", e);
            return false;
        }
    }

    /**
     * add permission the manger in the store
     *
     * @param ownerUsername   the username of the owner store
     * @param storeId         - of the store that want add permission the manger
     * @param managerUserName - the user name of the manger
     * @param permission      - the new permission
     * @param uuid
     * @return true if succeed
     */
    public boolean addPermission(@NotBlank String ownerUsername, int storeId, @NotBlank String managerUserName, @NotBlank String permission, UUID uuid) {
        try {
            UserSystem ownerUser = tradingSystem.getUser(ownerUsername, uuid);
            Store ownedStore = ownerUser.getOwnerStore(storeId);
            UserSystem managerStore = ownedStore.getManager(ownerUser, managerUserName);
            StorePermission storePermission = StorePermission.getStorePermission(permission);
            return ownedStore.addPermissionToManager(ownerUser, managerStore, storePermission);
        } catch (TradingSystemException e) {
            log.error("Add permission failed", e);
            return false;
        }
    }

    /**
     * remove manger from the store by the owner that appointed him
     *
     * @param ownerUsername   owner that appointed the manger
     * @param storeId         - the id that of the store that want remove the manger
     * @param managerUsername - the manger that want remove
     * @param uuid
     * @return true if succeed
     */
    public boolean removeManager(@NotBlank String ownerUsername, int storeId, @NotBlank String managerUsername, UUID uuid) {
        try {
            UserSystem ownerUser = tradingSystem.getUser(ownerUsername, uuid); //get registered user
            Store ownedStore = ownerUser.getOwnerStore(storeId);    //verify the remover is owner
            UserSystem managerStore = ownedStore.getManager(ownerUser, managerUsername);
            return tradingSystem.removeManager(ownedStore, ownerUser, managerStore);
        } catch (TradingSystemException e) {
            log.error("Add permission failed", e);
            return false;
        }
    }

    /**
     * the user name logout from the system
     *
     * @param username - the username that want logout
     * @return true if succeed
     */
    public boolean logout(@NotBlank String username, UUID uuid) {
        try {
            UserSystem user = tradingSystem.getUser(username, uuid);
            log.info("the user " + username + " logged out.");
            return user.logout();
        } catch (TradingSystemException e) {
            log.error("Failed to logout", e);
            return false;
        }
    }

    /**
     * open new store
     *
     * @param usernameOwner     - the user that open the store
     * @param purchasePolicyDto - the purchase policy
     * @param discountPolicyDto - the discount Policy
     * @param storeName         - the name of the new store
     * @return true if succeed
     */
    public StoreDto openStore(@NotBlank String usernameOwner, @NotNull PurchasePolicyDto purchasePolicyDto,
                             @NotNull DiscountPolicyDto discountPolicyDto, @NotBlank String storeName, UUID uuid) {
        try {
            UserSystem user = tradingSystem.getUser(usernameOwner, uuid);
            PurchasePolicy purchasePolicy = Objects.nonNull(purchasePolicyDto) ? modelMapper.map(purchasePolicyDto, PurchasePolicy.class) : null;
            DiscountPolicy discountPolicy = Objects.nonNull(discountPolicyDto)? modelMapper.map(discountPolicyDto, DiscountPolicy.class) : null;
            Store store = tradingSystem.openStore(user, purchasePolicy, discountPolicy, storeName);
            return Objects.nonNull(store) ? modelMapper.map(store, StoreDto.class) : null;
        } catch (TradingSystemException e) {
            log.error("failed to open store", e);
            return null;
        }
    }

    /**
     * register new user to the system
     *
     * @param userName  - the new username
     * @param password  - the password of the user
     * @param firstName - the first name of the new user
     * @param lastName  - the last name of the new user
     * @return true if succeed
     */
    public boolean registerUser(@NotBlank String userName, @NotBlank String password, @NotBlank String firstName, @NotBlank String lastName) {
        log.info("started session of registration for user: " + userName);
        UserSystem userSystem = factoryObjects.createSystemUser(userName, firstName, lastName, password);
        return tradingSystem.registerNewUser(userSystem);
    }


    /**
     * user login to the system
     *
     * @param userName - the username need to be register to the system for suc
     * @param password - the password must be the correct password of the user
     * @return true if succeed
     */
    public Pair<UUID, Boolean> login(@NotBlank String userName, @NotBlank String password) {
        try {
            return tradingSystem.login(userName, password);
        } catch (TradingSystemException e) {
            log.error("failed to login", e);
            return null;
        }
    }

    /**
     * view the store info
     *
     * @param storeId - the store id that want to see the details
     * @return the store details
     */
    public StoreDto viewStoreInfo(int storeId) {
        try {
            Store store = tradingSystem.getStore(storeId);
            return Objects.nonNull(store) ? modelMapper.map(store, StoreDto.class) : null;
        } catch (TradingSystemException e) {
            log.error("failed to see store info ", e);
            return null;
        }
    }

    /**
     * view product of specific store
     *
     * @param storeId   - the store id that include the product
     * @param productId - the product id that want view the product details
     * @return the product details
     */
    public ProductDto viewProduct(int storeId, int productId) {
        try {
            Store store = tradingSystem.getStore(storeId);
            Product product = store.getProduct(productId);
            return Objects.nonNull(product) ? modelMapper.map(product, ProductDto.class) : null;
        } catch (TradingSystemException e) {
            log.error("view product failed", e);
            return null;
        }
    }

    /**
     * search product by name
     *
     * @param productName - the product name that want to search
     * @return list of all the product with this name
     */
    public List<ProductDto> searchProductByName(@NotBlank String productName) {
        log.info("search product by name: " + productName);
        List<Product> products = tradingSystem.searchProductByName(productName);
        return convertProductDtoList(products);
    }

    /**
     * search product by category
     *
     * @param category - the category of product that want to search
     * @return list of all the products that belong to this category
     */
    public List<ProductDto> searchProductByCategory(@NotBlank String category) {
        try {
            log.info("search product by category: " + category);
            ProductCategory productCategory = ProductCategory.getProductCategory(category);
            List<Product> products = tradingSystem.searchProductByCategory(productCategory);
            return convertProductDtoList(products);
        } catch (TradingSystemException e) {
            log.error("failed search product by category", e);
            return null;
        }
    }

    /**
     * search product by keyWords
     *
     * @param keyWords - the keyWords that want search with
     * @return list of all the products that include the keyWords
     */
    public List<ProductDto> searchProductByKeyWords(@NotNull List<@NotBlank String> keyWords) {
        log.info("search product by keywords: " + keyWords);
        List<Product> products = tradingSystem.searchProductByKeyWords(keyWords);
        return convertProductDtoList(products);
    }

    /**
     * filter by range price
     *
     * @param productDtos the list of products
     * @param minPrice    - the minPrice price
     * @param maxPrice    - the maxPrice price
     * @return list of all the products filtered by range price
     */
    public List<ProductDto> filterByRangePrice(@NotNull List<@NotNull ProductDto> productDtos, double minPrice, double maxPrice) {
        log.info("filter products by price range [" + minPrice + "," + maxPrice + "]");
        List<Product> products = converterProductsList(productDtos);
        List<Product> productsFiltered = tradingSystem.filterByRangePrice(products, minPrice, maxPrice);
        return convertProductDtoList(productsFiltered);
    }

    /**
     * filter by product rank
     *
     * @param productDtos the list of products
     * @param rank        - the product rank
     * @return list of all the products filtered by the product rank
     */
    public List<ProductDto> filterByProductRank(@NotNull List<@NotNull ProductDto> productDtos, int rank) {
        log.info("filter products by product rank " + rank);
        List<Product> products = converterProductsList(productDtos);
        List<Product> productsFiltered = tradingSystem.filterByProductRank(products, rank);
        return convertProductDtoList(productsFiltered);
    }

    /**
     * filter by store rank
     *
     * @param productDtos the list of products
     * @param rank        the rank of the store
     * @return list of all the products filtered by the store rank
     */
    public List<ProductDto> filterByStoreRank(@NotNull List<@NotNull ProductDto> productDtos, int rank) {
        log.info("filter products by store rank " + rank);
        List<Product> products = converterProductsList(productDtos);
        List<Product> productsFiltered = tradingSystem.filterByStoreRank(products, rank);
        return convertProductDtoList(productsFiltered);
    }

    /**
     * filter by category
     *
     * @param productDtos the list of products
     * @param category    the category of the product
     * @return list of all the products filtered by the category
     */
    public List<ProductDto> filterByStoreCategory(@NotNull List<@NotNull ProductDto> productDtos, @NotBlank String category) {
        try {
            log.info("filter products by category " + category);
            List<Product> products = converterProductsList(productDtos);
            ProductCategory productCategory = ProductCategory.getProductCategory(category);
            List<Product> productsFiltered = tradingSystem.filterByStoreCategory(products, productCategory);
            return convertProductDtoList(productsFiltered);
        } catch (TradingSystemException e) {
            log.error("filter by store category", e);
            return null;
        }
    }

    /**
     * @param username  the username that want save ShoppingBag
     * @param storeId   the storeId that the product belong to
     * @param productSn - the sn of the prodcut
     * @param amount    - the amount that want save
     * @return true if succeed
     */
    public boolean saveProductInShoppingBag(@NotBlank String username, int storeId, int productSn, int amount, UUID uuid) {
        try {
            log.info("save product " + productSn + "in bag of store " + storeId);
            UserSystem user = tradingSystem.getUser(username, uuid);
            Store store = tradingSystem.getStore(storeId);
            Product product = store.getProduct(productSn);
            return user.saveProductInShoppingBag(store, product, amount);

        } catch (TradingSystemException e) {
            log.error("saveInBag", e);
            return false;
        }
    }

    /**
     * view products in shopping cart
     *
     * @param username the username that want view the ShoppingBag
     * @return shopping bag
     */
    public ShoppingCartDto watchShoppingCart(@NotBlank String username, UUID uuid) {
        try {
            UserSystem user = tradingSystem.getUser(username, uuid);
            ShoppingCart shoppingCart = user.getShoppingCart();
            return Objects.nonNull(shoppingCart) ?modelMapper.map(shoppingCart, ShoppingCartDto.class) : null;
        } catch (TradingSystemException e) {
            log.error("view products in shopping bag", e);
            return null;
        }
    }

    /**
     * remove product in shopping bag
     *
     * @param username  the username that want remove product from the ShoppingBag
     * @param storeId   - the id of the store that the product belong to
     * @param productSn - the sn of the product
     * @return true if succeed
     */
    public boolean removeProductInShoppingBag(@NotBlank String username, int storeId, int productSn, UUID uuid) {
        try {
            UserSystem user = tradingSystem.getUser(username, uuid);
            Store store = tradingSystem.getStore(storeId);
            Product product = store.getProduct(productSn);
            return user.removeProductInShoppingBag(store, product);
        } catch (TradingSystemException e) {
            log.error("Remove product in shopping bag failed", e);
            return false;
        }
    }

    /**
     * purchase shopping cart use for guest
     *
     * @param shoppingCartDto the shopping cart
     * @return the receipt
     */
    public List<ReceiptDto> purchaseShoppingCart(@NotNull ShoppingCartDto shoppingCartDto,
                                                 @NotNull PaymentDetailsDto paymentDetailsDto,
                                                 @NotNull BillingAddressDto billingAddressDto) {
        ShoppingCart shoppingCart = Objects.nonNull(shoppingCartDto) ? modelMapper.map(shoppingCartDto, ShoppingCart.class) : null;
        PaymentDetails paymentDetails = Objects.nonNull(paymentDetailsDto) ? modelMapper.map(paymentDetailsDto, PaymentDetails.class) : null;
        BillingAddress billingAddress = Objects.nonNull(billingAddressDto) ? modelMapper.map(billingAddressDto, BillingAddress.class) : null;
        List<Receipt> receipts = tradingSystem.purchaseShoppingCartGuest(shoppingCart, paymentDetails, billingAddress);
        return Objects.nonNull(receipts) ? convertReceiptList(receipts) : null;
    }

    /**
     * purchase shopping cart of resisted user
     *
     * @param username - the username that want purchase shopping cart
     * @return the receipt
     */
    public List<ReceiptDto> purchaseShoppingCart(@NotBlank String username,
                                                 @NotNull PaymentDetailsDto paymentDetailsDto,
                                                 @NotNull BillingAddressDto billingAddressDto,
                                                 UUID uuid) {
        try {
            UserSystem user = tradingSystem.getUser(username, uuid);
            PaymentDetails paymentDetails = Objects.nonNull(paymentDetailsDto) ? modelMapper.map(paymentDetailsDto, PaymentDetails.class) : null;
            BillingAddress billingAddress = Objects.nonNull(billingAddressDto) ? modelMapper.map(billingAddressDto, BillingAddress.class) : null;
            List<Receipt> receipts = tradingSystem.purchaseShoppingCart(paymentDetails, billingAddress, user);
            return Objects.nonNull(receipts) ? convertReceiptList(receipts) : null;
        } catch (TradingSystemException e) {
            log.error("tried to purchase cart failed", e);
            return null;
        }
    }

    //////////////////////////////// converters ///////////////////////////

    /**
     * converter of Receipt list to ReceiptDto list
     *
     * @param receipts - list of receipts
     * @return list of ReceiptDto
     */
    private List<ReceiptDto> convertReceiptList(@NotNull List<@NotNull Receipt> receipts) {
        Type listType = new TypeToken<List<ReceiptDto>>() {}.getType();
        return modelMapper.map(receipts, listType);
    }

    /**
     * converter of Product list to ProductDto list
     *
     * @param products - list of products
     * @return list of ProductDto
     */
    private List<ProductDto> convertProductDtoList(@NotNull List<@NotNull Product> products) {
        Type listType = new TypeToken<List<ProductDto>>() {}.getType();
        return modelMapper.map(products, listType);
    }

    /**
     * converter of ProductDto list to Product list
     *
     * @param productDtos - list of productDtos
     * @return list of products
     */
    private List<Product> converterProductsList(@NotNull List<@NotNull ProductDto> productDtos) {
        Type listType = new TypeToken<List<Product>>() {}.getType();
        return modelMapper.map(productDtos, listType);
    }

}
