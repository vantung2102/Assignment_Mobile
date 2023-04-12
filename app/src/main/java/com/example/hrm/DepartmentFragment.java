package com.example.hrm;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Message;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hrm.Adapters.DepartmentAdapter;
import com.example.hrm.Interfaces.Toastable;
import com.example.hrm.Response.AddResponse;
import com.example.hrm.Response.Attributes;
import com.example.hrm.Response.Data;
import com.example.hrm.Response.DataListHasMetaResponse;
import com.example.hrm.Response.DataResponseList;
import com.example.hrm.Response.Datum;
import com.example.hrm.Response.DatumTemplate;
import com.example.hrm.Response.Department;
import com.example.hrm.Response.StaffLeaveAttributes;
import com.example.hrm.Services.APIService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DepartmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DepartmentFragment extends Fragment implements Toastable {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public  static final String MY_TAG= "DepartmentFragment";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DepartmentFragment() {
        // Required empty public constructor
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
    public static DepartmentFragment newInstance(String param1, String param2) {
        DepartmentFragment fragment = new DepartmentFragment();
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
    private View mView;
    private RecyclerView rcvDes;
    private LinearLayout btn_add;
    List<Attributes> desDepartments=new ArrayList<>();
    DepartmentAdapter departmentAdapter;
    int currentPage=1;
    boolean isLoading = false;
    int lastPage=1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        if(getArguments()!=null){
            try {
                boolean isSuccess=getArguments().getBoolean("isSuccess");
                if(isSuccess) {
                    ((HomeActivity)getActivity()).showToast(true,"Create staff success!");
                    Log.d("getArguments","isSuccess");
                }
                else {
                    ((HomeActivity)getActivity()).showToast(false,"Create staff failed!");
                    Log.d("getArguments","!isSuccess");
                }
            } catch (Exception e){

            }

        } else Log.d("getArguments","null");
        mView=inflater.inflate(R.layout.fragment_department, container, false);
        rcvDes=mView.findViewById(R.id.rcv_departments);
        btn_add=mView.findViewById(R.id.btn_add);
        departmentAdapter=new DepartmentAdapter(Constant.DEPARTMENT_TYPE);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        rcvDes.addItemDecoration(dividerItemDecoration);
        rcvDes.setAdapter(departmentAdapter);
        rcvDes.setLayoutManager(linearLayoutManager);
        getData();
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getContext(), "btn_add", Toast.LENGTH_SHORT).show();
                    showFormAddDepartment();
            }
        });
        initScrollListener();
        return mView;
    }
    private void initScrollListener() {
        rcvDes.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == desDepartments.size() - 1&& (currentPage<lastPage))  {
                        //bottom of list!
                        loadMore(currentPage+1);
                        currentPage+=1;
                        isLoading = true;
                    }
                }
            }
        });

    }
    private void loadMore(int pageNum) {
        Log.d("loadMore",String.valueOf(pageNum));
        desDepartments.add(null);
        departmentAdapter.notifyItemInserted(desDepartments.size() - 1);
        Call<DataListHasMetaResponse<DatumTemplate<Attributes>>> call= APIService.getService().getDepartmentByPageNum(Common.getToken(),pageNum);
        call.enqueue(new Callback<DataListHasMetaResponse<DatumTemplate<Attributes>>>() {
            @Override
            public void onResponse(Call<DataListHasMetaResponse<DatumTemplate<Attributes>>> call, Response<DataListHasMetaResponse<DatumTemplate<Attributes>>> response) {
                DataListHasMetaResponse res= response.body();
                Log.d("onResponse","onResponse szie"+res.getData().size());
                //
                desDepartments.remove(desDepartments.size() - 1);
                int scrollPosition = desDepartments.size();
                departmentAdapter.notifyItemRemoved(scrollPosition);
                //
                res.getData().forEach(item->{
                    DatumTemplate<Attributes> datumTemplate= (DatumTemplate<Attributes>) item;
                    Attributes s=datumTemplate.getAttributes();
                    //Log.d("StaffLeaveAttributes",s.toString());
                    desDepartments.add(s);
                });
                departmentAdapter.notifyDataSetChanged();
                isLoading = false;
            }

            @Override
            public void onFailure(Call<DataListHasMetaResponse<DatumTemplate<Attributes>>> call, Throwable t) {
                Log.d("onFailure",t.getMessage());
            }
        });
    }
    private void showFormAddDepartment() {
        final View view = LayoutInflater.from(getContext()).inflate(R.layout.add_department_dialog, null);
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("Add Department");
        alertDialog.setIcon(R.drawable.add);
        alertDialog.setCancelable(true);
//        alertDialog.setMessage("Your Message Here");


        final EditText edt_name = (EditText) view.findViewById(R.id.edt_name);
        edt_name.setText("");
        final EditText edt_des = (EditText) view.findViewById(R.id.edt_des);
        edt_des.setText("");
        TextView txtMessName=view.findViewById(R.id.txt_mess_name);
        TextView txtMessDes=view.findViewById(R.id.txt_mess_des);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,"ADD", (DialogInterface.OnClickListener) null);

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });



        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String desName=edt_name.getText().toString().trim();
                        String desdes=edt_des.getText().toString().trim();
                        if(TextUtils.isEmpty(desName)){
                            txtMessName.setText("Name is required");
                            txtMessName.setVisibility(View.VISIBLE);
                            return;
                        } else if(TextUtils.isEmpty(desdes)){
                            txtMessName.setVisibility(View.GONE);
                            txtMessDes.setVisibility(View.VISIBLE);
                            txtMessDes.setText("Description is required");
                            return;
                        }
                        else {
                            txtMessDes.setVisibility(View.GONE);
                            txtMessName.setVisibility(View.GONE);
                        }
                        Map<String, Object> jsonParams = new ArrayMap<>();
                        jsonParams.put("description", desdes);
                        jsonParams.put("name", desName);
                        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(jsonParams)).toString());
                        Call<JsonObject> call = APIService.getServiceJson().addDepartment(body, Common.getToken());

                        Response res= null;
                        try {
                            res = call.execute();
                            Gson gson= (new GsonBuilder()).setPrettyPrinting().create();
                            JsonParser parser = new JsonParser();
                            if(res.isSuccessful()){

                                DatumTemplate<Department> d = new DatumTemplate<Department>();

                                JSONObject myResponse = new JSONObject(res.body().toString());
//                        DatumTemplate<Department> department=gson.fromJson(res.body().toString(),d.getClass());
                                JsonObject object = (JsonObject) parser.parse(res.body().toString());// response will be the json String
                                Log.d("eeeeeeeestr",res.body().toString());
                                DatumTemplate<Attributes> emp = gson.fromJson(object.get("data"), new TypeToken<DatumTemplate<Attributes>>() {}.getType());
                                Attributes department=emp.getAttributes();
                                desDepartments.add(0,department);
                                departmentAdapter.notifyDataSetChanged();
                                Log.d("eeeeeeee",emp.toString());
                                alertDialog.dismiss();
                                //Toast.makeText(getContext(), "Add department success!", Toast.LENGTH_SHORT).show();
                                ((HomeActivity)getActivity()).showToast(true,"Create Department success!");
                            }
                            else {
                                alertDialog.dismiss();
//                                Log.d("RETROFIT_ERROR :", String.valueOf(res.code()));
//                                JSONObject jsonObject = null;
//                                jsonObject = new JSONObject(res.errorBody().string());
//                                JSONArray jsonArray=jsonObject.getJSONArray("errors");
//                                Log.d("RETROFIT_ERROR_detail :", String.valueOf(jsonArray.getJSONObject(0).get("detail")));
                                //Toast.makeText(getContext(), String.valueOf(jsonArray.getJSONObject(0).get("detail")), Toast.LENGTH_SHORT).show();
                                ((HomeActivity)getActivity()).showToast(false,"Create Department failed!");
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        });
        alertDialog.setView(view);
        alertDialog.show();
    }

    private void getData() {
        Call<DataListHasMetaResponse<DatumTemplate<Attributes>>> call=APIService.getService().getDepartmentByPageNum(Common.getToken(),1);
        call.enqueue(new Callback<DataListHasMetaResponse<DatumTemplate<Attributes>>>() {
            @Override
            public void onResponse(Call<DataListHasMetaResponse<DatumTemplate<Attributes>>> call, Response<DataListHasMetaResponse<DatumTemplate<Attributes>>> response) {
                Log.d("getData",response.toString());
                DataListHasMetaResponse res= response.body();
                //departmentAdapter.setData(response.body());
                Log.d("getData", String.valueOf(response.body().getData().size()));
                res.getData().forEach(item->{
                    DatumTemplate<Attributes> datumTemplate= (DatumTemplate<Attributes>) item;
                    Attributes s=datumTemplate.getAttributes();
                    //Log.d("StaffLeaveAttributes",s.toString());
                    desDepartments.add(s);
                });
                departmentAdapter.setData(desDepartments,getContext(), (HomeActivity) getActivity());
                lastPage=res.getMeta().getLastPage();
            }

            @Override
            public void onFailure(Call<DataListHasMetaResponse<DatumTemplate<Attributes>>> call, Throwable t) {
                Log.d("getData",t.getMessage());
            }
        });
    }

    @Override
    public void showToast(boolean success, String content) {

    }
}