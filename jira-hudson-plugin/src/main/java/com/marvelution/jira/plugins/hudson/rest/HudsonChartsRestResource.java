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

package com.marvelution.jira.plugins.hudson.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.atlassian.jira.charts.jfreechart.ChartHelper;
import com.atlassian.sal.api.message.I18nResolver;
import com.marvelution.hudson.plugins.apiv2.client.HudsonClient;
import com.marvelution.hudson.plugins.apiv2.client.services.JobQuery;
import com.marvelution.hudson.plugins.apiv2.resources.model.job.Job;
import com.marvelution.jira.plugins.hudson.charts.BuildResultsRatioChartGenerator;
import com.marvelution.jira.plugins.hudson.charts.BuildTestResultsRatioChartGenerator;
import com.marvelution.jira.plugins.hudson.charts.HudsonChartGenerator;
import com.marvelution.jira.plugins.hudson.rest.exceptions.NoSuchAssociationException;
import com.marvelution.jira.plugins.hudson.rest.exceptions.NoSuchChartException;
import com.marvelution.jira.plugins.hudson.rest.exceptions.NoSuchJobException;
import com.marvelution.jira.plugins.hudson.rest.exceptions.NoSuchServerException;
import com.marvelution.jira.plugins.hudson.rest.model.Chart;
import com.marvelution.jira.plugins.hudson.rest.model.Option;
import com.marvelution.jira.plugins.hudson.services.HudsonClientFactory;
import com.marvelution.jira.plugins.hudson.services.associations.HudsonAssociation;
import com.marvelution.jira.plugins.hudson.services.associations.HudsonAssociationManager;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServer;
import com.marvelution.jira.plugins.hudson.services.servers.HudsonServerManager;
import com.sun.jersey.spi.resource.Singleton;

/**
 * REST Endpoint for Hudson Charts
 * 
 * Charts are available at: <code>[BASE_URL]/charts?filename=[CHART_LOCATION]</code>
 * 
 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
 */
@Path("charts")
@Consumes( { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_FORM_URLENCODED } )
@Produces( { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML } )
@Singleton
public class HudsonChartsRestResource {

	public static final int CHART_WIDTH = 350;
	public static final int CHART_HEIGHT = 250;

	private final I18nResolver i18nResolver;
	private final HudsonServerManager serverManager;
	private final HudsonAssociationManager associationManager;
	private final HudsonClientFactory clientFactory;

	private final Map<String, Class<? extends HudsonChartGenerator>> charts =
		new HashMap<String, Class<? extends HudsonChartGenerator>>();

	/**
	 * Constructor
	 * 
	 * @param i18nResolver the {@link I18nResolver} implementation
	 * @param serverManager the {@link HudsonServerManager} implementation
	 * @param associationManager the {@link HudsonAssociationManager} implementation
	 * @param clientFactory the {@link HudsonClientFactory} implementation
	 */
	public HudsonChartsRestResource(I18nResolver i18nResolver, HudsonServerManager serverManager,
			HudsonAssociationManager associationManager, HudsonClientFactory clientFactory) {
		this.i18nResolver = i18nResolver;
		this.serverManager = serverManager;
		this.associationManager = associationManager;
		this.clientFactory = clientFactory;
		// TODO Dynamically lookup all the HudsonChartGenerator implementations
		charts.put("BuildResultsRatioChartGenerator", BuildResultsRatioChartGenerator.class);
		charts.put("BuildTestResultsRatioChartGenerator", BuildTestResultsRatioChartGenerator.class);
	}

	@GET
	@Path("generate/{type}/{associationId}")
	public Chart getChart(@PathParam("type") String type, @PathParam("associationId") int associationId)
			throws IOException {
		if (associationManager.hasAssociation(associationId)) {
			final HudsonAssociation association = associationManager.getAssociation(associationId);
			return getChart(type, association.getServerId(), association.getJobName());
		} else {
			throw new NoSuchAssociationException(associationId);
		}
	}

	@GET
	@Path("generate/{type}")
	public Chart getChart(@PathParam("type") String type, @QueryParam("serverId") int serverId,
			@QueryParam("jobName") String jobname) throws IOException {
		if (serverManager.hasServer(serverId)) {
			final HudsonServer server = serverManager.getServer(serverId);
			final HudsonClient client = clientFactory.create(server);
			final Job job = client.find(JobQuery.createForJobByName(jobname));
			if (job != null && StringUtils.isNotBlank(job.getName())) {
				ChartHelper chartHelper = null;
				if (charts.containsKey(type)) {
					try {
						HudsonChartGenerator chartGenerator = charts.get(type).newInstance();
						chartGenerator.setData(server, job);
						chartHelper = chartGenerator.generateChart();
					} catch (InstantiationException e) {
						throw new RuntimeException("Failed to instantiate the Chart Generator: " + type, e);
					} catch (IllegalAccessException e) {
						throw new RuntimeException("Illegal access attempt", e);
					}
				} else {
					throw new NoSuchChartException(type);
				}
				chartHelper.generate(CHART_WIDTH, CHART_HEIGHT);
				return new Chart(chartHelper);
			} else {
				throw new NoSuchJobException(jobname);
			}
		} else {
			throw new NoSuchServerException(serverId);
		}
	}

	@GET
	public Response getCharts() {
		final Collection<Option> options = new ArrayList<Option>();
		for (Map.Entry<String, Class<? extends HudsonChartGenerator>> entry : charts.entrySet()) {
			final String label = i18nResolver.getText("hudson.charts." + entry.getKey() + ".title");
			options.add(new Option(label, entry.getKey()));
		}
		return Response.ok(new Charts(options)).build();
	}

	/**
	 * @author <a href="mailto:markrekveld@marvelution.com">Mark Rekveld</a>
	 */
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement
	public static class Charts {

		private static final ToStringStyle TO_STRING_STYLE = ToStringStyle.SIMPLE_STYLE;

		@XmlElement
		private Collection<Option> charts;

		/**
		 * Default Constructor
		 */
		public Charts() {
		}

		/**
		 * Constructor
		 * 
		 * @param charts the {@link Collection} of charts
		 */
		public Charts(Collection<Option> charts) {
			this.charts = charts;
		}

		/**
		 * Getter for charts
		 * 
		 * @return the charts
		 */
		public Collection<Option> getCharts() {
			return charts;
		}

		/**
		 * Setter for charts
		 * 
		 * @param charts the charts to set
		 */
		public void setCharts(Collection<Option> charts) {
			this.charts = charts;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean equals(Object object) {
			return EqualsBuilder.reflectionEquals(this, object);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int hashCode() {
			return HashCodeBuilder.reflectionHashCode(this);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this, Charts.TO_STRING_STYLE);
		}

	}

}
