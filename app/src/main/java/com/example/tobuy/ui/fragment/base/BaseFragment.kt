package com.example.tobuy.ui.fragment.base

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

}