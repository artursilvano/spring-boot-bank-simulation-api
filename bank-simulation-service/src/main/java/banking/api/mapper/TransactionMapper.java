package banking.api.mapper;

import banking.api.Annotation.EncodedMapping;
import banking.api.domain.Account;
import banking.api.domain.Transaction;
import banking.api.request.AccountPostRequest;
import banking.api.response.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TransactionMapper {

    TransactionGetResponse toTransactionGetResponse(Transaction transaction);
    List<TransactionGetResponse> toTransactionGetResponse(List<Transaction> transactions);

    TransactionPostResponse toTransactionPostResponse(Transaction transaction);

}
