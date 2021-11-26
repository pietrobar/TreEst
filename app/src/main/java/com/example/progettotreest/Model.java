package com.example.progettotreest;

import java.util.ArrayList;

public class Model {
    private static Model theInstance = null;
    private ArrayList<String> contacts = null;

    public static synchronized Model getInstance() {
        if (theInstance == null) {
            theInstance = new Model();
        }
        return theInstance;
    }

    private Model() {
        contacts = new ArrayList<String>();
    }

    public String get(int index) {
        return contacts.get(index);
    }

    public int getSize() {
        return contacts.size();
    }

    //creiamo dei dati di test
    public void initWithFakeData() {
        contacts.add("Milano Celoria - Milano Rogoredo");
        contacts.add("Milano Rogoredo - Milano Celoria");
        contacts.add("Milano Lambrate - Sesto San Giovanni");
        contacts.add("Milano Sesto San Giovanni - Milano Lambrate");
        contacts.add("Milano Celoria - Milano Rogoredo");
        contacts.add("Milano Rogoredo - Milano Celoria");
        contacts.add("Milano Lambrate - Sesto San Giovanni");
        contacts.add("Milano Sesto San Giovanni - Milano Lambrate");
        contacts.add("Milano Celoria - Milano Rogoredo");
        contacts.add("Milano Rogoredo - Milano Celoria");
        contacts.add("Milano Lambrate - Sesto San Giovanni");
        contacts.add("Milano Sesto San Giovanni - Milano Lambrate");
        contacts.add("Milano Celoria - Milano Rogoredo");
        contacts.add("Milano Rogoredo - Milano Celoria");
        contacts.add("Milano Lambrate - Sesto San Giovanni");
        contacts.add("Milano Sesto San Giovanni - Milano Lambrate");
        contacts.add("Milano Celoria - Milano Rogoredo");
        contacts.add("Milano Rogoredo - Milano Celoria");
        contacts.add("Milano Lambrate - Sesto San Giovanni");
        contacts.add("Milano Sesto San Giovanni - Milano Lambrate");
        contacts.add("Milano Celoria - Milano Rogoredo");
        contacts.add("Milano Rogoredo - Milano Celoria");
        contacts.add("Milano Lambrate - Sesto San Giovanni");
        contacts.add("Milano Sesto San Giovanni - Milano Lambrate");
        contacts.add("Milano Celoria - Milano Rogoredo");
        contacts.add("Milano Rogoredo - Milano Celoria");
        contacts.add("Milano Lambrate - Sesto San Giovanni");
        contacts.add("Milano Sesto San Giovanni - Milano Lambrate");
        contacts.add("Milano Celoria - Milano Rogoredo");
        contacts.add("Milano Rogoredo - Milano Celoria");
        contacts.add("Milano Lambrate - Sesto San Giovanni");
        contacts.add("Milano Sesto San Giovanni - Milano Lambrate");
        contacts.add("Milano Celoria - Milano Rogoredo");
        contacts.add("Milano Rogoredo - Milano Celoria");
        contacts.add("Milano Lambrate - Sesto San Giovanni");
        contacts.add("Milano Sesto San Giovanni - Milano Lambrate");
        contacts.add("Milano Celoria - Milano Rogoredo");
        contacts.add("Milano Rogoredo - Milano Celoria");
        contacts.add("Milano Lambrate - Sesto San Giovanni");
        contacts.add("Milano Sesto San Giovanni - Milano Lambrate");
    }


}
