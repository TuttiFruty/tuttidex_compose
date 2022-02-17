package fr.tuttifruty.pokeapp.domain.usecase

import arrow.core.Either
import arrow.core.merge
import fr.tuttifruty.pokeapp.domain.repository.PokemonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
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

    private var testDispatcher = StandardTestDispatcher()

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
        Mockito.`when`(mockPokemonRepository.persistPokemons()).thenReturn(Either.Right(0))

        //WHEN
        val result = useCase()
        val bodyResult = result.orNull()
        //THEN
        assertTrue(result is Either.Right)
        assertNull(bodyResult)
    }

    @Test
    fun `calling useCase should send back failed result when repo failed `() = runBlocking {
        //GIVEN
        Mockito.`when`(mockPokemonRepository.persistPokemons()).thenReturn(
            Either.Left(
                PersistAllPokemonUseCase.FailedToPersistAllPokemons()
            )
        )

        //WHEN
        val result = useCase()
        //THEN
        assertTrue(result is Either.Left)
        assertTrue(result.merge() is PersistAllPokemonUseCase.FailedToPersistAllPokemons)
    }
}