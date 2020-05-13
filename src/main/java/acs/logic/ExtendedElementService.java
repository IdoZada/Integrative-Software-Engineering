package acs.logic;


import java.util.List;

import acs.boundary.ElementBoundary;
import acs.boundary.ElementIdBoundary;


public interface ExtendedElementService  extends ElementService {
	public void bindExistingElementToAnExistingChildElement(String managerDomain,String managerEmail,String originElementDomain, String originElementId, ElementIdBoundary elementIdBoundary);
	public ElementBoundary[] getAllChildrenOfAnExistingElement(String userDomain,String userEmail,String originElementDomain,String originElementId);
	public ElementBoundary[] getAnArrayWithElementParent(String userDomain,String userEmail,String originElementDomain,String originElementId);
	public List<ElementBoundary> getAll(String userDomain, String userEmail, int size, int page);
	public ElementBoundary[] getAllChildrenOfAnExistingElement(String userDomain, String userEmail,
			String elementDomain, String elementId, int size, int page);
	public ElementBoundary[] getAnArrayWithElementParent(String userDomain, String userEmail, String elementDomain,
			String elementId, int size, int page);
	public ElementBoundary[] getAllByName(String userDomain, String userEmail, String name, int size, int page);
	public ElementBoundary[] getAllByType(String userDomain, String userEmail, String type, int size, int page);
	public ElementBoundary[] getAllByLocation(String userDomain, String userEmail, double lat, double lng,
			double distance, int size, int page);
}
