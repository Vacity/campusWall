package com.team.nju.campuswall.Model;

import java.util.Date;

/**
 * Created by user on 2016/9/19.
 */
public class MessageModel {
    int Acid;
    int Acsponsorid;
    String AcsponsT;
    String Accontent;
    String Actitle;
    int Acisliked;
    String Acsponsorname;
    int AccommentN;
    int AclikeN;

    public int isliked() {
        return Acisliked;
    }

    public void setIsliked(int isliked) {
        Acisliked = isliked;
    }

    public String getAcsponsorname() {
        return Acsponsorname;
    }

    public void setAcsponsorname(String acsponsorname) {
        Acsponsorname = acsponsorname;
    }

    public int getAccommentN() {
        return AccommentN;
    }

    public void setAccommentN(int accommentN) {
        AccommentN = accommentN;
    }

    public int getAcid() {
        return Acid;
    }

    public void setAcid(int acid) {
        Acid = acid;
    }

    public int getAclikeN() {
        return AclikeN;
    }

    public void setAclikeN(int aclikeN) {
        AclikeN = aclikeN;
    }

    public int getAcsponsorid() {
        return Acsponsorid;
    }

    public void setAcsponsorid(int acsponsorid) {
        Acsponsorid = acsponsorid;
    }

    public String getAccontent() {
        return Accontent;
    }

    public void setAccontent(String accontent) {
        Accontent = accontent;
    }

    public String getAcsponsT() {
        return AcsponsT;
    }

    public void setAcsponsT(String acsponsT) {
        AcsponsT = acsponsT;
    }

    public String getActitle() {
        return Actitle;
    }

    public void setActitle(String actitle) {
        Actitle = actitle;
    }


}
