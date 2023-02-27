package org.palladiosimulator.analyzer.slingshot.common.events;

import com.google.common.reflect.TypeToken;

public interface ReifiedEvent<T> {
	
	public TypeToken<T> getTypeToken();
	
}
