package com.example.hrm.Fragments.Performance;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.hrm.Adapters.PerformanceAdpater;
import com.example.hrm.Common;
import com.example.hrm.Fragments.Home.HomeActivity;
import com.example.hrm.R;
import com.example.hrm.Response.DataResponseList;
import com.example.hrm.Response.DatumTemplate;
import com.example.hrm.Response.PerformanceAttributes;
import com.example.hrm.Services.APIService;
import com.example.hrm.databinding.AddLeaveDialogBinding;
import com.example.hrm.databinding.AddPerformanceDialogBinding;
import com.example.hrm.databinding.FragmentPerformanceBinding;
import com.example.hrm.viewmodel.AddLeaveViewModel;
import com.example.hrm.viewmodel.AddPerformanceViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PerformanceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerformanceFragment extends Fragment {
    public  static final String MY_TAG= "Performance";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PerformanceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PerformanceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PerformanceFragment newInstance(String param1, String param2) {
        PerformanceFragment fragment = new PerformanceFragment();
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
    private List<PerformanceAttributes> data=new ArrayList<>();
    FragmentPerformanceBinding binding;
    private PerformanceAdpater adpater;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentPerformanceBinding.inflate(inflater);
        adpater=new PerformanceAdpater();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        binding.rcvStaff.setLayoutManager(linearLayoutManager);
        binding.rcvStaff.setAdapter(adpater);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);
        binding.rcvStaff.addItemDecoration(dividerItemDecoration);
        getData();
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddForm();
            }
        });
        binding.boxEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endPerformance();
            }
        });
        return binding.getRoot();
    }

    private void endPerformance() {
        JSONObject parent=new JSONObject();
        JSONObject child=new JSONObject();
        try {
            child.put("active",false);
            parent.put("data",child);
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), parent.toString());
            Call<ResponseBody> call = APIService.getService().endPerformance(Common.getToken(),body);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()){
                        data=new ArrayList<>();
                        updateView();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    private void showAddForm() {
        AlertDialog alertDialog=new AlertDialog.Builder(getContext()).create();
        AddPerformanceDialogBinding binding = AddPerformanceDialogBinding.inflate(LayoutInflater.from(getContext()));
        alertDialog.setView(binding.getRoot());
        AddPerformanceViewModel viewModel=new AddPerformanceViewModel();
        binding.setAddLeaveViewModel(viewModel);
        binding.idEdtStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        String date =  (++i1) +"-" +i2 + "-" + i;
                        binding.idEdtStartDate.setText(date);
                    }
                }, LocalDate.now().getYear(),LocalDate.now().getMonth().getValue()-1,LocalDate.now().getDayOfMonth());

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });
        binding.idEdtStartDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                binding.idEdtEndDate.setText("");
            }
        });
        Calendar calendar2 = Calendar.getInstance();
        binding.idEdtEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int fromM,fromD,fromY;
                String dateFrom=binding.idEdtStartDate.getText().toString();
                String[] tmp=dateFrom.split("-");
                fromM= Integer.parseInt(tmp[0]);
                fromD= Integer.parseInt(tmp[1]);
                fromY= Integer.parseInt(tmp[2]);
                DatePickerDialog datePickerDialog=new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        String date =  (++i1) +"-" +i2 + "-" + i;
                        binding.idEdtEndDate.setText(date);
                    }
                },fromY,fromM-1,fromD);

                calendar2.set(Calendar.SECOND, 0);
                calendar2.set(Calendar.MINUTE, 13);
                calendar2.set(Calendar.HOUR, 7);
                calendar2.set(Calendar.AM_PM, Calendar.AM);
                calendar2.set(Calendar.MONTH, fromM-1);
                calendar2.set(Calendar.DAY_OF_MONTH, fromD);
                calendar2.set(Calendar.YEAR, fromY);
                datePickerDialog.getDatePicker().setMinDate(calendar2.getTimeInMillis());
                Log.d("calendar", String.valueOf(calendar2.getTime()));
                datePickerDialog.show();
            }
        });
        binding.idEdtEndDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //asign data to 2 radio button
                int fromM,fromD,fromY;
                int fromM2,fromD2,fromY2;
                String dateFrom=binding.idEdtStartDate.getText().toString();
                if(dateFrom.equals("")) return;
                String[] tmp=dateFrom.split("-");
                fromM= Integer.parseInt(tmp[0]);
                fromD= Integer.parseInt(tmp[1]);
                fromY= Integer.parseInt(tmp[2]);
                String dateTo=binding.idEdtEndDate.getText().toString();
                if(dateTo.equals("")) return;
                String[] tmp2=dateTo.split("-");
                fromM2= Integer.parseInt(tmp2[0]);
                fromD2= Integer.parseInt(tmp2[1]);
                fromY2= Integer.parseInt(tmp2[2]);
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MINUTE, 13);
                calendar.set(Calendar.HOUR, 7);
                calendar.set(Calendar.AM_PM, Calendar.AM);
                calendar.set(Calendar.MONTH, fromM-1);
                calendar.set(Calendar.DAY_OF_MONTH, fromD);
                calendar.set(Calendar.YEAR, fromY);
                viewModel.setCalendarStart(calendar);
                //
                Calendar calendar2 = Calendar.getInstance();
                calendar2.set(Calendar.SECOND, 0);
                calendar2.set(Calendar.MINUTE, 13);
                calendar2.set(Calendar.HOUR, 7);
                calendar2.set(Calendar.AM_PM, Calendar.AM);
                calendar2.set(Calendar.MONTH, fromM2-1);
                calendar2.set(Calendar.DAY_OF_MONTH, fromD2);
                calendar2.set(Calendar.YEAR, fromY2);
                viewModel.setCalendarEnd(calendar2);

