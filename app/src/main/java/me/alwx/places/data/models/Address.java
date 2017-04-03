package me.alwx.places.data.models;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.LinkedList;
import java.util.List;

public class Address {
    private String location;
    private String street;
    private String city;
    @SerializedName("post_code")
    private String postCode;
    private String country;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        List<String> list = new LinkedList<>();

        addToList(list, getLocation());
        addToList(list, getStreet());
        addToList(list, getCity());
        addToList(list, getPostCode());
        addToList(list, getCountry());

        return TextUtils.join(",", list);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        if (location != null ? !location.equals(address.location) : address.location != null)
            return false;
        if (street != null ? !street.equals(address.street) : address.street != null) return false;
        if (city != null ? !city.equals(address.city) : address.city != null) return false;
        if (postCode != null ? !postCode.equals(address.postCode) : address.postCode != null)
            return false;
        return country != null ? country.equals(address.country) : address.country == null;
    }

    @Override
    public int hashCode() {
        int result = location != null ? location.hashCode() : 0;
        result = 31 * result + (street != null ? street.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (postCode != null ? postCode.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        return result;
    }

    private void addToList(List<String> list, @Nullable String field) {
        if (field != null && !field.isEmpty()) {
            list.add(field);
        }
    }
}
