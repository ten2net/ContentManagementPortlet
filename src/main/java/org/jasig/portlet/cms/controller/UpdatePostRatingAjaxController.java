/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.portlet.cms.controller;

import java.util.Collections;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.portlet.cms.model.Post;
import org.jasig.portlet.cms.model.UpdatePostRatingResponse;
import org.jasig.portlet.cms.model.repository.RepositoryDao;
import org.jasig.web.portlet.mvc.AbstractAjaxController;
import org.springframework.web.portlet.bind.PortletRequestUtils;

public class UpdatePostRatingAjaxController extends AbstractAjaxController {
	
	private final Log		logger			= LogFactory.getLog(getClass());
	
	private RepositoryDao	repositoryDao	= null;
	
	private RepositoryDao getRepositoryDao() {
		return repositoryDao;
	}
	
	@Override
	protected Map<String, ?> handleAjaxRequestInternal(final ActionRequest request,
			final ActionResponse resp)
					throws Exception {
		
		UpdatePostRatingResponse response = new UpdatePostRatingResponse();
		
		final String postPath = PortletRequestUtils.getRequiredStringParameter(request, "postPath");
		final int rateValue = PortletRequestUtils.getRequiredIntParameter(request, "rateValue");
		
		if (logger.isDebugEnabled()) {
			logger.debug("Retrieving repository post at " + postPath);
			logger.debug("Post rate value is " + rateValue);
		}
		
		final Post post = getRepositoryDao().getPost(postPath);
		if (post != null) {
			
			if (post.getRate() == 0)
				post.setRate(rateValue);
			else
				post.setRate((post.getRate() + rateValue) / 2);
			
			post.setRateCount(post.getRateCount() + 1);
			
			getRepositoryDao().setPost(post);
			response.setUpdateSuccessful(true);
		}
		
		response.setPost(post);
		
		return Collections.singletonMap("response", response);
	}
	
	public void setRepositoryDao(final RepositoryDao repositoryDao) {
		this.repositoryDao = repositoryDao;
	}
	
	
}
