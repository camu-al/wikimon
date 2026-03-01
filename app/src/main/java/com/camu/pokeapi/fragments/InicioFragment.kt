package com.camu.pokeapi.fragments

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.camu.pokeapi.R
import com.camu.pokeapi.activities.MainActivity
import com.camu.pokeapi.databinding.FragmentInicioBinding
import com.camu.pokeapi.retrofit.RetroFitInstance
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch
import kotlin.Int


class InicioFragment : Fragment() {
    private var _binding: FragmentInicioBinding? = null
    private val binding get() = _binding!!

    // Rango de IDs de Pokémon disponibles
    private var pokemonIdMax: Int = 1023
    private var pokemonIdMin: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            // Recuperar argumentos pasados al fragmento (si hay)
            pokemonIdMin = it.getInt("pokemon_id_min", 1)
            pokemonIdMax = it.getInt("pokemon_id_max", 1023)
        }
    }

    companion object {
        fun newInstance(id: Int, idMax: Int, idMin: Int) = InicioFragment().apply {
            arguments = Bundle().apply {
                putInt("pokemon_id_max", idMax)
                putInt("pokemon_id_min", idMin)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInicioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Pokemon random al iniciar
        cargarPokemonRandom()
        botonRegiones()

        binding.btnRandomPoke.setOnClickListener {
            cargarPokemonRandom()
        }

        binding.cvCrearEquipo.setOnClickListener {
            (requireActivity() as MainActivity).showToast("Proximamente")

        }

        binding.cvFavoritos.setOnClickListener {
            (requireActivity() as MainActivity).showToast("Proximamente")

        }
    }
    private fun botonRegiones() {
        val regiones = listOf(
            "Kanto", "Johto", "Hoenn", "Sinnoh", "Unova",
            "Kalos", "Alola", "Galar/Hisui", "Paldea"
        )

        val chipGroup = binding.chipGroupRegiones
        chipGroup.removeAllViews()

        regiones.forEachIndexed { index, region ->
            val chip = Chip(requireContext())
            chip.text = region
            chip.isCheckable = true
            chip.setOnClickListener {
                abrirListaPorRegion(index + 1)
            }
            chipGroup.addView(chip)
        }
    }

    private fun abrirListaPorRegion(regionIndex: Int) {
        val fragment = PokemonListaFragment().apply {
            arguments = Bundle().apply {
                putInt("region_index", regionIndex)
                putInt("generacion_seleccionada", regionIndex)
            }
        }

        (requireActivity() as MainActivity).cargarFragment(fragment)
    }



    private fun cargarPokemonRandom() {
        // Genera un pokemon random entre los IDs
        val randomId = (pokemonIdMin..pokemonIdMax).random()

        lifecycleScope.launch {
            try {
                val pokemon = RetroFitInstance.api.getPokemon(randomId)

                //Id i nombre del pokemon
                binding.tvRandomPokeId.text = "#${pokemon.id}"
                val nombre = pokemon.species.name.replaceFirstChar { it.uppercase() }
                binding.tvRandomPokeNombre.text = nombre


                // Ajustar color del texto según el tipo principal para legibilidad
                val colorTexto = getTextColorForBackground(getTipoColor(pokemon.types[0].type.name))
                binding.tvRandomPokeId.setTextColor(colorTexto)
                binding.tvRandomPokeNombre.setTextColor(colorTexto)
                // Imagen del pokemon
                Glide.with(this@InicioFragment)
                    .load(pokemon.sprites.other.artwork.frontDefault)
                    .into(binding.imgRandomPoke)

                // Tipo del pokemon
                aplicarTipo(binding.tvTipo1, pokemon.types[0].type.name)

                if (pokemon.types.size > 1) {
                    aplicarTipo(binding.tvTipo2, pokemon.types[1].type.name)
                } else {
                    binding.tvTipo2.visibility = View.GONE
                }

                // Descripcion del pokemon
                val species = RetroFitInstance.api.getPokemonSpecies(randomId)

                // Obtener la descripción: inglés > mensaje por defecto
                val descripcion = species.flavor_text_entries
                    .firstOrNull { it.language.name == "en" } // ingles
                    ?.flavor_text
                    ?: "Descripción no disponible" // Mensaje por defecto


                // Limpiar saltos de línea y caracteres extraños
                val descripcionLimpia = descripcion.replace("\n", " ").replace("\u000c", " ")

                // Asignar al TextView
                binding.tvRandomPokeDescripcion.text = descripcionLimpia

                binding.tvRandomPokeDescripcion.setTextColor(
                    getTextColorForBackground(
                        getTipoColor(
                            pokemon.types[0].type.name
                        )
                    )
                )

                // Color del card segun el tipo + animacion
                cambiarColorCardGradiente(pokemon.types[0].type.name)

                animarCard()

            } catch (e: Exception) {
                Log.e("ERROR", "Error cargando Pokémon Random", e)
            }
        }
    }

    // Aplicar color y borde a un TextView de tipo
    fun aplicarTipo(tv: TextView, tipo: String) {
        val color = getTipoColor(tipo)

        val drawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.bg_tipo) as GradientDrawable
        drawable.setColor(color)

        val borderColor =
            if (getTextColorForBackground(color) == Color.WHITE) Color.WHITE else Color.BLACK
        drawable.setStroke(3, borderColor)

        tv.background = drawable
        tv.setTextColor(getTextColorForBackground(color))

        tv.text = tipo.replaceFirstChar { it.uppercase() }
        tv.visibility = View.VISIBLE
    }

    // Obtener color asociado a cada tipo Pokémon
    fun getTipoColor(tipo: String): Int {
        return when (tipo.lowercase()) {
            "normal" -> 0xFFA8A77A.toInt()
            "fire" -> 0xFFEE8130.toInt()
            "water" -> 0xFF6390F0.toInt()
            "grass" -> 0xFF7AC74C.toInt()
            "electric" -> 0xFFF7D02C.toInt()
            "ice" -> 0xFF96D9D6.toInt()
            "fighting" -> 0xFFC22E28.toInt()
            "poison" -> 0xFFA33EA1.toInt()
            "ground" -> 0xFFE2BF65.toInt()
            "flying" -> 0xFFA98FF3.toInt()
            "psychic" -> 0xFFF95587.toInt()
            "bug" -> 0xFFA6B91A.toInt()
            "rock" -> 0xFFB6A136.toInt()
            "ghost" -> 0xFF735797.toInt()
            "dragon" -> 0xFF6F35FC.toInt()
            "dark" -> 0xFF705746.toInt()
            "fairy" -> 0xFFD685AD.toInt()
            "steel" -> 0xFFB7B7CE.toInt()
            else -> 0xFFA8A77A.toInt()
        }
    }

    // Obtener color de texto según luminosidad de fondo
    fun getTextColorForBackground(backgroundColor: Int): Int {
        val r = Color.red(backgroundColor)
        val g = Color.green(backgroundColor)
        val b = Color.blue(backgroundColor)
        val luminance = 0.299 * r + 0.587 * g + 0.114 * b
        return if (luminance < 150) Color.WHITE else Color.BLACK
    }

    // Cambiar color del card segun el tipo
    fun cambiarColorCardGradiente(tipo: String) {

        val color = getTipoColor(tipo)

        val gradient = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(color, lightenColor(color))
        )

        gradient.cornerRadius = 60f

        binding.cvPokemonDelDia.background = gradient
    }

    // Aumentar brillo de un color
    fun lightenColor(color: Int): Int {
        val factor = 0.2f
        val r = (Color.red(color) * (1 - factor) / 255 + factor) * 255
        val g = (Color.green(color) * (1 - factor) / 255 + factor) * 255
        val b = (Color.blue(color) * (1 - factor) / 255 + factor) * 255
        return Color.rgb(r.toInt(), g.toInt(), b.toInt())
    }

    // Animacion del card
    fun animarCard() {
        binding.cvPokemonDelDia.alpha = 0f
        binding.cvPokemonDelDia.animate()
            .alpha(1f)
            .setDuration(400)
            .start()
    }

}