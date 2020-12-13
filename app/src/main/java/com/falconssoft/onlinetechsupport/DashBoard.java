package com.falconssoft.onlinetechsupport;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.falconssoft.onlinetechsupport.Modle.ManagerLayout;
import com.falconssoft.onlinetechsupport.reports.CallCenterTrackingReport;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static com.falconssoft.onlinetechsupport.GClass.engList;
import static com.falconssoft.onlinetechsupport.GClass.engMList;
import static com.falconssoft.onlinetechsupport.GClass.engPerHourList;
import static com.falconssoft.onlinetechsupport.GClass.engPerSysList;
import static com.falconssoft.onlinetechsupport.GClass.holdCounts;
import static com.falconssoft.onlinetechsupport.GClass.inCount;
import static com.falconssoft.onlinetechsupport.GClass.listOfCallHour;
import static com.falconssoft.onlinetechsupport.GClass.listOfCallHourByEng;
import static com.falconssoft.onlinetechsupport.GClass.managerDashBord;
import static com.falconssoft.onlinetechsupport.GClass.outCount;
import static com.falconssoft.onlinetechsupport.GClass.sizeProgress;
import static com.falconssoft.onlinetechsupport.GClass.systemDashBordListByEng;
import static com.falconssoft.onlinetechsupport.GClass.systemList;
import static com.falconssoft.onlinetechsupport.GClass.systemListDashBoardSystem;
import static com.falconssoft.onlinetechsupport.GClass.systemListDashOnlyMax;
import static com.falconssoft.onlinetechsupport.MainActivity.cheakIn;
import static com.falconssoft.onlinetechsupport.MainActivity.cheakout;
import static com.falconssoft.onlinetechsupport.MainActivity.countChickHold;
import static com.falconssoft.onlinetechsupport.MainActivity.countChickIn;
import static com.falconssoft.onlinetechsupport.MainActivity.countChickOut;
import static com.falconssoft.onlinetechsupport.MainActivity.hold;
//implements OnChartValueSelectedListener
public class DashBoard extends AppCompatActivity  {
    private PieChart chart, chart1,chart2, chart3, chart4, chart5, chart6, halfChart, chartEng3,datePipChart;
    private HorizontalBarChart hBarChart,hBarChartSystem;
    private LineChart lineChart;
    private RadarChart redarChart;
    //private BarChart barChart, barChart2;
    TextView engName,sysName,doneFilter,fDate,sDate;

    private BarChart multiDataSetChart;
    private final int[] colors = new int[]{
            ColorTemplate.JOYFUL_COLORS[0],
            ColorTemplate.JOYFUL_COLORS[1],
            ColorTemplate.JOYFUL_COLORS[2]
    };
    private TextView CallNo,noOfInCaLL,noOfOutCall,noOfHoldCall,noOfEng,noOfSyste,percentEngCall,dateFilter;
    GClass gClass;
    int allCall=0;
    Timer T;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private Date date;
    int timeFlag=0;
//    List<Integer>sizeProgress;
    ManagerImport managerImport;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dash_board_activity);

        chart = findViewById(R.id.chart1);
        chart1 = findViewById(R.id.chart2);
        chart2 = findViewById(R.id.chart3);
        chart3 = findViewById(R.id.chartEng);
        chart4 = findViewById(R.id.chartEng2);
        chart5 = findViewById(R.id.chartSys);
        chart6 = findViewById(R.id.chartSys2);
        engName=findViewById(R.id.engName);
        sysName=findViewById(R.id.sysName);
//        halfChart=findViewById(R.id.halfPieChart);
        lineChart = findViewById(R.id.line_chart);
        chartEng3 = findViewById(R.id.chartEng3);
        dateFilter=findViewById(R.id.dateFilter);

        datePipChart = findViewById(R.id.datePipChart);
//        barChart = findViewById(R.id.BarChart1);
//        barChart2 = findViewById(R.id.BarChart2);
        multiDataSetChart= findViewById(R.id.BarChart2);
        redarChart=findViewById(R.id.redarChart);

        CallNo=findViewById(R.id.CallNo);
        noOfInCaLL=findViewById(R.id.inNo);
        noOfOutCall=findViewById(R.id.outNo);
        noOfHoldCall=findViewById(R.id.holdNo);
        noOfEng=findViewById(R.id.noOfEng);
        noOfSyste=findViewById(R.id.noOfSyste);
        percentEngCall=findViewById(R.id.percentEngCall);
        doneFilter=findViewById(R.id.doneFilter);
        fDate=findViewById(R.id.datePipChart1);
        sDate=findViewById(R.id.datePipChart2);

//        sizeProgress=new ArrayList<>();
        date = Calendar.getInstance().getTime();
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        fDate.setText(dateFormat.format(date));
        sDate.setText(dateFormat.format(date));
        dateFilter.setText(dateFormat.format(date));


        hBarChart = findViewById(R.id.hBarChart);
        hBarChartSystem=findViewById(R.id.hBarChartSystem);

        gClass=new GClass(null,null,DashBoard.this);
        gClass.filllistOfCallHour();
        gClass.filllistOfCallHourByEng();

        managerImport = new ManagerImport(DashBoard.this);
        managerImport.startSendingEngSys(DashBoard.this, 2);

        managerImport.dashBoardData(DashBoard.this,dateFilter.getText().toString());

