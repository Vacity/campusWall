package com.team.nju.campuswall.Model;

/**
 * Created by Senjoey on 16/09/29.
 */
public class CommentModel {
    int Commentid;
    String Comertel;
    String Comcontent;

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
