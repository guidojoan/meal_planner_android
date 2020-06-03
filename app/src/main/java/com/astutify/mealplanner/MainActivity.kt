package com.astutify.mealplanner

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.astutify.mealplanner.ingredient.presentation.list.IngredientsFragment
import com.astutify.mealplanner.recipe.presentation.list.RecipesFragment
import com.astutify.mealplanner.userprofile.presentation.profile.UserProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    lateinit var menu: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)

        menu = findViewById(R.id.navigationView)
        menu.setOnNavigationItemSelectedListener(this)

        setFragment(RecipesFragment())
    }

    private fun setFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content, fragment)
        transaction.commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.tab_recipes -> {
                setFragment(RecipesFragment())
                true
            }
            R.id.tab_ingredients -> {
                setFragment(IngredientsFragment())
                true
            }
            R.id.tab_user_profile -> {
                setFragment(UserProfileFragment())
                true
            }
            else -> false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragment = supportFragmentManager.findFragmentById(R.id.content)
        fragment?.onActivityResult(requestCode, resultCode, data)
    }
}
