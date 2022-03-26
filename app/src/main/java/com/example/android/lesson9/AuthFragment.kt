package com.example.android.lesson9

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.android.lesson9.databinding.FragmentAuthBinding

class AuthFragment : Fragment() {
    private lateinit var binding: FragmentAuthBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthBinding.inflate(inflater, container, false)

        binding.signInBtn.setOnClickListener {
            it.findNavController().navigate(AuthFragmentDirections.actionAuthFragmentToSearchFragment())
        }

        return binding.root
    }

}