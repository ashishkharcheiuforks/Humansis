package cz.applifting.humansis.ui.main.distribute.beneficiaries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import cz.applifting.humansis.R
import cz.applifting.humansis.extensions.visible
import cz.applifting.humansis.model.db.BeneficiaryLocal
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

        val viewAdapter = BeneficiariesAdapter { beneficiary ->
            this.findNavController().navigate(chooseDirection(beneficiary))
        }

        lc_beneficiaries.init(viewAdapter)
        lc_beneficiaries.setOnRefreshListener {
            viewModel.loadBeneficiaries(args.distributionId, true)
        }

        viewModel.searchResults.observe(viewLifecycleOwner, Observer {
            viewAdapter.update(it)
        })

        viewModel.beneficiariesViewStateLD.observe(viewLifecycleOwner, Observer {
            lc_beneficiaries.setState(it)
            showControls(!it.isRetrieving)
        })

        viewModel.statsLD.observe(viewLifecycleOwner, Observer {
            val (reachedBeneficiaries, totalBeneficiaries) = it
            cmp_reached_beneficiaries.setStats(reachedBeneficiaries, totalBeneficiaries)
        })

        cmp_search_beneficiary.onTextChanged(viewModel::search)
        cmp_search_beneficiary.onSort { viewModel.sortBeneficiaries() }

        viewModel.loadBeneficiaries(args.distributionId, true)

    }

    private fun showControls(show: Boolean) {
        cmp_reached_beneficiaries.visible(show)
        cmp_search_beneficiary.visible(show)
    }

    private fun chooseDirection(beneficiary: BeneficiaryLocal): NavDirections {

        //todo find differentiation, possible booklets not empty
        return if (beneficiary.familyName == "Bis") {
            BeneficiariesFragmentDirections.actionBeneficiariesFragmentToBeneficiaryFragment(
                beneficiary.id,
                getString(R.string.beneficiary_name, beneficiary.givenName, beneficiary.familyName),
                args.distributionName,
                args.projectName,
                beneficiary.distributed
            )
        } else {
            BeneficiariesFragmentDirections.actionBeneficiariesFragmentToQrBeneficiaryFragment(
                beneficiary.id,
                getString(R.string.beneficiary_name, beneficiary.givenName, beneficiary.familyName),
                args.distributionName,
                args.projectName,
                beneficiary.distributed
            )
        }
    }
}