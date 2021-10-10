package com.hackhack.coughit.ui.result

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.MaterialElevationScale
import com.hackhack.coughit.R
import com.hackhack.coughit.databinding.FragmentResultBinding
import com.hackhack.coughit.model.CoughData
import com.hackhack.coughit.repository.Repository
import com.hackhack.coughit.ui.RestViewModel
import com.hackhack.coughit.ui.ViewModelProviderFactory
import com.hackhack.coughit.util.Resource


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ResultFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!
    private val args: ResultFragmentArgs by navArgs()
    private var encodedString: String? = null
    lateinit var restViewModel: RestViewModel

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
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        // your code goes here
        exitTransition = MaterialElevationScale(false)
        enterTransition = MaterialElevationScale(true)
        encodedString = args.encodedString

        Log.d("resultencoded" , "encoded string: $encodedString")

        // init the repository and view model
        val restRepository = Repository(requireContext())
        val viewModelProviderFactory = ViewModelProviderFactory(restRepository)
        restViewModel = ViewModelProvider(this, viewModelProviderFactory)
            .get(RestViewModel::class.java)

        // make the api call
        restViewModel.getCoughResponse(CoughData(encodedString!!))

        // observer the api call
        restViewModel.coughSampleResult.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Success ->{
                    it.data?.let {
                        // here I will get the cough response model
                        val prediction = String.format("%.3f",it.predictions[0][0]).toDouble()
                        binding.predictionTextView.text = prediction.toString()
                    }
                }
            }
        })

        binding.run {
            nextButton.setOnClickListener {
                findNavController().navigate(R.id.action_resultFragment_to_homeFragment)
            }
        }


        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ResultFragment().apply {
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