package com.kotlinegitim.myapplicationmt3.ui.register


import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.kotlinegitim.myapplicationmt3.R
import com.kotlinegitim.myapplicationmt3.databinding.FragmentRegisterBinding


class RegisterFragment : Fragment() {

    private lateinit var _binding: FragmentRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel

    private val binding get() = _binding
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        registerViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(RegisterViewModel::class.java)

        auth = FirebaseAuth.getInstance()
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        return binding.root
    }

    private fun showConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Onay")
            .setMessage("Mailinize gelen doğrulama kodunu onaylayınız")
            .show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.textView4.setOnClickListener {
            findNavController().navigate(R.id.registerToLogin)
        }

        binding.btnSignin.setOnClickListener {
            val username = binding.editTextUsername.text.toString()
            val email = binding.editTextMail.text.toString()
            val password = binding.editTextPassword.text.toString()
            val comfirmpassword = binding.editTextComfirmPassword.text.toString()

            registerViewModel.signup(email, username, password)
            registerViewModel.passwordController(password, comfirmpassword)
            registerViewModel.getUsername(username)

            showConfirmationDialog()
        }

        registerViewModel._isSignin.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(R.id.action_registerFragment_to_mapsFragment)
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding
    }
}
