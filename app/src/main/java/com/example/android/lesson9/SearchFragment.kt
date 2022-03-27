package com.example.android.lesson9

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.lesson9.databinding.FragmentSearchBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jakewharton.rxbinding3.appcompat.queryTextChanges
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import io.reactivex.android.schedulers.AndroidSchedulers as AndroidSchedulersCompat


class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var textView: TextView

    private lateinit var recyclerViewAdapter: RecyclerViewAdapter
    private lateinit var jokesList: List<String>

    companion object {
        private const val FILE = "jokes.json"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        observeJSON()
        searchView = binding.searchView
        observeQueryChange(searchView)

        textView = binding.textPlug
        recyclerView = binding.recyclerView
        recyclerViewAdapter = RecyclerViewAdapter()
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        return binding.root
    }

    @SuppressLint("CheckResult")
    private fun observeQueryChange(searchView: SearchView) {
        searchView.queryTextChanges()
            .subscribeOn(AndroidSchedulersCompat.mainThread())
            .debounce(500, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulersCompat.mainThread())
            .subscribe({
                if (it.isNotEmpty()) {
                    recyclerView.visibility = View.VISIBLE
                    textView.visibility = View.GONE
                    recyclerViewAdapter.dataSet = ArrayList(searchJokes(it.toString()))
                } else {
                    recyclerView.visibility = View.GONE
                    textView.visibility = View.VISIBLE
                }
            }, { throwable ->
                Log.e("RxJava", throwable.message.toString())
            })
    }

    private fun searchJokes(seq: String): List<String> {
        return jokesList.filter { it.contains(seq, true) }
    }

    private fun observeJSON() {
        Observable.create<List<String>> { obs ->
            obs.onNext(getJokes())
        }.subscribeOn(Schedulers.io())
            .doOnNext {
                Log.d("RxJava", "Current thread: ${Thread.currentThread().name}")
            }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                Log.d("RxJava", "Current thread: ${Thread.currentThread().name}")
            }
            .subscribe({
                jokesList = it
                Log.d("RxJava", "Current thread: ${Thread.currentThread().name}")
            }, { throwable ->
                Log.e("RxJava", throwable.message.toString())
            })
    }

    private fun getJokes(): List<String> {
        val gson = Gson()
        val jsonString = readJSON()
        val sType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(jsonString, sType)
    }

    private fun readJSON(): String? {
        val buffer = context?.assets?.open(FILE)?.bufferedReader(Charsets.UTF_8)
        return buffer?.readText()
    }
}