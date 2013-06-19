package org.vpac.ndg.storage.dao;

import java.util.List;
import org.apache.commons.lang.NotImplementedException;
import org.springframework.transaction.annotation.Transactional;
import org.vpac.ndg.storage.model.Band;
import org.vpac.ndg.storage.model.Dataset;
import org.vpac.ndg.storage.util.CustomHibernateDaoSupport;

public class BandDaoImpl extends CustomHibernateDaoSupport implements BandDao {
	public BandDaoImpl() {
	}

	@Transactional
	@Override
	public Band create(Band b) {
		throw new NotImplementedException("Use DatasetDao");
	}

	@Transactional
	@Override
	public void update(Band b) {
		getHibernateTemplate().update(b);
	}

	@Transactional
	@Override
	public void delete(Band b) {
		getHibernateTemplate().delete(b);
	}

	@Transactional
	@Override
	public Band retrieve(String id) {
		return getHibernateTemplate().get(Band.class, id);
	}

	@Transactional
	@Override
	public Band find(String datasetId, String bandName) {
		@SuppressWarnings("unchecked")
		List<Band> list = getHibernateTemplate().find(
				"SELECT b FROM Dataset d join d.bands b WHERE d.id=? AND b.name=?",
				datasetId, bandName);
		if(list.size() <= 0)
			return null;
		return list.get(0);
	}

	@Transactional
	@Override
	public Dataset getParentDataset(String bandId) {
		@SuppressWarnings("unchecked")
		List<Dataset> list = getHibernateTemplate().find("SELECT d FROM Dataset d join d.bands b WHERE b.id=?", bandId);
		if(list.size() <= 0)
			return null;
		return list.get(0);
	}

}