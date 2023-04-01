package org.palladiosimulator.analyzer.slingshot.eventdriver.entity;

import com.google.common.reflect.TypeToken;

/**
 * Events that have generics must implement this interface in order to recognize the generic parameter.
 * Otherwise, the generic parameter will be ignored, and every subscriber listening to the event will
 * be called, regardless of the actual generic parameter.
 * 
 * @author Julijan Katic
 *
 * @param <T> The generic parameter
 */
public interface ReifiedEvent<T> {
	
	public TypeToken<T> getTypeToken();
	
}
