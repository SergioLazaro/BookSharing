package com.example.android.booksharing.Objects;

/**
 * Created by Sergio on 2/6/16.
 */
public class SQLInjection {

    public SQLInjection(){}

    /**
     * Method used to check every line inserted by users to avoid SQL Injection.
     * This method must be available on every file.
     * @param line
     * @return true if [line] doesnt contains words like DROP, INSERT INTO, DELETE FROM OR UPDATE.
     */
    public boolean checkSQLInjection(String line){
        if(!line.contains("DROP") || !line.contains("INSERT INTO") || !line.contains("DELETE FROM")
                || !line.contains("UPDATE") || !line.contains("<") || !line.contains(">")
                || !line.contains("OR")){
            return true;
        }
        else{
            return false;
        }
    }
}
