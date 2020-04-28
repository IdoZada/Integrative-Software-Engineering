package acs;

public class Element {
	private ElementId elementId;

	public Element() {
		
	}

	public Element(ElementId elementId) {
		this.elementId = elementId;
	}

	public ElementId getElementId() {
		return elementId;
	}

	public void setElementId(ElementId elementId) {
		this.elementId = elementId;
	}
}
