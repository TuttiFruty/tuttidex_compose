package fr.tuttifruty.pokeapp.domain.usecase

import fr.tuttifruty.pokeapp.domain.model.Pokemon
import fr.tuttifruty.pokeapp.domain.repository.PokemonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
internal class PersistAllPokemonUseCaseImplTest {

    private var testDispatcher = TestCoroutineDispatcher()

    @Mock
    private lateinit var mockPokemonRepository: PokemonRepository

    private val useCase: PersistAllPokemonUseCase
        get() = PersistAllPokemonUseCaseImpl(
            mockPokemonRepository,
        )

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `calling useCase should send back succeed result when repo succeed `() = runBlocking {
        //GIVEN
        Mockito.`when`(mockPokemonRepository.persistPokemons()).thenReturn(Result.success(0))

        //WHEN
        val result = useCase()
        val bodyResult = result.getOrNull()
        //THEN
        assertTrue(result.isSuccess)
        assertNull(bodyResult)
    }

    @Test
    fun `calling useCase should send back failed result when repo failed `() = runBlocking {
        //GIVEN
        Mockito.`when`(mockPokemonRepository.persistPokemons()).thenReturn(
            Result.failure(
                PersistAllPokemonUseCase.Errors.FailedToPersistAllPokemons()
            )
        )

        //WHEN
        val result = useCase()
        val bodyResult = result.exceptionOrNull()
        //THEN
        assertTrue(result.isFailure)
        assertTrue(bodyResult is PersistAllPokemonUseCase.Errors.FailedToPersistAllPokemons)
    }
}