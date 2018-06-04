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

import com.rabbitmq.perf.PerfTest;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class PerfTestHelp {

    public static void main(String[] args) {
        if (args == null || args.length == 0) {
            PerfTest.main(new String[] { "-?" });
        } else {
            List<Option> options = new ArrayList<>();
            int envMaxLength = -1;
            for (Object optionObj : PerfTest.getOptions().getOptions()) {
                Option option = (Option) optionObj;
                if ("?".equals(option.getOpt()) || "v".equals(option.getOpt())) {
                    continue;
                }
                String env = PerfTest.LONG_OPTION_TO_ENVIRONMENT_VARIABLE.apply(option.getLongOpt());
                envMaxLength = Math.max(envMaxLength, env.length());
                options.add(option);
            }

            options.sort(((o1, o2) -> o1.getLongOpt().compareToIgnoreCase(o2.getLongOpt())));
            StringBuilder builder = new StringBuilder();
            int width = HelpFormatter.DEFAULT_WIDTH;
            int maxWidth = 1 + envMaxLength + HelpFormatter.DEFAULT_DESC_PAD;
            String lineSep = System.getProperty("line.separator");
            for (Option option : options) {
                StringBuilder line = new StringBuilder();
                line.append(" ");
                line.append(PerfTest.LONG_OPTION_TO_ENVIRONMENT_VARIABLE.apply(option.getLongOpt()));
                while (line.length() < maxWidth) {
                    line.append(" ");
                }

                List<String> brokenDescription = breakString(option.getDescription(), width);
                line.append(brokenDescription.get(0)).append(lineSep);
                for (int i = 1; i < brokenDescription.size(); i++) {
                    int j = 0;
                    while (j < maxWidth) {
                        line.append(" ");
                        j++;
                    }
                    line.append(brokenDescription.get(i)).append(lineSep);
                }
                builder.append(line);
            }
            System.out.print(builder.toString());
        }
    }

    static List<String> breakString(String in, int max) {
        List<String> result = new ArrayList<>();
        while (in.length() > 0) {
            int indexToBreak = Math.min(in.length(), max);
            if (indexToBreak == max) {
                indexToBreak = in.substring(0, max).lastIndexOf(' ');
            }
            String part = in.substring(0, indexToBreak);
            result.add(part.trim());
            in = in.replace(part, "");
        }
        return result;
    }
}
