/*
 * Copyright (c) 2018 Pivotal Software Inc, All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rabbitmq.pcf;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 */
public class PlaceholderProcessingTest {

    @Test
    public void variableExtraction() {
        assertEquals(0, PcfPerfTest.extractVariables("").size());
        assertEquals(0, PcfPerfTest.extractVariables("test").size());
        assertEquals(0, PcfPerfTest.extractVariables("10").size());
        assertEquals("[PORT]", PcfPerfTest.extractVariables("${PORT}").toString());
        assertEquals(
            "[APP_ID, INSTANCE_ID]",
            PcfPerfTest.extractVariables("application=${APP_ID},instance=${APP_ID}-${INSTANCE_ID}").toString()
        );
    }

    @Test
    public void evaluate() {
        UnaryOperator<String> lookup = lookup("PORT", "8080", "APP_ID", "perf-test", "INSTANCE_ID", "123");
        assertEquals(
            "", PcfPerfTest.evaluate("", lookup)
        );
        assertEquals(
            "test", PcfPerfTest.evaluate("test", lookup)
        );
        assertEquals(
            "10", PcfPerfTest.evaluate("10", lookup)
        );
        assertEquals(
            "8080", PcfPerfTest.evaluate("${PORT}", lookup)
        );
        assertEquals(
            "application=perf-test,instance=perf-test-123",
            PcfPerfTest.evaluate("application=${APP_ID},instance=${APP_ID}-${INSTANCE_ID}", lookup)
        );
    }

    UnaryOperator<String> lookup(String... keyValues) {
        Map<String, String> values = new HashMap<>();
        for (int i = 0; i < keyValues.length - 1; i++) {
            values.put(keyValues[i], keyValues[i + 1]);
        }
        return key -> values.get(key);
    }
}
