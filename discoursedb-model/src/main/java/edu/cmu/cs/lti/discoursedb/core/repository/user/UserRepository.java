package edu.cmu.cs.lti.discoursedb.core.repository.user;

import java.util.Optional;

import edu.cmu.cs.lti.discoursedb.core.model.user.User;
import edu.cmu.cs.lti.discoursedb.core.repository.BaseRepository;

public interface UserRepository extends BaseRepository<User,Long>{
    
	public Optional<User> findById(Long id);    
    
	public long countByRealname(String realname);
    
}
