package com.example.hrm.Fragments.Performance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.hrm.Adapters.SelfPreviewViewpagerAdapter;
import com.example.hrm.Common;
import com.example.hrm.R;
import com.example.hrm.Response.DataResponseList;
import com.example.hrm.Response.DatumTemplate;
import com.example.hrm.Response.PerformanceAttributes;
import com.example.hrm.Services.APIService;
import com.example.hrm.databinding.FragmentSelfPreviewBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReviewForStaff#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReviewForStaff extends Fragment {
    public  static final String MY_TAG= "Review For Staff";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ReviewForStaff() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelfPreviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReviewForStaff newInstance(String param1, String param2) {
        ReviewForStaff fragment = new ReviewForStaff();
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
    FragmentSelfPreviewBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentSelfPreviewBinding.inflate(inflater);
        getData();

        binding.BottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.navigation_in_progress:
                        binding.viewpager2.setCurrentItem(0);
                        break;
                    case R.id.navigation_finished:
                        binding.viewpager2.setCurrentItem(1);
                        break;
                }
                return true;
            }
        });
        binding.viewpager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position){
                    case 0:
                        binding.BottomNavigationView.getMenu().findItem(R.id.navigation_in_progress).setChecked(true);
                        break;
                    case 1:
                        binding.BottomNavigationView.getMenu().findItem(R.id.navigation_finished).setChecked(true);
                        break;
                    default:
                        binding.BottomNavigationView.getMenu().findItem(R.id.navigation_in_progress).setChecked(true);
                        break;
                }

            }
        });
        return binding.getRoot();
    }
    SelfPreviewViewpagerAdapter adapter;
    public void getData() {

        Call<DataResponseList<DatumTemplate<PerformanceAttributes>>> call = APIService.getService().getPaFormsByMyReviewed(Common.getToken());
        call.enqueue(new Callback<DataResponseList<DatumTemplate<PerformanceAttributes>>>() {
            @Override
            public void onResponse(Call<DataResponseList<DatumTemplate<PerformanceAttributes>>> call, Response<DataResponseList<DatumTemplate<PerformanceAttributes>>> response) {
                if(response.isSuccessful()){
                    adapter=new SelfPreviewViewpagerAdapter(getActivity(),response.body().getData(),false,true);
                    binding.viewpager2.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<DataResponseList<DatumTemplate<PerformanceAttributes>>> call, Throwable t) {

            }
        });
    }


}