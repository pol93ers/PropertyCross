package mdpa.lasalle.propertycross.data.adapter;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import mdpa.lasalle.propertycross.ui.adapters.AdapterRecyclerFavourites;

public class PropertyFavouriteItem implements AdapterRecyclerFavourites.PropertyItem<PropertyFavouriteItem.Property> {

    public static class Property implements AdapterRecyclerFavourites.PropertyItem.Property {
        private String id, address, price, meters, type;
        private double latitude, longitude;
        private ArrayList<String> images;

        public Property(@NonNull String id, @NonNull String address, ArrayList<String> images, @NonNull String price, @NonNull String meters, double latitude, double longitude, @NonNull String type) {
            this.id = id;
            this.address = address;
            this.images = images;
            this.price = price;
            this.meters = meters;
            this.latitude = latitude;
            this.longitude = longitude;
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
        public ArrayList<String> getPhotos() {
            return images;
        }

        @Override
        public double getLatitude() {
            return latitude;
        }

        @Override
        public double getLongitude() {
            return longitude;
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
