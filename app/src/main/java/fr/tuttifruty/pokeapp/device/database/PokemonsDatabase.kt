package fr.tuttifruty.pokeapp.device.database

import androidx.room.Database
import androidx.room.RoomDatabase
import fr.tuttifruty.pokeapp.device.database.dao.PokemonDao
import fr.tuttifruty.pokeapp.device.database.entity.PokemonEntity
import fr.tuttifruty.pokeapp.BuildConfig

@Database(entities = [PokemonEntity::class], version = BuildConfig.VERSION_DB, exportSchema = false)
abstract class PokemonsDatabase : RoomDatabase() {
    abstract val pokemonDao: PokemonDao
}