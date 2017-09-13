package com.example.demo;

import net.schmizz.sshj.xfer.InMemorySourceFile;

import java.io.IOException;
import java.io.InputStream;


public class SimpleImMemorySourceFile extends InMemorySourceFile {
    private InputStream is;
    private String fileName;

    public SimpleImMemorySourceFile(InputStream is, String fileName) {
        this.is = is;
        this.fileName = fileName;
    }

    @Override
    public String getName() {
        return fileName;
    }

    @Override
    public long getLength() {
        return 0;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return is;
    }
}
