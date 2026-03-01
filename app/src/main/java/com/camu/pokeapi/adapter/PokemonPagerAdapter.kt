package com.camu.pokeapi.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.camu.pokeapi.fragments.PokemonMovesFragment
import com.camu.pokeapi.fragments.PokemonStatsFragment

class PokemonPagerAdapter(
    fragment: Fragment,
    pokemonId: Int
) : FragmentStateAdapter(fragment) {

    private val statsFragment = PokemonStatsFragment.newInstance(pokemonId)
    private val movesFragment = PokemonMovesFragment.newInstance(pokemonId)

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment = when(position) {
        0 -> statsFragment
        1 -> movesFragment
        else -> Fragment()
    }

    fun actualizarPokemon(id: Int) {
        statsFragment.actualizarPokemon(id)
        movesFragment.actualizarPokemon(id)
    }
}
