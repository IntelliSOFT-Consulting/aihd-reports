package org.openmrs.module.aihdreports.metadata;

import org.openmrs.module.metadatadeploy.bundle.AbstractMetadataBundle;
import org.springframework.stereotype.Component;

@Component
public class AihdReportsCommonMetadataBundle extends AbstractMetadataBundle {

    public void install() throws Exception {

        //install privileges here
        log.info("Installing Privileges");
        install(Privileges.APP_AIHDREPORTS_PRV);
        log.info("Privileges installed");

        // install roles
        log.info("Installing roles");
        install(Roles.REPORT_MANAGER);
        log.info("Roles installed");
    }
}
