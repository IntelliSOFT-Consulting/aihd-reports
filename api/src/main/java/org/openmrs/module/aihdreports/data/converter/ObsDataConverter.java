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
package org.openmrs.module.aihdreports.data.converter;

import org.openmrs.Obs;
import org.openmrs.module.aihdreports.reporting.metadata.Dictionary;
import org.openmrs.module.aihdreports.reporting.utils.CoreUtils;
import org.openmrs.module.reporting.data.converter.DataConverter;


public class ObsDataConverter implements DataConverter {
    @Override
    public Object convert(Object obj) {
        if (obj == null) {
            return "";
        }

        Obs obs = ((Obs) obj);

        if (obs.getValueCoded() != null) {
            if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.YES)) || obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.UNDER_TREATMENT))){
                return "Y";
            }
            else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.NO))){
                return "N";
            }
            else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.NEW_VISIT))){
                return "F";
            }
            else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.REVISIT))){
                return "R";
            }
            else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.LOW_CURRENT_RISK))){
                return "Low current risk";
            }
            else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.AT_RISK))){
                return "At risk";
            }
            else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.HIGH_RISK))){
                return "High risk";
            }
            else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.ULCERATED_FOOT))){
                return "Ulcerated foot";
            }
            else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.FOOT_EMERGENCY))){
                return "Foot emergency";
            }
            else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.POSITIVE))){
                return "Positive";
            }
            else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.NEGATIVE))){
                return "Negative";
            }
            else if(obs.getValueCoded().equals(Dictionary.getConcept(Dictionary.UNDER_TREATMENT))){
                return "Y";
            }
            else {
                return "";
            }
        }

        else if (obs.getValueDate() != null) {
            return CoreUtils.formatDates(obs.getValueDate());
        }

        else if (obs.getValueDatetime() != null) {
            return CoreUtils.formatDates(obs.getValueDatetime());
        }

        else if (obs.getValueNumeric() != null) {
            return obs.getValueNumeric().toString();
        }

        else if (obs.getValueText() != null) {
            return obs.getValueText();
        }

        return null;
    }

    @Override
    public Class<?> getInputDataType() {
        return Obs.class;
    }

    @Override
    public Class<?> getDataType() {
        return String.class;
    }
}
