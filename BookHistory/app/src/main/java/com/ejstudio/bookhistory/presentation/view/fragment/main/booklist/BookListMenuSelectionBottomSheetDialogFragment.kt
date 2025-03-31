package com.ejstudio.bookhistory.presentation.view.fragment.main.booklist

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent.getIntent
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.ejstudio.bookhistory.R
import com.ejstudio.bookhistory.databinding.FragmentBookListMenuSelectionBottomSheetDialogBinding
import com.ejstudio.bookhistory.databinding.FragmentTosBottomSheetDialogBinding
import com.ejstudio.bookhistory.presentation.view.activity.main.MainActivity
import com.ejstudio.bookhistory.presentation.view.viewmodel.login.SignUp2ViewModel
import com.ejstudio.bookhistory.presentation.view.viewmodel.main.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class BookListMenuSelectionBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private val TAG = BookListMenuSelectionBottomSheetDialogFragment::class.java.simpleName

//    private val mainViewModel: MainViewModel by viewModel()
    lateinit var mainViewModel: MainViewModel
    lateinit var binding: FragmentBookListMenuSelectionBottomSheetDialogBinding
    lateinit var root: View

    lateinit var state: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_book_list_menu_selection_bottom_sheet_dialog, container, false)
        mainViewModel = (activity as MainActivity).mainViewModel
        Log.i(TAG, "코드2: ${mainViewModel.hashCode()}")
        binding.mainViewModel = mainViewModel
        root = binding.root

        recvIntent()
        buttonClickListener()


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
        val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet)
//        val layoutParams = bottomSheet!!.layoutParams
//        layoutParams.height = getBottomSheetDialogDefaultHeight()
//        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.isDraggable = true
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

    fun recvIntent() {
        state = arguments?.getString("state")!!
        Log.i(TAG, "state 값: $state")
        when (state) {
            getString(R.string.before_read) -> {
                binding.ivBeforeReadCheckImage.visibility = View.VISIBLE
                binding.ivReadingCheckImage.visibility = View.INVISIBLE
                binding.ivEndReadCheckImage.visibility = View.INVISIBLE
                binding.tvBeforeRead.setTextColor(Color.parseColor("#ff5107"))
                binding.tvReading.setTextColor(Color.parseColor("#303030"))
                binding.tvEndRead.setTextColor(Color.parseColor("#303030"))
            }
            getString(R.string.reading) -> {
                binding.ivBeforeReadCheckImage.visibility = View.INVISIBLE
                binding.ivReadingCheckImage.visibility = View.VISIBLE
                binding.ivEndReadCheckImage.visibility = View.INVISIBLE
                binding.tvBeforeRead.setTextColor(Color.parseColor("#303030"))
                binding.tvReading.setTextColor(Color.parseColor("#ff5107"))
                binding.tvEndRead.setTextColor(Color.parseColor("#303030"))
            }
            getString(R.string.end_read) -> {
                binding.ivBeforeReadCheckImage.visibility = View.INVISIBLE
                binding.ivReadingCheckImage.visibility = View.INVISIBLE
                binding.ivEndReadCheckImage.visibility = View.VISIBLE
                binding.tvBeforeRead.setTextColor(Color.parseColor("#303030"))
                binding.tvReading.setTextColor(Color.parseColor("#303030"))
                binding.tvEndRead.setTextColor(Color.parseColor("#ff5107"))
            }
        }
    }

    fun buttonClickListener(){
        binding.tvBeforeRead.setOnClickListener {
            mainViewModel.changeList(MainViewModel.BEFORE_READ)
            dismiss()
        }
        binding.tvReading.setOnClickListener {
            mainViewModel.changeList(MainViewModel.READING)
            dismiss()
        }
        binding.tvEndRead.setOnClickListener {
            mainViewModel.changeList(MainViewModel.END_READ)
            dismiss()
        }
    }


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }
}
