package acs.logic;


import acs.boundary.ElementBoundary;
import acs.boundary.ElementIdBoundary;

public interface ExtendedElementService  extends ElementService {
	public void bindExistingElementToAnExistingChildElement(ElementIdBoundary elementIdBoundary);
	public ElementBoundary[] getAllChildrenOfAnExistingElement();
	public ElementBoundary[] getAnArrayWithElementParent();
}
