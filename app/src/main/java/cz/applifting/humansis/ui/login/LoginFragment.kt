package cz.applifting.humansis.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import cz.applifting.humansis.R
import cz.applifting.humansis.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * Created by Petr Kubes <petr.kubes@applifting.cz> on 15, August, 2019
 */
class LoginFragment : BaseFragment() {

    private val viewModel: LoginViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val navController = findNavController()

        btn_login.setOnClickListener {
            viewModel.login(et_username.text.toString(), et_password.text.toString())
        }

        viewModel.viewStateLD.observe(viewLifecycleOwner, Observer { viewState ->
            et_username.isEnabled = viewState.etUsernameIsEnabled
            et_password.isEnabled = viewState.etPasswordIsEnabled
            btn_login.visibility = viewState.btnLoginVisibility
            pb_loading.visibility = viewState.pbLoadingVisible

            if (viewState.errorMessage != null) {
                tv_error.visibility = View.VISIBLE
                tv_error.text = viewState.errorMessage
            } else {
                tv_error.visibility = View.GONE
            }
        })

        viewModel.loginLD.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                val action = LoginFragmentDirections.actionLoginFragmentToMainFragment(it.email, it.username)
                navController.navigate(action)
            }
        })


        et_username.setText("demo@humansis.org")
        et_password.setText("Tester123")

    }
}