package com.example.android.lesson9

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.android.lesson9.databinding.FragmentAuthBinding
import com.jakewharton.rxbinding4.widget.textChangeEvents
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

class AuthFragment : Fragment() {
    private lateinit var binding: FragmentAuthBinding
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText

    companion object {
        private const val USERNAME = "username"
        private const val PASSWORD = "password"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthBinding.inflate(inflater, container, false)
        usernameEditText = binding.usernameEditText
        passwordEditText = binding.passwordEditText

        observeEditViews()
        savedInstanceState?.let {
            usernameEditText.setText(it.getString(USERNAME))
            passwordEditText.setText(it.getString(PASSWORD))
        }

        binding.signInBtn.setOnClickListener {
            it.findNavController()
                .navigate(AuthFragmentDirections.actionAuthFragmentToSearchFragment())
        }

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(USERNAME, usernameEditText.text.toString())
        outState.putString(PASSWORD, passwordEditText.text.toString())
    }

    private fun observeEditViews() {
        Observable.combineLatest(
            usernameEditText.textChangeEvents(),
            passwordEditText.textChangeEvents(),
            { firstEmit, secondEmit ->
                firstEmit to secondEmit
            }
        ).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ eventPair ->
                binding.signInBtn.isEnabled =
                    eventPair.first.text.length > 5 && eventPair.second.text.length > 5
            }, { throwable ->
                Log.e("RxJava", throwable.message.toString())
            })

    }

}