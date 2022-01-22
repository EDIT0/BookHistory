package com.ejstudio.bookhistory.presentation.base

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.ejstudio.bookhistory.R
import com.google.android.material.snackbar.Snackbar
import io.reactivex.rxjava3.disposables.CompositeDisposable

abstract class BaseActivity<B : ViewDataBinding>(
    @LayoutRes val layoutId: Int
) : AppCompatActivity() {
    lateinit var binding: B
    private val compositeDisposable = CompositeDisposable()
    lateinit var mActivity: Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.lifecycleOwner = this

        mActivity = this
    }

    protected fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    protected fun showSnackbar(msg: String) {
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT).show()
    }

    protected fun showSnackbarAction(msg: String) {
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT)
            .setAction("확인", object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    finish()
                    overridePendingTransition(R.anim.not_move_activity, R.anim.rightout_activity)
                }
            }).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }
}