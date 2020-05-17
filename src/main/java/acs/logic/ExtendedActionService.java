package acs.logic;

import java.util.List;

import acs.boundary.ActionBoundary;

public interface ExtendedActionService extends ActionService{

	public List<ActionBoundary> getAllActions(String adminDomain, String adminEmail, int size, int page);
}
