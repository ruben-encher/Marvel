package edu.iesam.marvelapp.features.superheroes

class GetSuperHeroesUseCase(private val superHeroRepository: SuperHeroRepository) {

    operator fun invoke() : List<SuperHero> {
        return superHeroRepository.getAll()
    }
}