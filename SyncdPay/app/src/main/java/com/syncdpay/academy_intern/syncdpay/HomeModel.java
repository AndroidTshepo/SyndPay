package com.syncdpay.academy_intern.syncdpay;

/**
 * Created by Academy_Intern on 2016/01/22.
 */
public class HomeModel
{
    private String street_name;
    private String key_features;
    private String location;
    private String value;
    private String image;

    public void setImage(String image)
    {
        this.image = image;
    }
    public String getImage()
    {
        return image;
    }

    public void setStreet_name(String street_n)
    {
        this.street_name = street_n;
    }

    public String getStreet_name()
    {
        return street_name;
    }

    public void setKey_features(String key_f)
    {
        this.key_features = key_f;
    }

    public String getKey_features()
    {
        return key_features;
    }
    public void setLocation(String loc)
    {
        this.location = loc;
    }

    public String getLocation()
    {
        return location;
    }
    public void setValue(String value)
    {
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }
}

