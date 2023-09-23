package be.samuel.gamehistorique.fragment

import android.R
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import be.samuel.gamehistorique.adapter.ISwitchMain
import be.samuel.gamehistorique.adapter.PartyHistoriqueListAdapter
import be.samuel.gamehistorique.adapter.StatTimeListAdapter
import be.samuel.gamehistorique.databinding.ActivityHistoriqueListBinding
import be.samuel.gamehistorique.viewModel.MainViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class PartyHistoriqueListFragment : Fragment(){

    private val mainViewModel : MainViewModel by viewModels()
    private lateinit var switchDetail: ISwitchMain

    private var _binding: ActivityHistoriqueListBinding? = null
    private val binding
        get() = checkNotNull(_binding){
            "Cannot access binding bacause it is null. Is the view is Visible ?"
        }
    private var idGameFilter : UUID? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        runBlocking{
            mainViewModel.init()
        }

        _binding = ActivityHistoriqueListBinding.inflate(layoutInflater, container, false)

        binding.historiqueRecyclerView.layoutManager = LinearLayoutManager(context)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.games.collect { games ->
                    var list : List<String> = listOf()
                    for(game in games){
                        list += game.name
                    }
                    val spinner = binding.gameSpinner
                    val adapter = ArrayAdapter(
                        requireActivity().applicationContext,
                        R.layout.simple_spinner_item,
                        list
                    )

                    spinner.adapter = adapter

                    spinner.onItemSelectedListener = object :
                        AdapterView.OnItemSelectedListener {
                        override fun onItemSelected( parent: AdapterView<*>,view: View,position: Int,id: Long) {
                            idGameFilter = mainViewModel.getGameNameSelect(position)
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {

                        }
                    }

                }
            }
        }

        binding.filterButton.setOnClickListener {
            var numberPlayer = binding.numberPlayerFilterInput.text.toString()
            if(numberPlayer == ""){
                numberPlayer = "-1"
            }
            viewLifecycleOwner.lifecycleScope.launch{
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                    mainViewModel.players.collect{players ->
                        binding.historiqueRecyclerView.adapter = PartyHistoriqueListAdapter(mainViewModel.getPartyFilter(idGameFilter, numberPlayer.toInt()), players, switchDetail)
                        binding.historiqueRecyclerView.setHasFixedSize(true)
                    }
                }
            }

        }

        binding.statButton.setOnClickListener {
            switchDetail.switchStat()
        }

        binding.addPartieButton.setOnClickListener {
            switchDetail.switchEdition()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch{
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
               mainViewModel.partys.collect{partys ->
                   binding.historiqueRecyclerView.adapter = PartyHistoriqueListAdapter(partys, mainViewModel.getPlayer(), switchDetail)
               }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is ISwitchMain){
            switchDetail = context
        }
    }

}