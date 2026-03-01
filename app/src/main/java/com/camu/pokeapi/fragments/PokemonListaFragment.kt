package com.camu.pokeapi.fragments

import PokemonListResponse
import android.R
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.camu.pokeapi.activities.MainActivity
import com.camu.pokeapi.adapter.PokemonAdapter
import com.camu.pokeapi.databinding.FragmentListPokeBinding
import com.camu.pokeapi.pojo.PokemonList
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import com.camu.pokeapi.retrofit.RetroFitInstance
import com.camu.pokeapi.adapter.OnClickListener
import kotlinx.coroutines.launch

class PokemonListaFragment : Fragment(), OnClickListener {

    private var _binding: FragmentListPokeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PokemonAdapter
    private var textoBusqueda: String = ""
    private var generacionSeleccionada: Int = 0 //Todas las generaciones
    private var listaAllPokemon: List<PokemonList> = listOf()

    // Rangos por generacion
    val rangoGeneraciones = listOf(
        0..1022,    // Todas 1023 Pokémon
        0..150,     // Gen 1
        151..250,   // Gen 2
        251..385,   // Gen 3
        386..492,   // Gen 4
        493..648,   // Gen 5
        649..720,   // Gen 6
        721..806,   // Gen 7
        807..902,   // Gen 8
        903..1022   // Gen 9
    )

    private var pokemonId: Int = 0
    private var pokemonIdMax: Int = 0
    private var pokemonIdMin: Int = 0
    private var regionIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            pokemonId = it.getInt("pokemon_id", 0)
            pokemonIdMax = it.getInt("pokemon_id_max", 0)
            pokemonIdMin = it.getInt("pokemon_id_min", 0)
            regionIndex = it.getInt("region_index", 0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListPokeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurar RecyclerView
        adapter = PokemonAdapter(listOf(), this)
        binding.recyclerPoke.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerPoke.adapter = adapter

        binding.buscarPoke.queryHint = "Buscar Pokémon por nombre o ID"

        val searchEditText = binding.buscarPoke.findViewById<EditText>(
            androidx.appcompat.R.id.search_src_text
        )

        val isNightMode = (resources.configuration.uiMode and
                android.content.res.Configuration.UI_MODE_NIGHT_MASK) ==
                android.content.res.Configuration.UI_MODE_NIGHT_YES

        searchEditText.setHintTextColor(if (isNightMode) Color.LTGRAY else Color.DKGRAY)
        searchEditText.setTextColor(if (isNightMode) Color.WHITE else Color.BLACK)
        searchEditText.textSize = 16f

        // Icono de lupa
        val searchIcon = binding.buscarPoke.findViewById<ImageView>(
            androidx.appcompat.R.id.search_mag_icon
        )
        searchIcon.setColorFilter(if (isNightMode) Color.LTGRAY else Color.DKGRAY)

        // Listener de búsqueda
        binding.buscarPoke.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?): Boolean {
                textoBusqueda = newText.orEmpty()
                aplicarFiltros()
                return true
            }
        })

        //------------ Spinner Config ------------
        val generaciones = listOf(
            "Todas las gens",
            "Gen 1 (Kanto)", "Gen 2 (Johto)", "Gen 3 (Hoenn)",
            "Gen 4 (Sinnoh)", "Gen 5 (Unova)", "Gen 6 (Kalos)",
            "Gen 7 (Alola)", "Gen 8 (Galar/Hisui)", "Gen 9 (Paldea)"
        )

        // Configurar el Spinner
        val spinnerAdapter =
            ArrayAdapter(requireContext(), R.layout.simple_spinner_item, generaciones)
        spinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.spinnerGen.adapter = spinnerAdapter

        // Seleccionar la región correspondiente si se pasa el index desde inicio
        if (regionIndex in 1..9) {
            binding.spinnerGen.setSelection(regionIndex)
            generacionSeleccionada = regionIndex
        }

        // Manejar la selección del Spinner
        binding.spinnerGen.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                generacionSeleccionada = position
                regionIndex = if (position in 1..9) position else 0

                cargarPokemons()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    //------------ FUN CARGAR POKEMON ------------
    private fun cargarPokemons() {
        binding.progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                // Determinar rango según regionIndex o generación seleccionada
                val rango = if (regionIndex in 1..9) {
                    when(regionIndex) {
                        1 -> 0..150      // Kanto
                        2 -> 151..250    // Johto
                        3 -> 251..385    // Hoenn
                        4 -> 386..492    // Sinnoh
                        5 -> 493..648    // Unova
                        6 -> 649..720    // Kalos
                        7 -> 721..806    // Alola
                        8 -> 807..902    // Galar/Hisui
                        9 -> 903..1022   // Paldea
                        else -> 0..1022
                    }
                } else {
                    rangoGeneraciones[generacionSeleccionada]
                }

                val offset = rango.first
                val limit = rango.last - rango.first + 1

                val pokemon = RetroFitInstance.api.getPokemonList(limit, offset)

                listaAllPokemon = pokemon.results.map { r: PokemonListResponse ->
                    val id = r.url.trimEnd('/').split("/").last().toInt()
                    val nombre = r.name.replaceFirstChar { it.uppercase() }
                    PokemonList(
                        id = id,
                        name = nombre,
                        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
                    )
                }
                aplicarFiltros()
            } catch (e: Exception) {
                Log.e("POKE", "Error cargando Pokémon", e)
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    //------------ FUN FILTRAR ------------
    private fun aplicarFiltros() {
        val texto = textoBusqueda.lowercase()
        val rango = if (regionIndex in 1..9) {
            when(regionIndex) {
                1 -> 0..150
                2 -> 151..250
                3 -> 251..385
                4 -> 386..492
                5 -> 493..648
                6 -> 649..720
                7 -> 721..806
                8 -> 807..902
                9 -> 903..1022
                else -> 0..1022
            }
        } else {
            rangoGeneraciones[generacionSeleccionada]
        }

        // Filtrar Pokémon por texto y rango
        val filtrada = listaAllPokemon.filter {
            (it.name.lowercase().contains(texto) || it.id.toString() == texto) && it.id in rango
        }
        adapter.actualizarLista(filtrada)

    }

    //------------ FUN IR FRAGMENT DETALLE ------------
    private fun abrirDetalle(id: Int) {
        val fragment = PokemonDetallesFragment().apply {
            arguments = Bundle().apply {
                putInt("pokemon_id", id)
                putInt("pokemon_id_max", listaAllPokemon.maxOfOrNull { it.id } ?: id)
                putInt("pokemon_id_min", listaAllPokemon.minOfOrNull { it.id } ?: id)
            }
        }
        (requireActivity() as MainActivity).cargarFragment(fragment)
    }

    override fun onClickPokemon(pokemon: PokemonList) {
        Log.d("POKE", "Pokemon ${pokemon.name} clicado")
        abrirDetalle(pokemon.id)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}