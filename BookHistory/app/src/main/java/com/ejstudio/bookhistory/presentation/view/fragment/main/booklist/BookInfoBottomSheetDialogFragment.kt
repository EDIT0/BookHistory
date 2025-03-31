package com.ejstudio.bookhistory.presentation.view.fragment.main.booklist

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.ejstudio.bookhistory.R
import com.ejstudio.bookhistory.databinding.FragmentBookInfoBottomSheetDialogBinding
import com.ejstudio.bookhistory.presentation.view.activity.main.booklist.BookActivity
import com.ejstudio.bookhistory.presentation.view.viewmodel.main.booklist.BookViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BookInfoBottomSheetDialogFragment : BottomSheetDialogFragment() {
    private val TAG = BookInfoBottomSheetDialogFragment::class.java.simpleName

    //    private val mainViewModel: MainViewModel by viewModel()
    lateinit var bookViewModel: BookViewModel
    lateinit var binding: FragmentBookInfoBottomSheetDialogBinding
    lateinit var root: View

    lateinit var state: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_book_info_bottom_sheet_dialog, container, false)
        bookViewModel = (activity as BookActivity).bookViewModel
        binding.bookViewModel = bookViewModel
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
        val layoutParams = bottomSheet!!.layoutParams
        layoutParams.height = getBottomSheetDialogDefaultHeight()
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.isDraggable = false
        behavior.skipCollapsed = true
    }

    private fun getBottomSheetDialogDefaultHeight(): Int {
        return getWindowHeight() * 85 / 100
    }

    private fun getWindowHeight(): Int {
        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    fun recvIntent() {
//        state = arguments?.getString("state")!!
//        Log.i(TAG, "state 값: $state")

    }

    fun buttonClickListener(){
//        binding.tvBeforeRead.setOnClickListener {
//            mainViewModel.changeList(MainViewModel.BEFORE_READ)
//            dismiss()
//        }
//        binding.tvReading.setOnClickListener {
//            mainViewModel.changeList(MainViewModel.READING)
//            dismiss()
//        }
//        binding.tvEndRead.setOnClickListener {
//            mainViewModel.changeList(MainViewModel.END_READ)
//            dismiss()
//        }
        binding.btnBackButton.setOnClickListener {
            dismiss()
        }
    }


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }
}