//        dataCall();
        chart3.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                PieEntry pe = (PieEntry) e;
                Log.e("LABEL",pe.getLabel());

                try {
                    int index= (int) h.getX()+1;
                    engName.setText(engPerHourList.get(index).getName());
                    Toast.makeText(DashBoard.this, engPerHourList.get(index).getName()+"   master"+engPerHourList.get(index).getId()+"  "+h.getX(), Toast.LENGTH_SHORT).show();
                    managerImport.dashBoardDataByEngId(DashBoard.this, engPerHourList.get(index).getId(),dateFilter.getText().toString());



                }catch (Exception ex){
                    Toast.makeText(DashBoard.this, ex.toString()+"   Emaster"+e.getY()+"   "+e.getX()+"  "+(int)h.getX(), Toast.LENGTH_SHORT).show();
                }
//                Toast.makeText(DashBoard.this, "master"+e.getY()+"   "+e.getX()+"  "+h.getX(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        hBarChartSystem.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                int index= (int) h.getX()/10;
                Log.e("systemListDashBoard",""+index+"    "+h.getX());
//                Toast.makeText(DashBoard.this, systemListDashBoardSystem.get(index).getSystemName()+"   master"+systemListDashBoardSystem.get(index).getSystemName()+"  "+h.getX(), Toast.LENGTH_SHORT).show();
                sysName.setText(systemListDashBoardSystem.get(index).getSystemName());
                managerImport.dashBoardDataBySysId(DashBoard.this, systemListDashBoardSystem.get(index).getSystemNo(),dateFilter.getText().toString());
                Log.e("systemListDashBoardss",""+index+"    "+systemListDashBoardSystem.get(index).getSystemNo());


            }

            @Override
            public void onNothingSelected() {

            }
        });


        doneFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                managerImport.dashBoardDataByTwoDate(DashBoard.this,fDate.getText().toString(),sDate.getText().toString());

            }
        });

