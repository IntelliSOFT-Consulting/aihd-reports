/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.aihdreports.activator;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.BaseModuleActivator;
import org.openmrs.module.aihdreports.metadata.AihdReportsCommonMetadataBundle;
import org.openmrs.module.metadatadeploy.api.MetadataDeployService;

/**
 * This class contains the logic that is run every time this module is either started or stopped.
 */
public class AIHDReportsActivator extends BaseModuleActivator {
	
	protected Log log = LogFactory.getLog(getClass());

	public List<Initializer> getInitializers() {
		List<Initializer> l = new ArrayList<Initializer>();
		l.add(new AppConfigInitializer());
		l.add(new ReportInitializer());
		return l;
	}

	@Override
	public void started() {
		MetadataDeployService deployService = Context.getService(MetadataDeployService.class);
		// install commonly used metadata
		installCommonMetadata(deployService);
		log.info("AIHD Reports module started - initializing...");
		for (Initializer initializer : getInitializers()) {
			initializer.started();
		}
	}

	@Override
	public void stopped() {
		for (int i = getInitializers().size() - 1; i >= 0; i--) {
			getInitializers().get(i).stopped();
		}
		log.info("AIHD Reports module stopped");
	}

	private void installCommonMetadata(MetadataDeployService deployService) {
		try {
			deployService.installBundle(Context.getRegisteredComponents(AihdReportsCommonMetadataBundle.class).get(0));
		}
		catch (Exception e) {
			throw new RuntimeException("failed to install the common metadata ", e);
		}
	}
}
