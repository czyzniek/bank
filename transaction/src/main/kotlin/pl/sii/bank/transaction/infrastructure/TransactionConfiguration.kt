package pl.sii.bank.transaction.infrastructure

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.sii.bank.transaction.domain.AccountBalanceStore
import pl.sii.bank.transaction.domain.AuthorizeTransactionUseCase
import pl.sii.bank.transaction.domain.ConfirmTransactionUseCase
import pl.sii.bank.transaction.domain.CustomerAccountStore
import pl.sii.bank.transaction.domain.ExternalTransferProvider
import pl.sii.bank.transaction.domain.FetchTransactionStatusUseCase
import pl.sii.bank.transaction.domain.InitializeTransactionUseCase
import pl.sii.bank.transaction.domain.SaveCustomerAccountUseCase
import pl.sii.bank.transaction.domain.TransactionStore

@Configuration
class TransactionConfiguration {

    @Bean
    fun initializeTransactionUseCase(transactionStore: TransactionStore): InitializeTransactionUseCase =
        InitializeTransactionUseCase(transactionStore)

    @Bean
    fun confirmTransactionUseCase(
        transactionStore: TransactionStore,
        accountBalanceStore: AccountBalanceStore
    ): ConfirmTransactionUseCase =
        ConfirmTransactionUseCase(transactionStore, accountBalanceStore)

    @Bean
    fun authorizeTransactionUseCase(
        transactionStore: TransactionStore,
        customerAccountStore: CustomerAccountStore,
        externalTransferProvider: ExternalTransferProvider
    ): AuthorizeTransactionUseCase =
        AuthorizeTransactionUseCase(transactionStore, customerAccountStore, externalTransferProvider)

    @Bean
    fun fetchTransactionStatusUseCase(transactionStore: TransactionStore): FetchTransactionStatusUseCase =
        FetchTransactionStatusUseCase(transactionStore)

    @Bean
    fun saveCustomerAccountUseCase(customerAccountStore: CustomerAccountStore): SaveCustomerAccountUseCase =
        SaveCustomerAccountUseCase(customerAccountStore)
}
