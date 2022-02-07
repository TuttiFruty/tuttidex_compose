package fr.tuttifruty.pokeapp.data.repository

import fr.tuttifruty.pokeapp.data.model.*
import fr.tuttifruty.pokeapp.data.service.PokemonService
import fr.tuttifruty.pokeapp.device.database.dao.PokemonDao
import fr.tuttifruty.pokeapp.device.database.entity.PokemonEntity
import fr.tuttifruty.pokeapp.device.database.entity.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import retrofit2.Response

@ExperimentalCoroutinesApi
internal class PokemonRepositoryImplTest {

    private fun <T> any(): T {
        return Mockito.any<T>()
    }

    private var testDispatcher = TestCoroutineDispatcher()
    private val fakeFlowOfListPokemon: Flow<List<PokemonEntity>> = flow {
        emit(
            listOf(
                generatePokemonEntity(1),
                generatePokemonEntity(2),
                generatePokemonEntity(3),
            )
        )
    }

    private val fakeResultNetwork: ResultNetwork = ResultNetwork(
        count = 3,
        next = null,
        previous = null,
        results = listOf(
            NamedResultNetwork(
                name = "Boulou",
                url = "https://boulou.com/1/"
            ),
            NamedResultNetwork(
                name = "Boulou",
                url = "https://boulou.com/2/"
            ),
            NamedResultNetwork(
                name = "Boulou",
                url = "https://boulou.com/3/"
            ),
        ),
    )


    private fun generatePokemonEntity(id: Int): PokemonEntity {
        return PokemonEntity(
            id = id,
            name = "Boulou${id}",
            baseExperience = id * 10,
            height = id * 10,
            weight = id * 10,
            types = "feu",
            image = "",
            isCaptured = false,
            hp = 10,
            attack = 20,
            defense = 30,
            spAttack = 40,
            spDefense = 50,
            speed = 60,
            totalStat = 340,
            species = 6,
            imageOfCaptureBack = "/url",
            imageOfCaptureFront = "/url",
            description = "Fake pokemon"
        )
    }

    private fun generatePokemonNetwork(id: Int): PokemonNetwork {
        return PokemonNetwork(
            id = id,
            name = "Boulou${id}",
            base_experience = id * 10,
            height = id * 10,
            weight = id * 10,
            types = listOf(
                TypeNetwork(slot = 1, type = NamedResultNetwork(name = "feu", url = null)),
                TypeNetwork(slot = 2, type = NamedResultNetwork(name = "ombre", url = null)),
            ),
            sprites = Sprites(front_default = "/img.png", SpritesOther(SpritesOfficial("/img.png"))),
            stats = listOf(StatNetwork(10, StatTypeNetwork("hp"))),
            species = NamedResultNetwork("species", "species/1"),
        )
    }

    @Mock
    private lateinit var mockPokemonService: PokemonService

    @Mock
    private lateinit var mockPokemonDao: PokemonDao

    private val repository: PokemonRepositoryImpl
        get() = PokemonRepositoryImpl(
            pokemonService = mockPokemonService,
            pokemonDao = mockPokemonDao,
            emptyUrl = "empty/url"
        )

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `persistPokemons should send back a Result in Success`() = runBlocking {
        //GIVEN
        `when`(
            mockPokemonService.getAllPokemon(
                0,
                0
            )
        ).thenReturn(Response.success(fakeResultNetwork))
        `when`(mockPokemonDao.getCountOfPokemons()).thenReturn(0)
        `when`(
            mockPokemonService.getAllPokemon(
                fakeResultNetwork.count,
                0
            )
        ).thenReturn(Response.success(fakeResultNetwork))
        fakeResultNetwork.results.forEach {
            `when`(mockPokemonService.getPokemon(it.getIdFromUrl()!!)).thenReturn(Response.success(generatePokemonNetwork(it.getIdFromUrl()!!)))
        }
        `when`(mockPokemonDao.insert(any())).thenReturn(0)

        `when`(mockPokemonDao.hasPokemon(anyInt())).thenReturn(false)
        //WHEN
        val result = repository.persistPokemons()

        //THEN
        assertTrue(result.isSuccess)
    }

    @Test
    fun `getPokemons should send back a Flow with a list of Pokemon`() = runBlocking {
        //GIVEN
        `when`(mockPokemonDao.getAllPokemon()).thenReturn(fakeFlowOfListPokemon)

        //WHEN
        val result = repository.getPokemons()

        //THEN
        result.collect { listPokemon ->
            assertTrue(listPokemon.isNotEmpty())
        }
    }

    @Test
    fun `updatePokemon should send back 1 when DAO respond a PokemonEntity`() = runBlocking {
        //GIVEN
        val fakePokemon = generatePokemonEntity(1).asDomainModel()
        val fakePokemonEntity = generatePokemonEntity(1)

        `when`(mockPokemonDao.getPokemon(fakePokemon.number)).thenReturn(fakePokemonEntity)
        doNothing().`when`(mockPokemonDao).update(fakePokemonEntity)
        //WHEN
        val result = repository.updatePokemon(fakePokemon)
        //THEN
        assertTrue(result == 1)
    }

    @Test
    fun `updatePokemon should send back 0 when DAO can't respond a PokemonEntity`() = runBlocking {
        //GIVEN
        val fakePokemon = generatePokemonEntity(1).asDomainModel()
        val fakePokemonEntity = null

        `when`(mockPokemonDao.getPokemon(fakePokemon.number)).thenReturn(fakePokemonEntity)
        //WHEN
        val result = repository.updatePokemon(fakePokemon)
        //THEN
        assertTrue(result == 0)
    }
}