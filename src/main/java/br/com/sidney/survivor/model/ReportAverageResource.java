package br.com.sidney.survivor.model;

public class ReportAverageResource {
	private String description;
	private double waterAverage;
	private double foodAverage;
	private double medicationAverage;
	private double ammunitionAverage;
	public ReportAverageResource(double waterAverage, double foodAverage, double medicationAverage,
			double ammunitionAverage) {
		this.description = "Average amount of each kind of resource by survivor";
		this.waterAverage = waterAverage;
		this.foodAverage = foodAverage;
		this.medicationAverage = medicationAverage;
		this.ammunitionAverage = ammunitionAverage;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getWaterAverage() {
		return waterAverage;
	}
	public void setWaterAverage(double waterAverage) {
		this.waterAverage = waterAverage;
	}
	public double getFoodAverage() {
		return foodAverage;
	}
	public void setFoodAverage(double foodAverage) {
		this.foodAverage = foodAverage;
	}
	public double getMedicationAverage() {
		return medicationAverage;
	}
	public void setMedicationAverage(double medicationAverage) {
		this.medicationAverage = medicationAverage;
	}
	public double getAmmunitionAverage() {
		return ammunitionAverage;
	}
	public void setAmmunitionAverage(double ammunitionAverage) {
		this.ammunitionAverage = ammunitionAverage;
	}
	@Override
	public String toString() {
		return "ReportAverageResource [description=" + description + ", waterAverage=" + waterAverage + ", foodAverage="
				+ foodAverage + ", medicationAverage=" + medicationAverage + ", ammunitionAverage=" + ammunitionAverage
				+ "]";
	}
	
}
