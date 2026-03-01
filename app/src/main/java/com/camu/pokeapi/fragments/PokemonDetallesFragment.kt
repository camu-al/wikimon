package com.camu.pokeapi.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.camu.pokeapi.adapter.PokemonPagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.camu.pokeapi.R
import com.camu.pokeapi.retrofit.RetroFitInstance
import com.camu.pokeapi.databinding.FragmentDetallePokeBinding
import kotlinx.coroutines.launch
import android.graphics.drawable.GradientDrawable
import com.google.android.material.tabs.TabLayoutMediator

class PokemonDetallesFragment : Fragment() {
    private var _binding: FragmentDetallePokeBinding? = null

    private val binding get() = _binding!!
    private lateinit var pagerAdapter: PokemonPagerAdapter
    private var idActual: Int = 1
    private var idMin : Int = 1
    private var idMax : Int = 1023

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetallePokeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Recoger id (Pasado desde fragmentList)
        idActual = arguments?.getInt("pokemon_id") ?: 1
        idMax = arguments?.getInt("pokemon_id_max") ?:1

        pagerAdapter = PokemonPagerAdapter(this, idActual)
        binding.viewPager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when(position) {
                0 -> "Stats"
                1 -> "Moves"
                else -> "Tab ${position+1}"
            }
        }.attach()

        cargarPokemon(idActual)

        binding.imgNext.setOnClickListener {
            if (idActual < idMax) {
                idActual++
                cargarPokemon(idActual)
                pagerAdapter.actualizarPokemon(idActual)
            }
        }

        binding.imgPrev.setOnClickListener {
            if (idActual > idMin) {
                idActual--
                cargarPokemon(idActual)
                pagerAdapter.actualizarPokemon(idActual)
            }
        }

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

    }

    //------------ FUN CARGAR POKEMON ------------
    private fun cargarPokemon(id: Int) {
        lifecycleScope.launch {
            try {
                val pokemon = RetroFitInstance.api.getPokemon(id)

                // Imagen , id,  nombre
                binding.tvPokemonNumber.text = "#${pokemon.id}"

                val nombre = pokemon.species.name.replaceFirstChar { it.uppercase() }
                binding.tvPokemonName.text = nombre

                Glide.with(this@PokemonDetallesFragment)
                    .load(pokemon.sprites.other.artwork.frontDefault)
                    .into(binding.imgPokemon)

                // Tipos dinámicos con color
                aplicarTipo(binding.tvTipo1, pokemon.types[0].type.name)

                if (pokemon.types.size > 1) {
                    aplicarTipo(binding.tvTipo2, pokemon.types[1].type.name)
                } else {
                    binding.tvTipo2.visibility = View.GONE
                }

                cambiarFondoGradiente(pokemon.types[0].type.name)

                actualizarBotones()
            } catch (e: Exception) {
                Log.e("ERROR", "Error cargando Pokémon", e)
            }
        }
    }

    private fun actualizarFragments() {

        val adapter = binding.viewPager.adapter as PokemonPagerAdapter

        val statsFragment =
            childFragmentManager.findFragmentByTag("f0") as? PokemonStatsFragment

        val movesFragment =
            childFragmentManager.findFragmentByTag("f1") as? PokemonMovesFragment

        statsFragment?.actualizarPokemon(idActual)
        movesFragment?.actualizarPokemon(idActual)
    }

    //------------ FUN ACTUALIZAR BOTONES ------------
    private fun actualizarBotones() {
        // Boton Anterior
        if (idActual > idMin) {
            binding.imgPrev.isEnabled = true
            binding.imgPrev.visibility = View.VISIBLE
        } else {
            binding.imgPrev.isEnabled = false
            binding.imgPrev.visibility = View.INVISIBLE
        }

        // Boton Siguiente
        if (idActual < idMax) {
            binding.imgNext.isEnabled = true
            binding.imgNext.visibility = View.VISIBLE
        } else {
            binding.imgNext.isEnabled = false
            binding.imgNext.visibility = View.INVISIBLE
        }
    }

    // Colorear los TextViews de tipos
    fun aplicarTipo(tv: TextView, tipo: String) {
        val color = getTipoColor(tipo)
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.bg_tipo) as GradientDrawable
        drawable.setColor(color)

        val borderColor = if (getTextColorForBackground(color) == Color.WHITE) Color.WHITE else Color.BLACK
        drawable.setStroke(3, borderColor)

        tv.background = drawable
        tv.setTextColor(getTextColorForBackground(color))
        tv.text = tipo.replaceFirstChar { it.uppercase() }
        tv.visibility = View.VISIBLE
    }

    fun cambiarFondoGradiente(tipo: String) {
        val color = getTipoColor(tipo)
        val gradient = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(color, lightenColor(color))
        )
        gradient.cornerRadius = 0f
        binding.detallePoke.background = gradient
    }

    fun lightenColor(color: Int): Int {
        val factor = 0.2f
        val r = (Color.red(color) * (1 - factor) / 255 + factor) * 255
        val g = (Color.green(color) * (1 - factor) / 255 + factor) * 255
        val b = (Color.blue(color) * (1 - factor) / 255 + factor) * 255
        return Color.rgb(r.toInt(), g.toInt(), b.toInt())
    }

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

    fun getTextColorForBackground(backgroundColor: Int): Int {
        val r = Color.red(backgroundColor)
        val g = Color.green(backgroundColor)
        val b = Color.blue(backgroundColor)
        val luminance = 0.299 * r + 0.587 * g + 0.114 * b
        return if (luminance < 150) Color.WHITE else Color.BLACK
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }



}