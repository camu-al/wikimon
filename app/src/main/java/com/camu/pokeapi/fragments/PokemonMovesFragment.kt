package com.camu.pokeapi.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.camu.pokeapi.databinding.FragmentPokemonMovesBinding
import com.camu.pokeapi.retrofit.RetroFitInstance
import kotlinx.coroutines.launch

class PokemonMovesFragment : Fragment() {

    private var _binding: FragmentPokemonMovesBinding? = null
    private val binding get() = _binding!!

    private var pokemonId: Int = 0
    private var pendingPokemonId: Int? = null

    companion object {
        fun newInstance(id: Int): PokemonMovesFragment {
            val fragment = PokemonMovesFragment()
            fragment.arguments = Bundle().apply { putInt("pokemon_id", id) }
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { pokemonId = it.getInt("pokemon_id", 0) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPokemonMovesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pendingPokemonId?.let {
            pokemonId = it
            pendingPokemonId = null
        }

        cargarMoves()
    }

    private fun cargarMoves() {
        lifecycleScope.launch {
            try {
                val pokemon = RetroFitInstance.api.getPokemon(pokemonId)
                // Aquí puedes llenar tu RecyclerView/List de moves
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun actualizarPokemon(id: Int) {
        if (_binding == null) {
            pendingPokemonId = id
        } else {
            pokemonId = id
            cargarMoves()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
