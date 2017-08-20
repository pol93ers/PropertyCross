package mdpa.lasalle.propertycross.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.os.ResultReceiver;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GeocoderIntentService extends IntentService {
    private static final String TAG = GeocoderIntentService.class.getSimpleName();
    private static final int NUM_ATTEMPTS = 4;
    public static final int FAILURE_RESULT = 0;
    public static final int SUCCESS_RESULT = 1;
    public static final String RECEIVER
            = GeocoderIntentService.class.getPackage().getName() + ".RECEIVER";
    public static final String RESULT_DATA_KEY
            = GeocoderIntentService.class.getPackage().getName() + ".RESULT_DATA_KEY";
    public static final String LOCATION_DATA_EXTRA
            = GeocoderIntentService.class.getPackage().getName() + ".LOCATION_DATA_EXTRA";
    public static final String NAME_DATA_EXTRA
            = GeocoderIntentService.class.getPackage().getName() + ".NAME_DATA_EXTRA";

    public static <R extends ResultReceiver> Intent getAddressesIntent(
            Context context, R receiver, Location location)
    {
        Intent intent = new Intent(context, GeocoderIntentService.class);
        intent.putExtra(GeocoderIntentService.RECEIVER, receiver);
        intent.putExtra(GeocoderIntentService.LOCATION_DATA_EXTRA, location);
        return intent;
    }

    public static <R extends ResultReceiver> Intent getAddressesIntent(
            Context context, R receiver, String locationName)
    {
        Intent intent = new Intent(context, GeocoderIntentService.class);
        intent.putExtra(GeocoderIntentService.RECEIVER, receiver);
        intent.putExtra(GeocoderIntentService.NAME_DATA_EXTRA, locationName);
        return intent;
    }

    protected ResultReceiver receiver;

    public GeocoderIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        receiver = intent.getParcelableExtra(RECEIVER);

        if (receiver == null) {
            Log.wtf(TAG, "No receiver received. There is nowhere to send the results.");
            return;
        }

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;
        if (intent.getParcelableExtra(LOCATION_DATA_EXTRA) != null) {
            Location location = intent.getParcelableExtra(LOCATION_DATA_EXTRA);
            addresses = getAddressesFromLocation(geocoder, location, NUM_ATTEMPTS);
        } else if (intent.getStringExtra(NAME_DATA_EXTRA) != null) {
            String locationName = intent.getStringExtra(NAME_DATA_EXTRA);
            addresses = getAddressesFromName(geocoder, locationName, NUM_ATTEMPTS);
        } else {
            throw new IllegalArgumentException("Invalid parameters!");
        }

        if (addresses != null && addresses.size() != 0) {
            deliverResultToReceiver(SUCCESS_RESULT, new ArrayList<Parcelable>(addresses));
        } else {
            deliverResultToReceiver(FAILURE_RESULT, null);
        }
    }

    public static @Nullable
    List<Address> getAddressesFromLocation(Geocoder geocoder, Location location, int nubAttempts) {
        List<Address> addresses = null;
        int attempts = 0;
        do {
            attempts += 1;
            try {
                addresses = getAddressesFromLocation(geocoder, location);
            } catch (IOException e) {
                Log.w(TAG, "IO error while loading addresses from location", e);
            }
        } while (addresses == null && attempts < nubAttempts);

        return addresses;
    }

    public static @Nullable
    List<Address> getAddressesFromName(Geocoder geocoder, String locationName, int nubAttempts) {
        List<Address> addresses = null;
        int attempts = 0;
        do {
            attempts += 1;
            try {
                addresses = getAddressesFromName(geocoder, locationName);
            } catch (IOException e) {
                Log.w(TAG, "IO error while loading addresses from location", e);
            }
        } while (addresses == null && attempts < nubAttempts);

        return addresses;
    }

    public static List<Address> getAddressesFromLocation(Geocoder geocoder, Location location) throws IOException {
        return geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 5);
    }

    public static List<Address> getAddressesFromName(Geocoder geocoder, String locationName) throws IOException {
        return geocoder.getFromLocationName(locationName, 5);
    }

    /**
     * Sends a resultCode and message to the receiver.
     */
    private void deliverResultToReceiver(int resultCode, ArrayList<Parcelable> parcelable) {
        Bundle bundle = new Bundle();
        if (parcelable != null) {
            Log.i(TAG, "Found addresses: " + parcelable.toString());
            bundle.putParcelableArrayList(RESULT_DATA_KEY, parcelable);
        } else {
            Log.e(TAG, "No valid addresses found");
        }
        receiver.send(resultCode, bundle);
    }
}
