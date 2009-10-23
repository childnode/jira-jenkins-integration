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

package com.marvelution.jira.plugins.hudson.api.model;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import com.marvelution.jira.plugins.hudson.api.model.ApiImplementation;

/**
 * TestCase for {@link ApiImplementation}
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
public class ApiImplementationTest {

	private ApiImplementation api;

	private Properties props;

	/**
	 * Setup tests
	 * 
	 * @throws Exception in case of errors
	 */
	@Before
	public void setUp() throws Exception {
		props = new Properties();
		props.load(getClass().getResourceAsStream("ApiImplementation.properties"));
		api = ApiImplementation.getApiImplementation();
	}

	/**
	 * Test {@link ApiImplementation#hashCode()}
	 */
	@Test
	public void testHashCode() {
		assertEquals(props.getProperty("current.version").hashCode(), api.hashCode());
	}

	/**
	 * Test {@link ApiImplementation#equals(Object)}
	 */
	@Test
	public void testEqualsObject() {
		assertFalse(api.equals(null));
		assertFalse(api.equals("1.0"));
	}

	/**
	 * Test {@link ApiImplementation#equals(Object)}
	 * 
	 * @throws Exception in case of errors
	 */
	@Test
	public void testEqualsApiImplementation() throws Exception {
		assertTrue(api.equals(ApiImplementation.getApiImplementation()));
	}

	/**
	 * Test {@link ApiImplementation#toString()}
	 */
	@Test
	public void testToString() {
		assertEquals(props.getProperty("current.version"), api.toString());
	}

	/**
	 * Test {@link ApiImplementation#isCompatibleWith(ApiImplementation)} by Major version
	 * @throws Exception in case of test exceptions
	 */
	@Test
	public void testMajorVersionCompatiblity() throws Exception {
		final ApiImplementation otherApi = ApiImplementation.getApiImplementation();
		final Field versionField = ApiImplementation.class.getDeclaredField("version");
		versionField.setAccessible(true);
		versionField.set(otherApi, props.getProperty("current.version").split("\\.")[0] + ".99.99");
		assertTrue(api.isCompatibleWith(otherApi));
	}

}