package com.kotlinegitim.myapplicationmt3.ui.login

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.kotlinegitim.myapplicationmt3.ui.activity.MainActivity
import com.kotlinegitim.myapplicationmt3.R
import com.kotlinegitim.myapplicationmt3.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var _binding: FragmentLoginBinding
    private lateinit var loginViewModel: LoginFragment2ViewModel

    private val binding get() = _binding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        loginViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[LoginFragment2ViewModel::class.java]

        auth = FirebaseAuth.getInstance()
        val currentuser = auth.currentUser;
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

       if(currentuser != null) {
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.textViewCreateAcc.setOnClickListener {
            findNavController().navigate(R.id.loginToRegister)
        }
        binding.btnLogin.setOnClickListener {
            val email = binding.editTextMail.text.toString()
            val password = binding.editTextPassword.text.toString()
            loginViewModel.login(email, password)
        }
        loginViewModel._isLogin.observe(viewLifecycleOwner) {
            if (it) {
                val intent = Intent(requireActivity(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding
    }
}