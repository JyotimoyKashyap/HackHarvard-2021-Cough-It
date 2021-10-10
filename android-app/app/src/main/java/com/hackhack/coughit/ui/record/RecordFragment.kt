package com.hackhack.coughit.ui.record

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.hackhack.coughit.R
import com.hackhack.coughit.databinding.FragmentRecordBinding
import com.hackhack.coughit.repository.Repository
import com.hackhack.coughit.ui.RestViewModel
import com.hackhack.coughit.ui.ViewModelProviderFactory
import com.hackhack.coughit.util.RecordingState


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class RecordFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentRecordBinding? = null
    private val binding get() = _binding!!
    lateinit var restViewModel: RestViewModel
    private var encodedString: String? = null
    private var state: Boolean = false

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

        // init the repository & view model
        val restRepository = Repository(requireContext())
        val viewModelProviderFactory = ViewModelProviderFactory(restRepository)
        restViewModel = ViewModelProvider(this, viewModelProviderFactory)
            .get(RestViewModel::class.java)

        // code goes here
        binding.run {
            // start the timer on button click
            playButton.setOnClickListener {
                state = true
                restViewModel.startCountdown(playButton, requireContext(), state)
                playButton.isEnabled = false
            }

            restViewModel.countdownValue.observe(viewLifecycleOwner, Observer {
                countdownTimer.text = it
            })


            restViewModel.flag.observe(viewLifecycleOwner, Observer {
                if(RecordingState.STOP == it){
                    encodedString = restViewModel.getAudioString()
                    val action = RecordFragmentDirections.
                    actionRecordFragmentToResultFragment(encodedString = encodedString!!)
                    findNavController().navigate(action)
                }
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