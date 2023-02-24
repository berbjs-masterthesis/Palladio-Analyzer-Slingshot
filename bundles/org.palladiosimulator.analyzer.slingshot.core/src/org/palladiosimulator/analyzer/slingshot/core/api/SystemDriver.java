package org.palladiosimulator.analyzer.slingshot.core.api;

import org.palladiosimulator.analyzer.slingshot.common.events.SystemEvent;

public interface SystemDriver {

	void postEvent(final SystemEvent systemEvent);

	void postEventAndThen(final SystemEvent systemEvent, final Runnable after);

	boolean isRunning();

}
