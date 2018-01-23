package org.openmrs.module.aihdreports.api.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.aihdreports.api.AihdReportsService;
import org.openmrs.module.aihdreports.api.db.AihdReportsDAO;

/**
 * It is a default implementation of {@link AihdReportsService}.
 */
public class AihdReportsServiceImpl extends BaseOpenmrsService implements AihdReportsService {

	protected final Log log = LogFactory.getLog(this.getClass());

	private AihdReportsDAO dao;

	/**
	 * @return the dao
	 */
	public AihdReportsDAO getDao() {
		return dao;
	}

	/**
	 * @param dao the dao to set
	 */
	public void setDao(AihdReportsDAO dao) {
		this.dao = dao;
	}
}