//        T = new Timer();
//        T.schedule(new TimerTask() {
//            @Override
//            public void run() {
//
//                gClass.filllistOfCallHour();
//                gClass.filllistOfCallHourByEng();
//                managerImport.startSendingEngSys(DashBoard.this, 2);
//                managerImport.dashBoardData(DashBoard.this,dateFilter.getText().toString());
//
//            }
////9000000
//        }, 0, 900000);


        dateFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeFlag = 0;
                new DatePickerDialog(DashBoard.this, gClass.openDatePickerDialog(timeFlag,dateFilter,dateFilter,4), calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        fDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeFlag = 0;
                new DatePickerDialog(DashBoard.this, gClass.openDatePickerDialog(timeFlag,fDate,sDate,3), calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        sDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeFlag = 1;
                new DatePickerDialog(DashBoard.this, gClass.openDatePickerDialog(timeFlag,fDate,sDate,3), calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


    }


    public void CallDataToFillDashBord (){

        gClass.filllistOfCallHour();
        gClass.filllistOfCallHourByEng();
        managerImport.startSendingEngSys(DashBoard.this, 2);
        managerImport.dashBoardData(DashBoard.this,dateFilter.getText().toString());

    }

 void dataCall(){
//     sizeProgress.clear();
//     inCount = Integer.parseInt(countChickIn.getText().toString());
//     outCount = Integer.parseInt(countChickOut.getText().toString());
//     holdCounts = Integer.parseInt(countChickHold.getText().toString());
//     sizeProgress.add(inCount);
//     sizeProgress.add(outCount);
//     sizeProgress.add(holdCounts);
     if(sizeProgress.size()!=0) {
         allCall = sizeProgress.get(0) + sizeProgress.get(1) + sizeProgress.get(2);
         int engSize = engList.size() - 1;
         double perCal =0;
         try {
              perCal = ((double) (allCall / engSize));
         }catch (Exception e){
             perCal=0;
         }

         CallNo.setText("" + allCall + " Call");


         noOfInCaLL.setText("" + sizeProgress.get(0));
         noOfOutCall.setText("" + sizeProgress.get(1));
         noOfHoldCall.setText("" + sizeProgress.get(2));
         noOfEng.setText("" + engSize + " No Of Eng");
         noOfSyste.setText("" + 20 + " System ");
         percentEngCall.setText("" + perCal + " Call For Each Eng");
     }else{
         CallNo.setText("0");


         noOfInCaLL.setText("0");
         noOfOutCall.setText("0");
         noOfHoldCall.setText("0");
         noOfEng.setText("0");
         noOfSyste.setText("0");
         percentEngCall.setText("0");
     }
 }


    void barChartFill(BarChart chart, int flag) {

        chart.getDescription().setEnabled(false);
        chart.getDescription().setTextColor(Color.WHITE);
        chart.getXAxis().setTextColor(Color.WHITE);
        chart.getAxisRight().setTextColor(Color.WHITE);
        chart.getAxisLeft().setTextColor(Color.WHITE);
        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false);

        chart.setDrawBarShadow(false);
        chart.setDrawGridBackground(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisRight().setDrawGridLines(false);
        // add a nice and smooth animation
        chart.animateY(1500);

        chart.getLegend().setEnabled(false);
        setBarChartData(chart, flag);
    }


    void setBarChartData(BarChart chart, int flag) {


        ArrayList<BarEntry> values = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            float multi = (9 + 1);
            float val = (float) (Math.random() * multi) + multi / 3;
            values.add(new BarEntry(i, val));
        }

        BarDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            set1.setDrawValues(!set1.isDrawValuesEnabled());
            set1.setValueTextColor(Color.GRAY);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(values, "Data Set");
            if (flag == 1) {
                set1.setColors(DashBoard.this.getResources().getColor(R.color.dark_blue));
            } else {
                set1.setColors(DashBoard.this.getResources().getColor(R.color.exit_hover));
            }
            set1.setDrawValues(false);
            set1.setDrawValues(!set1.isDrawValuesEnabled());
            set1.setValueTextColor(Color.GRAY);
            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            chart.setData(data);
            chart.setFitBars(true);
        }

        chart.invalidate();


    }

    public void fillLineChart(LineChart chart) {

//        chart.setOnChartValueSelectedListener(this);
        chart.getXAxis().setTextColor(Color.GRAY);
        chart.setDrawGridBackground(false);
        chart.getDescription().setEnabled(true);
        chart.getDescription().setTextColor(Color.WHITE);
        chart.getDescription().setText("Call Count Per Hour ");
        chart.getDescription().setTextSize(15f);
        chart.getDescription().setTextColor(Color.WHITE);
        chart.setDrawBorders(false);

        chart.getAxisLeft().setEnabled(false);
        chart.getAxisRight().setDrawAxisLine(false);
        chart.getAxisRight().setDrawGridLines(false);
        chart.getXAxis().setDrawAxisLine(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisRight().setTextColor(Color.GRAY);
        chart.getLegend().setTextColor(Color.GRAY);
        // enable touch gestures
        chart.setTouchEnabled(true);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);


//        chart.animateY(2500);
        chart.animateXY(2500, 2500);
        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(true);

        setLineChart(chart);

    }


    void setLineChart(LineChart chart) {

        chart.resetTracking();

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        for (int z = 0; z < listOfCallHour.size(); z++) {

            ArrayList<Entry> values = new ArrayList<>();

            for (int i = 0; i < listOfCallHour.get(z).size(); i++) {
                double val = (Math.random() * 100) + 3;
                values.add(new Entry((float) listOfCallHour.get(z).get(i).getHour(), (float) listOfCallHour.get(z).get(i).getCount()));
            }
            LineDataSet d = new LineDataSet(values, "");
            if (z == 0) {
                d = new LineDataSet(values, "In Process " );
            } else if (z == 1) {
                    d = new LineDataSet(values, "Waiting In Hold" );
            } else if (z == 2) {
                d = new LineDataSet(values, "Finish Complete" );
            }
            d.setValueTextColor(Color.WHITE);
            d.setLineWidth(2.5f);
            d.setCircleRadius(4f);
            d.setDrawFilled(true);
            int color = colors[z % colors.length];
            d.setColor(color);
            d.setCircleColor(color);
            d.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            dataSets.add(d);
        }

        // make the first DataSet dashed
//        ((LineDataSet) dataSets.get(0)).enableDashedLine(10, 10, 0);
//        ((LineDataSet) dataSets.get(0)).setColors(ColorTemplate.VORDIPLOM_COLORS);
//        ((LineDataSet) dataSets.get(0)).setCircleColors(ColorTemplate.VORDIPLOM_COLORS);

        LineData data = new LineData(dataSets);

        chart.setData(data);
        chart.invalidate();

    }
    public void fillChartPipePros() {
        EngWorkByCallPipeChart(chart, 0,"Efficiency");

    }

    public void fillChart() {

        EngWorkByCallPipeChart(chart, 0,"Efficiency");
        EngWorkByCallPipeChart(chart1, 1," % Percentage Of Calls ForEach Engineer");
        EngWorkByCallPipeChart(chart3, 3,"The Number Of Calls That The Engineer Receives");
        EngWorkByCallPipeChart(chart4, 4,"");
        EngWorkByCallPipeChart(chart5, 5,"");

        EngWorkByCallPipeChart(chartEng3, 7,"System Processing By Eng");
        HoriBarChart(hBarChart);
        HoriBarChart(hBarChartSystem);
//        setHorizontalBarChart(hBarChartSystem);
        fillLineChart(lineChart);
//        barChartFill(barChart, 0);
//        barChartFill(barChart2, 1);
        SetDataMultiChart(multiDataSetChart);
        readerChartFill(redarChart);
    }
    void fillCallCountPerHour(){
        fillLineChart(lineChart);
    }
    void fill(){
//        EngWorkByCallPipeChart(chartEng3, 7);
        SetDataMultiChart(multiDataSetChart);
    }

    void fill4(){
//        EngWorkByCallPipeChart(chartEng3, 7);
        EngWorkByCallPipeChart(chart6, 6,"Engineer Work In System");
    }

    void fill2(){
        EngWorkByCallPipeChart(chartEng3, 7,"The Systems In Which The Engineer Works");
//        SetDataMultiChart(multiDataSetChart);
    }
    void fill3(){
        HoriBarChart(hBarChartSystem);
        EngWorkByCallPipeChart(chart2,8,"Top 3 Systems Consume Connections");
    }
    void fill5(){

        EngWorkByCallPipeChart(datePipChart,9,"Compare Between Two Date");
    }


    void EngWorkByCallPipeChart(PieChart charts, int flag,String message) {
        PieChart chart=null;
        chart=charts;

        chart.setUsePercentValues(true);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);

        chart.setDragDecelerationFrictionCoef(0.95f);

