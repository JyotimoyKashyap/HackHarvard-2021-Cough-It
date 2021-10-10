package com.hackhack.coughit.ui.home

import android.Manifest
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.transition.MaterialElevationScale
import com.hackhack.coughit.R
import com.hackhack.coughit.databinding.FragmentHomeBinding
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class HomeFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val perms =
        arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )


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
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        // code goes here
        enterTransition = MaterialElevationScale(false)
        exitTransition = MaterialElevationScale(true)

        // customize toolbar
        customizeToolbar()

        // the app will need permission to access microphone
        Handler(Looper.getMainLooper()).postDelayed({
            if(!EasyPermissions.hasPermissions(requireContext(), *perms)){
                requestPermissions()
            }
        }, 500)


        // move to next fragment on click
        binding.run {
            addButton.setOnClickListener {
                if(EasyPermissions.hasPermissions(requireContext(), *perms)){
                    binding.root.findNavController().navigate(R.id.action_homeFragment_to_recordFragment)
                }else{
                    requestPermissions()
                }

            }
        }


        return binding.root
    }

    companion object {

        const val PERMISSION_REQUEST_CODE = 1

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun customizeToolbar(){
        val radius = 30
        val background = binding.homeToolbar.background as MaterialShapeDrawable

        background.shapeAppearanceModel =
            background.shapeAppearanceModel.toBuilder()
                .setAllCorners(CornerFamily.ROUNDED, radius.toFloat())
                .build()

    }

    fun handlePermissions() =
        EasyPermissions.hasPermissions(
            requireContext(),
            *perms
        )

    fun requestPermissions() =
        EasyPermissions.requestPermissions(
            this,
            "These permissions are necessary for total functionality of the app",
            PERMISSION_REQUEST_CODE,
            *perms
        )

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if(EasyPermissions.somePermissionDenied(this, perms.first())){
            SettingsDialog.Builder(requireActivity()).build().show()
        }else{
            requestPermissions()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Toast.makeText(
            requireContext(),
            "Permissions Granted",
            Toast.LENGTH_SHORT
        ).show()
    }
}