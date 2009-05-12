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

package hudson.plugins.jiraapi.converters;

import com.marvelution.jira.plugins.hudson.model.Result;

/**
 * Converter class to convert a Hudson Result into a Jira Integration Model Result
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class HudsonResultConverter {

	/**
	 * Convert a Hudson Result into a Jira Integration Model Result
	 * 
	 * @param hudsonResult the Hudson Result to convert
	 * @return the converted {@link Result}
	 */
	public static Result convertHudsonResult(hudson.model.Result hudsonResult) {
		if (hudsonResult == hudson.model.Result.SUCCESS) {
			return Result.SUCCESS;
		} else if (hudsonResult == hudson.model.Result.FAILURE) {
			return Result.FAILURE;
		} else if (hudsonResult == hudson.model.Result.UNSTABLE) {
			return Result.UNSTABLE;
		} else if (hudsonResult == hudson.model.Result.NOT_BUILT) {
			return Result.NOT_BUILT;
		} else if (hudsonResult == hudson.model.Result.ABORTED) {
			return Result.ABORTED;
		} else {
			return Result.NOT_BUILT;
		}
	}

}
