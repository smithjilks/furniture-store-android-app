package com.smith.furniturestore.ui.main

import android.Manifest
import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.smith.furniturestore.R
import com.smith.furniturestore.app.App
import com.smith.furniturestore.data.database.entity.OrderItem
import com.smith.furniturestore.databinding.FragmentCheckoutBinding
import com.smith.furniturestore.databinding.FragmentCreateItemBinding
import com.smith.furniturestore.ui.MainActivity
import com.smith.furniturestore.viewmodel.CreateItemFragmentViewModel
import com.smith.furniturestore.viewmodel.CreateItemFragmentViewModelFactory
import com.smith.furniturestore.viewmodel.SharedAuthViewModel
import com.smith.furniturestore.viewmodel.SharedAuthViewModelFactory
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import kotlin.io.path.Path

class CreateItemFragment : Fragment() {
    private var _binding: FragmentCreateItemBinding? = null
    private val binding get() = _binding!!

    private var progressBar: ProgressBar? = null
    private var imageFile: MultipartBody.Part? = null

    private val viewModel: CreateItemFragmentViewModel by viewModels {
        CreateItemFragmentViewModelFactory(
            (activity?.application as App).furnitureRepository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = binding.createItemProgressBar


        binding.createItemImagePickerButton.setOnClickListener {

            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1
                    )
                } else {
                    ActivityCompat.requestPermissions(
                        requireActivity(),
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1
                    )
                }
            } else {
                openImageGallery()
            }


        }

        binding.createItemButton.setOnClickListener {
            if (isEnteredDataValid()) {
                progressBar!!.visibility = View.VISIBLE
                binding.createItemButton.isEnabled = false

                val title = RequestBody.create(
                    MediaType.parse("multipart/form-data"),
                    binding.createItemItemTitleEditText.text.toString()
                )
                val shortDesc = RequestBody.create(
                    MediaType.parse("multipart/form-data"),
                    binding.createItemShortDescEditText.text.toString()
                )
                val longDesc = RequestBody.create(
                    MediaType.parse("multipart/form-data"),
                    binding.createItemLongDescEditText.text.toString()
                )
                val itemPrice = RequestBody.create(
                    MediaType.parse("multipart/form-data"),
                    binding.createItemPriceEditText.text.toString()
                )

                imageFile?.let { it1 ->
                    viewModel.createNewItem( title, shortDesc, longDesc, itemPrice,
                        it1
                    )
                }
            }
        }

        viewModel.status.observe(viewLifecycleOwner, Observer {

            if(it == "success") {
                Toast.makeText(context, "Item added Successfully", Toast.LENGTH_LONG).show()
                findNavController().popBackStack()
            } else {
                progressBar!!.visibility = View.GONE
                binding.createItemButton.isEnabled = true
                Toast.makeText(context, "Create new item failed", Toast.LENGTH_LONG).show()
            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun isEnteredDataValid(): Boolean {
        val title = binding.createItemItemTitleEditText
        val shortDesc = binding.createItemShortDescEditText
        val longDesc = binding.createItemLongDescEditText
        val price = binding.createItemPriceEditText
        val imageError = binding.createItemErrorImageViewTextView

        if (title.text.isNullOrEmpty()) {
            title.error = getString(R.string.title_error)
        }

        if (shortDesc.text.isNullOrEmpty()) {
            shortDesc.error = getString(R.string.short_desc_error)
        }

        if (longDesc.text.isNullOrEmpty()) {
            longDesc.error = getString(R.string.long_desc_error)
        }

        if (price.text.isNullOrEmpty()) {
            price.error = getString(R.string.error_price)
        }

        if (binding.createItemImageView.drawable == null) {
            imageError.error = getString(R.string.image_error)
        } else {
            imageError.error = null
        }

        return !(title.error != null ||
                shortDesc.error != null ||
                longDesc.error != null ||
                price.error != null
                || imageError.error != null)


    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    if ((ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) ==
                                PackageManager.PERMISSION_GRANTED)
                    ) {
                        Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }


    private fun openImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        resultLauncher.launch(intent)
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data

                data?.data?.let {
                    binding.createItemImageView.setImageURI(it)
                    imageFile = createImageRequestBody(it)
                }

            }
        }

    private fun createImageRequestBody(uri: Uri): MultipartBody.Part {
        val file = File(getRealPathFromURI(uri))
        Log.d("Image File path", file.absolutePath)
        val requestFile = RequestBody.create(
            MediaType.parse(context?.contentResolver?.getType(uri)), file
        )
        return MultipartBody.Part.createFormData("image", file.name, requestFile)
    }

    private fun getRealPathFromURI(uri: Uri?): String {
        var path = ""
        if (context?.contentResolver  != null) {
            val cursor: Cursor? = context?.contentResolver!!.query(uri!!, null, null, null, null)
            if (cursor != null) {
                cursor.moveToFirst()
                val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                path = cursor.getString(idx)
                cursor.close()
            }
        }
        return path
    }
}