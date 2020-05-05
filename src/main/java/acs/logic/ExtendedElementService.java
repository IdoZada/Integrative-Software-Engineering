package acs.logic;


import acs.boundary.ElementBoundary;
import acs.boundary.ElementIdBoundary;

public interface ExtendedElementService  extends ElementService {
	public void bindExistingElementToAnExistingChildElement(String originElementId, ElementIdBoundary elementIdBoundary);
	public ElementBoundary[] getAllChildrenOfAnExistingElement(String originElementId);
	public ElementBoundary[] getAnArrayWithElementParent();
}
