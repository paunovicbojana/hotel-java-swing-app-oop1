package entity;


import enums.ServiceType;

public class Service {
	protected int id;
	protected String serviceName;
	protected ServiceType serviceType;
	protected double price;
	protected boolean isDeleted;
	protected int pricelistID;
	

	protected Service(int id, String serviceName, ServiceType serviceType, double price, boolean isDeleted, int pricelistID) {
		
		this.id = id;
		this.serviceName = serviceName;
		this.serviceType = serviceType;
		this.price = price;
		this.isDeleted = isDeleted;
		this.pricelistID = pricelistID;
		
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getServiceName() {
		return serviceName;
	}


	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}


	public ServiceType getServiceType() {
		return serviceType;
	}


	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}


	public double getPrice() {
		return price;
	}


	public void setPrice(double price) {
		this.price = price;
	}


	public boolean isDeleted() {
		return isDeleted;
	}


	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}


	public int getPricelistID() {
		return pricelistID;
	}


	public void setPricelistID(int pricelistID) {
		this.pricelistID = pricelistID;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (obj instanceof Service) {
			Service service = (Service) obj;
			return service.getServiceName().equals(this.getServiceName());
		}
		return false;
	}
	
	

}
