
data class PokemonListResponse(
    val name: String,
    val url: String
)

data class PokemonListResult(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonListResponse>
)