//        chart.setCenterTextTypeface(Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf"));
        chart.setCenterText(generateCenterSpannableText(message));

        chart.setExtraOffsets(20.f, 0.f, 20.f, 0.f);

        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);

        chart.setDrawCenterText(true);

        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);


        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener
//        chart.setOnChartValueSelectedListener(DashBoard.this);


        chart.animateY(1400, Easing.EaseInOutQuad);
        // chart.spin(2000, 0, 360);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(false);

        boolean toSet = !chart.isDrawRoundedSlicesEnabled() || !chart.isDrawHoleEnabled();
        chart.setDrawRoundedSlices(true);
        if (toSet && !chart.isDrawHoleEnabled()) {
            chart.setDrawHoleEnabled(true);
        }
        if (toSet && chart.isDrawSlicesUnderHoleEnabled()) {
            chart.setDrawSlicesUnderHole(false);
        }




        switch (flag) {

            case 0:
                setDataProgress(3, 100, chart);
                break;

            case 1:
                setDataEngChartPerHour(10, 100, chart);
                break;
            case 2:
                setDataSystemHalf(10, 100, chart);
                break;
            case 3:
                setDataEngHour( chart);
                break;
            case 4:
                setData(10, 100, chart);
                break;
            case 6:
                setDataEngSystemDataSysID( chart);
                break;

            case 7:
                setDataEngSystem( chart);
                break;

            case 8:
                setDataEngSystemData( chart);
                break;
            case 9:
                setDataCompare( chart);
                break;


        }


    }


    void SetDataMultiChart(BarChart chart){

//        chart.setOnChartValueSelectedListener(this);
        chart.getDescription().setEnabled(false);
//        chart.getDescription().setTextSize(12);
//        chart.getDescription().setTextColor(Color.WHITE);
//        chart.getDescription().setText(" Engineer Worked Per Hour ");
//        chart.setDrawBorders(true);

        // scaling can now only be done on x- and y-axis separately"
        chart.setPinchZoom(false);

        chart.setDrawBarShadow(false);

        chart.setDrawGridBackground(false);
        chart.getLegend().setTextColor(Color.GRAY);
        chart.setDrawGridBackground(false);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
//        MarkerView mv = new MarkerView(this, R.layout.custom_marker_view);
//        mv.setChartView(chart); // For bounds control
//        chart.setMarker(mv); // Set the marker to the chart

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
//        l.setTypeface(tfLight);
        l.setYOffset(0f);
        l.setXOffset(10f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);

        XAxis xAxis = chart.getXAxis();
//        xAxis.setTypeface(tfLight);

        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.GRAY);
        xAxis.setGranularity(1f);
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.valueOf((int) value);
            }
        });

        YAxis leftAxis = chart.getAxisLeft();
//        leftAxis.setTypeface(tfLight);
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(35f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.setTextColor(DashBoard.this.getResources().getColor(R.color.darkblue_2));
        chart.animateY(2500);
        chart.getAxisRight().setEnabled(false);
        setMultiBarChartData(chart);
    }

    void setMultiBarChartData(BarChart chart){

        float groupSpace = 0.08f;
        float barSpace = 0.03f; // x4 DataSet
        float barWidth = 0.2f; // x4 DataSet
        // (0.2 + 0.03) * 4 + 0.08 = 1.00 -> interval per "group"

        int groupCount = 10;

        ArrayList<BarEntry> values1 = new ArrayList<>();
        ArrayList<BarEntry> values2 = new ArrayList<>();
        ArrayList<BarEntry> values3 = new ArrayList<>();

        float randomMultiplier = (float) (251.02 * 100000f);

        for (int i = 9; i < 18; i++) {
            values1.add(new BarEntry((float) listOfCallHourByEng.get(0).get(i).getHour(), (float) listOfCallHourByEng.get(0).get(i).getCount()));
            values2.add(new BarEntry((float) listOfCallHourByEng.get(1).get(i).getHour(), (float) listOfCallHourByEng.get(1).get(i).getCount()));
            values3.add(new BarEntry((float) listOfCallHourByEng.get(2).get(i).getHour(), (float) listOfCallHourByEng.get(2).get(i).getCount()));

        }


        BarDataSet set1, set2, set3;

        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {

            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set2 = (BarDataSet) chart.getData().getDataSetByIndex(1);
            set3 = (BarDataSet) chart.getData().getDataSetByIndex(2);
//            set4 = (BarDataSet) chart.getData().getDataSetByIndex(3);
            set1.setValues(values1);
            set2.setValues(values2);
            set3.setValues(values3);
//            set4.setValues(values4);
//            set1.setDrawValues(true);
//            set2.setDrawValues(true);
//            set3.setDrawValues(true);
            set1.setValueTextColor(Color.GRAY);
            set2.setValueTextColor(Color.GRAY);
            set3.setValueTextColor(Color.GRAY);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();


        } else {
            // create 4 DataSets
            set1 = new BarDataSet(values1, "In Process");
            set1.setColor(Color.rgb(104, 241, 175));
            set2 = new BarDataSet(values2, "Finish Complete");
            set2.setColor(Color.rgb(164, 228, 251));
            set3 = new BarDataSet(values3, "Waiting In Hold");
            set3.setColor(Color.rgb(242, 247, 158));
            set1.setDrawValues(true);
            set2.setDrawValues(true);
            set3.setDrawValues(true);
            set1.setValueTextColor(Color.GRAY);
            set2.setValueTextColor(Color.GRAY);
            set3.setValueTextColor(Color.GRAY);

            BarData data = new BarData(set1, set2, set3);
            data.setValueFormatter(new LargeValueFormatter());
//            data.setValueTypeface(tfLight);

            chart.setData(data);
        }

        // specify the width each bar should have
        chart.getBarData().setBarWidth(barWidth);

        // restrict the x-axis range
        chart.getXAxis().setAxisMinimum(9);

        // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
        chart.getXAxis().setAxisMaximum(9 + chart.getBarData().getGroupWidth(groupSpace, barSpace) * groupCount);
        chart.groupBars(9, groupSpace, barSpace);
        chart.invalidate();


    }


    void HoriBarChart(HorizontalBarChart chart) {

//        chart.setOnChartValueSelectedListener(this);
        // chart.setHighlightEnabled(false);


        chart.setDrawBarShadow(false);

        chart.setDrawValueAboveBar(true);

        chart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false);

        // draw shadows for each bar that show the maximum value
        // chart.setDrawBarShadow(true);

        chart.setDrawGridBackground(false);

        XAxis xl = chart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xl.setTypeface(tfLight);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
        xl.setTextColor(DashBoard.this.getResources().getColor(R.color.darkblue_2));
        xl.setGranularity(10f);

        YAxis yl = chart.getAxisLeft();
