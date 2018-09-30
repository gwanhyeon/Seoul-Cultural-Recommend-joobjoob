package net.joobjoob.app.seoul_culture_api.Common;

/**
 * Created by kgh on 2018. 8. 11..
 */
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.io.Serializable;

//comment culture Model
public class CulturalInfo implements Serializable {
    private String CULTCODE;
    private String SUBJCODE;
    private String CODENAME;
    private String TITLE;
    private String STRTDATE;
    private String END_DATE;
    private String TIME;
    private String PLACE;
    private String ORG_LINK;
    private String MAIN_IMG;
    private String HOMEPAGE;
    private String USE_TRGT;
    private String USE_FEE;
    private String SPONSOR;
    private String INQUIRY;
    private String SUPPORT;
    private String ETC_DESC;
    private String AGELIMIT;
    private String IS_FREE;
    private String TICKET;
    private String PROGRAM;
    private String PLAYER;
    private String CONTENTS;
    private String GCODE;

    public CulturalInfo(String cultcode, String subjcode, String codename, String title, String strtdate, String end_date, String time, String place, String org_link, String main_img, String homepage, String use_trgt, String use_fee, String sponsor, String inquiry, String support, String etc_desc, String agelimit, String is_free, String gcode) {
        this.CULTCODE = cultcode;
        this.SUBJCODE = subjcode;
        this.CODENAME = codename;
        this.TITLE = title;
        this.TIME = time;
        this.END_DATE = end_date;
        this.PLACE = place;
        this.STRTDATE = strtdate;
        this.ORG_LINK = org_link;
        this.MAIN_IMG = main_img;
        this.HOMEPAGE = homepage;
        this.USE_TRGT = use_trgt;
        this.IS_FREE = is_free;
        this.USE_FEE = use_fee;
        this.SPONSOR = sponsor;
        this.INQUIRY = inquiry;
        this.SUPPORT = support;
        this.ETC_DESC = etc_desc;
        this.AGELIMIT = agelimit;
        this.GCODE = gcode;
    }

//,this.TICKET = TICKET ,this.PROGRAM = PROGRAM , this.PLAYER = PLAYER ,this.CONTENTS = CONTENTS;;
    public CulturalInfo(String CULTCODE, String SUBJCODE, String CODENAME, String TITLE, String STRTDATE, String END_DATE, String TIME, String PLACE, String ORG_LINK, String MAIN_IMG, String HOMEPAGE, String USE_TRGT, String USE_FEE, String SPONSOR, String INQUIRY, String SUPPORT, String ETC_DESC, String AGELIMIT, String IS_FREE, String TICKET, String PROGRAM, String PLAYER, String CONTENTS, String GCODE) {
        this.CULTCODE = CULTCODE;//
        this.SUBJCODE = SUBJCODE;//
        this.CODENAME = CODENAME;//
        this.TITLE = TITLE;//
        this.STRTDATE = STRTDATE;
        this.END_DATE = END_DATE;//
        this.TIME = TIME;//
        this.PLACE = PLACE;//
        this.ORG_LINK = ORG_LINK;///
        this.MAIN_IMG = MAIN_IMG;//
        this.HOMEPAGE = HOMEPAGE;//
        this.USE_TRGT = USE_TRGT;//
        this.USE_FEE = USE_FEE;//
        this.SPONSOR = SPONSOR;//
        this.INQUIRY = INQUIRY;//
        this.SUPPORT = SUPPORT;//
        this.ETC_DESC = ETC_DESC;//
        this.AGELIMIT = AGELIMIT;//
        this.IS_FREE = IS_FREE;//
        this.TICKET = TICKET;
        this.PROGRAM = PROGRAM;
        this.PLAYER = PLAYER;
        this.CONTENTS = CONTENTS;
        this.GCODE = GCODE;//
    }

    public String getCULTCODE() {
        return this.CULTCODE;
    }

    public void setCULTCODE(String CULTCODE) {
        this.CULTCODE = CULTCODE;
    }

