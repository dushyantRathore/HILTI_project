package com.fingerprint.sample;

import com.google.gson.annotations.SerializedName;

/**
 * Created by brainbreaker on 02/12/17.
 */

public class QRResult {
    @SerializedName("item_id")
    public String item_id;

    @SerializedName("item_amount")
    public String item_amount;

    @SerializedName("title")
    public String item_name;

    @SerializedName("quantity")
    public String quantity;
}
