package com.example.nournet.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.core.view.WindowCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenCreated
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.nournet.R
import com.example.nournet.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.AndroidEntryPoint
import com.example.nournet.repository.NourNetRepositoryImpl
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var appBarConfiguration : AppBarConfiguration
    private lateinit var auth : FirebaseAuth
    private lateinit var firebaseDatabase : FirebaseDatabase

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setSupportActionBar(binding.toolbar)

        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        val drawerLayout : DrawerLayout = binding.drawerLayout
        val navigationView : NavigationView = binding.navigationView
        navigationView.setupWithNavController(navController)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.adminHomeFragment,
                R.id.donorsHomeFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id in listOf(R.id.splashFragment, R.id.loginFragment)) {
                supportActionBar?.hide()
            }
            if (destination.id in listOf(
                    R.id.donateFragment,
                    R.id.receiveFragment,
                    R.id.donationsFragment,
                    R.id.foodMapFragment,
                    R.id.historyFragment,
                    R.id.aboutUsFragment,
                )
            ) {
                supportActionBar?.show()
                supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
            }
        }

        val header = binding.navigationView.getHeaderView(0)
        val imageView = header.findViewById < ImageView > (R.id.imageView)
        val userImage = auth.currentUser?.photoUrl
        lifecycleScope.launch {
            whenCreated {
                NourNetRepositoryImpl.getInstance().getCurrentUserEmail { current ->
                    val userEmailText = header.findViewById < android.widget.TextView > (R.id.userEmail)
                    userEmailText.text = current
                }
            }
        }

        Glide
            .with(this)
            .load(userImage)
            .apply(RequestOptions().override(150, 150))
            .placeholder(R.drawable.ic_person)
            .into(imageView)

        binding.navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_help -> {
                    startActivity(Intent(this, HelpActivity::class.java))
                    true
                }
                R.id.action_home -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_share -> {
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.type = "text/plain"
                    intent.putExtra(Intent.EXTRA_SUBJECT, "NourNet")
                    startActivity(Intent.createChooser(intent, "Share via"))
                    true
                }
                R.id.action_feedback -> {
                    val intent = Intent(Intent.ACTION_SENDTO)
                    intent.data =
                        Uri.parse("to : " + "leencelidoros@gmail.com")
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback")
                    if (intent.resolveActivity(this.packageManager) != null) {
                        startActivity(intent)
                    }
                    true
                }
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setSupportActionBar(binding.toolbar)
        binding.drawerLayout.closeDrawer(GravityCompat.START)
    }

    override fun onSupportNavigateUp() : Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}