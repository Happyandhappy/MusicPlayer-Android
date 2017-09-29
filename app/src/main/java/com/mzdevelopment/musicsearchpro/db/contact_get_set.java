package com.mzdevelopment.musicsearchpro.db;


public class contact_get_set
{

    private String categoryId;
    private String contactID;
    private String email;
    private String isRegister;
    private String name;
    private String phonenumber;

    public contact_get_set()
    {
    }

    public String getCategoryId()
    {
        return categoryId;
    }

    public String getContactID()
    {
        return contactID;
    }

    public String getEmail()
    {
        return email;
    }

    public String getIsRegister()
    {
        return isRegister;
    }

    public String getName()
    {
        return name;
    }

    public String getPhonenumber()
    {
        return phonenumber;
    }

    public void setCategoryId(String s)
    {
        categoryId = s;
    }

    public void setContactID(String s)
    {
        contactID = s;
    }

    public void setEmail(String s)
    {
        email = s;
    }

    public void setIsRegister(String s)
    {
        isRegister = s;
    }

    public void setName(String s)
    {
        name = s;
    }

    public void setPhonenumber(String s)
    {
        phonenumber = s;
    }
}
