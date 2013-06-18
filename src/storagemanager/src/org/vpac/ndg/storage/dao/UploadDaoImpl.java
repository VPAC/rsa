package org.vpac.ndg.storage.dao;

import org.springframework.transaction.annotation.Transactional;
import org.vpac.ndg.storage.model.TimeSlice;
import org.vpac.ndg.storage.model.Upload;
import org.vpac.ndg.storage.util.CustomHibernateDaoSupport;

public class UploadDaoImpl extends CustomHibernateDaoSupport implements UploadDao {

	@Transactional
	@Override
	public Upload create(Upload u) {
		// manual check cause no foreign key in database
		TimeSlice ts = getHibernateTemplate().get(TimeSlice.class, u.getTimeSliceId());
		if(ts == null)
			throw new IllegalArgumentException(String.format("TimeSlice \"%s\" not found.", u.getTimeSliceId()));

		getHibernateTemplate().save(u);
		return u;
	}

	@Transactional
	@Override
	public void update(Upload u) {
		getHibernateTemplate().update(u);
	}

	@Transactional
	@Override
	public void delete(Upload u) {
		getHibernateTemplate().delete(u);
	}

	@Transactional
	@Override
	public Upload retrieve(String id) {
		return getHibernateTemplate().get(Upload.class, id);
	}
}