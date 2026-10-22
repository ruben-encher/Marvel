package edu.iesam.marvelapp.features.superheroes

interface SuperHeroRepository {
    fun getAll(): List<SuperHero>
}