package com.example.andriy.dehack;

import android.animation.ValueAnimator;
import android.app.DialogFragment;
import android.app.Fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.github.mikephil.charting.utils.ColorTemplate.COLORFUL_COLORS;
import static com.github.mikephil.charting.utils.ColorTemplate.rgb;

public class CostsGromad extends Fragment {
    CollectionReference costs;
    FirebaseFirestore firebase = FirebaseFirestore.getInstance();
    Map<String, Object> dataMap;
    TextView labelCost, labelGrivna, labelThousand, labelMillion;
    PieChart pieChart;
    View view;
    TextView text2, text3, text4;
    LinearLayout detailsCost, layoutDetails;
    public static final int[] MATERIAL_COLORS = {
            rgb("#2ecc71"), rgb("#ffeb3b"), rgb("#e74c3c"), rgb("#4fc3f7"), rgb("#00796b"), rgb("#fb8c00"), rgb("#3f51b5")
    };


    final DialogFragment loadFragment = new Load();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.content_costs_gromad, container, false);
        labelCost = (TextView) view.findViewById(R.id.labelCost);
        detailsCost = (LinearLayout) view.findViewById(R.id.detailsCosts);
        layoutDetails = (LinearLayout) view.findViewById(R.id.layoutDetails);
        layoutDetails.setVisibility(View.INVISIBLE);
        labelGrivna = (TextView) view.findViewById(R.id.labelGrivna);
        labelMillion = (TextView) view.findViewById(R.id.labeMillion);
        labelThousand = (TextView) view.findViewById(R.id.labelThousand);
        labelMillion.setVisibility(View.INVISIBLE);
        labelThousand.setVisibility(View.INVISIBLE);
        labelGrivna.setVisibility(View.INVISIBLE);

        getDataFromFirebase();


        return view;

    }

    private void setupPieChart(Map data) {
        String[] key = {};
        Set<String> keySet = data.keySet();
        key = keySet.toArray(new String[data.size()]);
        List<PieEntry> list = new ArrayList<>();
        for (int i = 0; i < keySet.size(); i++) {
            list.add(new PieEntry(Float.valueOf(data.get(key[i]).toString()), key[i]));
        }
        PieDataSet pieDataSet = new PieDataSet(list, "Витрати");
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData pieData = new PieData(pieDataSet);
        pieData.setDrawValues(false);
        pieData.setHighlightEnabled(true);
        pieChart = (PieChart) view.findViewById(R.id.chart);
        pieChart.setData(pieData);
        pieChart.setDescription(null);
        pieChart.setCenterText("Витрати загального фонду");
        pieChart.getLegend().setEnabled(false);
        pieChart.animateY(1500);
        pieChart.setDrawEntryLabels(false);
        loadFragment.dismiss();
        text2 = (TextView) view.findViewById(R.id.lichulnikMln);
        text3 = (TextView) view.findViewById(R.id.lichulnikThou);
        text4 = (TextView) view.findViewById(R.id.lichulnik);
        labelMillion.setVisibility(View.VISIBLE);
        labelThousand.setVisibility(View.VISIBLE);
        labelGrivna.setVisibility(View.VISIBLE);
        animateTextView(0, 50, text2);
        animateTextView(0, 754, text3);
        animateTextView(0, 0, text4);
        pieChart.invalidate();
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                String[] key = {};
                Set<String> keySet = dataMap.keySet();
                key = keySet.toArray(new String[dataMap.size()]);
                layoutDetails.setVisibility(View.VISIBLE);
                labelCost.setText(key[(int) h.getX()]);
                pieChart.setCenterText(key[(int) h.getX()]);
                loadDetailsAboutCosts(key[(int) h.getX()]);
            }


            @Override
            public void onNothingSelected() {
                pieChart.setCenterText("Витрати загального фонду");
                layoutDetails.setVisibility(View.INVISIBLE);
                float dp = getActivity().getResources().getDisplayMetrics().density;
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 10 * (int) dp);
                detailsCost.setLayoutParams(params);
            }
        });
    }


    public void getDataFromFirebase() {

        loadFragment.show(getFragmentManager(), "Comment");
        String path = "/data/user/0/com.example.andriy.dehack/databases/Gromad";
        SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, 0);
        Cursor c = db.query("Gromad", null, null, null, null, null, null);
        c.moveToFirst();
        try {
            costs = firebase.collection(c.getString(c.getColumnIndex("name")));
            costs.document("Витрати ЗФП").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null) {

                            dataMap = document.getData();
                            setupPieChart(dataMap);

                        } else {
                            Log.d("ok", "No such document");
                        }
                    } else {
                        Log.d("ok", "get failed with ", task.getException());
                    }
                }
            });

        } catch (Exception e) {

        }
    }

    private void loadDetailsAboutCosts(String nameCosts) {
        final CollectionReference detailsCosts = costs.document("Витрати ЗФП").collection(nameCosts);
        detailsCost.removeAllViews();
        detailsCosts.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            float dp = getActivity().getResources().getDisplayMetrics().density;
                            if (task.getResult().size() > 0) {

                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                params.setMargins(25 * (int) dp, 25 * (int) dp, 25 * (int) dp, 0);
                                params.gravity = Gravity.CENTER_HORIZONTAL;
                                for (DocumentSnapshot document : task.getResult()) {
                                    params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    detailsCost.setLayoutParams(params);
                                    TextView newTextView = new TextView(getActivity());
                                    newTextView.setGravity(Gravity.CENTER_HORIZONTAL);
                                    newTextView.setTextColor(Color.BLACK);
                                    newTextView.setText(document.getId());
                                    newTextView.setLayoutParams(params);
                                    newTextView.setTypeface(Typeface.DEFAULT_BOLD);
                                    detailsCost.addView(newTextView);
                                    addListView(document);
                                }
                            } else {
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 140 * (int) dp);

                                detailsCost.setLayoutParams(params);
                                TextView newTextView = new TextView(getActivity());
                                newTextView.setGravity(Gravity.CENTER_HORIZONTAL);
                                newTextView.setTextColor(Color.RED);
                                newTextView.setText("Немає інформації");
                                newTextView.setTypeface(Typeface.DEFAULT_BOLD);
                                params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                params.setMargins(25 * (int) dp, 25 * (int) dp, 25 * (int) dp, 0);
                                params.gravity = Gravity.CENTER_HORIZONTAL;
                                newTextView.setLayoutParams(params);
                                detailsCost.addView(newTextView);

                            }
                        } else {
                            Log.d("ok", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void addListView(DocumentSnapshot document) {
        Map data = document.getData();
        float dp = getActivity().getResources().getDisplayMetrics().density;
        ListView listView = new ListView(getActivity());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (80 * data.size() + 10) * (int) dp);
        listView.setLayoutParams(params);
        String[] key = {};
        Set<String> keySet = data.keySet();
        key = keySet.toArray(new String[data.size()]);
        ArrayList<DataClass> arrayData = new ArrayList<>();
        for (int i = 0; i < keySet.size(); i++) {
            double j = Double.valueOf(data.get(key[i]).toString());
            int k = (int) j;
            String string = "";
            if (k >= 1000000) {
                string = string + String.valueOf((k - (k % 1000000)) / 1000000) + " млн " + String.valueOf((k - (k - (k % 1000000)) - (k % 1000)) / 1000) + " тис " + String.valueOf((k - (k - (k - (k % 1000000)) - (k % 1000))) % 1000) + " грн";
            } else if (k >= 1000) {
                string = string + String.valueOf((k - (k % 1000)) / 1000) + " тис " + String.valueOf((k - (k - (k % 1000))) % 1000) + " грн";

            } else {
                string = string + String.valueOf(k) + " грн";
            }
            arrayData.add(new DataClass(key[i], string));
        }
        Log.d("ok", String.valueOf(data.size()));
        DetailsCostsAdapter adapter = new DetailsCostsAdapter(getActivity(), arrayData);
        listView.setAdapter(adapter);
        detailsCost.addView(listView);
    }


    public void animateTextView(int initialValue, int finalValue, final TextView textview) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(initialValue, finalValue);
        valueAnimator.setDuration(2000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                textview.setText(valueAnimator.getAnimatedValue().toString());
            }
        });
        valueAnimator.start();
    }


}
