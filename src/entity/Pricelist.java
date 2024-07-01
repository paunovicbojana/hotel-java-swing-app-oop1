package entity;
import java.time.LocalDate;
import java.util.HashMap;


public class Pricelist {
	protected int id;
	protected LocalDate dateFrom;
	protected LocalDate dateTo;
	protected String name;
	protected HashMap<Integer, Service> services;
	protected boolean isDeleted;
	protected int priority;
	
	public Pricelist(int id, String name, LocalDate dateFrom, LocalDate dateTo, boolean isDeleted, int priority) {
        this.id = id;
		this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.isDeleted = isDeleted;
        this.services = new HashMap<Integer, Service>();
        this.name = name;
        this.priority = priority;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public LocalDate getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(LocalDate dateFrom) {
		this.dateFrom = dateFrom;
	}

	public LocalDate getDateTo() {
		return dateTo;
	}

	public void setDateTo(LocalDate dateTo) {
		this.dateTo = dateTo;
	}
	
	public HashMap<Integer, Service> getServices() {
		return services;
	}
	
	public void setServices(HashMap<Integer, Service> services) {
		this.services = services;
	}
	
	public boolean isDeleted() {
		return isDeleted;
	}
	
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getPriority() {
		return priority;
	}
	
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public String toFileString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(id);
		stringBuilder.append(",");
		stringBuilder.append(name);
		stringBuilder.append(",");
		stringBuilder.append(dateFrom);
		stringBuilder.append(",");
		stringBuilder.append(dateTo);
		stringBuilder.append(",");
		stringBuilder.append(isDeleted);
		stringBuilder.append(",");
		stringBuilder.append(priority);
		return stringBuilder.toString();
	}

}
