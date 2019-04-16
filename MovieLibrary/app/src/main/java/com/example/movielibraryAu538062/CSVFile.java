package com.example.movielibraryAu538062;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;



// https://stackoverflow.com/questions/43055661/reading-csv-file-in-android-app
public class CSVFile {
    InputStream inputStream;

    public CSVFile(InputStream inputStream){
        this.inputStream = inputStream;
    }

    public MovieModel[] read(){
        List<String[]> resultList = new ArrayList<String[]>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                String[] row = csvLine.split(";");
                resultList.add(row);
            }
        }
        catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: "+ex);
        }
        finally {
            try {
                inputStream.close();
            }
            catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: "+e);
            }
        }


        // now we got all the data, lets split it up to a useable array!
        MovieModel[] Reallist = new MovieModel[resultList.size() -1];
        for (int i = 1; i < resultList.size() ; i++) {
            String[] currentString = resultList.get(i); // after the headers
            MovieModel currentMovie = new MovieModel(currentString[0] , currentString[1] , currentString[2],currentString[3] );
            Reallist[i-1] = (currentMovie);
        }

        return Reallist;
    }
}