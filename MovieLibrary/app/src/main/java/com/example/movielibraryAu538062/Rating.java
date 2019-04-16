
/* http://www.jsonschema2pojo.org/ */
package com.example.movielibraryAu538062;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Rating implements Serializable
{

    @SerializedName("Source")
    @Expose
    public String source;
    @SerializedName("Value")
    @Expose
    public String value;
    private final static long serialVersionUID = 6100273873033480524L;

}
