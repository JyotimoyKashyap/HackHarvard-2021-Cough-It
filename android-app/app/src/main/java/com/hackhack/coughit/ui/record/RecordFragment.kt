package com.hackhack.coughit.ui.record

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.hackhack.coughit.R
import com.hackhack.coughit.databinding.FragmentRecordBinding
import com.hackhack.coughit.ui.RestViewModel


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class RecordFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentRecordBinding? = null
    private val binding get() = _binding!!
    private val restViewModel: RestViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRecordBinding.inflate(inflater, container, false)
        // code goes here
        binding.run {
            // start the timer on button click
            playButton.setOnClickListener {
                restViewModel.startCountdown(playButton)
                playButton.isEnabled = false
            }

            restViewModel.countdownValue.observe(viewLifecycleOwner, Observer {
                countdownTimer.text = it
            })
        }

        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RecordFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}