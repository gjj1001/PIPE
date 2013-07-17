package cn.com.casit.vo;

import java.io.Serializable;

public class ReportEventBean implements Serializable{
	//private int id;
	private int inspectstaffid;//userid
	private int deviceid;//1001
	private int pollingeventid;
	private String reporttime;
	private String pipecode;
	private String description;
//	private String photo;
	private double longitude;
	private double latitude;
//	private int status;
	
	public int getInspectstaffid() {
		return inspectstaffid;
	}
	public void setInspectstaffid(int inspectstaffid) {
		this.inspectstaffid = inspectstaffid;
	}
	public int getDeviceid() {
		return deviceid;
	}
	public void setDeviceid(int deviceid) {
		this.deviceid = deviceid;
	}
	public int getPollingeventid() {
		return pollingeventid;
	}
	public void setPollingeventid(int pollingeventid) {
		this.pollingeventid = pollingeventid;
	}
	public String getReporttime() {
		return reporttime;
	}
	public void setReporttime(String reporttime) {
		this.reporttime = reporttime;
	}
	public String getPipecode() {
		return pipecode;
	}
	public void setPipecode(String pipecode) {
		this.pipecode = pipecode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}


}
