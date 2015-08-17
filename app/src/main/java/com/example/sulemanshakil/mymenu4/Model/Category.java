package com.example.sulemanshakil.mymenu4.Model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Suleman Shakil on 06.08.2015.
 */
public class Category implements Serializable{

    public String name="";
    public final List<Integer> ID = new ArrayList<Integer>();
    public final List<Float> money = new ArrayList<Float>();
    public final List<String> date = new ArrayList<String>();
    public final List<String> description = new ArrayList<String>();
    public String CategoryType="";


    public Category(String name) {
        this.name = name;
    }

    public Float getTotalAmount(){
        Float totalamount= Float.valueOf(0);
        for (int i = 0; i < this.money.size(); i++) {
            totalamount=totalamount+money.get(i);
        }
        return totalamount;
    }

}
