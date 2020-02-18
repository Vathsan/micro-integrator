/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.micro.integrator.management.apis.internal;

import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.event.EventConstants;
import org.wso2.micro.integrator.management.apis.Constants;

/**
 * Logging updater service component.
 */
@Component(name = "org.wso2.micro.integrator.management.apis", immediate = true, property = EventConstants.EVENT_TOPIC
        + "=" + Constants.PAX_LOGGING_CONFIGURATION_TOPIC)
public class LoggingUpdaterServiceComponent {

    @Reference(name = "osgi.configadmin.service",
            service = ConfigurationAdmin.class,
            unbind = "unsetConfigAdminService",
            cardinality = ReferenceCardinality.MANDATORY,
            policy = ReferencePolicy.DYNAMIC)
    public void setConfigAdminService(ConfigurationAdmin configAdminService) {

        DataHolder.getInstance().setConfigurationAdmin(configAdminService);
    }

    @Activate
    public void activate(ComponentContext componentContext) {}

    @Deactivate
    public void deactivate() {}

    public void unsetConfigAdminService(ConfigurationAdmin configAdminService) {

        DataHolder.getInstance().setConfigurationAdmin(null);
    }
}
