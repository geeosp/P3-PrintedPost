package com.p3.printedpost;

import android.app.Application;

import com.parse.Parse;


/**
 * Created by Geovane on 01/03/2015.
 */
public class PrintedPost extends Application {
    public static Fachada fachada;// = new Fachada();

    public void onCreate() {
        fachada = new Fachada();
        Parse.initialize(this, "hGNzYptoQo0eLE4NNjYrom3xoRvr6zeNPkhUtSbI", "SP1UR3W9A41NPH1plxDOSzXKgJVezcavMu7zeUtB");


    }
}
