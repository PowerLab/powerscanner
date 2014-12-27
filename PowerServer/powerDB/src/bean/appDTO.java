package bean;

public class appDTO {
	private int id;
	private String packagename;
	private double avg_power;

	public appDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public appDTO(int id, String packagename, double avg_power) {
		super();
		this.id = id;
		this.packagename = packagename;
		this.avg_power = avg_power;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPackagename() {
		return packagename;
	}

	public void setPackagename(String packagename) {
		this.packagename = packagename;
	}

	public double getAvg_power() {
		return avg_power;
	}

	public void setAvg_power(double avg_power) {
		this.avg_power = avg_power;
	}

	@Override
	public String toString() {
		return "appDTO [id=" + id + ", packagename=" + packagename
				+ ", avg_power=" + avg_power + "]";
	}
}
