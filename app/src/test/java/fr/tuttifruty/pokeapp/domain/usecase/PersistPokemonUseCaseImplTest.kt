package fr.tuttifruty.pokeapp.domain.usecase

import arrow.core.Either
import arrow.core.merge
import fr.tuttifruty.pokeapp.domain.model.Pokemon
import fr.tuttifruty.pokeapp.domain.repository.PokemonRepository
import fr.tuttifruty.pokeapp.domain.usecase.PersistPokemonUseCase.PokemonToUpdate
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
internal class PersistPokemonUseCaseImplTest {

    private var testDispatcher = StandardTestDispatcher()

    private fun generatePokemon(id: Int): Pokemon {
        return Pokemon(
            number = id,
            name = "Boulou${id}",
            baseExp = id * 10,
            height = id * 10f,
            weight = id * 10f,
            types = "feu",
            imageUrl = "",
            isCaptured = false,
            description = "Fake pokemon",
            imageOfCaptureFront = "front/img/of/pokemon",
            imageOfCaptureBack = "back/img/of/pokemon",
            stats = listOf(Pokemon.Stat.HP(10, 100))
        )
    }

    @Mock
    private lateinit var mockPokemonRepository: PokemonRepository

    private val useCase: PersistPokemonUseCase
        get() = PersistPokemonUseCaseImpl(
            mockPokemonRepository,
        )

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `calling useCase should send back succeed result when repo succeed and input exist`() =
        runBlocking {
            //GIVEN
            val fakeInput = PokemonToUpdate(generatePokemon(1))
            Mockito.`when`(mockPokemonRepository.updatePokemon(fakeInput.pokemon)).thenReturn(1)

            //WHEN
            val result = useCase(fakeInput)
            val bodyResult = result.orNull()
            //THEN
            assertTrue(result is Either.Right)
            assertNull(bodyResult)
        }

    @Test
    fun `calling useCase should send back failed (PokemonToPersistMustExist) result when input doesn't exist`() =
        runBlocking {
            //GIVEN

            //WHEN
            val result = useCase()
            //THEN
            assertTrue(result is Either.Left)
            assertTrue(result.merge() is PersistPokemonUseCase.PokemonToPersistMustExist)
        }

    @Test
    fun `calling useCase should send back failed (FailedToPersistPokemon) result when repo failed`() =
        runBlocking {
            //GIVEN
            val fakeInput = PokemonToUpdate(generatePokemon(2))
            Mockito.`when`(mockPokemonRepository.updatePokemon(fakeInput.pokemon)).thenReturn(0)

            //WHEN
            val result = useCase(fakeInput)
            //THEN
            assertTrue(result is Either.Left)
            assertTrue(result.merge() is PersistPokemonUseCase.FailedToPersistPokemon)
        }
}