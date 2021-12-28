package agh.ics.oop.gui;

import agh.ics.oop.interfaces.IWorldMap;
import javafx.scene.chart.LineChart;
import java.util.ArrayList;

public class AllCharts {
    public IWorldMap map;
    public ArrayList<Chart> charts = new ArrayList<>();
    public AllCharts(IWorldMap map){
        this.map = map;
    }
    public void addChart(String xLabel, String yLabel){
        charts.add(new Chart(xLabel,yLabel));
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
