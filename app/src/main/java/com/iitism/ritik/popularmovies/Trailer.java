package com.iitism.ritik.popularmovies;

import io.realm.RealmObject;

/**
 * Created by Ritik on 02-10-2016.
 */
public class Trailer extends RealmObject{

    private String Tname;
    private String Tkey;

    public Trailer()
    {

    }

    public Trailer(String name,String key)
    {
        this.Tname=name;
        this.Tkey=key;
    }

    public String getTname() {
        return Tname;
    }

    public void setTname(String tname) {
        Tname = tname;
    }

    public String getTkey() {
        return Tkey;
    }

    public void setTkey(String tkey) {
        Tkey = tkey;
    }
}
