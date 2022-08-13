package org.palladiosimulator.analyzer.slingshot.core.extension;

import java.util.List;
import java.util.Collections;
import java.util.LinkedList;

import com.google.inject.AbstractModule;

public abstract class AbstractSlingshotExtension extends AbstractModule {
	
	private List<Class<? extends SimulationBehaviorExtension>> behaviorExtensions;
	
	protected final void install(final Class<? extends SimulationBehaviorExtension> behaviorExtension) {
		if (this.behaviorExtensions == null) {
			this.behaviorExtensions = new LinkedList<>();
		}
		
		this.behaviorExtensions.add(behaviorExtension);
	}
	
	@Override
	protected abstract void configure();
	
	public final List<Class<? extends SimulationBehaviorExtension>> getBehaviorExtensions() {
		if (this.behaviorExtensions == null) {
			return Collections.emptyList();
		} else {
			return Collections.unmodifiableList(this.behaviorExtensions);
		}
	}
	
	public abstract String getName();
	
}