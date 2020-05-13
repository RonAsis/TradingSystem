package com.wsep202.TradingSystem.web.controllers;

import com.wsep202.TradingSystem.dto.ReceiptDto;
import com.wsep202.TradingSystem.dto.StoreDto;
import com.wsep202.TradingSystem.service.user_service.SellerManagerService;
import com.wsep202.TradingSystem.web.controllers.api.PublicApiPaths;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(PublicApiPaths.SELLER_MANAGER_PATH)
@CrossOrigin(origins = "http://localhost:4200")
@Api(value = "API to seller manager", produces = "application/json")
@RequiredArgsConstructor
public class SellerManagerController {

    private final SellerManagerService sellerManagerService;

    /**
     * View store purchase history
     */
    @ApiOperation(value = "view purchase history of manager")
    @GetMapping("view-purchase-history-of-manager/{username}/{storeId}/{uuid}")
    public List<ReceiptDto> viewPurchaseHistoryOfManager(@PathVariable String username,
                                                         @PathVariable int storeId,
                                                         @PathVariable UUID uuid) {
        return sellerManagerService.viewPurchaseHistoryOfManager(username, storeId, uuid);
    }

    /**
     * add manager
     */
    @ApiOperation(value = "get manage stores")
    @GetMapping("get-manage-stores/{manageUsername}/{uuid}")
    public List<StoreDto> getManageStores(@PathVariable String manageUsername,
                                          @PathVariable UUID uuid) {
        return sellerManagerService.getMangeStores(manageUsername, uuid);
    }

    /**
     * add manager
     */
    @ApiOperation(value = "get operations can do")
    @GetMapping("get-operations-can-do/{manageUsername}/{storeId}/{uuid}")
    public List<String> getOperationsCanDo(@PathVariable String manageUsername,
                                           @PathVariable int storeId,
                                           @PathVariable UUID uuid) {
        return sellerManagerService.getOperationsCanDo(manageUsername, storeId, uuid);
    }


}
