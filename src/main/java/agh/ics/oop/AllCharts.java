package agh.ics.oop;

import javafx.scene.chart.LineChart;

import java.util.ArrayList;

public class AllCharts {
    public IWorldMap map;
    public ArrayList<Chart> charts = new ArrayList<>();
    public AllCharts(IWorldMap map){
        this.map = map;
    }
    public void addChart(String xLabel, String yLabel, String title){
        charts.add(new Chart(xLabel,yLabel,title));
    }
    public ArrayList<LineChart> getLineCharts(){
        ArrayList<LineChart> lineCharts = new ArrayList<>();
        for(Chart chart:charts){
            lineCharts.add(chart.getChart());
        }
        return lineCharts;
    }

    public ArrayList<Chart> getCharts(){
        return charts;
    }


}
