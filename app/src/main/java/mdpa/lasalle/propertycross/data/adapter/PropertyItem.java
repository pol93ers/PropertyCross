package mdpa.lasalle.propertycross.data.adapter;

import android.net.Uri;
import android.support.annotation.NonNull;

import mdpa.lasalle.propertycross.ui.adapters.AdapterRecyclerMain;

public class PropertyItem implements AdapterRecyclerMain.PropertyItem<PropertyItem.Property> {

    public static class Property implements AdapterRecyclerMain.PropertyItem.Property {
        private String id, address, price, meters, type;
        private Uri uri;
        private boolean isFavourite;

        public Property(@NonNull String id, @NonNull String address, Uri uri, @NonNull String price, @NonNull String meters, @NonNull String type, boolean isFavourite) {
            this.id = id;
            this.address = address;
            this.uri = uri;
            this.price = price;
            this.meters = meters;
            this.type = type;
            this.isFavourite = isFavourite;
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
