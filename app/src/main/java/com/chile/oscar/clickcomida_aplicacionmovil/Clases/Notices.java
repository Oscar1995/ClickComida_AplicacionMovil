package com.chile.oscar.clickcomida_aplicacionmovil.Clases;

/**
 * Created by Oscar on 16-07-2017.
 */

public class Notices
{
    public int getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(int noticeId) {
        this.noticeId = noticeId;
    }

    public int getVacantsNotice() {
        return vacantsNotice;
    }

    public void setVacantsNotice(int vacantsNotice) {
        this.vacantsNotice = vacantsNotice;
    }

    public int getAvailableNotice() {
        return availableNotice;
    }

    public void setAvailableNotice(int availableNotice) {
        this.availableNotice = availableNotice;
    }

    public String getDateNotice() {
        return dateNotice;
    }

    public void setDateNotice(String dateNotice) {
        this.dateNotice = dateNotice;
    }

    public String getRequirementsNotice() {
        return requirementsNotice;
    }

    public void setRequirementsNotice(String requirementsNotice) {
        this.requirementsNotice = requirementsNotice;
    }

    int noticeId, vacantsNotice, availableNotice;
    String dateNotice, requirementsNotice;
}
