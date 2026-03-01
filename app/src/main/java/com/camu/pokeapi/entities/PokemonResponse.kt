package com.camu.pokeapi.entities

import com.google.gson.annotations.SerializedName

data class PokemonResponse(
    val id: Int,
    val species: Species,
    val sprites: Sprites,
    val stats: List<Stats>,
    val types: List<TypeSlot>
)

data class Species(
    val name: String
)

data class Sprites(
    val other: Other
)

data class Other(
    @SerializedName("official-artwork")
    val artwork : Artwork
)

data class Artwork(
    @SerializedName("front_default")
    val frontDefault: String
)
data class Stats(
    val base_stat: Int,
    val stat: Stat
)

data class Stat(
    val name: String
)

data class TypeSlot(
    val slot: Int,
    val type: Type
)

data class Type(
    val name: String
)