package com.example.tobuy.ui.fragment.base

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavDirections
import com.example.tobuy.arch.ToBuyViewModel
import com.example.tobuy.ui.activity.MainActivity


abstract class BaseFragment : Fragment() {

    protected val activityHandler: MainActivity
        get() = activity as MainActivity

    protected val navController by lazy { activityHandler.navController }

    protected val sharedViewModel: ToBuyViewModel by activityViewModels()

    protected fun navigateUp() {
        activityHandler.navController.navigateUp()
    }

    protected fun navigateViaNavGraph(actionId: NavDirections) {
        activityHandler.navController.navigate(actionId)
    }

    fun showToastMessage(msg: String) {

        view?.apply {
            Toast.makeText(
                this.context,
                msg,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun showSoftKeyboard(view: View) {

        if (view.requestFocus()) {
            val imm =
                activityHandler.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    fun hideSoftKeyboard(view: View) {

        view.apply {
            val imm: InputMethodManager? =
                activityHandler.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.hideSoftInputFromWindow(this.windowToken, 0)
        }
    }

}