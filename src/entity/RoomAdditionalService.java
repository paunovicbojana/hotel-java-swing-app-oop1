package entity;

import enums.ServiceType;

public class RoomAdditionalService extends Service{

	public RoomAdditionalService(int id, String serviceName, ServiceType serviceType, double price, boolean isDeleted, int pricelistID) {
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

		if (obj instanceof RoomAdditionalService) {
			RoomAdditionalService service = (RoomAdditionalService) obj;
			return service.getServiceName().equals(this.getServiceName());
		}
		return false;
	}


}
