package manage;

import java.util.HashMap;

import entity.Service;

public class ServiceManager {
	
	private HashMap<Integer, Service> serviceList;

	public ServiceManager() {
		serviceList = new HashMap<Integer, Service>();
	}
	
	public HashMap<Integer, Service> getServiceList() {
		return serviceList;
	}
	
	public void setServiceList(HashMap<Integer, Service> serviceList) {
		this.serviceList = serviceList;
	}
	
	
}
