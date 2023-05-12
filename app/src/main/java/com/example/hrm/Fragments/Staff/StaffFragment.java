package com.example.hrm.Fragments.Staff;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hrm.Adapters.UserAdapter;
import com.example.hrm.Common;
import com.example.hrm.Fragments.Home.HomeActivity;
import com.example.hrm.Helpler.Helper;
import com.example.hrm.R;
import com.example.hrm.Response.Attributes;
import com.example.hrm.Response.DataResponseList;
import com.example.hrm.Response.DataStaff;
import com.example.hrm.Response.DatumStaff;
import com.example.hrm.Response.DatumTemplate;
import com.example.hrm.Response.StaffAttributes;
import com.example.hrm.Services.APIService;
import com.example.hrm.ViewModel.StaffInActiveShareViewModel;
import com.example.hrm.ViewModel.StaffShareViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StaffFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StaffFragment extends Fragment implements StaffFragmentView {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public  static final String MY_TAG= "Staff";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StaffFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        Log.d("StaffFragment","onResume");

        super.onResume();
//        getParentFragmentManager().setFragmentResultListener("staffFragment",getActivity(),(requestKey, result) -> {
//            StaffAttributes staff = (StaffAttributes) result.getSerializable("staff");
//            Log.d("StaffFragment : StaffResult: ",staff.toString());
//            if(staff!=null){
//                users.add(0,new DatumStaff(staff));
//                userAdapter.notifyDataSetChanged();
//                ((HomeActivity)getActivity()).showToast(true,"Create Staff Success!");
//                Log.d("StaffFragment : StaffResult: ","Success");
//            } else {
//                ((HomeActivity)getActivity()).showToast(true,"Create Staff Failed!");
//                Log.d("StaffFragment : StaffResult: ","Failed");
//            }
//        });
    }

    @Override
    public void onPause() {
        Log.d("StaffFragment","onPause");
        super.onPause();
    }

    @Override
    public void onDestroy() {
        Log.d("StaffFragment","onDestroy");
        super.onDestroy();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FirstFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StaffFragment newInstance(String param1, String param2) {
        StaffFragment fragment = new StaffFragment();
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
        StaffShareViewModel staffShareViewModel = new ViewModelProvider(getActivity()).get(StaffShareViewModel.class);
        staffShareViewModel.getStaff().observe(getActivity(),staff -> {
            if(staff!=null){
                users.add(0,new DatumStaff(staff));
                userAdapter.notifyItemInserted(0);
                ((HomeActivity)getActivity()).showToast(true,"staff success!");
            } else  ((HomeActivity)getActivity()).showToast(false,"staff failed!");


        });
        StaffInActiveShareViewModel staffInActiveShareViewModel = new ViewModelProvider(getActivity()).get(StaffInActiveShareViewModel.class);
        staffInActiveShareViewModel.getLeaveApp().observe(getActivity(),staff -> {
            if(staff!=null&&users!=null&&userAdapter!=null){
                Log.d("notifyItemRemovedS",String.valueOf(users.size()));
                users.remove(staff.getPositon());
                Log.d("notifyItemRemovedE",String.valueOf(users.size()));
                userAdapter.notifyItemRemoved(staff.getPositon());
                Log.d("notifyItemRemoved",String.valueOf(staff.getPositon())+staff.getLeaveApp().getFullname()+ users.get(staff.getPositon()).getAttributes().getFullname());
            } else {

            }

        });


    }
    private View mView;
    private RecyclerView rcvDes;
    private LinearLayout btn_add;
    private Button btnSearch;
    private EditText edtStaffName;
    UserAdapter userAdapter;
    private AutoCompleteTextView departments;
    private AutoCompleteTextView jobtitles;
    private AutoCompleteTextView positions;
    ArrayAdapter<String> departmentAdapter,jobtitleAdapter,positionAdapter;
    List<DatumTemplate<Attributes>> departmentsAtts,jobtitlesAtts,positionsAtts;
    List<DatumTemplate<StaffAttributes>> staffAtts;
    String[] departmentNames,jobtitleNames,positionNames,staffNames;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("StaffFragment","onViewCreated");
// set your restored data to your view
        getData();
     }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d("StaffFragment :","onSaveInstanceState");

        super.onSaveInstanceState(outState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mView=inflater.inflate(R.layout.fragment_staff, container, false);
        rcvDes=mView.findViewById(R.id.rcv_departments);
        btn_add=mView.findViewById(R.id.btn_add);
        userAdapter=new UserAdapter(this);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        rcvDes.addItemDecoration(dividerItemDecoration);
        rcvDes.setAdapter(userAdapter);
        rcvDes.setLayoutManager(linearLayoutManager);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getContext(), "btn_add", Toast.LENGTH_SHORT).show();
                    showFormAddDepartment(null);
            }
        });
        edtStaffName=mView.findViewById(R.id.edtStaffName);
        btnSearch=mView.findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String staffName=edtStaffName.getText().toString();
                String desName=departments.getText().toString();
                String posName=positions.getText().toString();
                String jobName=jobtitles.getText().toString();
                Toast.makeText(getContext(), desName+" "+posName+" "+jobName, Toast.LENGTH_SHORT).show();
                if(!staffName.equals("")||!desName.equals("")||!posName.equals("")||!jobName.equals("")){
                    userAdapter.showLike(staffName,desName,posName,jobName);
              } else {
                    userAdapter.showAll();
                }
            }
        });
        departments=mView.findViewById(R.id.edtDepartments);
        jobtitles=mView.findViewById(R.id.edtJobtitle);
        positions=mView.findViewById(R.id.edtPositions);
        departments.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                departments.showDropDown();
                return false;
            }
        });
        departments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("itemclick",departmentsAtts.get(i).getAttributes().getName());
                Helper.closeKeyboard(getActivity());
            }
        });

        jobtitles.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                jobtitles.showDropDown();
                return false;
            }
        });
        jobtitles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Log.d("itemclick",jobtitlesAtts.get(i).getAttributes().getName());
                Helper.closeKeyboard(getActivity());
            }
        });

        positions.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                positions.showDropDown();
                return false;
            }
        });
        positions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("itemclick",positionsAtts.get(i).getAttributes().getName());
                Helper.closeKeyboard(getActivity());
            }
        });
        Log.d("StaffFragment","onCreateView");
        return mView;
    }

    @Override
    public void onDestroyView() {
        StaffShareViewModel staffShareViewModel = new ViewModelProvider(getActivity()).get(StaffShareViewModel.class);
        staffShareViewModel.getStaff().observe(getActivity(),staff -> {
        });
        Log.d("StaffFragment","onDestroyView");
        Bundle savedState=new Bundle();
        if (departmentsAtts!=null){
            savedState.putSerializable("departmentsAtts", (Serializable) departmentsAtts);
        }
        if (jobtitlesAtts!=null){
            savedState.putSerializable("jobtitlesAtts", (Serializable) jobtitlesAtts);
        }
        if (positionsAtts!=null){
            savedState.putSerializable("positionsAtts", (Serializable) positionsAtts);
        }
        if (staffAtts!=null){
            savedState.putSerializable("staffAtts", (Serializable) staffAtts);
        }
        if (departmentNames!=null){
            savedState.putStringArray("departmentNames",departmentNames);
        }
        if (jobtitleNames!=null){
            savedState.putStringArray("jobtitleNames",jobtitleNames);
        }
        if (positionNames!=null){
            savedState.putStringArray("positionNames",positionNames);
        }
        if (staffNames!=null){
            savedState.putStringArray("staffNames",staffNames);
        }
        if (getArguments()==null){
            setArguments(new Bundle());
        }
        getArguments().putBundle("saved_state",savedState);
        super.onDestroyView();
    }

    @Override
    public void showFormAddDepartment(StaffAttributes staffAttributes) {
            HomeActivity homeActivity= (HomeActivity) getActivity();
        homeActivity.addOrRemoveBackButton(true);
            NewEmployeeFragment fragment=new NewEmployeeFragment(staffAttributes);
            final Bundle args = new Bundle();
            if(staffAttributes!=null) args.putString("TAG", "Update Employee"); else args.putString("TAG", NewEmployeeFragment.MY_TAG);
            args.putSerializable("departmentAtts", (Serializable) departmentsAtts);
            args.putSerializable("jobtitleAtts", (Serializable) jobtitlesAtts);
            args.putSerializable("positionAtts", (Serializable) positionsAtts);
        args.putSerializable("staffAtts", (Serializable) staffAtts);
            args.putStringArray("departmentNames", departmentNames);
            // Log.d("positionNames put", String.valueOf(departmentNames.length));
            args.putSerializable("jobtitleNames", jobtitleNames);
            args.putSerializable("positionNames", positionNames);
            args.putSerializable("staffNames", staffNames);
            fragment.setArguments(args);
            homeActivity.relaceFragment(fragment);
    }
    List<DatumStaff> users=new ArrayList<>();
    private void getData() {
        Call<DataStaff> call=APIService.getService().get_all_staff(Common.getToken());
        call.enqueue(new Callback<DataStaff>() {
            @Override
            public void onResponse(Call<DataStaff> call, Response<DataStaff> response) {
                Log.d("getData",response.toString());
                //departmentAdapter.setData(response.body());
                Log.d("getData", String.valueOf(response.body().getData().size()));
                users= response.body().getData();
                userAdapter.setData(users,getContext(),(HomeActivity) getActivity());
            }

            @Override
            public void onFailure(Call<DataStaff> call, Throwable t) {
                Log.d("getData",t.getMessage());
            }
        });
        Call<DataResponseList<DatumTemplate<Attributes>>> call2=APIService.getService().getAllDepartments(Common.getToken());
        call2.enqueue(new Callback<DataResponseList<DatumTemplate<Attributes>>>() {
            @Override
            public void onResponse(Call<DataResponseList<DatumTemplate<Attributes>>> call, Response<DataResponseList<DatumTemplate<Attributes>>> response) {
                Log.d("getData",response.toString());
                //departmentAdapter.setData(response.body());
                Log.d("getData", String.valueOf(response.body().getData().size()));
                departmentNames=new String[response.body().getData().size()];
                departmentsAtts= response.body().getData();
                departmentAdapter=new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, departmentNames);
                for(int i=0;i<departmentsAtts.size();i++){
                    departmentNames[i]=departmentsAtts.get(i).getAttributes().getName();
                }

                departments.setAdapter(departmentAdapter);

            }

            @Override
            public void onFailure(Call<DataResponseList<DatumTemplate<Attributes>>> call, Throwable t) {
                Log.d("getData",t.getMessage());
            }
        });
        //
        Call<DataResponseList<DatumTemplate<Attributes>>> call3=APIService.getService().getAllJobTitles(Common.getToken());
        call3.enqueue(new Callback<DataResponseList<DatumTemplate<Attributes>>>() {
            @Override
            public void onResponse(Call<DataResponseList<DatumTemplate<Attributes>>> call, Response<DataResponseList<DatumTemplate<Attributes>>> response) {
                Log.d("getData",response.toString());
                //departmentAdapter.setData(response.body());
                Log.d("getData", String.valueOf(response.body().getData().size()));
                jobtitleNames=new String[response.body().getData().size()];
                jobtitlesAtts= response.body().getData();
                jobtitleAdapter=new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, jobtitleNames);
                for(int i=0;i<jobtitlesAtts.size();i++){
                    jobtitleNames[i]=jobtitlesAtts.get(i).getAttributes().getTitle();
                }

                jobtitles.setAdapter(jobtitleAdapter);

            }

            @Override
            public void onFailure(Call<DataResponseList<DatumTemplate<Attributes>>> call, Throwable t) {
                Log.d("getData",t.getMessage());
            }
        });
        Call<DataResponseList<DatumTemplate<Attributes>>> call4=APIService.getService().getAllPositions(Common.getToken());
        call4.enqueue(new Callback<DataResponseList<DatumTemplate<Attributes>>>() {
            @Override
            public void onResponse(Call<DataResponseList<DatumTemplate<Attributes>>> call, Response<DataResponseList<DatumTemplate<Attributes>>> response) {
                Log.d("getData",response.toString());
                //departmentAdapter.setData(response.body());
                Log.d("getData", String.valueOf(response.body().getData().size()));
                positionNames=new String[response.body().getData().size()];
                positionsAtts = response.body().getData();
                positionAdapter=new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, positionNames);
                for(int i=0;i<positionsAtts.size();i++){
                    positionNames[i]=positionsAtts.get(i).getAttributes().getName();
                }

                positions.setAdapter(positionAdapter);

            }

            @Override
            public void onFailure(Call<DataResponseList<DatumTemplate<Attributes>>> call, Throwable t) {
                Log.d("getData",t.getMessage());
            }
        });
        Call<DataResponseList<DatumTemplate<StaffAttributes>>> call5= APIService.getService().getAllStaff(Common.getToken());
        call5.enqueue(new Callback<DataResponseList<DatumTemplate<StaffAttributes>>>() {
            @Override
            public void onResponse(Call<DataResponseList<DatumTemplate<StaffAttributes>>> call, Response<DataResponseList<DatumTemplate<StaffAttributes>>> response) {
                Log.d("getAllStaff","onResponse");
                List<StaffAttributes> staffAttributes=new ArrayList<>();

                DataResponseList<DatumTemplate<StaffAttributes>> res=response.body();
                staffAtts=res.getData();
                staffNames=new String[res.getData().size()];
                for(int i=0;i<res.getData().size();i++){
                    StaffAttributes att=res.getData().get(i).getAttributes();
                    staffNames[i]=att.getFullname();
                    staffAttributes.add(att);
                }
                Common.setStaffs(staffAttributes);
                Common.setStaffNames(staffNames);
                Log.d("names",staffNames[0]);
            }

            @Override
            public void onFailure(Call<DataResponseList<DatumTemplate<StaffAttributes>>> call, Throwable t) {
                Log.d("getAllStaff","onFailure");
            }
        });
    }
}