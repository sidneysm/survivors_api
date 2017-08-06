package br.com.sidney.survivor.model;

public class ReportInfectedPercentage {
	private String description;
	private double percentage;
	
	public ReportInfectedPercentage(double percentage) {
		description = "Percentage of infected survivors";
		this.percentage = percentage;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	@Override
	public String toString() {
		return String.format("ReportInfectedPercentage [description=%s, percentage=%2.f]", description, percentage);
	}
	
	
}
