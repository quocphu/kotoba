package bean;

public class CSVHeader {
	private String header;
	private String property;
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public CSVHeader(){
		
	}
	public CSVHeader(String header, String property) {
		this.header = header;
		this.property = property;
	}
	public String toString(){
		return this.header;
	}
}
