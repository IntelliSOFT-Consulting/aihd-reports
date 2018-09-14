package org.openmrs.module.aihdreports.metadata;

import org.openmrs.module.metadatadeploy.descriptor.PrivilegeDescriptor;
import org.openmrs.util.PrivilegeConstants;

public class Privileges {

    public static PrivilegeDescriptor APP_AIHDREPORTS_PRV = new PrivilegeDescriptor() {
        public String uuid() {
            return "5eaac022-b812-11e8-9f1d-4f049800f6d2";
        }

        public String privilege() {
            return "App: aihdreports.viewReports";
        }

        public String description() {
            return "Access to the Reports app";
        }
    };

    public static PrivilegeDescriptor PRV_GET_PATIENTS = new PrivilegeDescriptor() {
        public String uuid() {
            return "802d5b4e-b81a-11e8-a218-535c8ca87942";
        }

        public String privilege() {
            return PrivilegeConstants.GET_PATIENTS;
        }

        public String description() {
            return "Get patients";
        }
    };

    public static PrivilegeDescriptor PRV_GET_CONCEPTS = new PrivilegeDescriptor() {
        public String uuid() {
            return "30355898-b81b-11e8-8441-5b70e5bf8ec0";
        }

        public String privilege() {
            return PrivilegeConstants.GET_CONCEPTS;
        }

        public String description() {
            return "Get concepts";
        }
    };

    public static PrivilegeDescriptor PRV_GET_USERS = new PrivilegeDescriptor() {
        public String uuid() {
            return "c688ed0a-b81b-11e8-96f7-13f6ed506608";
        }

        public String privilege() {
            return PrivilegeConstants.GET_USERS;
        }

        public String description() {
            return "Get users";
        }
    };

    public static PrivilegeDescriptor PRV_GET_LOCATIONS = new PrivilegeDescriptor() {
        public String uuid() {
            return "508dd48a-b820-11e8-9c64-fff2b21c8de0";
        }

        public String privilege() {
            return PrivilegeConstants.GET_LOCATIONS;
        }

        public String description() {
            return "Get Locations";
        }
    };
}
