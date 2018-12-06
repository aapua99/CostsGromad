package com.example.andriy.dehack;

/**
 * Created by Andriy on 08.02.2018.
 */

public class Suggest {
    String name, price,describe;
    String voted;
    Suggest(){};
    Suggest(String name, String price, String describe, String  voted){
        this.describe=describe;
        this.name=name;
        this.price=price;
        this.voted=voted;
    }

    public String getName() {
        return name;
    }

    public String getDescribe() {
        return describe;
    }

    public String getPrice() {
        return price;
    }

    public String getVoted() {
        return voted;
    }
}