//        yl.setTypeface(tfLight);
        yl.setDrawAxisLine(false);
        yl.setDrawGridLines(false);
        yl.setDrawGridLinesBehindData(true);
        yl.setTextColor(DashBoard.this.getResources().getColor(R.color.darkblue_2));
        yl.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//        yl.setInverted(true);

        YAxis yr = chart.getAxisRight();
//        yr.setTypeface(tfLight);
        yr.setDrawAxisLine(false);
        yr.setDrawGridLines(false);
        yr.setTextColor(DashBoard.this.getResources().getColor(R.color.darkblue_2));
        yr.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//        yr.setInverted(true);

        chart.setFitBars(true);

        chart.animateY(2500);
//        chart.animateXY(2000, 2000);
        // setting data


        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setXEntrySpace(4f);
        setDataForSystem(10, 100, chart);
    }
    void readerChartFill(RadarChart chart){

//        chart.setBackgroundColor(Color.rgb(60, 65, 82));

        chart.getDescription().setEnabled(false);

        chart.setWebLineWidth(1f);
        chart.setWebColor(Color.LTGRAY);
        chart.setWebLineWidthInner(1f);
        chart.setWebColorInner(Color.LTGRAY);
        chart.setWebAlpha(100);

//        // create a custom MarkerView (extend MarkerView) and specify the layout
//        // to use for it
//        MarkerView mv = new MarkerView(this, R.layout.radar_marker);
//        mv.setChartView(chart); // For bounds control
//        chart.setMarker(mv); // Set the marker to the chart

        setDataRaderChart(chart);

        chart.animateXY(1400, 1400, Easing.EaseInOutQuad);

        XAxis xAxis = chart.getXAxis();
//        xAxis.setTypeface(tfLight);
        xAxis.setTextSize(9f);
        xAxis.setYOffset(0f);
        xAxis.setXOffset(0f);
        xAxis.setValueFormatter(new ValueFormatter() {

            private final String[] mActivities = new String[]{"SAT", "SUN", "MON", "TUE", "WED" ,"THU"};

            @Override
            public String getFormattedValue(float value) {
                return mActivities[(int) value % mActivities.length];
            }
        });
        xAxis.setTextColor(Color.WHITE);

        YAxis yAxis = chart.getYAxis();
//        yAxis.setTypeface(tfLight);
        yAxis.setLabelCount(5, false);
        yAxis.setTextSize(9f);
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(80f);
        yAxis.setDrawLabels(false);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
//        l.setTypeface(tfLight);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
        l.setTextColor(Color.WHITE);

    }

    private void setDataRaderChart(RadarChart chart) {

        float mul = 80;
        float min = 20;
        int cnt = 6;

        ArrayList<RadarEntry> entries1 = new ArrayList<>();
        ArrayList<RadarEntry> entries2 = new ArrayList<>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < cnt; i++) {
            float val1 = (float) (Math.random() * mul) + min;
            entries1.add(new RadarEntry(val1));

            float val2 = (float) (Math.random() * mul) + min;
            entries2.add(new RadarEntry(val2));
        }

        RadarDataSet set1 = new RadarDataSet(entries1, "Last Week");
        set1.setColor(Color.rgb(103, 110, 129));
        set1.setFillColor(Color.rgb(103, 110, 129));
        set1.setDrawFilled(true);
        set1.setFillAlpha(180);
        set1.setLineWidth(2f);
        set1.setDrawHighlightCircleEnabled(true);
        set1.setDrawHighlightIndicators(false);

        RadarDataSet set2 = new RadarDataSet(entries2, "This Week");
        set2.setColor(Color.rgb(121, 162, 175));
        set2.setFillColor(Color.rgb(121, 162, 175));
        set2.setDrawFilled(true);
        set2.setFillAlpha(180);
        set2.setLineWidth(2f);
        set2.setDrawHighlightCircleEnabled(true);
        set2.setDrawHighlightIndicators(false);

        ArrayList<IRadarDataSet> sets = new ArrayList<>();
        sets.add(set1);
        sets.add(set2);

        RadarData data = new RadarData(sets);
