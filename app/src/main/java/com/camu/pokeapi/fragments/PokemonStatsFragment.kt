package com.camu.pokeapi.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.camu.pokeapi.databinding.FragmentPokemonStatsBinding
import com.camu.pokeapi.databinding.ItemStatBarBinding
import com.camu.pokeapi.retrofit.RetroFitInstance
import kotlinx.coroutines.launch

class PokemonStatsFragment : Fragment() {

    private var _binding: FragmentPokemonStatsBinding? = null
    private val binding get() = _binding!!

    private var pokemonId: Int = 0
    private var pendingPokemonId: Int? = null

    companion object {
        fun newInstance(id: Int): PokemonStatsFragment {
            val fragment = PokemonStatsFragment()
            fragment.arguments = Bundle().apply { putInt("pokemon_id", id) }
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { pokemonId = it.getInt("pokemon_id", 0) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPokemonStatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pendingPokemonId?.let {
            pokemonId = it
            pendingPokemonId = null
        }

        cargarStats()
    }

    private fun cargarStats() {
        lifecycleScope.launch {
            try {
                val pokemon = RetroFitInstance.api.getPokemon(pokemonId)

                pokemon.stats.forEach {
                    when (it.stat.name) {
                        "hp" -> setStat(binding.statHP, "HP", it.base_stat)
                        "attack" -> setStat(binding.statAttack, "Attack", it.base_stat)
                        "defense" -> setStat(binding.statDefense, "Defense", it.base_stat)
                        "special-attack" -> setStat(binding.statSpAttack, "Sp. Attack", it.base_stat)
                        "special-defense" -> setStat(binding.statSpDefense, "Sp. Defense", it.base_stat)
                        "speed" -> setStat(binding.statSpeed, "Speed", it.base_stat)
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun setStat(statBinding: ItemStatBarBinding, name: String, value: Int) {
        statBinding.tvStatName.text = name
        statBinding.tvStatValue.text = value.toString()
        statBinding.progressStat.max = 200
        statBinding.progressStat.progress = value

        val color = when {
            value < 50 -> 0xFFE57373.toInt()
            value < 90 -> 0xFFFFB74D.toInt()
            else -> 0xFF81C784.toInt()
        }
        statBinding.progressStat.progressTintList =
            android.content.res.ColorStateList.valueOf(color)
    }

    fun actualizarPokemon(id: Int) {
        if (_binding == null) {
            pendingPokemonId = id
        } else {
            pokemonId = id
            cargarStats()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
