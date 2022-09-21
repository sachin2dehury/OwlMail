package github.owlmail.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import github.owlmail.auth.databinding.FragmentAuthBinding

@AndroidEntryPoint
class AuthFragment : Fragment() {

    private var _binding: FragmentAuthBinding? = null

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAuthBinding.inflate(inflater)
        return _binding?.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpClickListener()
        subscribeToObservers()
    }

    private fun setUpClickListener() = _binding?.run {
        _binding?.button?.setOnClickListener {
            makeAuthRequest()
        }
    }

    private fun makeAuthRequest() = _binding?.run {
        val username = _binding?.editTextTextPersonName?.text?.trim().toString()
        val password = _binding?.editTextTextPassword2?.text?.trim().toString()
    }

    private fun subscribeToObservers() {

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}