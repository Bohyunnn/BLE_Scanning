package com.example.hansung.ifindthanq.model;

/* BLE 검색 데이터 모델
 * @macAddress: BLE 검색 Mac 주소
 * @bleName: BLE 검색 이름
 */
public class SearchBLE {
    private String macAddress;
    private String bleName;

    public SearchBLE() {
    }

    public SearchBLE(String macAddress, String bleName) {
        this.macAddress = macAddress;
        this.bleName = bleName;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getBleName() {
        return bleName;
    }

    public void setBleName(String bleName) {
        this.bleName = bleName;
    }
}
