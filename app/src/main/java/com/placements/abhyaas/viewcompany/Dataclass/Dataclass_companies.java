package com.placements.abhyaas.viewcompany.Dataclass;

public class Dataclass_companies {
    String Name;
    String Category;
    String photoURL;
    String job;
    String cpi;
    String CTC;
    String skills;
    String work;
    Dataclass_companies()
    {

    }
    public Dataclass_companies(String Name, String Category, String photoURL, String job, String cpi, String CTC,String skills,String work){
        this.Name= Name;
        this.Category=Category;
        this.photoURL= photoURL;
        this.cpi= cpi;
        this.job= job;
        this.CTC= CTC;
        this.skills=skills;
        this.work=work;
    }




    public String getName() {
        return Name;
    }

    public String getCategory() {
        return Category;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public String getjob() {
        return job;
    }

    public String getcpi() {
        return cpi;
    }
    public String getCTC()
    {
        return CTC;
    }
    public String getSkills(){ return skills;}
    public String getwork(){ return work;}
}
