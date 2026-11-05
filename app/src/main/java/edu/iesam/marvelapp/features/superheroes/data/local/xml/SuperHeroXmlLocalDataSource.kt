package edu.iesam.marvelapp.features.superheroes.data.local.xml

import android.content.Context
import edu.iesam.marvelapp.R
import edu.iesam.marvelapp.core.cache.AppCacheTTL
import edu.iesam.marvelapp.core.serialization.JsonSerializer
import edu.iesam.marvelapp.features.superheroes.domain.SuperHero
import kotlinx.serialization.serializer

/*
* Conector para gestionar datos en un fichero XML a través de SharedPreferences.
* Acoplada completamente a SharedPreferences.
*
* */

class SuperHeroXmlLocalDataSource (private val context: Context,
                                   private val appCache: AppCacheTTL,
                                   private val serializer: JsonSerializer<List<SuperHero>>) {

    val sharedPref = context.getSharedPreferences(
        context.getString(R.string.local_source_superhero), Context.MODE_PRIVATE)


    /*
    * Metodo para guardar un listado de superheroes
    * */
    fun save(superHeroes: List<SuperHero>) {

        //apply{ } Scope Function
        sharedPref.edit().apply() {
            putString(keySuperHero, serializer.toJson(superHeroes))
            apply()
        }
        appCache.updateTime(keySuperHero)
    }

    public fun findAll(): List<SuperHero> {

        if(!appCache.isExpired(keySuperHero, ttl)) {
            val jsonList = sharedPref.getString(keySuperHero, null)
            //let { } Scope Function, aquí significa que si NO es null, se ejecuta
            jsonList?.let {
                return serializer.fromJson(it)
            }
        }

        return emptyList()
    }

    fun findById(id: String): SuperHero? {

        return findAll().find { it.id  == id}
    }

    fun deleteById(id: String) {
        save(findAll().filterNot { it.id == id })
    }

    fun deleteAll() {
        sharedPref.edit().apply() {
            remove(keySuperHero)
            apply()
        }

    }

    companion object {
        private val keySuperHero: String = "superhero"

        private val ttl: Long = 5 * 60 * 100

    }
}