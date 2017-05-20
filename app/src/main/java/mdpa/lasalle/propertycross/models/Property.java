package mdpa.lasalle.propertycross.models;

import java.util.ArrayList;

/**
 * Created by Pol on 08/05/2017.
 */

public class Property {

    private String id;
    private String name;
    private String description;
    private String zipcode;
    private String city;
    private int price;
    private String propertyType;
    private int views;
    private Location location;
    private int area;
    private User user;
    private ArrayList<String> images;
    private double distance;
    private ArrayList<Comment> comments;

    public Property(String id, String name, String description, String zipcode, String city, int price, String propertyType, int views, Location location, int area, User user, ArrayList<String> images, double distance, ArrayList<Comment> comments) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.zipcode = zipcode;
        this.city = city;
        this.price = price;
        this.propertyType = propertyType;
        this.views = views;
        this.location = location;
        this.area = area;
        this.user = user;
        this.images = images;
        this.distance = distance;
        this.comments = comments;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }
}
