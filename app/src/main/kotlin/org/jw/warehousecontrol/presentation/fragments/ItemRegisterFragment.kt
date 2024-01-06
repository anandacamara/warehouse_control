package org.jw.warehousecontrol.presentation.fragments

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import org.jw.warehousecontrol.R
import org.jw.warehousecontrol.databinding.FragmentItemRegisterBinding
import org.jw.warehousecontrol.presentation.model.ItemModel
import org.jw.warehousecontrol.presentation.model.state.ItemRegisterState
import org.jw.warehousecontrol.presentation.util.doNothing
import org.jw.warehousecontrol.presentation.util.isNetworkAvailable
import org.jw.warehousecontrol.presentation.viewmodel.ItemRegisterViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

/**
 * @author Ananda Camara
 */
internal class ItemRegisterFragment : BaseFragment() {
    private lateinit var view: FragmentItemRegisterBinding

    private val viewModel: ItemRegisterViewModel by activityViewModel()

    private var imageUri: Uri? = null

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) {
            imageUri = it
            setPhotoUri(it)
        }

    private val takePhotoLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) {
            when (it) {
                true -> setPhotoUri(imageUri)
                false -> imageUri = null
            }
        }

    private val permissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { granted ->
            granted.entries.forEach {
                when (it.value) {
                    true -> showChooseOptionDialog()
                    false -> doNothing()
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = FragmentItemRegisterBinding.inflate(inflater, container, false)
        return view.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
        setObservers()
        verifyNetworkAvailability { doNothing() }
    }

    private fun setListeners() {
        view.addPhotoButton.setOnClickListener {
            if (checkCameraPermission()) showChooseOptionDialog()
            else permissions.launch(arrayOf(Manifest.permission.CAMERA))
        }
        view.nameEditText.doAfterTextChanged { verifyEnableSaveButton() }
        view.descriptionEditText.doAfterTextChanged { verifyEnableSaveButton() }
        view.saveButton.setOnClickListener {
            verifyNetworkAvailability { uploadImageToStorage()  }
        }
    }

    private fun setObservers() = lifecycleScope.launch {
        viewModel.stateFlow.collect { onStateChange(it) }
    }

    private fun onStateChange(state: ItemRegisterState) = when (state) {
        is ItemRegisterState.Failure -> {
            hideLoad()
            showSnackbar(view.root, state.message)
        }
        is ItemRegisterState.UploadImageSuccess -> {
            val item = ItemModel(
                name = getItemName(),
                img = state.url,
                description = getItemDescription()
            )

            viewModel.uploadItemToDatabase(item)
        }
        is ItemRegisterState.UploadItemSuccess -> {
            hideLoad()
            findNavController().navigateUp()
            showSnackbar(view.root, state.message)
        }
        ItemRegisterState.None -> doNothing()
    }

    private fun uploadImageToStorage() {
        imageUri?.let {
            showLoad()
            viewModel.uploadImageToStorage(getItemName(), it)
        }
    }

    private fun setPhotoUri(uri: Uri?) {
        uri?.let { view.photo.setImageURI(it) }

        verifyEnableSaveButton()
    }

    private fun verifyEnableSaveButton() {
        view.saveButton.isEnabled = areInputsFilled()
    }

    private fun areInputsFilled(): Boolean {
        if (getItemName().isBlank()) return false
        if (getItemDescription().isBlank()) return false
        if (imageUri == null) return false

        return true
    }

    private fun getItemName() = view.nameEditText.text.toString()

    private fun getItemDescription() = view.descriptionEditText.text.toString()

    private fun showChooseOptionDialog() {
        val items = arrayOf(
            resources.getString(R.string.warehouse_control_take_a_new_picture_message),
            resources.getString(R.string.warehouse_control_take_from_gallery_message)
        )

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.warehouse_control_choose_an_option_message))
            .setItems(items) { _, which ->
                when (which) {
                    0 -> {
                        if (imageUri == null) imageUri =
                            requireContext().contentResolver.insert(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                ContentValues()
                            )
                        takePhotoLauncher.launch(imageUri)
                    }
                    1 -> pickImageLauncher.launch(LAUNCH_IMAGE)
                    else -> doNothing()
                }
            }
            .show()
    }

    private fun checkCameraPermission(): Boolean {
        val result =
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)

        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun verifyNetworkAvailability(doSomething: () -> Unit) {
        if (isNetworkAvailable(requireContext())) doSomething()
        else showSnackbar(
            view.root,
            getString(R.string.warehouse_needed_network_availability_message)
        )
    }

    companion object {
        private const val LAUNCH_IMAGE = "image/*"
    }
}