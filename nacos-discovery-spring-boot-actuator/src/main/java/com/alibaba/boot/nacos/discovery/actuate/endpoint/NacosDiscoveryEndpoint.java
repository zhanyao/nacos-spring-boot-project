/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.boot.nacos.discovery.actuate.endpoint;

import static com.alibaba.nacos.spring.util.NacosBeanUtils.DISCOVERY_GLOBAL_NACOS_PROPERTIES_BEAN_NAME;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.AbstractEndpoint;
import org.springframework.boot.actuate.endpoint.Endpoint;
import org.springframework.context.ApplicationContext;

import com.alibaba.boot.nacos.discovery.NacosDiscoveryConstants;
import com.alibaba.boot.nacos.discovery.properties.NacosDiscoveryProperties;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.spring.factory.CacheableEventPublishingNacosServiceFactory;
import com.alibaba.nacos.spring.factory.NacosServiceFactory;
import com.alibaba.nacos.spring.util.NacosUtils;

/**
 * Actuator {@link Endpoint} to expose Nacos Discovery Meta Data
 *
 * @author <a href="mailto:fangjian0423@gmail.com">Jim</a>
 * @see Endpoint
 */
public class NacosDiscoveryEndpoint extends AbstractEndpoint<Map<String, Object>> {

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired(required = false)
	private NacosDiscoveryProperties nacosDiscoveryProperties;

	private static final Integer PAGE_SIZE = 100;

    public NacosDiscoveryEndpoint() {
        super(NacosDiscoveryConstants.ENDPOINT_PREFIX);
    }

	@Override
	public Map<String, Object> invoke() {
		Map<String, Object> result = new HashMap<>();

		result.put("nacosDiscoveryGlobalProperties",
				applicationContext.getBean(DISCOVERY_GLOBAL_NACOS_PROPERTIES_BEAN_NAME));

		result.put("nacosDiscoveryProperties", nacosDiscoveryProperties);

		NacosServiceFactory nacosServiceFactory = applicationContext.getBean(
				CacheableEventPublishingNacosServiceFactory.BEAN_NAME,
				NacosServiceFactory.class);

		JSONArray array = new JSONArray();
		for (NamingService namingService : nacosServiceFactory.getNamingServices()) {
			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put("servicesOfServer",
						namingService.getServicesOfServer(0, PAGE_SIZE));
				jsonObject.put("subscribeServices", namingService.getSubscribeServices());
				array.add(jsonObject);
			}
			catch (NacosException e) {
				jsonObject.put("serverStatus", namingService.getServerStatus() + ": "
						+ e.getErrCode() + NacosUtils.SEPARATOR + e.getErrMsg());
			}
		}

		result.put("namingServersStatus", array);
		return result;
	}

}
