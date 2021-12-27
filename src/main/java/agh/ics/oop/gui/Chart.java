package agh.ics.oop.gui;

import javafx.scene.chart.*;

import java.awt.datatransfer.Clipboard;

public class Chart {
    final CategoryAxis xAxis = new CategoryAxis();
    final NumberAxis yAxis = new NumberAxis();
    final LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
    XYChart.Series<String, Number> series = new XYChart.Series<>();
    public Chart(String xLabel, String yLabel){
        xAxis.setLabel(xLabel);
        xAxis.setAnimated(false);
        yAxis.setLabel(yLabel);
        yAxis.setAnimated(false);
        chart.setAnimated(false);
        chart.getData().add(series);

    }
    public void addData(String x,Number y){
        series.getData().add(new XYChart.Data<>(x,y));
        if (series.getData().size() > 5)
            series.getData().remove(0);
    }
    public LineChart getChart(){
        return chart;
    }

    public XYChart.Series<String, Number> getSeries() {
        return series;
    }
}