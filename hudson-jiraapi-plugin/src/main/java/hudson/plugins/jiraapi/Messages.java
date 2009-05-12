/*
 * Licensed to Marvelution under one or more contributor license 
 * agreements.  See the NOTICE file distributed with this work 
 * for additional information regarding copyright ownership.
 * Marvelution licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package hudson.plugins.jiraapi;

import org.jvnet.localizer.ResourceBundleHolder;

/**
 * Internationalisation implementation for messages within the Jira Project Key plugin
 *  
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class Messages {

	private static final ResourceBundleHolder HOLDER = ResourceBundleHolder.get(Messages.class);

	/**
	 * Gets the {@link JiraProjectKeyJobProperty} display name
	 * 
	 * @return the display name
	 */
	public static String getJiraKeyPropertyDisplayName() {
		return HOLDER.format("JiraKeyProperty.DisplayName", new Object[0]);
	}

}
