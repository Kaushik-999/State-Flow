package com.kaushik.stateflow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.kaushik.stateflow.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collect

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener{
            val userName = binding.ipName.text.toString().trim()
            val password = binding.inPW.text.toString()

            Log.d("credentials-get", "$userName $password")

            if(userName!="" && password!="")
            viewModel.loginUiState(
                userName,
                password
            )
        }

        // Collect/Observe the stateflow when the app is in started state ( when the app is showing on the screen)
        lifecycleScope.launchWhenStarted {
            viewModel.loginUiState.collect {

                when(it) {
                    is MainViewModel.LoginUiState.Success-> {
                        Toast.makeText(this@MainActivity,"Logged In", Toast.LENGTH_LONG).show()
                        binding.progressBar.visibility = View.GONE
                        binding.inPW.text = null
                    }
                    is MainViewModel.LoginUiState.Error-> {
                        Toast.makeText(this@MainActivity,"Wrong Credentials", Toast.LENGTH_LONG).show()
                        binding.progressBar.visibility = View.GONE
                        binding.inPW.text = null
                    }
                    is MainViewModel.LoginUiState.Loading-> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    else -> Unit
                }

            }
        }

    }
}