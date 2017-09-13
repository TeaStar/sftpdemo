package com.example.demo;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.xfer.LocalSourceFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

import static java.lang.String.format;

@Service
public class SftpService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SftpConfiguration config;

    @Autowired
    public SftpService(SftpConfiguration config) {
        this.config = config;
    }

    void uploadFile(String filename, InputStream inputStream) {

        try (SSHClient sshClient = new SSHClient()) {

            sshClient.addHostKeyVerifier(new PromiscuousVerifier());

            logger.info(format("Connecting to SFTP. host: [%s], port: [%s]", config.getHostname(), config.getPort()));
            sshClient.connect(config.getHostname(), Integer.valueOf(config.getPort()));
            sshClient.authPassword(config.getUsername(), config.getPassword());

            LocalSourceFile file = new SimpleImMemorySourceFile(inputStream, filename);

            sftpUpload(file, sshClient);

        } catch (Exception e) {
            String msg = format("Failed to upload file to SFTP [%s]", filename);
            throw new RuntimeException(msg, e);
        }
    }

    private void sftpUpload(LocalSourceFile file, SSHClient sshClient) throws IOException {
        try (SFTPClient sftpClient = sshClient.newSFTPClient()) {
            String workingDir = config.getWorkingDir();
            sftpClient.open(workingDir).length();

            logger.info(format("Uploading file with filename: [%s] into working dir: [%s]", file.getName(), workingDir));
            sftpClient.put(file, workingDir);
        }
    }


}