// Calculate the number of days between dates

            }
        });
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(viewModel.checkAll()){
                   SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    String dateS=format.format(viewModel.getCalendarStart().getTime());
                   String dateE=format.format(viewModel.getCalendarEnd().getTime());
                    Log.d("TimeS",dateS);
                   Log.d("TimeE",dateE);
                   JSONObject parent=new JSONObject();
                   JSONObject child=new JSONObject();
                   try {
                       child.put("end_date",dateE);
                       child.put("start_date",dateS);
                       parent.put("pa_form",child);
                       RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), parent.toString());
                       Call<ResponseBody> call = APIService.getService().addPerformance(Common.getToken(), body);
                       Response<ResponseBody> res = call.execute();
                       if(res.isSuccessful()){
                           Log.d("pa_form","isSuccessful");
                           getData();
                       }
                       alertDialog.dismiss();
                   } catch (JSONException e) {

                   } catch (IOException e) {
                       throw new RuntimeException(e);
                   }
               }
            }
        });
        binding.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();

    }

    private void getData() {
        Call<DataResponseList<DatumTemplate<PerformanceAttributes>>> call = APIService.getService().getAllPerformanceAppraisalForms(Common.getToken());
        call.enqueue(new Callback<DataResponseList<DatumTemplate<PerformanceAttributes>>>() {
            @Override
            public void onResponse(Call<DataResponseList<DatumTemplate<PerformanceAttributes>>> call, Response<DataResponseList<DatumTemplate<PerformanceAttributes>>> response) {
                if(response.isSuccessful()){
                    response.body().getData().forEach(item->{data.add(item.getAttributes());});
                    adpater.setData(data, (HomeActivity) getActivity());
                    updateView();
                }
            }

            @Override
            public void onFailure(Call<DataResponseList<DatumTemplate<PerformanceAttributes>>> call, Throwable t) {

            }
        });
    }

    private void updateView() {
        if(data.isEmpty()){
            binding.boxStart.setVisibility(View.VISIBLE);
            binding.boxEnd.setVisibility(View.GONE);
            binding.header.setVisibility(View.GONE);
            binding.rcvStaff.setVisibility(View.GONE);
        } else {
            binding.boxStart.setVisibility(View.GONE);
            binding.boxEnd.setVisibility(View.VISIBLE);
            binding.header.setVisibility(View.VISIBLE);
            binding.txtStartDay.setText(data.get(0).getStartDate());
            binding.txtEndDay.setText(data.get(0).getEndDate());
            binding.rcvStaff.setVisibility(View.VISIBLE);
        }
    }
}