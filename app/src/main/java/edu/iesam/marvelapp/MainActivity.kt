package edu.iesam.marvelapp

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import edu.iesam.marvelapp.core.cache.AppCacheTTL
import edu.iesam.marvelapp.core.serialization.KotlinJsonSerializer
import edu.iesam.marvelapp.core.time.LocalAppTime
import edu.iesam.marvelapp.features.superheroes.data.SuperHeroDataRepository
import edu.iesam.marvelapp.features.superheroes.data.local.xml.SuperHeroXmlLocalDataSource
import edu.iesam.marvelapp.features.superheroes.domain.GetSuperHeroesUseCase
import edu.iesam.marvelapp.features.superheroes.domain.SuperHero
import edu.iesam.marvelapp.features.superheroes.presentation.SuperHeroListViewModel

class MainActivity : AppCompatActivity() {

    val useCase = GetSuperHeroesUseCase(
        SuperHeroDataRepository(
        SuperHeroXmlLocalDataSource(
            this,
            AppCacheTTL(this, LocalAppTime()),
            KotlinJsonSerializer.create())
        )
    )
    private val viewModel: SuperHeroListViewModel by viewModels {
        SuperHeroListViewModel.factory(useCase)
    }

    /**
     * Ciclo de vida: Paso 1 OnCreate
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        Log.d("MainActivity", "OnCreate")

        initObserver()
        viewModel.loadSuperHeroes()

    }

    private fun initObserver() {
        viewModel.uiState.observe(this) { uiState ->
            Log.d("MainActivity", uiState.isLoading.toString())
            bind(uiState)
        }
    }


    // Se recoge la información del ViewModel.

    private fun bind(uiState: SuperHeroListViewModel.UiState) {
        if (uiState.isLoading) {
            //Show loading
        } else {
            //hide loading
        }

        if (uiState.hasError) {
            //Show error view
        } else {
            //Hide error view
        }

        uiState.superHeroes?.let { superHeroes ->
                if (superHeroes.isEmpty()) {
                    //muestro pantalla de listado vacio
                } else {
                    //muestro el listado de superheroes
                    renderUi(superHeroes.first())
                }
            }
    }
    private fun renderUi(superHero: SuperHero) {

        val superheroLayout = findViewById<ConstraintLayout>(R.id.superhero_layout)

        val labelNameSuperHero =
            superheroLayout.
            findViewById<TextView>(R.id.superhero_name)
        labelNameSuperHero.text = superHero.name


        val labelSlugSuperHero =
            superheroLayout.
            findViewById<TextView>(R.id.superhero_slug)
        labelSlugSuperHero.text = superHero.slug
    }
}