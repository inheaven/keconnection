package org.complitex.keconnection.heatmeter.entity;

/**
 * @author Anatoly A. Ivanov java@inheaven.ru
 *         Date: 17.09.12 16:08
 */
public class HeatmeterWrapper {
    private int row;
    private String organizationCode;
    private String buildingCode;
    private String ls;

    private String address;
    private String fileName;

    public HeatmeterWrapper(int row) {
        this.row = row;
    }

    @Override
    public String toString() {
        return  "row=" + row +
                ", organizationCode='" + organizationCode + '\'' +
                ", buildingCode='" + buildingCode + '\'' +
                ", ls='" + ls + '\'' +
                ", fileName='" + fileName + '\'' ;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getBuildingCode() {
        return buildingCode;
    }

    public void setBuildingCode(String buildingCode) {
        this.buildingCode = buildingCode;
    }

    public String getLs() {
        return ls;
    }

    public void setLs(String ls) {
        this.ls = ls;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
