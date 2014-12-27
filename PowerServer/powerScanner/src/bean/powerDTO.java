package bean;

public class powerDTO {
	private int id;
	private String packagename;
	private int total;
	private int led;
	private int cpu;
	private int wifi;
	private int threeg;
	private int gps;
	private int audio;
	private int time;
	
	public powerDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public powerDTO(int id, String packagename, int total, int led, int cpu,
			int wifi, int threeg, int gps, int audio, int time) {
		super();
		this.id = id;
		this.packagename = packagename;
		this.total = total;
		this.led = led;
		this.cpu = cpu;
		this.wifi = wifi;
		this.threeg = threeg;
		this.gps = gps;
		this.audio = audio;
		this.time = time;
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
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getLed() {
		return led;
	}
	public void setLed(int led) {
		this.led = led;
	}
	public int getCpu() {
		return cpu;
	}
	public void setCpu(int cpu) {
		this.cpu = cpu;
	}
	public int getWifi() {
		return wifi;
	}
	public void setWifi(int wifi) {
		this.wifi = wifi;
	}
	public int getThreeg() {
		return threeg;
	}
	public void setThreeg(int threeg) {
		this.threeg = threeg;
	}
	public int getGps() {
		return gps;
	}
	public void setGps(int gps) {
		this.gps = gps;
	}
	public int getAudio() {
		return audio;
	}
	public void setAudio(int audio) {
		this.audio = audio;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "powerDTO [id=" + id + ", packagename=" + packagename
				+ ", total=" + total + ", led=" + led + ", cpu=" + cpu
				+ ", wifi=" + wifi + ", threeg=" + threeg + ", gps=" + gps
				+ ", audio=" + audio + ", time=" + time + "]";
	}
}
