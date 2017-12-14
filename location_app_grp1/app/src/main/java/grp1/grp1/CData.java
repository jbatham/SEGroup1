package grp1.grp1;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Din on 11/12/2017.
 */

//class to store array of CrimeData objects
public class CData {
    LatLng clatlng;
    String cat;

    CData(LatLng clatlng, String cat){
        this.clatlng = clatlng;
        this.cat = cat;
    }
}
