/*
 * Copyright 2018 Odysseus Data Services, inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Company: Odysseus Data Services, Inc.
 * Product Owner/Architecture: Gregory Klebanov
 * Authors: Anton Gackovka
 * Created: August 7, 2018
 */

package com.odysseusinc.arachne.commons.config.flyway;

import java.sql.Connection;
import org.flywaydb.core.api.FlywayException;
import org.flywaydb.core.api.resolver.MigrationExecutor;
import org.springframework.context.ApplicationContext;

/**
 * Adapter for executing migrations implementing ApplicationContextAwareSpringMigration.
 */
public class ApplicationContextAwareSpringJdbcMigrationExecutor implements MigrationExecutor {

    private final ApplicationContext applicationContext;

    private final String className;

    /**
     * Creates a new ApplicationContextAwareSpringMigration.
     *
     * @param springJdbcMigration The Application Context Aware Spring Jdbc Migration to execute.
     * @param applicationContext
     */
    public ApplicationContextAwareSpringJdbcMigrationExecutor(ApplicationContext applicationContext, String className) {

        this.applicationContext = applicationContext;
        this.className = className;
    }

    @Override
    public void execute(Connection connection) {
        try {
            ApplicationContextAwareSpringMigration springJdbcMigration = (ApplicationContextAwareSpringMigration)applicationContext.getBean(className);
            springJdbcMigration.migrate();
        } catch (Exception e) {
            throw new FlywayException("Migration failed !", e);
        }
    }

    @Override
    public boolean executeInTransaction() {
        return true;
    }
}
