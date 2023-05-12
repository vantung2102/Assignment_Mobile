package com.example.hrm.Fragments.Performance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.hrm.Adapters.SeflPreviewAdpater;
import com.example.hrm.Fragments.Home.HomeActivity;
import com.example.hrm.R;
import com.example.hrm.Response.DatumTemplate;
import com.example.hrm.Response.PerformanceAttributes;
import com.example.hrm.databinding.FinishedSelfPreviewLayoutBinding;
import com.example.hrm.databinding.InprogressSelfPreviewLayoutBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelfPreviewFinishedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelfPreviewFinishedFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SelfPreviewFinishedFragment(List<PerformanceAttributes> finished) {
        // Required empty public constructor
        data=finished;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SelfPreviewInProgressFragment.
     */
    public SelfPreviewFinishedFragment(){

    }
    // TODO: Rename and change types and number of parameters
    public static SelfPreviewFinishedFragment newInstance(String param1, String param2) {
        SelfPreviewFinishedFragment fragment = new SelfPreviewFinishedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    FinishedSelfPreviewLayoutBinding binding;
    SeflPreviewAdpater adpater;
    private List<PerformanceAttributes> data=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FinishedSelfPreviewLayoutBinding.inflate(inflater);
        adpater=new SeflPreviewAdpater();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        binding.rcvFinished.setLayoutManager(linearLayoutManager);
        binding.rcvFinished.setAdapter(adpater);
        adpater.setData(data,(HomeActivity) getActivity(),false,false);
        return binding.getRoot();
    }
}