//        data.setValueTypeface(tfLight);
        data.setValueTextSize(8f);
        data.setDrawValues(false);
        data.setValueTextColor(Color.WHITE);

        chart.setData(data);
        chart.invalidate();
    }
//  private void setDataForSystem(int count, float range, HorizontalBarChart chart) {
//
//        float barWidth = 7f;
//        float spaceForBar = 10f;
//        ArrayList<BarEntry> values = new ArrayList<>();
//        ArrayList<String> ylabels = new ArrayList<>();
//        for (int i = 0; i < systemListDashBoardSystem.size(); i++) {
//            float val = (float) (Math.random() * range);
//            values.add(new BarEntry(i * spaceForBar, (float) systemListDashBoardSystem.get(i).getSystemCount()));
//            ylabels.add(" "+systemListDashBoardSystem.get(i).getSystemName());
//        }
//
//        BarDataSet set1;
//
//        if (chart.getData() != null &&
//                chart.getData().getDataSetCount() > 0) {
//            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
//            set1.setValues(values);
//
//            chart.getData().notifyDataChanged();
//            chart.notifyDataSetChanged();
//        } else {
//
//
//
//
//
//            set1 = new BarDataSet(values, "System");
//
//            set1.setDrawIcons(false);
//            set1.setValueTextColor(Color.WHITE);
//
//            set1.setBarBorderColor(Color.WHITE);
//
//            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
//
//            ArrayList<Integer> colors = new ArrayList<>();
//
//            for (int c : ColorTemplate.VORDIPLOM_COLORS)
//                colors.add(c);
//
//            for (int c : ColorTemplate.JOYFUL_COLORS)
//                colors.add(c);
////
//            for (int c : ColorTemplate.COLORFUL_COLORS)
//                colors.add(c);
//
//            for (int c : ColorTemplate.LIBERTY_COLORS)
//                colors.add(c);
//
//            for (int c : ColorTemplate.PASTEL_COLORS)
//                colors.add(c);
//
//            colors.add(ColorTemplate.getHoloBlue());
//
//            set1.setColors(colors);
////            String sysName[] = new String[systemList.size()];
////            systemList.toArray(sysName);
//
//
//            dataSets.add(set1);
//
//            BarData data = new BarData(dataSets);
//            data.setValueTextSize(10f);
//            data.setValueTextColor(Color.WHITE);
////            data.setValueTypeface(tfLight);
//            data.setBarWidth(barWidth);
//            chart.getLegend().setTextColor(Color.WHITE);
//            chart.setData(data);
//        }
//    }

    private void setDataForSystem(int count, float range, HorizontalBarChart chart) {

        float barWidth = 7f;
        float spaceForBar = 10f;
//        ArrayList<BarEntry> values = new ArrayList<>();
        ArrayList<String> ylabels = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
//
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        for (int i = 1; i < systemListDashBoardSystem.size(); i++) {
            float val = (float) (Math.random() * range);
            ArrayList<BarEntry> values = new ArrayList<>();
            values.add(new BarEntry(i * spaceForBar, (float) systemListDashBoardSystem.get(i).getSystemCount()));
            ylabels.add(" "+systemListDashBoardSystem.get(i).getSystemName());
            BarDataSet set1;
            set1 = new BarDataSet(values, systemListDashBoardSystem.get(i).getSystemName());
            try{
                set1.setColors(colors.get(i));
            }catch (Exception e){
                Log.e("ColorSize",""+colors.size()+"  i"+i);
            }
            set1.setDrawIcons(false);
            set1.setValueTextColor(Color.WHITE);

            set1.setBarBorderColor(Color.WHITE);
            dataSets.add(set1);
        }


            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setValueTextColor(Color.WHITE);
//            data.setValueTypeface(tfLight);
            data.setBarWidth(barWidth);
            chart.getLegend().setTextColor(Color.WHITE);
            chart.setData(data);

    }


//    private BarDataSet getDataSet(int color, float position, float value, String entryLabel, String datasetLabel) {
//        ArrayList<BarEntry> entries = new ArrayList();
//        entries.add(new BarEntry(position, value,entryLabel));
//
//        BarDataSet dataset = new BarDataSet(entries,datasetLabel);
//        dataset.setColor(color);
//        return dataset;
//    }

    private void setDataHori(int count, float range, HorizontalBarChart chart) {

        float barWidth = 9f;
        float spaceForBar = 10f;
        ArrayList<BarEntry> values = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random() * range);
            values.add(new BarEntry(i * spaceForBar, val,
                    getResources().getDrawable(R.drawable.button_f_)));
        }

        BarDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(values, "DataSet 1");

            set1.setDrawIcons(false);
            set1.setValueTextColor(Color.WHITE);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();

//            ArrayList<Integer> colors = new ArrayList<>();
//
//            for (int c : ColorTemplate.VORDIPLOM_COLORS)
//                colors.add(c);
//
//            for (int c : ColorTemplate.JOYFUL_COLORS)
//                colors.add(c);
////
//            for (int c : ColorTemplate.COLORFUL_COLORS)
//                colors.add(c);
//
//            for (int c : ColorTemplate.LIBERTY_COLORS)
//                colors.add(c);
//
//            for (int c : ColorTemplate.PASTEL_COLORS)
//                colors.add(c);
//
//            colors.add(ColorTemplate.getHoloBlue());
//
//            set1.setColors(colors);


            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
