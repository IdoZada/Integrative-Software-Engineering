package acs;

public class Element {
	private ElementID elementId;

	public Element() {
		
	}

	public Element(ElementID elementId) {
		this.elementId = elementId;
	}

	public ElementID getElementId() {
		return elementId;
	}

	public void setElementId(ElementID elementId) {
		this.elementId = elementId;
	}
}
