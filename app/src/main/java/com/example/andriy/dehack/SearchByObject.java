package com.example.andriy.dehack;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by Andriy on 17.02.2018.
 */

public class SearchByObject extends Fragment {

    View view;
    ListView listView;
    LinearLayout buttonSearch;
    AutoCompleteTextView editObject;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.content_search_by_object, container, false);
        listView = (ListView) view.findViewById(R.id.dataByObject);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, OBJECTS);
        editObject = (AutoCompleteTextView) view.findViewById(R.id.editObject);
        editObject.setAdapter(adapter);
        buttonSearch = (LinearLayout) view.findViewById(R.id.buttonSearch);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editObject.setVisibility(View.INVISIBLE);
                buttonSearch.setVisibility(View.INVISIBLE);
                getData();
            }
        });


        return view;
    }

    public void getData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.spending.gov.ua/api/v2/api/transactions/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServerApi messagesApi = retrofit.create(ServerApi.class);
        final Call<List<Message>> messages = messagesApi.getProducts();

        messages.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                if (response.isSuccessful()) {

                    Log.d("ok", "response " + response.body().get(0).getPayer_bank());
                    ArrayList<DataClass> arrayList = new ArrayList<>();
                    for (int i = 0; i < response.body().size(); i++) {
                        arrayList.add(new DataClass(response.body().get(i).getPayment_details(), response.body().get(i).getAmount()));
                    }
                    DetailsCostsAdapter detailsCostsAdapter = new DetailsCostsAdapter(getActivity(), arrayList);
                    listView.setAdapter(detailsCostsAdapter);
                } else {
                    Log.d("ok", "response code " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                Log.d("bad", "failure " + t);
            }
        });
    }


    public interface ServerApi {
        @GET("?payers_edrpous=22351740")
        Call<List<Message>> getProducts();

    }

    private static final String[] OBJECTS = new String[]{"Веселівська середньо загальноосвітня школа"};

}
