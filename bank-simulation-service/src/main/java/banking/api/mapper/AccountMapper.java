package banking.api.mapper;

import banking.api.Annotation.EncodedMapping;
import banking.api.domain.Account;
import banking.api.request.AccountPostRequest;
import banking.api.response.AccountGetResponse;
import banking.api.response.AccountOwnedGetResponse;
import banking.api.response.AccountPostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {PasswordEncoderMapper.class}
)
public interface AccountMapper {


    @Mapping(target = "password", qualifiedBy = EncodedMapping.class)
    Account toAccount(AccountPostRequest accountPostRequest);

    AccountGetResponse toAccountGetResponse(Account account);
    AccountPostResponse toAccountPostResponse(Account account);

    AccountOwnedGetResponse toAccountOwnedGetResponse(Account account);
}
