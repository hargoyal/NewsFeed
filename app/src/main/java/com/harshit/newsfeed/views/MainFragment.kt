package com.harshit.newsfeed.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.harshit.newsfeed.adapters.NewsFeedRecyclerViewAdapter
import com.harshit.newsfeed.databinding.MainFragmentBinding
import com.harshit.newsfeed.hide
import com.harshit.newsfeed.models.NewsFeedModel
import com.harshit.newsfeed.show
import com.harshit.newsfeed.viewmodels.MainViewModel

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var layoutManager: LinearLayoutManager
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: NewsFeedRecyclerViewAdapter
    private var list: MutableList<NewsFeedModel.Article> = mutableListOf<NewsFeedModel.Article>()
    private var isPaginating:MutableLiveData<Boolean> = MutableLiveData(false)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = NewsFeedRecyclerViewAdapter(list, requireActivity())
        binding.recyclerViewNewsFeed.adapter = adapter
        layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewNewsFeed.layoutManager = layoutManager
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.getNewsFeedData().observe(viewLifecycleOwner, Observer {
            list.addAll(it)
            binding.recyclerViewNewsFeed.adapter?.notifyItemInserted(list.size)
//            binding.recyclerViewNewsFeed.adapter?.notifyDataSetChanged()
            if (it.isEmpty()) {
                showProgressbar()
            } else {
                hideProgressbar()
                isPaginating.value = false
            }
        })

        binding.recyclerViewNewsFeed.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isPaginating.value!! ) {
                    if (layoutManager.findLastCompletelyVisibleItemPosition() == list.size - 1 ) {
                        if(list.size < 100 ){
                            viewModel.getNewsFeedData()
                            isPaginating.value = true
                        } else {
                            Toast.makeText(requireActivity(),"100 records reached, There is a developer account limitation." , Toast.LENGTH_SHORT).show()
//                        isPaginating.value = false
                        }

                    }
                }
            }
        })

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                layoutManager.scrollToPositionWithOffset(positionStart, 0)
            }
        })

        isPaginating.observe(viewLifecycleOwner, Observer {
            with(binding.progressBarPagination){
                if (it) this.show() else this.hide()
            }
        })
    }

    private fun showProgressbar() {
        binding.progressBar.show()
        binding.recyclerViewNewsFeed.hide()
    }

    private fun hideProgressbar() {
        binding.progressBar.hide()
        binding.recyclerViewNewsFeed.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}