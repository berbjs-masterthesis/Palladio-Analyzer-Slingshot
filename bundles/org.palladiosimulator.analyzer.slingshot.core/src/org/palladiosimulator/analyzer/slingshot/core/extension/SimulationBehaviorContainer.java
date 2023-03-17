package org.palladiosimulator.analyzer.slingshot.core.extension;

import java.util.List;
import java.util.stream.Collectors;

import com.google.inject.AbstractModule;

public class SimulationBehaviorContainer extends AbstractModule {

	private final AbstractSlingshotExtension extension;

	public SimulationBehaviorContainer(final AbstractSlingshotExtension extension) {
		this.extension = extension;
	}

	@Override
	public void configure() {
		this.extension.getBehaviorExtensions().stream()
			.filter(extension -> SimulationBehaviorExtension.class.isAssignableFrom(extension))
			.forEach(this::bind);


		this.extension.getBindee2provider().entrySet().stream().forEach(entry -> bind(entry.getKey()).toProvider(entry.getValue()));
	}

	public List<Class<? extends SimulationBehaviorExtension>> getExtensions() {
		return this.extension.getBehaviorExtensions().stream()
				.filter(extension -> SimulationBehaviorExtension.class.isAssignableFrom(extension))
				.map(extension -> (Class<? extends SimulationBehaviorExtension>) extension)
				.collect(Collectors.toUnmodifiableList());
	}

}
