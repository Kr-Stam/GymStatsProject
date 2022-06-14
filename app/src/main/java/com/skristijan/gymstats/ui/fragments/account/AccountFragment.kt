package com.skristijan.gymstats.ui.fragments.account

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.skristijan.gymstats.databinding.FragmentAccountBinding

class AccountFragment : Fragment(), LifecycleObserver {

    companion object {
        fun newInstance() = AccountFragment()
    }

    private val viewModel: AccountViewModel by viewModels()

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.lifecycle?.addObserver(this)

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreateEvent() {
        // Remove the LifecycleObserver once you get a callback to ON_CREATE
        activity?.lifecycle?.removeObserver(this)

        // Then do your logic that specifically needs to wait for the Activity
        // to be created
        Log.d("callback", "lifecycle observer is called")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}