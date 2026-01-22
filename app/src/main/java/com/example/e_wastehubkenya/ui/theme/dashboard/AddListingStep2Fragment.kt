package com.example.e_wastehubkenya.ui.theme.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.e_wastehubkenya.R
import com.example.e_wastehubkenya.data.Resource
import com.example.e_wastehubkenya.databinding.FragmentAddStep2Binding
import com.example.e_wastehubkenya.viewmodel.AddListingViewModel
import com.example.e_wastehubkenya.viewmodel.ListingViewModel
import com.example.e_wastehubkenya.viewmodel.MpesaViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AddListingStep2Fragment : Fragment() {

    private var _binding: FragmentAddStep2Binding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: AddListingViewModel by activityViewModels()
    private val listingViewModel: ListingViewModel by viewModels()
    private val mpesaViewModel: MpesaViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddStep2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.switchDonate.setOnCheckedChangeListener { _, isChecked ->
            binding.tilPrice.isVisible = !isChecked
            binding.tilLocation.isVisible = true
        }

        binding.btnSubmitListing.setOnClickListener {
            handleSubmit()
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        listingViewModel.createListingResult.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.isVisible = true
                }
                is Resource.Success -> {
                    binding.progressBar.isVisible = false
                    Toast.makeText(context, resource.data, Toast.LENGTH_LONG).show()
                    sharedViewModel.clear()
                    findNavController().popBackStack(R.id.my_listings_fragment, false)
                }
                is Resource.Error -> {
                    binding.progressBar.isVisible = false
                    Toast.makeText(context, "Failed to create listing: ${resource.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        mpesaViewModel.stkPushResult.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.isVisible = true
                }
                is Resource.Success -> {
                    binding.progressBar.isVisible = false
                    val checkoutRequestID = resource.data!!.checkoutRequestID
                    Toast.makeText(context, "STK push initiated. Please check your phone.", Toast.LENGTH_LONG).show()
                    startPolling(checkoutRequestID)
                }
                is Resource.Error -> {
                    binding.progressBar.isVisible = false
                    Toast.makeText(context, "Failed to initiate STK push: ${resource.message}", Toast.LENGTH_LONG).show()
                }
            }
        }

        mpesaViewModel.stkQueryResult.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    // Optional: Show a different indicator for polling
                }
                is Resource.Success -> {
                    val resultCode = resource.data!!.resultCode
                    if (resultCode == "0") {
                        // Payment successful
                        stopPolling()
                        Toast.makeText(context, "Payment successful!", Toast.LENGTH_SHORT).show()
                        createListing()
                    } else {
                        if (resultCode != "1037") { // 1037 is request still being processed
                            stopPolling()
                            Toast.makeText(context, "Payment failed: ${resource.data.resultDesc}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                is Resource.Error -> {
                    stopPolling()
                    Toast.makeText(context, "Failed to query transaction status: ${resource.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private var isPolling = false
    private fun startPolling(checkoutRequestID: String) {
        if (isPolling) return
        isPolling = true
        viewLifecycleOwner.lifecycleScope.launch {
            var retries = 0
            while (isPolling && retries < 12) { // Poll for 1 minute (12 * 5 seconds)
                delay(5000)
                mpesaViewModel.queryStkPushStatus(checkoutRequestID)
                retries++
            }
            if(isPolling) {
                stopPolling()
                Toast.makeText(context, "Payment verification timed out.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun stopPolling() {
        isPolling = false
    }

    private fun handleSubmit() {
        val description = binding.etDescription.text.toString().trim()
        val isDonation = binding.switchDonate.isChecked
        val priceText = if (!isDonation) binding.etPrice.text.toString().trim() else "0"
        val location = binding.etLocation.text.toString().trim()

        if (description.isEmpty() || location.isEmpty() || (!isDonation && priceText.isEmpty())) {
            Toast.makeText(context, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
            return
        }

        // For now, using a hardcoded phone number. You should get this from the user.
        val phoneNumber = "2547xxxxxxxx" // Replace with a valid phone number
        val amount = priceText.toDoubleOrNull()?.toInt()?.toString() ?: "0"

        if (amount != "0") {
            mpesaViewModel.initiateStkPush(amount, phoneNumber, "E-Waste Hub")
        } else {
            createListing()
        }
    }

    private fun createListing() {
        val description = binding.etDescription.text.toString().trim()
        val isDonation = binding.switchDonate.isChecked
        val priceText = if (!isDonation) binding.etPrice.text.toString().trim() else "0"
        val location = binding.etLocation.text.toString().trim()
        
        val currentListing = sharedViewModel.listingInProgress.value!!
        val imageUris = sharedViewModel.imageUris.value!!

        val finalListing = currentListing.copy(
            description = description,
            price = priceText.toDoubleOrNull() ?: 0.0,
            location = location,
            isDonation = isDonation
        )

        listingViewModel.createListing(finalListing, imageUris)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        stopPolling()
        _binding = null
    }
}