package com.example.readify;

import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.Response;
import org.json.JSONArray;

public class CustomPriorityRequest extends JsonArrayRequest {
    // default value
    Priority mPriority = Priority.HIGH;

    public CustomPriorityRequest(int method, String url, JSONArray jsonRequest, Response.Listener<org.json.JSONArray> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }
    @Override
    public Priority getPriority() {
        return mPriority;
    }
    public void setPriority(Priority p){
        mPriority = p;
    }
}