    public String getSUBJCODE() {
        return this.SUBJCODE;
    }

    public void setSUBJCODE(String SUBJCODE) {
        this.SUBJCODE = SUBJCODE;
    }

    public String getCODENAME() {
        return this.CODENAME;
    }

    public void setCODENAME(String CODENAME) {
        this.CODENAME = CODENAME;
    }

    public String getTITLE() {
        return this.TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getSTRTDATE() {
        return this.STRTDATE;
    }

    public void setSTRTDATE(String STRTDATE) {
        this.STRTDATE = STRTDATE;
    }

    public String getEND_DATE() {
        return this.END_DATE;
    }

    public void setEND_DATE(String END_DATE) {
        this.END_DATE = END_DATE;
    }

    public String getTIME() {
        return this.TIME;
    }

    public void setTIME(String TIME) {
        this.TIME = TIME;
    }

    public String getPLACE() {
        return this.PLACE;
    }

    public void setPLACE(String PLACE) {
        this.PLACE = PLACE;
    }

    public String getORG_LINK() {
        return this.ORG_LINK;
    }

    public void setORG_LINK(String ORG_LINK) {
        this.ORG_LINK = ORG_LINK;
    }

    public String getMAIN_IMG() {
        return this.MAIN_IMG;
    }

    public void setMAIN_IMG(String MAIN_IMG) {
        this.MAIN_IMG = MAIN_IMG;
    }

    public String getHOMEPAGE() {
        return this.HOMEPAGE;
    }

    public void setHOMEPAGE(String HOMEPAGE) {
        this.HOMEPAGE = HOMEPAGE;
    }

    public String getUSE_TRGT() {
        return this.USE_TRGT;
    }

    public void setUSE_TRGT(String USE_TRGT) {
        this.USE_TRGT = USE_TRGT;
    }

    public String getUSE_FEE() {
        return this.USE_FEE;
    }

    public void setUSE_FEE(String USE_FEE) {
        this.USE_FEE = USE_FEE;
    }

    public String getSPONSOR() {
        return this.SPONSOR;
    }

    public void setSPONSOR(String SPONSOR) {
        this.SPONSOR = SPONSOR;
    }

    public String getINQUIRY() {
        return this.INQUIRY;
    }

    public void setINQUIRY(String INQUIRY) {
        this.INQUIRY = INQUIRY;
    }

    public String getSUPPORT() {
        return this.SUPPORT;
    }

    public void setSUPPORT(String SUPPORT) {
        this.SUPPORT = SUPPORT;
    }

    public String getETC_DESC() {
        return this.ETC_DESC;
    }

    public void setETC_DESC(String ETC_DESC) {
        this.ETC_DESC = ETC_DESC;
    }

    public String getAGELIMIT() {
        return this.AGELIMIT;
    }

    public void setAGELIMIT(String AGELIMIT) {
        this.AGELIMIT = AGELIMIT;
    }

    public String getIS_FREE() {
        return this.IS_FREE;
    }

    public void setIS_FREE(String IS_FREE) {
        this.IS_FREE = IS_FREE;
    }

    public String getTICKET() {
        return this.TICKET;
    }

    public void setTICKET(String TICKET) {
        this.TICKET = TICKET;
    }

    public String getPROGRAM() {
        return this.PROGRAM;
    }

    public void setPROGRAM(String PROGRAM) {
        this.PROGRAM = PROGRAM;
    }

    public String getPLAYER() {
        return this.PLAYER;
    }

    public void setPLAYER(String PLAYER) {
        this.PLAYER = PLAYER;
    }

    public String getCONTENTS() {
        return this.CONTENTS;
    }

    public void setCONTENTS(String CONTENTS) {
        this.CONTENTS = CONTENTS;
    }

    public String getGCODE() {
        return this.GCODE;
    }

    public void setGCODE(String GCODE) {
        this.GCODE = GCODE;
    }
}
