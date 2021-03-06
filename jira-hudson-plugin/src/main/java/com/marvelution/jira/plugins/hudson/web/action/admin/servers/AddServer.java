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

package com.marvelution.jira.plugins.hudson.web.action.admin.servers;

import java.util.ArrayList;
import java.util.Collection;

import com.marvelution.jira.plugins.hudson.services.servers.HudsonClientFactory;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServer;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServerManager;
import com.marvelution.jira.plugins.hudson.web.action.admin.KeyValuePair;
import com.marvelution.jira.plugins.hudson.web.action.admin.ModifyActionType;

/**
 * Add {@link HudsonServer} Web Action implementation
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@SuppressWarnings("unchecked")
public class AddServer extends AbstractModifyServer {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param serverManager the {@link HudsonServerManager} implementation
	 * @param clientFactory the {@link HudsonClientFactory} implementation
	 */
	protected AddServer(HudsonServerManager serverManager, HudsonClientFactory clientFactory) {
		super(serverManager, clientFactory);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doValidation() {
		super.doValidation();
		if (serverManager.hasServer(getName())) {
			addError("name", getText("hudson.server.name.duplicate", getName()));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveServer(String name, String description, String host, String publicHost,
					String username, String password, boolean includeInStreams) {
		serverManager.addServer(name, description, host, publicHost, username, password, includeInStreams, false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ModifyActionType getActionType() {
		return ModifyActionType.ADD_SERVER;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<KeyValuePair> getExtraHiddenInput() {
		return new ArrayList<KeyValuePair>();
	}

}
