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

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.rabbitmq.perf.PerfTest;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 */
public class PcfPerfTest {

    public static void main(String[] args) throws InterruptedException {
        String uris = uris(System.getenv("VCAP_SERVICES"));
        if (uris == null) {
            throw new IllegalArgumentException("Unable to retrieve broker URI(s) from VCAP_SERVICES");
        }
        args = Arrays.copyOf(args, args.length + 2);
        args[args.length - 2] = "--uris";
        args[args.length - 1] = uris;
        PerfTest.main(args);
    }

    static String uris(String vcapServices) {
        Gson gson = new Gson();
        JsonObject jsonObject = (JsonObject) gson.fromJson(vcapServices, JsonElement.class);
        String uris = null;
        for (String serviceType : jsonObject.keySet()) {
            JsonArray array = jsonObject.getAsJsonArray(serviceType);
            boolean isAmqp = array.get(0).getAsJsonObject().get("tags").getAsJsonArray().contains(new JsonPrimitive("amqp"));
            if (isAmqp) {
                Stream.Builder<String> builder = Stream.builder();
                array.get(0).getAsJsonObject().get("credentials").getAsJsonObject().get("uris").getAsJsonArray()
                    .forEach(element -> builder.accept(element.getAsString()));
                uris = String.join(",", builder.build().collect(Collectors.toList()));
                break;
            }
        }
        return uris;
    }
}
