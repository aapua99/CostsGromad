package com.example.andriy.dehack;

/**
 * Created by Andriy on 06.02.2018.
 */

public class DataClass {
    private String name="",value="";
    DataClass(String name, String value){
        this.name=name;
        this.value=value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
