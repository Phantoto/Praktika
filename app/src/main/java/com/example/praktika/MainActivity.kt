package com.example.praktika

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.sax.RootElement
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.example.praktika.act.EditAdsAct
import com.example.praktika.databinding.ActivityMainBinding
import com.example.praktika.dialoghelper.DialogConst
import com.example.praktika.dialoghelper.DialogHelper
import com.example.praktika.dialoghelper.GoogleAccConst
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var rootElement: ActivityMainBinding
    private val dialogHelper = DialogHelper(this)
    val mAuth = FirebaseAuth.getInstance()
    private lateinit var tvAccount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootElement = ActivityMainBinding.inflate(layoutInflater)
        val view = rootElement.root
        setContentView(view)
        init()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == GoogleAccConst.GOOGLE_SIGN_IN_REQUEST_CODE){
            //Log.d("MyLog", "Sign in result")
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if(account != null){
                    dialogHelper.accHelper.signInFirebaseWithGoogle(account.idToken!!)
                }
            }
            catch (e:ApiException)
            {
                Log.d("MyLog", "Api error :${e.message}")
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    override fun onStart() {
        super.onStart()
        uiUpdate(mAuth.currentUser)

    }
    private fun init()
    {
        setSupportActionBar(rootElement.mainContent.toolbar)
        var toggle = ActionBarDrawerToggle(this, rootElement.drawerLayout,rootElement.mainContent.toolbar, R.string.open,R.string.close)
        tvAccount = rootElement.navView.getHeaderView(0).findViewById(R.id.tvAccountEmail)
        rootElement.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        rootElement.navView.setNavigationItemSelectedListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.id_new_ads){
            val  i = Intent(this, EditAdsAct::class.java)
            startActivity(i)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.id_my_ads ->{
                Toast.makeText(this,"Pressed id_my_ads", Toast.LENGTH_LONG).show()
            }
            R.id.id_car ->{
                Toast.makeText(this,"Pressed id_car", Toast.LENGTH_LONG).show()
            }
            R.id.id_pc ->{
                Toast.makeText(this,"Pressed id_pc", Toast.LENGTH_LONG).show()
            }
            R.id.id_smartphone ->{
                Toast.makeText(this,"Pressed id_smartphone", Toast.LENGTH_LONG).show()
            }
            R.id.id_dm ->{
                Toast.makeText(this,"Pressed id_dm", Toast.LENGTH_LONG).show()
            }
            R.id.id_sign_up ->{
                dialogHelper.createSignDialog(DialogConst.SIGN_UP_STATE)
                //Toast.makeText(this,"Pressed id_sign_up", Toast.LENGTH_LONG).show()
            }
            R.id.id_sign_in ->{
                //Toast.makeText(this,"Pressed id_sign_in", Toast.LENGTH_LONG).show()
                dialogHelper.createSignDialog(DialogConst.SIGN_IN_STATE)
            }
            R.id.id_sign_out ->{
                uiUpdate(null)
                mAuth.signOut()
                dialogHelper.accHelper.signOutGoogle()
            }
        }
        rootElement.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    fun uiUpdate(user:FirebaseUser?){
        tvAccount.text = if (user == null){
            resources.getString(R.string.not_reg)
        }
        else
        {
            user.email
        }
    }
}