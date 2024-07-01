package entity;

import enums.ServiceType;

public class RoomType extends Service{
	
	protected int capacity;
	protected int beds;
	
	
	public RoomType(int id, String serviceName, int beds, int capacity,double price, boolean isDeleted, int pricelistID, ServiceType serviceType) {
		super(id, serviceName, serviceType, price, isDeleted, pricelistID);
		this.capacity = capacity;
		this.beds = beds;
	}
	
	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	
	
	public int getBeds() {
		return beds;
	}
	
	public void setBeds(int beds) {
		this.beds = beds;
	}	
	
	public String toFileString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(id);
		stringBuilder.append(",");
		stringBuilder.append(serviceName);
		stringBuilder.append(",");
		stringBuilder.append(beds);
		stringBuilder.append(",");
		stringBuilder.append(capacity);
		stringBuilder.append(",");
		stringBuilder.append(price);
		stringBuilder.append(",");
		stringBuilder.append(isDeleted);
		stringBuilder.append(",");
		stringBuilder.append(pricelistID);
		stringBuilder.append(",");
		stringBuilder.append(serviceType);
		return stringBuilder.toString();

	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof RoomType)) {
			return false;
		}
		else {
		return serviceName.equals(((RoomType) other).serviceName);
	}}
}
