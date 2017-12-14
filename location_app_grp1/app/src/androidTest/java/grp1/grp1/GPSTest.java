package grp1.grp1;

import org.junit.Test;
import android.location.Location;

import static android.support.test.InstrumentationRegistry.getContext;
import static org.junit.Assert.*;

/**
 * Created by Din on 13/12/2017.
 */
public class GPSTest {
    @Test
    public void getLocation() throws Exception {
        double output;
        Location location = new Location("");
        Location expLocation = location;
        GPS gps = new GPS(getContext());
        //location= gps.getLocation();
        assertEquals(expLocation, location);

    }

    @Test
    public void onLocationChanged() throws Exception {

    }

    @Test
    public void onStatusChanged() throws Exception {

    }

    @Test
    public void onProviderEnabled() throws Exception {

    }

    @Test
    public void onProviderDisabled() throws Exception {

    }

}