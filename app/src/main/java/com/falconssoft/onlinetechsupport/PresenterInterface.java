package com.falconssoft.onlinetechsupport;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface PresenterInterface {

    public JSONObject onGetResponse(JSONObject response);

    public void onGetErrorResponse(VolleyError error);


}
