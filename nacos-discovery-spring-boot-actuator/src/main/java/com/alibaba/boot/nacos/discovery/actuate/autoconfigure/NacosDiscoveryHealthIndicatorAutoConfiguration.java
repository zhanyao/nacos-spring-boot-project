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
package com.alibaba.boot.nacos.discovery.actuate.autoconfigure;

import com.alibaba.boot.nacos.discovery.NacosDiscoveryConstants;
import org.springframework.boot.actuate.autoconfigure.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import com.alibaba.boot.nacos.discovery.actuate.health.NacosDiscoveryHealthIndicator;
import org.springframework.context.annotation.Configuration;

/**
 * Nacos {@link NacosDiscoveryHealthIndicator} Auto Configuration
 *
 * @author <a href="mailto:fangjian0423@gmail.com">Jim</a>
 */
@Configuration
@ConditionalOnClass({HealthIndicator.class})
@ConditionalOnEnabledHealthIndicator(NacosDiscoveryConstants.ENDPOINT_PREFIX)
public class NacosDiscoveryHealthIndicatorAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public NacosDiscoveryHealthIndicator nacosDiscoveryHealthIndicator() {
		return new NacosDiscoveryHealthIndicator();
	}

}
