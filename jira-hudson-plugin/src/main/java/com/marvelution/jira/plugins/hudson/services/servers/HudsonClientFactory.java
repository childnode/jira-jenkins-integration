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

package com.marvelution.jira.plugins.hudson.services.servers;

import com.marvelution.hudson.plugins.apiv2.client.Host;
import com.marvelution.hudson.plugins.apiv2.client.HudsonClient;

/**
 * {@link HudsonClient} factory interface
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public interface HudsonClientFactory {

	/**
	 * Create a {@link HudsonClient} for the given {@link Host}
	 * 
	 * @param host the {@link Host} to get the {@link HudsonClient} for
	 * @return the {@link HudsonClient}
	 */
	HudsonClient create(Host host);

	/**
	 * Create a {@link HudsonClient} for the given {@link HudsonServer}
	 * 
	 * @param server the {@link HudsonServer} to get the {@link HudsonClient} for
	 * @return the {@link HudsonClient}
	 */
	HudsonClient create(HudsonServer server);

}
