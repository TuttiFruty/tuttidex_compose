package fr.tuttifruty.pokeapp.domain.usecase

import fr.tuttifruty.pokeapp.data.repository.PokemonRepositoryImpl
import fr.tuttifruty.pokeapp.device.database.dao.PokemonDao
import fr.tuttifruty.pokeapp.device.database.entity.PokemonEntity
import fr.tuttifruty.pokeapp.domain.model.Pokemon
import fr.tuttifruty.pokeapp.domain.repository.PokemonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
internal class GetAllPokemonUseCaseImplTest {

    private var testDispatcher = TestCoroutineDispatcher()

    private val fakeFlowOfListPokemon: Flow<List<Pokemon>> = flow {
        emit(
            listOf(
                generatePokemon(1),
                generatePokemon(2),
                generatePokemon(3),
            )
        )
    }

    private fun generatePokemon(id: Int): Pokemon {
        return Pokemon(
            number = id,
            name = "Boulou${id}",
            baseExp = id * 10,
            height = id * 10f,
            weight = id * 10f,
            types = "feu",
            imageUrl = "",
            isCaptured = false
        )
    }

    @Mock
    private lateinit var mockPokemonRepository: PokemonRepository

    private val useCase: GetAllPokemonUseCase
        get() = GetAllPokemonUseCaseImpl(
            mockPokemonRepository,
        )

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `calling useCase should send back succeed result with a Flow of a list of Pokemon`() = runBlocking{
        //GIVEN
        val fakeInput = null
        Mockito.`when`(mockPokemonRepository.getPokemons()).thenReturn(fakeFlowOfListPokemon)

        //WHEN
        val result = useCase(fakeInput)
        val bodyResult = result.getOrNull()
        //THEN
        assertTrue(result.isSuccess)

        bodyResult!!.pokemons.collect { listPokemon ->
            assertTrue(listPokemon.isNotEmpty())
        }
    }
}