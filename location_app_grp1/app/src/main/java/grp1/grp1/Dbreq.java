package grp1.grp1;

/**
 * Created by Din on 27/10/2017.
 */

import com.android.volley.toolbox.StringRequest;
import com.android.volley.Response;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Dbreq extends StringRequest{
    private static final String DBREQ_REQUEST_URL = "http://ec2-35-176-82-29.eu-west-2.compute.amazonaws.com/index.php";
    private Map<String, String> params;


    public Dbreq(String lat, String lang, String did, Response.Listener<String> listener){
        super(Method.POST, DBREQ_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("lat",lat);
        params.put("lang",lang);
        params.put("did",did);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
