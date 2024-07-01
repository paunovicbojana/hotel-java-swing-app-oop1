package entity;
import enums.ServiceType;

public class AService extends Service {
	
	public AService(int id, String serviceName, ServiceType serviceType, double price, boolean isDeleted, int pricelistID) {
		super(id, serviceName, serviceType, price, isDeleted, pricelistID);
	}

	public String toFileString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(id);
		stringBuilder.append(",");
		stringBuilder.append(serviceName);
		stringBuilder.append(",");
		stringBuilder.append(serviceType);
		stringBuilder.append(",");
		stringBuilder.append(price);
		stringBuilder.append(",");
		stringBuilder.append(isDeleted);
		stringBuilder.append(",");
		stringBuilder.append(pricelistID);
		return stringBuilder.toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof AService) {
			AService service = (AService) obj;
			return service.getServiceName().equals(this.getServiceName());
		}
		return false;
	}
}