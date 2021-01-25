package edu.covidianie.ui.restrictions

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import edu.covidianie.R

class RestrictionsFragment : Fragment() {

    companion object {
        fun newInstance() = RestrictionsFragment()
    }

    private lateinit var viewModel: RestrictionsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.restrictions_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RestrictionsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}