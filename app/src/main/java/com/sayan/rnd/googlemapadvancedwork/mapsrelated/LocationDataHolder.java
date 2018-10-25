package com.sayan.rnd.googlemapadvancedwork.mapsrelated;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LocationDataHolder {
    private static final String response = "{\n" +
            "  \"result\": \"success\",\n" +
            "  \"locations\": [\n" +
            "    {\n" +
            "      \"lat\": \"22.6489190\",\n" +
            "      \"lon\": \"88.4157811\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"lat\": \"22.6489190\",\n" +
            "      \"lon\": \"88.4157811\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"lat\": \"22.6489190\",\n" +
            "      \"lon\": \"88.4157811\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"lat\": \"22.6489190\",\n" +
            "      \"lon\": \"88.4157811\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"lat\": \"22.648909\",\n" +
            "      \"lon\": \"88.415931\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"lat\": \"22.648860\",\n" +
            "      \"lon\": \"88.416092\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"lat\": \"22.648780\",\n" +
            "      \"lon\": \"88.416339\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"lat\": \"22.648810\",\n" +
            "      \"lon\": \"88.416479\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"lat\": \"22.648810\",\n" +
            "      \"lon\": \"88.416479\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"lat\": \"22.648810\",\n" +
            "      \"lon\": \"88.416479\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"lat\": \"22.648810\",\n" +
            "      \"lon\": \"88.416479\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"lat\": \"22.649068\",\n" +
            "      \"lon\": \"88.416532\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"lat\": \"22.649352\",\n" +
            "      \"lon\": \"88.416603\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"lat\": \"22.649659\",\n" +
            "      \"lon\": \"88.416587\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"lat\": \"22.649946\",\n" +
            "      \"lon\": \"88.416651\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"lat\": \"22.650110\",\n" +
            "      \"lon\": \"88.416716\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"lat\": \"22.650110\",\n" +
            "      \"lon\": \"88.416716\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"lat\": \"22.650110\",\n" +
            "      \"lon\": \"88.416716\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"lat\": \"22.650110\",\n" +
            "      \"lon\": \"88.416716\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"lat\": \"22.650154\",\n" +
            "      \"lon\": \"88.416866\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"lat\": \"22.650055\",\n" +
            "      \"lon\": \"88.417118\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"lat\": \"22.649872\",\n" +
            "      \"lon\": \"88.417687\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"lat\": \"22.650288\",\n" +
            "      \"lon\": \"88.417729\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"lat\": \"22.650491\",\n" +
            "      \"lon\": \"88.417965\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"lat\": \"22.650758\",\n" +
            "      \"lon\": \"88.418073\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"lat\": \"22.651016\",\n" +
            "      \"lon\": \"88.418191\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"lat\": \"22.651377\",\n" +
            "      \"lon\": \"88.418239\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"lat\": \"22.651446\",\n" +
            "      \"lon\": \"88.418593\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"lat\": \"22.652090\",\n" +
            "      \"lon\": \"88.418770\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"lat\": \"22.652293\",\n" +
            "      \"lon\": \"88.418958\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"lat\": \"22.652293\",\n" +
            "      \"lon\": \"88.418958\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"lat\": \"22.652293\",\n" +
            "      \"lon\": \"88.418958\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"lat\": \"22.652293\",\n" +
            "      \"lon\": \"88.418958\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"lat\": \"22.652293\",\n" +
            "      \"lon\": \"88.418958\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"lat\": \"22.652293\",\n" +
            "      \"lon\": \"88.418958\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    public static ArrayList<LatLng> getLocationsJSON(){
        try {
            ArrayList<LatLng> latLngs = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(response);
            JSONArray locations = jsonObject.getJSONArray("locations");
            for (int i = 0; i < locations.length(); i++){
                JSONObject jsonObjectLocation = locations.getJSONObject(i);
                String lat = jsonObjectLocation.getString("lat");
                String lon = jsonObjectLocation.getString("lon");
                LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
                latLngs.add(latLng);
            }
            return latLngs;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
