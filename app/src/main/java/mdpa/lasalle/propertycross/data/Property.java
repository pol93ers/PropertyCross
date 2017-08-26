package mdpa.lasalle.propertycross.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Property implements Serializable{

    @SerializedName("_id") private String id;
    @SerializedName("name")private String name;
    @SerializedName("description") private String description;
    @SerializedName("zipcode") private String zipcode;
    @SerializedName("address") private String address;
    @SerializedName("city") private String city;
    @SerializedName("price") private int price;
    @SerializedName("type") private String propertyType;
    @SerializedName("views") private int views;
    @SerializedName("location") private Location location;
    @SerializedName("m2") private int area;
    @SerializedName("owner") private Owner user;
    @SerializedName("images") private ArrayList<String> images;

    public Property(String id, String name, String description, String zipcode, String address, String city, int price, String propertyType, int views, Location location, int area, Owner user, ArrayList<String> images) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.zipcode = zipcode;
        this.address = address;
        this.city = city;
        this.price = price;
        this.propertyType = propertyType;
        this.views = views;
        this.location = location;
        this.area = area;
        this.user = user;
        this.images = images;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public Owner getUser() {
        return user;
    }

    public void setUser(Owner user) {
        this.user = user;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "Property{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", zipcode='" + zipcode + '\'' +
                ", city='" + city + '\'' +
                ", price=" + price +
                ", propertyType='" + propertyType + '\'' +
                ", views=" + views +
                ", location=" + location +
                ", area=" + area +
                ", user=" + user +
                ", images=" + images +
                '}';
    }
}
