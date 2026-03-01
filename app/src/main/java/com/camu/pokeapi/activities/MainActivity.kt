package com.camu.pokeapi.activities

import MusicaPrincipal
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.camu.pokeapi.R
import com.camu.pokeapi.databinding.ActivityMainBinding
import com.camu.pokeapi.fragments.AjustesFragment
import com.camu.pokeapi.fragments.PokemonListaFragment
import com.camu.pokeapi.fragments.InicioFragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener  {
    private lateinit var binding: ActivityMainBinding
    private lateinit var musicaPrincipal: MusicaPrincipal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // MUSICA
        musicaPrincipal = MusicaPrincipal(this)

        //------------ CARGAR FRAGMENT INICIAL ------------
        if (savedInstanceState == null) {
            cargarFragment(InicioFragment(), addToBackStack = false)
        }

        binding.bottonNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_Home -> {
                    cargarFragment(InicioFragment())
                    true
                }
                R.id.bottom_Busqueda -> {
                    cargarFragment(PokemonListaFragment())
                    true
                }
                else -> false
            }
        }

        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbarInclude.miToolBar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        // Aplicar menu drawer
        binding.navView.setNavigationItemSelectedListener(this)

        /*-------------------- MODO OSCURO --------------------*/
        val modoOscuro: Boolean = PreferenceManager
            .getDefaultSharedPreferences(this)
            .getBoolean("oscuro", false)

        if (modoOscuro){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_stats -> showToast("Proximamente")
            R.id.nav_ajustes -> {
                cargarFragment(AjustesFragment())
                val preferences: SharedPreferences= PreferenceManager.getDefaultSharedPreferences(this)
            }
            R.id.nav_salir -> showToast("Salir de wikimon")
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    //------------ FUN MOSTRAR TOAST ------------
    fun cargarFragment(
        fragment: Fragment,
        addToBackStack: Boolean = true
    ) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.contenedorFragments, fragment)
            if (addToBackStack) addToBackStack(null)
            commit()
        }
    }
    //------------ FUN MOSTRAR TOAST ------------
    fun showToast(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    /*-------------------- MUSICA --------------------*/
    fun iniciarMusica() {
        musicaPrincipal.reproducirSnd(R.raw.pokemon_music)
    }

    fun pararMusica() {
        musicaPrincipal.detenerMusica()
    }

    override fun onStart() {
        super.onStart()

        val musicaActivada = androidx.preference.PreferenceManager
            .getDefaultSharedPreferences(this)
            .getBoolean("musica", true)

        if (musicaActivada) {
            musicaPrincipal.reproducirSnd(R.raw.pokemon_music)
        }
    }

    override fun onStop() {
        musicaPrincipal.detenerMusica()
        super.onStop()
    }
}