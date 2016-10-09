package com.team.nju.campuswall.Model;

/**
 * Created by Senjoey on 16/09/29.
 */
public class CommentModel {
    int Commentid;
    String Comertel;
    String Comcontent;
    String Comerimgurl;
    String ComerUalais;

    public String getComerimgurl() {
        return Comerimgurl;
    }

    public void setComerimgurl(String comerimgurl) {
        Comerimgurl = comerimgurl;
    }

    public String getComerUalais() {
        return ComerUalais;
    }

    public void setComerUalais(String comerUalais) {
        ComerUalais = comerUalais;
    }

    public String getComertel() {
        return Comertel;
    }

    public void setComertel(String comertel) {
        Comertel = comertel;
    }

    public String getComcontent() {
        return Comcontent;
    }

    public void setComcontent(String comcontent) {
        Comcontent = comcontent;
    }

    public int getCommentid() {

        return Commentid;
    }

    public void setCommentid(int commentid) {
        Commentid = commentid;
    }
}
