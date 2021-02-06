package com.helius.dao;

import java.sql.Timestamp;

import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.helius.entities.MyRevision;

public class MyRevisionListener implements RevisionListener {

	@Override
	public void newRevision(Object revisionEntity) {
		MyRevision rev = (MyRevision) revisionEntity;
		//Identity identity = (Identity) Component.getInstance("org.jboss.seam.security.identity");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String name ="";
		if (auth != null) {
			if (auth.getName() != null) {
				name = auth.getName();
			} else {
				name = "anonymousUser";
			}
		} else {
			name = "anonymousUser";
		}
		System.out.println("=======name===="+name);
		rev.setUserName(name);
		rev.setModifiedDate(new Timestamp(rev.getTimestamp()));
	}
}
