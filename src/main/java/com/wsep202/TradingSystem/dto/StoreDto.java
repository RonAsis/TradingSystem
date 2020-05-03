package com.wsep202.TradingSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreDto {

    private int storeId;

    private String storeName;

    private  Set<ProductDto> products;

    private PurchasePolicyDto purchasePolicy;

    private Set<DiscountPolicyDto> discountPolicy;

    private String discountType;

    private String purchaseType;

    private List<ReceiptDto> receipts;

    private int rank;
}
