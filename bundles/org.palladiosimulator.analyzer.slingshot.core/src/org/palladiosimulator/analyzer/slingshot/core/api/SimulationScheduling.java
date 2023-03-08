package org.palladiosimulator.analyzer.slingshot.core.api;

import org.palladiosimulator.analyzer.slingshot.common.events.DESEvent;

public interface SimulationScheduling {
	
	public void scheduleEvent(final DESEvent event);
	
	public void scheduleEventAt(final DESEvent event, final double simulationTime);
	
}
