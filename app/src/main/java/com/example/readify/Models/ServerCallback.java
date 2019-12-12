package com.example.readify.Models;

import org.json.JSONArray;
import org.json.JSONObject;

public interface ServerCallback {
    void onSuccess(JSONObject result);
    //void onSuccess(JSONArray result);
}
