package acs.logic;


import acs.boundary.ElementBoundary;
import acs.boundary.ElementIdBoundary;

public interface ExtendedElementService  extends ElementService {
	public void bindExistingElementToAnExistingChildElement(String managerDomain,String managerEmail,String originElementDomain, String originElementId, ElementIdBoundary elementIdBoundary);
	public ElementBoundary[] getAllChildrenOfAnExistingElement(String userDomain,String userEmail,String originElementDomain,String originElementId);
	public ElementBoundary[] getAnArrayWithElementParent(String userDomain,String userEmail,String originElementDomain,String originElementId);
}
