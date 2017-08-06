package br.com.sidney.survivor.model;

public class ReportNonInfectedPercentage {
	private String description;
	private double percentage;
	
	public ReportNonInfectedPercentage(double percentage) {
		description = "Percentage of non infected survivors";
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
		return String.format("ReportNonInfectedPercentage [description=%s, percentage=%2.f]", description, percentage);
	}
	
	
}
