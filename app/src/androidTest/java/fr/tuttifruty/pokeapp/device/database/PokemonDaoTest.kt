package fr.tuttifruty.pokeapp.device.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import fr.tuttifruty.pokeapp.device.database.dao.PokemonDao
import fr.tuttifruty.pokeapp.device.database.entity.PokemonEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class PokemonDaoTest {
    private lateinit var database: PokemonsDatabase
    private lateinit var pokemonDao: PokemonDao
    private var testDispatcher = StandardTestDispatcher()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private fun generatePokemonEntity(
        id: Int,
        name: String = "Boulou${id}",
        isCaptured: Boolean = false,
    ): PokemonEntity {
        return PokemonEntity(
            id = id,
            name = name,
            baseExperience = id * 10,
            height = id * 10,
            weight = id * 10,
            types = "feu",
            image = "",
            isCaptured = isCaptured,
            hp = 10,
            attack = 20,
            defense = 30,
            spAttack = 40,
            spDefense = 50,
            speed = 60,
            totalStat = 340,
            species = 6,
        )
    }

    @Before
    fun createDb() {
        Dispatchers.setMain(testDispatcher)

        runBlocking {
            val context = InstrumentationRegistry.getInstrumentation().targetContext
            database = Room.inMemoryDatabaseBuilder(context, PokemonsDatabase::class.java).build()
            pokemonDao = database.pokemonDao
        }
    }

    @After
    fun closeDb() {
        database.close()

        Dispatchers.resetMain()
    }

    @Test
    fun insertAndGetPokemonTest() = runBlocking {
        //GIVEN
        val pokemonToInsert = generatePokemonEntity(id = 1)
        val id = pokemonDao.insert(pokemonToInsert)

        //WHEN
        val result = pokemonDao.getPokemon(id.toInt())

        //THEN
        assertEquals(pokemonToInsert, result)
    }

    @Test
    fun updatePokemonTest() = runBlocking {
        //GIVEN
        val pokemonToInsert = generatePokemonEntity(id = 1)
        val id = pokemonDao.insert(pokemonToInsert).toInt()
        val pokemonToUpdate = pokemonDao.getPokemon(id)
        val pokemonUpdated = pokemonToUpdate?.copy(
            isCaptured = !pokemonToUpdate.isCaptured
        )
        //WHEN
        pokemonDao.update(pokemonUpdated!!)
        val result = pokemonDao.getPokemon(id)
        //THEN
        assertEquals(pokemonUpdated, result)
        assertNotEquals(pokemonToInsert.isCaptured, result?.isCaptured)
    }

    @Test
    fun getAllPokemonTest() = runBlocking {
        //GIVEN
        for (x in 1..5) {
            pokemonDao.insert(generatePokemonEntity(id = x))
        }

        //WHEN
        val result = pokemonDao.getAllPokemon()

        //THEN
        val listPokemon = result.take(1).toList()[0]
        assertTrue(listPokemon.size == 5)
    }

    @Test
    fun getCountOfPokemonsTest() = runBlocking {
        //GIVEN
        for (x in 1..5) {
            pokemonDao.insert(generatePokemonEntity(id = x))
        }

        //WHEN
        val result = pokemonDao.getCountOfPokemons()

        //THEN
        assertEquals(5, result)
    }

    @Test
    fun getPokemonsByQueryTest() = runBlocking {
        //GIVEN
        val pokemonToFind = generatePokemonEntity(id = 6, name = "MyPokemon")
        pokemonDao.insert(pokemonToFind)

        for (x in 1..5) {
            pokemonDao.insert(generatePokemonEntity(x))
        }

        //WHEN
        val result = pokemonDao.getPokemonsByQuery("MyPokemon")

        //THEN
        val listPokemonFound = result.take(1).toList()[0]
        assertTrue(listPokemonFound.size == 1)
        assertEquals(pokemonToFind, listPokemonFound[0])
    }

    @Test
    fun getPokemonCapturedTest() = runBlocking {
        //GIVEN
        val pokemonToFind = generatePokemonEntity(id = 6, name = "MyPokemon", isCaptured = true)
        pokemonDao.insert(pokemonToFind)

        for (x in 1..5) {
            pokemonDao.insert(generatePokemonEntity(x))
        }

        //WHEN
        val result = pokemonDao.getPokemonCaptured()

        //THEN
        val listPokemonFound = result.take(1).toList()[0]
        assertTrue(listPokemonFound.size == 1)
        assertEquals(pokemonToFind, listPokemonFound[0])
    }

    @Test
    fun hasPokemonFoundTest() = runBlocking {
        //GIVEN
        val pokemonToFind = generatePokemonEntity(id = 6, name = "MyPokemon", isCaptured = true)
        pokemonDao.insert(pokemonToFind)

        for (x in 1..5) {
            pokemonDao.insert(generatePokemonEntity(x))
        }

        //WHEN
        val result = pokemonDao.hasPokemon(6)

        //THEN
        assertTrue(result)
    }

    @Test
    fun hasPokemonNotFoundTest() = runBlocking {
        //GIVEN
        for (x in 1..5) {
            pokemonDao.insert(generatePokemonEntity(x))
        }

        //WHEN
        val result = pokemonDao.hasPokemon(6)

        //THEN
        assertFalse(result)
    }
}