package cz.applifting.humansis.ui.main.distribute.beneficiaries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import cz.applifting.humansis.R
import cz.applifting.humansis.extensions.visible
import cz.applifting.humansis.ui.BaseFragment
import cz.applifting.humansis.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_beneficiaries.*

/**
 * Created by Vaclav Legat <vaclav.legat@applifting.cz>
 * @since 5. 9. 2019
 */

class BeneficiariesFragment : BaseFragment() {

    val args: BeneficiariesFragmentArgs by navArgs()

    private val viewModel: BeneficiariesViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_beneficiaries, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as MainActivity).supportActionBar?.title = args.distributionName
        (activity as MainActivity).supportActionBar?.subtitle = getString(R.string.beneficiaries_title)

        val viewManager = LinearLayoutManager(context)
        val viewAdapter = BeneficiariesAdapter { beneficiary ->
            // todo implement on click logic
        }

        rv_beneficiaries.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        viewModel.beneficiariesLD.observe(viewLifecycleOwner, Observer {
            viewAdapter.update(it)
        })

        viewModel.beneficiariesViewStateLD.observe(viewLifecycleOwner, Observer {
            pb_loading.visible(it.refreshing)
            tv_beneficiaries_reached.visible(!it.refreshing)
            pb_beneficiaries_reached.visible(!it.refreshing)
        })

        // todo set values from BE
        tv_beneficiaries_reached.text = getString(R.string.beneficiaries_reached, 1_000_000, 1_500_000)
        pb_beneficiaries_reached.progress = 1_000_000*100/1_500_000

        viewModel.loadBeneficiaries(args.distributionId)

    }
}