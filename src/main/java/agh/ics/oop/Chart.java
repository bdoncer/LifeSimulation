package agh.ics.oop;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class Chart {
    final CategoryAxis xAxis = new CategoryAxis();
    final NumberAxis yAxis = new NumberAxis();
    final LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
    XYChart.Series<String, Number> series = new XYChart.Series<>();
    public Chart(String xLabel, String yLabel, String title){
        xAxis.setLabel(xLabel);
        xAxis.setAnimated(false);
        yAxis.setLabel(yLabel);
        yAxis.setAnimated(false);
        chart.setTitle(title);
        chart.setAnimated(false);
        chart.getData().add(series);
    }
    public void addData(String x,Number y){
        series.getData().add(new XYChart.Data<>(x,y));


    }
    public LineChart getChart(){
        return chart;
    }
}
