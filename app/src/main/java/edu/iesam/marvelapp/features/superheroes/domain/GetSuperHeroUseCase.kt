package edu.iesam.marvelapp.features.superheroes.domain

class GetSuperHeroUseCase (private val superHeroRepository: SuperHeroRepository){

    operator fun invoke(id: String) {
        return superHeroRepository.getById(id)
    }
}