//            data.setValueTypeface(tfLight);
            data.setBarWidth(barWidth);
            chart.setData(data);
        }
    }

    private void setData(int count, float range, PieChart chart) {

        ArrayList<PieEntry> entries = new ArrayList<>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        //parties[i % parties.length])
        for (int i = 0; i < engList.size(); i++) {
            if (i != 0) {
                entries.add(new PieEntry((float) (Math.random() * range) + range / 5, engList.get(i)));
            }
        }

        PieDataSet dataSet = new PieDataSet(entries, "Election Results");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);


        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
        dataSet.setUsingSliceColorAsValueLineColor(true);//line color not black- is colors the same chart-

        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.GRAY);
        chart.setData(data);

        // undo all highlights
        chart.highlightValues(null);

        chart.invalidate();
    }

    private void setDataEngChartPerHour(int count, float range, PieChart chart) {

        ArrayList<PieEntry> entries = new ArrayList<>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        //parties[i % parties.length])
        for (int i = 0; i < engPerHourList.size(); i++) {
            if (i != 0) {
                Log.e("engper", "" + engPerHourList.get(i).getPercCall());
                entries.add(new PieEntry((float) engPerHourList.get(i).getPercCall(), engPerHourList.get(i).getName()));
            }
        }

        PieDataSet dataSet = new PieDataSet(entries, "Election Results");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);


        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
        dataSet.setUsingSliceColorAsValueLineColor(true);//line color not black- is colors the same chart-

        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.GRAY);
        chart.setData(data);

        // undo all highlights
        chart.highlightValues(null);
        chart.setUsePercentValues(true);
        data.setValueFormatter(new PercentFormatter(chart));

        chart.invalidate();
    }

    private void setDataSystemHalf(int count, float range, PieChart chart) {

        ArrayList<PieEntry> entries = new ArrayList<>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        //parties[i % parties.length])
        for (int i = 0; i < systemList.size(); i++) {
            if (i != 0) {
                entries.add(new PieEntry((float) (Math.random() * range) + range / 5, systemList.get(i)));
            }
        }

        PieDataSet dataSet = new PieDataSet(entries, "Election Results");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);


        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
        dataSet.setUsingSliceColorAsValueLineColor(true);//line color not black- is colors the same chart-

        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.GRAY);
        chart.setData(data);

        // undo all highlights
        chart.highlightValues(null);

        chart.invalidate();
    }


    private void setDataProgress(int count, float range, PieChart chart) {

        ArrayList<PieEntry> entries = new ArrayList<>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        //parties[i % parties.length])
        for (int i = 0; i < sizeProgress.size(); i++) {
            if (i == 0) {
                entries.add(new PieEntry((float) sizeProgress.get(i), "In Process"));
            } else if (i == 1) {
                entries.add(new PieEntry((float) sizeProgress.get(i), "Finish Complete "));
            } else if (i == 2) {
                entries.add(new PieEntry((float) sizeProgress.get(i), "Waiting In Hold"));

            }
        }

        PieDataSet dataSet = new PieDataSet(entries, "Election Results");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);


        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
        dataSet.setUsingSliceColorAsValueLineColor(true);//line color not black- is colors the same chart-

        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.WHITE);
        chart.setData(data);

        // undo all highlights
        data.setValueFormatter(new PercentFormatter(chart));
        chart.setUsePercentValues(true);
        chart.highlightValues(null);

        chart.invalidate();
    }


    private void setDataCompare( PieChart chart) {

        ArrayList<PieEntry> entries = new ArrayList<>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        //parties[i % parties.length])
        for (int i = 0; i < managerDashBord.size(); i++) {

            entries.add(new PieEntry((float) managerDashBord.get(i).getFirstHourCount(),  managerDashBord.get(i).getFirstHour()));
            entries.add(new PieEntry((float) managerDashBord.get(i).getSecondHourCount(),  managerDashBord.get(i).getSecondHour()));

        }

        PieDataSet dataSet = new PieDataSet(entries, "Election Results");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);


        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
        dataSet.setUsingSliceColorAsValueLineColor(true);//line color not black- is colors the same chart-

        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.WHITE);
        chart.setData(data);

        // undo all highlights
        chart.highlightValues(null);
        chart.setUsePercentValues(false);
        chart.invalidate();
    }

    private void setDataEngHour( PieChart chart) {

        ArrayList<PieEntry> entries = new ArrayList<>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        //parties[i % parties.length])
        for (int i = 0; i < engPerHourList.size(); i++) {
            if (i != 0) {
//                float avg= (float) ((engPerHourList.get(i).getNoOfCountCall()*allCall)/100);
                entries.add(i-1,new PieEntry((float) engPerHourList.get(i).getNoOfCountCall(), engPerHourList.get(i).getName()));

            }
        }

        PieDataSet dataSet = new PieDataSet(entries, "Election Results");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);


        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
        dataSet.setUsingSliceColorAsValueLineColor(true);//line color not black- is colors the same chart-

        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.WHITE);
        chart.setData(data);

        // undo all highlights
        chart.highlightValues(null);
        chart.setUsePercentValues(false);
        chart.invalidate();
    }

    private void setDataEngSystemDataSysID( PieChart chart) {

        ArrayList<PieEntry> entries = new ArrayList<>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        //parties[i % parties.length])
        for (int i = 0; i < engPerSysList.size(); i++) {
//            if (i != 0) {
                entries.add(i,new PieEntry((float) engPerSysList.get(i).getNoOfCountCall(), engPerSysList.get(i).getName()));

//            }
        }

        PieDataSet dataSet = new PieDataSet(entries, "Election Results");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);


        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
        dataSet.setUsingSliceColorAsValueLineColor(true);//line color not black- is colors the same chart-

        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.WHITE);
        chart.setData(data);

        // undo all highlights
        chart.highlightValues(null);
        chart.setUsePercentValues(false);
        chart.invalidate();
    }

    private void setDataEngSystem( PieChart chart) {

        ArrayList<PieEntry> entries = new ArrayList<>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        //parties[i % parties.length])
        for (int i = 0; i < systemDashBordListByEng.size(); i++) {
//            if (i != 0) {
//                float avg= (float) ((engPerHourList.get(i).getNoOfCountCall()*allCall)/100);
                entries.add(i,new PieEntry((float) systemDashBordListByEng.get(i).getSystemCount(), systemDashBordListByEng.get(i).getSystemName()));

//            }
        }

        PieDataSet dataSet = new PieDataSet(entries, "Election Results");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);


        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
        dataSet.setUsingSliceColorAsValueLineColor(true);//line color not black- is colors the same chart-

        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.WHITE);

        chart.setData(data);

        // undo all highlights
        chart.highlightValues(null);
        chart.setUsePercentValues(false);
        chart.invalidate();
    }
    private void setDataEngSystemData( PieChart chart) {

        ArrayList<PieEntry> entries = new ArrayList<>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        //parties[i % parties.length])
        if(systemListDashOnlyMax.size()!=0) {
            for (int i = 0; i < 3; i++) {
//            if (i != 0) {
                entries.add(i, new PieEntry((float) systemListDashOnlyMax.get(i).getSystemCount(), systemListDashOnlyMax.get(i).getSystemName()));

//            }
            }
        }

        PieDataSet dataSet = new PieDataSet(entries, "Election Results");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);


        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
        dataSet.setUsingSliceColorAsValueLineColor(true);//line color not black- is colors the same chart-

        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.WHITE);

        chart.setData(data);

        // undo all highlights
        chart.highlightValues(null);
        chart.setUsePercentValues(false);
        chart.invalidate();
    }

