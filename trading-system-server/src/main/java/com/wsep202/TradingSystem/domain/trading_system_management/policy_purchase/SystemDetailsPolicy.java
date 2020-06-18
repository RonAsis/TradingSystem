package com.wsep202.TradingSystem.domain.trading_system_management.policy_purchase;

import com.wsep202.TradingSystem.domain.exception.PurchasePolicyException;
import com.wsep202.TradingSystem.domain.exception.TradingSystemException;
import com.wsep202.TradingSystem.domain.trading_system_management.purchase.BillingAddress;
import com.wsep202.TradingSystem.domain.trading_system_management.Product;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;

@Setter
@Getter
@Slf4j
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class SystemDetailsPolicy extends PurchasePolicy {


    /**
     * the days in the week any user is permitted to perform a purchase
     */
    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = Day.class, fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Set<Day> storeWorkDays;

    @Override
    public boolean isApproved(Purchase purchase, Map<Product, Integer> products,
                              BillingAddress userAddress) throws TradingSystemException {
        Day purchaseDate = Day.getDay(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
        if(!storeWorkDays.contains(purchaseDate)){
            //user tried to purchase NOT in one of the work days of the store so policy failed
            //notice the user
            log.info("you tried to purchase in store out its working days.\n" +
                    "purchase policy ID: "+purchase.getPurchaseId());
            throw new PurchasePolicyException("Sorry," +
                    " but purchase doesn't stands at the store purchase policy:" +
                    " store is closed at: "+purchaseDate.toString());

        }
        //stands in the policy terms
        log.info("the user passed the purchase policy with ID:" +
                " " +purchase.getPurchaseId()+".");
        return true;
    }

    /**
     * edit the days that the store works
     * @param storeWorkDays - relevant days of work
     * @return true if success. else false
     */
    public boolean edit(Purchase purchase, Set<Day> storeWorkDays){
        if (storeWorkDays != null && !storeWorkDays.isEmpty()){
            this.storeWorkDays = storeWorkDays;
            log.info("days was updated in system purchase policy number " + purchase.getPurchaseId());
            return true;
        }
        log.info("problem with updating days in system purchase policy number " + purchase.getPurchaseId());
        return false;
    }
}
