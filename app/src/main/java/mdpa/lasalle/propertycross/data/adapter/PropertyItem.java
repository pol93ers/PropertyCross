package mdpa.lasalle.propertycross.data.adapter;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import mdpa.lasalle.propertycross.ui.adapters.AdapterRecyclerProperties;

public class PropertyItem implements AdapterRecyclerProperties.PropertyItem<PropertyItem.Property> {

    public static class Property implements AdapterRecyclerProperties.PropertyItem.Property {
        private String id, name, address, price, meters, type;
        private double latitude, longitude;
        private ArrayList<String> images;
        private boolean isFavourite;

        public Property(@NonNull String id, @NonNull String name, @NonNull String address, ArrayList<String> images, @NonNull String price, @NonNull String meters, @NonNull String type, double latitude, double longitude, boolean isFavourite) {
            this.id = id;
            this.name = name;
            this.address = address;
            this.price = price;
            this.meters = meters;
            this.type = type;
            this.latitude = latitude;
            this.longitude = longitude;
            this.images = images;
            this.isFavourite = isFavourite;
        }

        @NonNull @Override
        public String getId() {
            return id;
        }

        @NonNull @Override
        public String getName() {
            return name;
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

        @Override
        public boolean isFavourite(){ return isFavourite;}

        @Override
        public void setFavourite(boolean isFavourite){
            this.isFavourite = isFavourite;
        }
    }

    private Object item;

    public PropertyItem(@NonNull Property property) {
        this.item = property;
    }

    @NonNull @Override
    public Property getProperty() {
        return (Property)item;
    }
}
