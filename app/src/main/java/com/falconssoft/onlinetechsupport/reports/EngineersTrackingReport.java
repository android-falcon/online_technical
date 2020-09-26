package com.falconssoft.onlinetechsupport.reports;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.falconssoft.onlinetechsupport.Modle.ManagerLayout;
import com.falconssoft.onlinetechsupport.PresenterClass;
import com.falconssoft.onlinetechsupport.PresenterInterface;
import com.falconssoft.onlinetechsupport.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EngineersTrackingReport extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EngineersTrackingAdapter adapter;
    private TextView listSize;
    public static List<ManagerLayout> engineerTrackingList = new ArrayList<>();
    public static List<ManagerLayout> childList = new ArrayList<>();

    private PresenterClass presenterClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_engineers_tracking_report);

        listSize = findViewById(R.id.tracking_report_count);
        recyclerView = findViewById(R.id.tracking_report_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        presenterClass = new PresenterClass(this);
        presenterClass.getTrackingEngineerReportData(this, -1, -1);// when child

    }

    public void engineersTrackingReportFilter() {
        listSize.setText("" + engineerTrackingList.size());
        adapter = new EngineersTrackingAdapter(this);
        recyclerView.setAdapter(adapter);
    }

    public void getChildData(int index) {
        presenterClass.getTrackingEngineerReportData(this, Integer.parseInt(engineerTrackingList.get(index).getSerial()), index);
    }

    public void fillChildData(List<ManagerLayout> list) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.tracking_dialog);

        Log.e("size", "" + list.size());
        TableLayout tableLayout = dialog.findViewById(R.id.engineersTrackingDialog_children_table);

        for (int i = 0; i < list.size(); i++) {
            TableRow tableRow = new TableRow(this);

            if (list.get(i).getState().equals("1")) {// in service/ check in
                tableRow.setBackgroundColor(getResources().getColor(R.color.greenColor2));

            } else if (list.get(i).getState().equals("2")) {// check out
                tableRow.setBackgroundColor(getResources().getColor(R.color.light_blue_over));
            }

            for (int k = 0; k < 10; k++) {
                TextView textView = new TextView(this);
                textView.setTextSize(14);
                textView.setGravity(Gravity.CENTER);
                textView.setTextColor(ContextCompat.getColor(this, R.color.black));
                textView.setVerticalScrollBarEnabled(true);
                textView.setMovementMethod(new ScrollingMovementMethod());
                textView.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);

                TableRow.LayoutParams param1 = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT,1f);
                textView.setPadding(0, 5, 0, 5);

                TableRow.LayoutParams param2 = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT,2f);
                textView.setPadding(0, 5, 0, 5);

                switch (k) {
                    case 0:
//                        textView.setBackgroundColor(getResources().getColor(R.color.greenColor2));
                        textView.setLayoutParams(param1);
                        textView.setText(list.get(i).getEnginerName());
                        break;
                    case 1:
//                        textView.setBackgroundColor(getResources().getColor(R.color.light_blue_over));
                        textView.setLayoutParams(param1);
                        textView.setText(list.get(i).getSystemName());
                        break;
                    case 2:
//                        textView.setBackgroundColor(getResources().getColor(R.color.greenColor2));
                        textView.setLayoutParams(param2);
                        textView.setText(list.get(i).getProplem());
                        break;
                    case 3:
//                        textView.setBackgroundColor(getResources().getColor(R.color.light_blue_over));
                        textView.setLayoutParams(param1);
                        textView.setText(list.get(i).getCustomerName());
                        break;
                    case 4:
//                        textView.setBackgroundColor(getResources().getColor(R.color.greenColor2));
                        textView.setLayoutParams(param1);
                        textView.setText(list.get(i).getPhoneNo());
                        break;
                    case 5:
//                        textView.setBackgroundColor(getResources().getColor(R.color.light_blue_over));
                        textView.setLayoutParams(param1);
                        textView.setText(list.get(i).getCompanyName());
                        break;
                    case 6:
//                        textView.setBackgroundColor(getResources().getColor(R.color.greenColor2));
                        textView.setLayoutParams(param1);
                        textView.setText(list.get(i).getCheakInTime());
                        break;
                    case 7:
                        textView.setBackgroundColor(getResources().getColor(R.color.light_blue_over));
                        textView.setLayoutParams(param1);
                        textView.setText(list.get(i).getCheakOutTime());
                        break;
                    case 8:
//                        textView.setBackgroundColor(getResources().getColor(R.color.greenColor2));
                        textView.setLayoutParams(param1);
                        textView.setText(list.get(i).getHoldTime());
                        break;
                    case 9:
//                        textView.setBackgroundColor(getResources().getColor(R.color.light_blue_over));
                        textView.setLayoutParams(param1);
                        textView.setText(list.get(i).getTransactionDate());
                        break;
                }

                tableRow.addView(textView);
            }
            tableLayout.addView(tableRow);
            TableRow emptyTableRow = new TableRow(this);
            TableRow.LayoutParams tableRowParam = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 100);
            TextView emptyTextView = new TextView(this);
            emptyTableRow.setLayoutParams(tableRowParam);
            emptyTableRow.addView(emptyTextView);
            tableLayout.addView(emptyTableRow);
        }
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

}