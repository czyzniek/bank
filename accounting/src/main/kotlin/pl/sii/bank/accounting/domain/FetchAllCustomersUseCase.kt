package pl.sii.bank.accounting.domain

class FetchAllCustomersUseCase(private val customerStore: CustomerStore) {

    fun execute(): Output =
        Output(customerStore.findAll())

    data class Output(
        val customers: List<Customer>
    )
}
