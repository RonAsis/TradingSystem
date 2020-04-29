package com.wsep202.TradingSystem.service.user_service;

import com.wsep202.TradingSystem.domain.trading_system_management.TradingSystemFacade;
import com.wsep202.TradingSystem.dto.ReceiptDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdministratorService {

    private final TradingSystemFacade tradingSystemFacade;

    /**
     * View store purchase history
     */
    public List<ReceiptDto> viewPurchaseHistory(String administratorUsername, int storeId, UUID uuid){
        return tradingSystemFacade.viewPurchaseHistory(administratorUsername, storeId, uuid);
    }

    /**
     * View buyer purchase history
     */
    public List<ReceiptDto> viewPurchaseHistory(String administratorUsername, String userName, UUID uuid){
        return tradingSystemFacade.viewPurchaseHistory(administratorUsername, userName, uuid);
    }

}
