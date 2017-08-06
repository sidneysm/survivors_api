package br.com.sidney.survivor.model;

public class ReportLostPoints {
	
	private String description;
	private int lostPoints;
	
	public ReportLostPoints(int lostPoints) {
		description = "Points lost because of infected survivor";
		this.lostPoints = lostPoints;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getLostPoints() {
		return lostPoints;
	}
	public void setLostPoints(int lostPoints) {
		this.lostPoints = lostPoints;
	}
	@Override
	public String toString() {
		return "ReportLostPoints [description=" + description + ", lostPoints=" + lostPoints + "]";
	}
	
		
}
