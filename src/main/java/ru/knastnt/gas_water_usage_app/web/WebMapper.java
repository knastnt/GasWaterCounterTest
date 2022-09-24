package ru.knastnt.gas_water_usage_app.web;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.knastnt.gas_water_usage_app.model.Account;
import ru.knastnt.gas_water_usage_app.web.dto.AccountInfoDto;

@Component
public class WebMapper {
    @Transactional
    public AccountInfoDto map(Account account) {
        if (account == null) return null;
        AccountInfoDto result = new AccountInfoDto();

        result.setUserName(account.getCustomer().getDemoFio());
        result.setAccountNumber(account.getAccountNum());
        result.setMeters(account.);

        return result;
    }
}
