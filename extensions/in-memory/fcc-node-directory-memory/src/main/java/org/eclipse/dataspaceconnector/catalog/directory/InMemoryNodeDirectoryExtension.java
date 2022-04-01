/*
 *  Copyright (c) 2021 Microsoft Corporation
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       Microsoft Corporation - Initial implementation
 *
 */

package org.eclipse.dataspaceconnector.catalog.directory;

import org.eclipse.dataspaceconnector.catalog.spi.FederatedCacheNodeDirectory;
import org.eclipse.dataspaceconnector.spi.system.Provides;
import org.eclipse.dataspaceconnector.spi.system.ServiceExtension;
import org.eclipse.dataspaceconnector.spi.system.ServiceExtensionContext;

@Provides(FederatedCacheNodeDirectory.class)
public class InMemoryNodeDirectoryExtension implements ServiceExtension {

    @Override
    public String name() {
        return "In-Memory Node Directory";
    }

    @Override
    public void initialize(ServiceExtensionContext context) {
        context.registerService(FederatedCacheNodeDirectory.class, new InMemoryNodeDirectory());
    }
}
