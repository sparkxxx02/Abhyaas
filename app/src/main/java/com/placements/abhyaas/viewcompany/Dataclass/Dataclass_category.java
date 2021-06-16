package com.placements.abhyaas.viewcompany.Dataclass;

public class Dataclass_category {
    String Category;
    String photo_cat;

    Dataclass_category()
    {

    }
    Dataclass_category(String Category, String photo_cat){
        this.Category=Category;
        this.photo_cat=photo_cat;

    }



    public String getCategory() {
        return Category;
    }



    public String getPhoto_cat() {
        return photo_cat;
    }


}
