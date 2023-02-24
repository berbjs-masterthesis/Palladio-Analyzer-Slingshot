package org.palladiosimulator.analyzer.slingshot.ui;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.palladiosimulator.analyzer.slingshot.core.Slingshot;
import org.palladiosimulator.analyzer.slingshot.workflow.SimulationWorkflowPlugin;

public class SlingshotUIPlugin extends Plugin implements BundleActivator {

	private static SlingshotUIPlugin instance = null;

	// Activate the workflow as well
	private SimulationWorkflowPlugin workflowPlugin = null;

	private Slingshot slingshot = null;

	@Override
	public void start(final BundleContext context) throws Exception {
		instance = this;
		slingshot = Slingshot.getInstance();
		workflowPlugin = SimulationWorkflowPlugin.getInstance();
		super.start(context);
	}

	@Override
	public void stop(final BundleContext context) throws Exception {
		instance = null;
		slingshot = null;
		super.stop(context);
	}



}
