package mdpa.lasalle.propertycross.data.adapter;

import android.net.Uri;
import android.support.annotation.NonNull;

import mdpa.lasalle.propertycross.ui.adapters.AdapterRecyclerFavourites;

public class PropertyFavouriteItem implements AdapterRecyclerFavourites.PropertyItem<PropertyFavouriteItem.Property> {

    public static class Property implements AdapterRecyclerFavourites.PropertyItem.Property {
        private String id, address, price, meters, type;
        private double distance;
        private Uri uri;

        public Property(@NonNull String id, @NonNull String address, Uri uri, @NonNull String price, @NonNull String meters, double distance, @NonNull String type) {
            this.id = id;
            this.address = address;
            this.uri = uri;
            this.price = price;
            this.meters = meters;
            this.distance = distance;
            this.type = type;
        }

        @NonNull @Override
        public String getId() {
            return id;
        }

        @NonNull @Override
        public String getAddress() {
            return address;
        }

        @NonNull @Override
        public String getMeters() {
            return meters;
        }

        @NonNull @Override
        public String getPrice() {
            return price;
        }

        @NonNull @Override
        public String getType() {
            return type;
        }

        @NonNull @Override
        public Uri getPhoto() {
            return uri;
        }

        @Override
        public double getDistance() {
            return distance;
        }
    }

    private Object item;

    public PropertyFavouriteItem(@NonNull Property property) {
        this.item = property;
    }

    @NonNull @Override
    public Property getProperty() {
        return (Property)item;
    }
}
