package org.palladiosimulator.analyzer.slingshot.eventdriver.entity;

import org.palladiosimulator.analyzer.slingshot.eventdriver.returntypes.Result;

@FunctionalInterface
public interface EventHandler<T> {
	
	public Result<?> acceptEvent(final T event) throws Exception;
	
}
