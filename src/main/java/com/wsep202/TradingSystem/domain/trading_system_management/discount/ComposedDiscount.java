package com.wsep202.TradingSystem.domain.trading_system_management.discount;

import com.wsep202.TradingSystem.domain.trading_system_management.Product;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Setter
@Getter
@Slf4j
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ComposedDiscount extends DiscountPolicy {

    /**
     * amount of product from to apply discount
     */
    private Map<Product, Integer> amountOfProductsForApplyDiscounts;

    /**
     * products that has the specified discount
     */
    private Map<Product, Integer> productsUnderThisDiscount;

    /**
     * children components of the composite conditional discount
     */
    private List<Discount> composedDiscounts;

    /**
     * the operation between the conditionals discounts
     */
    private CompositeOperator compositeOperator;

    @Override
    public void applyDiscount(Discount discount, Map<Product, Integer> products) {
        if (!amountOfProductsForApplyDiscounts.isEmpty() && isApprovedProducts(discount, products)) {
            createConditionalDiscount().applyDiscount(discount, products);
        } else {
            switch (compositeOperator) {
                case AND:
                    composedDiscounts.forEach(discountPolicy -> discountPolicy.applyDiscount(products));
                    break;
                case OR:
                    AtomicBoolean oneAlreadyApply = new AtomicBoolean(false);
                    composedDiscounts.forEach(discountCur -> {
                        if (!oneAlreadyApply.get()) {
                            if (discountCur.isApprovedProducts(products)) {
                                discountCur.applyDiscount(products);
                                oneAlreadyApply.set(true);
                            }
                        }
                        discountCur.setApplied(true);
                    });
                    break;
                case XOR:
                    AtomicInteger numOfPolicyApproved = new AtomicInteger(getNumOfPolicyApproved(discount, products));
                    numOfPolicyApproved = numOfPolicyApproved.get() % 2 != 0 ? numOfPolicyApproved : new AtomicInteger(numOfPolicyApproved.get() - 1);
                    AtomicInteger finalNumOfPolicyApproved = numOfPolicyApproved;
                    composedDiscounts.forEach(discountCur -> {
                        if (finalNumOfPolicyApproved.get() > 0) {
                            if (discountCur.isApprovedProducts(products)) {
                                finalNumOfPolicyApproved.getAndDecrement();
                                discountCur.applyDiscount(products);
                            }
                        }
                        discountCur.setApplied(true);
                    });
                    break;
            }
        }
    }

    private ConditionalProductDiscount createConditionalDiscount(){
        return ConditionalProductDiscount.builder()
                .amountOfProductsForApplyDiscounts(amountOfProductsForApplyDiscounts)
                .productsUnderThisDiscount(productsUnderThisDiscount)
                .build();
    }
    @Override
    public boolean isApprovedProducts(Discount discount, Map<Product, Integer> products) {
        boolean isApproved = !isExpired(discount);
        switch (compositeOperator) {
            case AND:
                isApproved = isApproved && composedDiscounts.stream()
                        .allMatch(discountPolicy -> discountPolicy.isApprovedProducts(products));
                break;
            case OR:
                isApproved = isApproved && composedDiscounts.stream()
                        .anyMatch(discountCur -> discountCur.isApprovedProducts(products));
                break;
            case XOR:
                isApproved = isApproved && getNumOfPolicyApproved(discount, products) % 2 != 0;
                break;
        }
        return isApproved;
    }

    @Override
    public void undoDiscount(Discount discount, Map<Product, Integer> products) {

    }

    private int getNumOfPolicyApproved(Discount discount, Map<Product, Integer> products) {
        return composedDiscounts.stream()
                .filter(discountCur -> discountCur.isApprovedProducts(products))
                .toArray().length;
    }
}
