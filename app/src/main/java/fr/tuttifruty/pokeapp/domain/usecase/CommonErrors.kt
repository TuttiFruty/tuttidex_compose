package fr.tuttifruty.pokeapp.domain.usecase


sealed interface CommonErrors :
    GetPokemonUseCase.GetPokemonUseCaseErrors,
    PersistAllPokemonUseCase.PersistAllPokemonUseCaseErrors,
    PersistPokemonUseCase.PersistPokemonUseCaseErrors

class HttpUnsuccessfulCodeError(
    val httpCode: Int,
    override val message: String = "Remote call failed"
) : CommonErrors

class RemoteServerNotReachedError(override val message: String = "Remote server unreachable, try again later") :
    CommonErrors

class UnexpectedApiCommunicationError(override val message: String = "A fatal error occurred, keep calm and catch some pokemon") :
    CommonErrors