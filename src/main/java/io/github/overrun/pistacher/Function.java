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

/**
 * The expression contain a name and statements.<br>
 * Maps to {@code name:}
 *
 * @author squid233
 * @since 2021/03/16
 */
public class Function implements IExpression {
    private final String name;
    private final Statement[] statements;

    public Function(String name, Statement... statements) {
        this.name = name;
        this.statements = statements;
    }

    public Function invoke() {
        for (Statement statement : statements) {
            statement.run();
        }
        return this;
    }

    public String getName() {
        return name;
    }

    public Statement[] getStatements() {
        return statements;
    }
}
