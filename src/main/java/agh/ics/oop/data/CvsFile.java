package agh.ics.oop.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

//kod czesciowo wziety ze stacka
public class CvsFile {
    PrintWriter pw = null;
    StringBuilder builder = new StringBuilder();
    public CvsFile(String filename){
        try {
            pw = new PrintWriter(new File(filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        builder.append("Nr of animals,Nr of grass,Avg energy,Avg length of life,Avg nr of children" +"\n");
    }
    //wczytuje do pliku dane ze wszystkich poprzednich dni
    public void dataToFile(ArrayList<String[]> allData){
        int n = allData.size();
        for(int i=0;i<n;i++){
            String[] args = allData.get(i);
            builder.append(args[0]+";"+args[1]+";"+args[2]+";"+args[3]+";"+args[4]);
            builder.append('\n');
        }
        builder.append("Average:"+'\n');
        builder.append(getAverage(allData,0)+";"+getAverage(allData,1)+";"+getAverage(allData,2)+
                ";"+getAverage(allData,3)+";"+getAverage(allData,4));
        pw.write(builder.toString());
        pw.close();
    }
    //oblicza srednia, ktora jest na koncu pliku
    private float getAverage(ArrayList<String[]> allData,int index){
        int sum = 0;
        for(int i = 0; i< allData.size();i++){
            String[] tmp = allData.get(i);
            sum += Float.valueOf(tmp[index]);
        }
        return (float) sum/ allData.size();
    }
}