void setHorizontalBarChart(HorizontalBarChart barChart)
    {


        BarData data = new BarData();

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();

        ArrayList<String> labels = new ArrayList<>();
        labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");
        labels.add("June");
        ArrayList<String> ylabels = new ArrayList<>();
        int dataCount=0;
        for (int i=0;i<6;++i) {
            BarEntry entry = new BarEntry(dataCount,(i+1)*2);
            valueSet1.add(entry);
            ylabels.add(" "+i);
            dataCount++;
        }
        List<IBarDataSet> dataSets = new ArrayList<>();
        BarDataSet bds = new BarDataSet(valueSet1, " ");
        bds.setColors(ColorTemplate.MATERIAL_COLORS);
        String[] xAxisLabels = labels.toArray(new String[0]);
        String[] yAxisLabels = ylabels.toArray(new String[0]);
        bds.setStackLabels(xAxisLabels);
        dataSets.add(bds);
        data.addDataSet(bds);
        data.setDrawValues(true);
        data.setBarWidth(0.4f);

        XAxis xaxis = barChart.getXAxis();
        xaxis.setDrawGridLines(false);
        xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xaxis.setGranularityEnabled(true);
        xaxis.setGranularity(1);
        xaxis.setDrawLabels(true);
        xaxis.setLabelCount(dataCount+1);
        xaxis.setXOffset(10);
        xaxis.setDrawAxisLine(false);
        CategoryBarChartXaxisFormatter xaxisFormatter = new CategoryBarChartXaxisFormatter(xAxisLabels);
        xaxis.setValueFormatter(xaxisFormatter);

        YAxis yAxisLeft = barChart.getAxisLeft();
        yAxisLeft.setEnabled(false);

        YAxis yAxisRight = barChart.getAxisRight();
        yAxisRight.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        yAxisRight.setDrawGridLines(false);
        yAxisRight.setDrawAxisLine(false);

        Legend legend = barChart.getLegend();
        legend.setEnabled(false);

        barChart.setFitBars(true);
        barChart.setData(data);
        barChart.setDescription(null);
    }
    public class CategoryBarChartXaxisFormatter extends ValueFormatter implements IAxisValueFormatter {

        private String[] mValues;

        public CategoryBarChartXaxisFormatter(String[] values) {
            this.mValues = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {

            int val = (int)value;
            String label="";
            if(val>=0 && val<mValues.length) {
                label= mValues[val];
            }
            else {
                label= "";
            }
            return label;
        }
    }



    private SpannableString generateCenterSpannableText(String message) {

        SpannableString s = new SpannableString(message);
        s.setSpan(new RelativeSizeSpan(1f), 0, s.length(), 0);
//        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
//        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 14, 0);
//        s.setSpan(new RelativeSizeSpan(.65f), 14, s.length() - 15, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), 0, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), 0 , s.length(), 0);
        return s;
    }

//    @Override
//    public void onValueSelected(Entry e, Highlight h) {
//
//        if (e == null)
//            return;
//        Log.i("VAL SELECTED",
//                "Value: " + e.getY() + ", xIndex: " + e.getX()
//                        + ", DataSet index: " + h.getDataSetIndex());
//
//
//    }
//
//    @Override
//    public void onNothingSelected() {
//        Log.i("PieChart", "nothing selected");
//    }

}
