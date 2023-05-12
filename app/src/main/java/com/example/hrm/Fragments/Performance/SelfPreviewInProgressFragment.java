package com.example.hrm.Fragments.Performance;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hrm.Adapters.SeflPreviewAdpater;
import com.example.hrm.Fragments.Home.HomeActivity;
import com.example.hrm.Response.PerformanceAttributes;
import com.example.hrm.databinding.InprogressSelfPreviewLayoutBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelfPreviewInProgressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class SelfPreviewInProgressFragment extends Fragment {
    public void refresh(PerformanceAttributes item, int pos){
    Log.d("SelfPreviewInProgressFragment",String.valueOf(pos));
        data.set(pos,item);
        adpater.notifyItemChanged(pos);
    }
    class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            SelfPreviewInProgressFragment.this.refresh((PerformanceAttributes)intent.getExtras().getSerializable("item"),intent.getExtras().getInt("pos"));
        }
    }
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(r);
    }

    public void onResume() {
        super.onResume();
        r = new MyReceiver();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(r,
                new IntentFilter("TAG_REFRESH"));
    }
    MyReceiver r;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<PerformanceAttributes> data=new ArrayList<>();
    public SelfPreviewInProgressFragment(List<PerformanceAttributes> inProgress,boolean staffEdit,boolean bossEdit) {
        // Required empty public constructor
        this.staffEdit=staffEdit;
        this.bossEdit=bossEdit;
        data=inProgress;
        Log.d("dataSize", String.valueOf(data.size()));
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SelfPreviewInProgressFragment.
     */
    // TODO: Rename and change types and number of parameters
    public SelfPreviewInProgressFragment(){

    }
    private boolean staffEdit,bossEdit;
    public static SelfPreviewInProgressFragment newInstance(String param1, String param2) {
        SelfPreviewInProgressFragment fragment = new SelfPreviewInProgressFragment();
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
    InprogressSelfPreviewLayoutBinding binding;
    SeflPreviewAdpater adpater;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=InprogressSelfPreviewLayoutBinding.inflate(inflater);
        adpater=new SeflPreviewAdpater();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        binding.rcvInProgress.setLayoutManager(linearLayoutManager);
        binding.rcvInProgress.setAdapter(adpater);
        adpater.setData(data,(HomeActivity) getActivity(),staffEdit,bossEdit);
        return binding.getRoot();
    }

    public void getData() {

    }


}