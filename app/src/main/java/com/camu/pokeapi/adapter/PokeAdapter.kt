package com.camu.pokeapi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.camu.pokeapi.pojo.PokemonList
import com.camu.pokeapi.R

class PokemonAdapter(
    private var lista: List<PokemonList>,
    private val listener: OnClickListener
) : RecyclerView.Adapter<PokemonAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.imgPoke)
        val name: TextView = view.findViewById(R.id.tvPokeName)
        val id: TextView = view.findViewById(R.id.tvPokeId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_poke, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pokemon = lista[position]

        //Mostra imagen, id i nombre
        holder.name.text = pokemon.name.replaceFirstChar { it.uppercase() }
        holder.id.text = "#${pokemon.id}"

        Glide.with(holder.itemView)
            .load(pokemon.imageUrl)
            .into(holder.img)

        holder.itemView.setOnClickListener {
            listener.onClickPokemon(pokemon)
        }
    }

    override fun getItemCount() = lista.size

    fun actualizarLista(nuevaLista: List<PokemonList>) {
        lista = nuevaLista
        notifyDataSetChanged()
    }
}
