package banking.api.mapper;

import banking.api.domain.Account;
import banking.api.request.AccountPostRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AccountMapper {

    Account toAccount(AccountPostRequest accountPostRequest);

}
