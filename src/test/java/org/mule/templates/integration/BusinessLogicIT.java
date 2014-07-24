/**
 * Mule Anypoint Template
 * Copyright (c) MuleSoft, Inc.
 * All rights reserved.  http://www.mulesoft.com
 */

package org.mule.templates.integration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.context.notification.NotificationException;
import org.mule.modules.salesforce.bulk.EnrichedSaveResult;
import org.mule.tck.probe.PollingProber;
import org.mule.tck.probe.Prober;
import org.mule.templates.test.utils.ListenerProbe;
import org.mule.templates.test.utils.PipelineSynchronizeListener;

import com.mulesoft.module.batch.BatchTestHelper;

/**
 * The objective of this class is to validate the correct behavior of the flows
 * for this Mule Template that make calls to external systems.
 */
public class BusinessLogicIT extends AbstractTemplateTestCase {
	protected static final int TIMEOUT = 60;
	private static final Logger LOGGER = Logger.getLogger(BusinessLogicIT.class);
	private static final String POLL_FLOW_NAME = "triggerFlow";
	private static final String ACCOUNT_NAME = "Product Test Name";
	
	private BatchTestHelper helper;
	private Map<String, Object> account;
	
	private static final int TIMEOUT_SEC = 120;

	private final Prober pollProber = new PollingProber(60000, 1000);
	private final PipelineSynchronizeListener pipelineListener = new PipelineSynchronizeListener(POLL_FLOW_NAME);

	
	@BeforeClass
	public static void init() {
		System.setProperty("watermark.default.expression",
				"#[groovy: new Date(System.currentTimeMillis() - 10000).format(\"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'\", TimeZone.getTimeZone('UTC'))]");
	}

	@Before
	public void setUp() throws Exception {
		stopFlowSchedulers(POLL_FLOW_NAME);
		registerListeners();
		helper = new BatchTestHelper(muleContext);

		// prepare test data
		account = createSalesforceAccount();
		insertSalesforceAccount(account);
	}

	@After
	public void tearDown() throws Exception {
		stopFlowSchedulers(POLL_FLOW_NAME);
		// delete previously created account from DB by matching ID
		final Map<String, Object> acc = new HashMap<String, Object>();
		acc.put("Name", account.get("Name"));
		acc.put("Id", account.get("Id"));

		deleteMaterialFromSap(acc);
		deleteProductFromSalesforce(acc);
	}

	@Test
	public void testMainFlow() throws Exception {
		// Run poll and wait for it to run
		runSchedulersOnce(POLL_FLOW_NAME);
		waitForPollToRun();

		// Wait for the batch job executed by the poll flow to finish
		helper.awaitJobTermination(TIMEOUT_SEC * 1000, 500);
		helper.assertJobWasSuccessful();

		final MuleEvent event = runFlow("queryProductFromSapFlow", account);
		final List<?> payload = (List<?>) event.getMessage().getPayload();

		// print result
		for (Object acc : payload){
			LOGGER.info("selectAccountFromDB response: " + acc);
		}

		Assert.assertEquals("The product should have been sync", 1, payload.size());
		Assert.assertEquals("The product name should match", account.get("Name"), ((Map<?, ?>) payload.get(0)).get("Name"));
	}

	@SuppressWarnings("unchecked")
	private void insertSalesforceAccount(final Map<String, Object> account) throws Exception {
		final MuleEvent event = runFlow("createProductsInSalesforceFlow", Collections.singletonList(account));
		final List<EnrichedSaveResult> result = (List<EnrichedSaveResult>) event.getMessage().getPayload();

		// store Id into our product
		for (EnrichedSaveResult item : result) {
			LOGGER.info("response from insertSalesforceAccountSubFlow: " + item);
			account.put("Id", item.getId());
			account.put("LastModifiedDate", item.getPayload().getField("LastModifiedDate"));
		}
	}

	private void deleteProductFromSalesforce(final Map<String, Object> acc) throws Exception {
		List<Object> idList = new ArrayList<Object>();
		idList.add(acc.get("Id"));
		runFlow("deleteProductsFromSalesforceFlow", idList);
	}

	private void deleteMaterialFromSap(final Map<String, Object> account) throws Exception {
		final MuleEvent event = runFlow("deleteProductsFromSapFlow", account);
		final Object result = event.getMessage().getPayload();
		LOGGER.info("deleteMaterialFromSap result: " + result);
	}

	private Map<String, Object> createSalesforceAccount() {
		final Map<String, Object> account = new HashMap<String, Object>();
		account.put("Name", ACCOUNT_NAME + System.currentTimeMillis());
		return account;
	}

	private void registerListeners() throws NotificationException {
		muleContext.registerListener(pipelineListener);
	}

	private void waitForPollToRun() {
		System.out.println("Waiting for poll to run ones...");
		pollProber.check(new ListenerProbe(pipelineListener));
		System.out.println("Poll flow done");
	}

}
