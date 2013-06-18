package org.vpac.ndg.storage.dao;

import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.transaction.annotation.Transactional;
import org.vpac.ndg.storage.model.Dataset;
import org.vpac.ndg.storage.model.TimeSlice;
import org.vpac.ndg.storage.util.CustomHibernateDaoSupport;

public class TimeSliceDaoImpl extends CustomHibernateDaoSupport implements TimeSliceDao {
	public TimeSliceDaoImpl() {
	}
	@Transactional
	@Override
	public TimeSlice create(TimeSlice ds) {
		throw new NotImplementedException("please use DatasetDao");
	}
	@Transactional
	@Override
	public void update(TimeSlice ts) {
		getHibernateTemplate().update(ts);
	}
	@Transactional
	@Override
	public void delete(TimeSlice ts) {
		getHibernateTemplate().delete(ts);
	}
	@Transactional
	@Override
	public TimeSlice retrieve(String id) {
		return getHibernateTemplate().get(TimeSlice.class, id);
	}
	@Transactional
	@Override
	public Dataset getParentDataset(String timeSliceId) {
		@SuppressWarnings("unchecked")
		List<Dataset> list = getHibernateTemplate().find("SELECT d FROM Dataset d join d.slices s WHERE s.id=?", timeSliceId);
		if(list.size() <= 0)
			return null;
		return list.get(0);
	}
}