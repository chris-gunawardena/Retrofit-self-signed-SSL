
package com.ibm.chris.retrofitssl;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Autocomplete {

    @SerializedName("data")
    @Expose
    public List<String> data = null;
    @SerializedName("total")
    @Expose
    public Integer total;

}
