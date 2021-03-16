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

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import static io.github.overrun.pistacher.Expressions.*;

/**
 * @author squid233
 * @since 2021/03/14
 */
public final class Pistachio {
    public static final Pattern LABEL_PATTERN = Pattern.compile("^.+:");
    public static final Pattern PRINTLN_PATTERN = Pattern.compile("\\s*println\\s(std(out|err),\\s)?\".*\"");
    public static final Pattern CALL_PATTERN = Pattern.compile("\\s*call\\s+.+");

    private static void printError(String file,
                                   int linePos,
                                   String line,
                                   String msg) {
        System.err.println("Error in file [" + file + "] at line " + linePos);
        System.err.println(line);
        System.err.println(msg);
    }

    public static void interpret(String file)
            throws IOException {
        try (Scanner sc = new Scanner(new File(file), StandardCharsets.UTF_8)) {
            int pos = 0;
            List<Statement> statements = new ArrayList<>();
            Map<String, Function> functions = new HashMap<>(16);
            String funcName = null;
            while (sc.hasNextLine()) {
                ++pos;
                String line = sc.nextLine();
                if (Pattern.matches("\\s*//.*", line)
                        || Pattern.matches("\\s*", line)) {
                    continue;
                }
                if (LABEL_PATTERN.matcher(line).matches()) {
                    if (funcName != null) {
                        functions.put(funcName,
                                createFunc(funcName,
                                        statements.toArray(new Statement[0])));
                        if (!statements.isEmpty()) {
                            statements.clear();
                        }
                    }
                    funcName = line.substring(0, line.length() - 1);
                } else if (PRINTLN_PATTERN.matcher(line).matches()) {
                    Statement statement = createPrintln(line.replaceFirst("\\s*", "")
                            .split("\\s", 2));
                    if (funcName != null) {
                        statements.add(statement);
                    } else {
                        statement.run();
                    }
                } else if (CALL_PATTERN.matcher(line).matches()) {
                    String[] arr = line.replaceFirst("\\s*", "")
                            .split("\\s+");
                    String lbNm = arr[arr.length - 1];
                    Function function = functions.get(lbNm);
                    if (function != null) {
                        function.invoke();
                    } else {
                        printError(file, pos, line, "Label not found: " + lbNm);
                        return;
                    }
                } else {
                    printError(file, pos, line, "^ --> Not a statement");
                    return;
                }
//                String[] ex = line.split("\\s", 2);
//                if (ex[1].contains("std")) {
//                    String[] args = ex[1].split(",\\s");
//                    String content = StringEscapeUtils.unescapeJava(args[1].substring(1, args[1].length() - 1));
//                    if ("stdout".equals(args[0])) {
//                        System.out.println(content);
//                    } else if ("stderr".equals(args[0])) {
//                        System.err.println(content);
//                    }
//                } else {
//                    System.out.println(StringEscapeUtils.unescapeJava(ex[1].substring(1, ex[1].length() - 1)));
//                }
            }
        }
    }
}
