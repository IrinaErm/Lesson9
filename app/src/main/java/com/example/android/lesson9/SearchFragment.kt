package com.example.android.lesson9

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.android.lesson9.databinding.FragmentSearchBinding


class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val inflated = inflater.inflate(R.menu.search_menu, menu)
        val searchItem = menu.findItem(R.id.search)
        searchItem.expandActionView()

        return inflated
    }

}