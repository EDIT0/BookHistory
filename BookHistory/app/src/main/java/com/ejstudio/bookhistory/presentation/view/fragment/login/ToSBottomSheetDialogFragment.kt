package com.ejstudio.bookhistory.presentation.view.fragment.login

import android.R.attr
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.ejstudio.bookhistory.R
import com.ejstudio.bookhistory.databinding.FragmentTosBottomSheetDialogBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.content.DialogInterface
import android.app.Activity

import android.util.DisplayMetrics
import androidx.lifecycle.Observer
import com.ejstudio.bookhistory.presentation.view.viewmodel.login.SignUp2ViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.R.attr.data
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream


class ToSBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private val TAG = ToSBottomSheetDialogFragment::class.java.simpleName

    private val signUp2ViewModel: SignUp2ViewModel by viewModel()
    lateinit var binding: FragmentTosBottomSheetDialogBinding
    lateinit var root: View

    var data: String = "";
    var inputStream: InputStream? = null
    var byteArrayOutputStream: ByteArrayOutputStream = ByteArrayOutputStream()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tos_bottom_sheet_dialog, container, false)
        binding.signUp2ViewModel = signUp2ViewModel
        root = binding.root

        viewModelCallback()
        inputStream = resources.openRawResource(R.raw.tos)
        binding.tvTos.text = readTxt()

        return root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface: DialogInterface? ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog?
            setupRatio(bottomSheetDialog!!)
        }
        return dialog
    }

    private fun setupRatio(bottomSheetDialog: BottomSheetDialog) {
        //id = com.google.android.material.R.id.design_bottom_sheet for Material Components
        //id = android.support.design.R.id.design_bottom_sheet for support librares
        val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet)
        val layoutParams = bottomSheet!!.layoutParams
        layoutParams.height = getBottomSheetDialogDefaultHeight()
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.isDraggable = false
        behavior.skipCollapsed = true
    }

    private fun getBottomSheetDialogDefaultHeight(): Int {
        return getWindowHeight() * 100 / 100
    }

    private fun getWindowHeight(): Int {
        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    fun viewModelCallback() {
        signUp2ViewModel.dissmissTos.observe(viewLifecycleOwner, Observer {
            this.dismiss()
        })
    }

    fun readTxt(): String {
        try {
            var read: Int = inputStream!!.read()
            while (read != -1) {
                byteArrayOutputStream.write(read)
                read = inputStream!!.read()
            }
            data = String(byteArrayOutputStream.toByteArray())
            inputStream?.close()

        } catch (e: IOException) {
            e.printStackTrace()
        }

        return data
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }
}