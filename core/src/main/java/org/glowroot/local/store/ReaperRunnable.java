/*
 * Copyright 2012-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.glowroot.local.store;

import java.sql.SQLException;

import org.glowroot.common.Clock;
import org.glowroot.common.ScheduledRunnable;
import org.glowroot.config.ConfigService;

import static java.util.concurrent.TimeUnit.HOURS;

class ReaperRunnable extends ScheduledRunnable {

    private final ConfigService configService;
    private final AggregateDao aggregateDao;
    private final TraceDao traceDao;
    private final GaugePointDao gaugePointDao;
    private final Clock clock;

    ReaperRunnable(ConfigService configService, AggregateDao aggregateDao, TraceDao traceDao,
            GaugePointDao gaugePointDao, Clock clock) {
        this.configService = configService;
        this.aggregateDao = aggregateDao;
        this.traceDao = traceDao;
        this.gaugePointDao = gaugePointDao;
        this.clock = clock;
    }

    @Override
    protected void runInternal() throws SQLException {
        long aggregateCaptureTime = clock.currentTimeMillis()
                - HOURS.toMillis(configService.getStorageConfig().aggregateExpirationHours());
        aggregateDao.deleteBefore(aggregateCaptureTime);

        long traceCaptureTime = clock.currentTimeMillis()
                - HOURS.toMillis(configService.getStorageConfig().traceExpirationHours());
        traceDao.deleteBefore(traceCaptureTime);

        long gaugeCaptureTime = clock.currentTimeMillis()
                - HOURS.toMillis(configService.getStorageConfig().gaugeExpirationHours());
        gaugePointDao.deleteBefore(gaugeCaptureTime);
    }
}
