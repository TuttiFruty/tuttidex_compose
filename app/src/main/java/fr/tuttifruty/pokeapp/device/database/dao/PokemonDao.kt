package fr.tuttifruty.pokeapp.device.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import fr.tuttifruty.pokeapp.device.database.entity.PokemonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {

    @Insert(onConflict = REPLACE)
    fun insert(pokemon: PokemonEntity):Long

    @Update(onConflict = REPLACE)
    fun update(pokemon: PokemonEntity)

    @Query("SELECT * FROM pokemon_table ORDER BY id ASC")
    fun getAllPokemon(): Flow<List<PokemonEntity>>

    @Query("SELECT COUNT(*) FROM pokemon_table")
    suspend fun getCountOfPokemons(): Int

    @Query("SELECT * FROM pokemon_table WHERE pokemon_name LIKE :query " +
            "OR pokemon_types LIKE :query " +
            "OR pokemon_weight LIKE :query " +
            "OR pokemon_height LIKE :query " +
            "OR id LIKE :query " +
            "ORDER BY id ASC")
    fun getPokemonsByQuery(query: String?): Flow<List<PokemonEntity>>

    @Query("SELECT * FROM pokemon_table WHERE pokemon_is_captured = 1 ORDER BY id ASC")
    fun getPokemonCaptured():Flow<List<PokemonEntity>>

    @Query("SELECT * FROM pokemon_table WHERE id = :id")
    fun getPokemon(id: Int): PokemonEntity?

    @Query("SELECT EXISTS (SELECT * FROM pokemon_table WHERE id = :id)")
    fun hasPokemon(id: Int):Boolean
}