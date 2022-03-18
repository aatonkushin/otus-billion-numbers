package org.tonkushin;

import java.io.IOException;

public interface FileArray extends AutoCloseable {
    void set(int value) throws IOException;

    void set(int[] values) throws IOException;

    int get() throws IOException;

    boolean hasNext() throws IOException;

    int[] getArray() throws IOException;
}
