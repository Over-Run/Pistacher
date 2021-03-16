/*
 * MIT License
 *
 * Copyright (c) 2021 OverRuna Organization
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.overrun.pistacher;

import java.util.function.Consumer;

import static org.apache.commons.text.StringEscapeUtils.unescapeJava;

/**
 * @author squid233
 * @since 2021/03/16
 */
public final class Expressions {
    private static final Consumer<String[]> PRINTLN = strings -> {
        if (strings[1].startsWith("std")) {
            String[] args = strings[1].split(",\\s+");
            String content = unescapeJava(args[args.length - 1].substring(1,
                    args[args.length - 1].length() - 1));
            if ("stdout".equals(args[0])) {
                System.out.println(content);
            } else if ("stderr".equals(args[0])) {
                System.err.println(content);
            }
            return;
        }
        System.out.println(unescapeJava(strings[1].substring(1, strings[1].length() - 1)));
    };

    public static LabelExp createLabel(String name,
                                       Statement... statements) {
        return new LabelExp(name, statements);
    }

    public static Statement createStatement(Consumer<Object[]> operation,
                                            Object... args) {
        return new Statement(operation, args);
    }

    public static Statement createPrintln(String[] strings) {
        Consumer<Object[]> cons = objects -> PRINTLN.accept((String[]) objects[0]);
        return createStatement(cons, new Object[]{strings});
    }

    public static Statement createCallStatement(LabelExp label) {
        Consumer<Object[]> cons = objects -> label.invoke();
        return createStatement(cons, label);
    }
}
