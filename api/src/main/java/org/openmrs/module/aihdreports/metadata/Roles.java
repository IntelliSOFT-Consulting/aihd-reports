package org.openmrs.module.aihdreports.metadata;

import org.openmrs.module.metadatadeploy.descriptor.PrivilegeDescriptor;
import org.openmrs.module.metadatadeploy.descriptor.RoleDescriptor;

import java.util.Arrays;
import java.util.List;

public class Roles {

    public static RoleDescriptor REPORT_MANAGER = new RoleDescriptor() {
        @Override
        public String role() {
            return "aihdreports Role: Report Manager";
        }

        @Override
        public String description() {
            return "Use to manage aihdreports only";
        }

        @Override
        public List<PrivilegeDescriptor> privileges() {
            return Arrays.asList(
                    Privileges.APP_AIHDREPORTS_PRV,
                    Privileges.PRV_GET_PATIENTS,
                    Privileges.PRV_GET_CONCEPTS,
                    Privileges.PRV_GET_USERS
            );
        }

        public String uuid() {
            return "a841423e-b811-11e8-97b9-a7a770591ec5";
        }
    };
}
