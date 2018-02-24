package com.fingerprint.sample;

import com.google.gson.annotations.SerializedName;

/**
 * Created by brainbreaker on 03/12/17.
 */

public class PayResponse {
    @SerializedName("meter_address")
    public String meter_add;

    @SerializedName("hash")
    public String hash;
}
