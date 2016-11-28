package com.cloud.configure;

import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.file.filters.AbstractFileListFilter;
import org.springframework.integration.file.remote.session.CachingSessionFactory;
import org.springframework.integration.file.remote.session.SessionFactory;
import org.springframework.integration.ftp.filters.FtpSimplePatternFileListFilter;
import org.springframework.integration.ftp.inbound.FtpInboundFileSynchronizer;
import org.springframework.integration.ftp.inbound.FtpInboundFileSynchronizingMessageSource;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
import org.springframework.messaging.MessageHandler;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * ftp server config
 * Created by micky on 11/29/16.
 */
@Configuration
public class FtpConfiguration {

    @Autowired
    private ServerConfig config;

    @Bean
    public SessionFactory<FTPFile> ftpFileSessionFactory() {
        DefaultFtpSessionFactory sf = new DefaultFtpSessionFactory();
        sf.setHost("localhost");
        sf.setPort(config.getPort());
        sf.setUsername(config.getUserId());
        sf.setPassword(config.getPassword());

        return new CachingSessionFactory<>(sf);
    }

    @Bean
    public FtpInboundFileSynchronizer ftpInboundFileSynchronizer() {
        FtpInboundFileSynchronizer fileSynchronizer = new FtpInboundFileSynchronizer(ftpFileSessionFactory());
        // TODO : file remove will use trash system
        fileSynchronizer.setDeleteRemoteFiles(false);
        fileSynchronizer.setFilter(new FtpSimplePatternFileListFilter("*.*"));
        fileSynchronizer.setRemoteDirectory("/");


        return fileSynchronizer;
    }

    @Bean
    @InboundChannelAdapter(channel="PrivateFtpChannel")
    public MessageSource<File> ftpMessageSource() {
        FtpInboundFileSynchronizingMessageSource source =
                new FtpInboundFileSynchronizingMessageSource(ftpInboundFileSynchronizer());

        source.setLocalDirectory(new File(config.getSyncDirectory()));
        source.setAutoCreateLocalDirectory(true);
        source.setLocalFilter(new DocPhotoFileListFilter());

        return source;
    }

    // TODO : Custom integrate channel
    @Bean
    @ServiceActivator(inputChannel = "PrivateFtpChannel")
    public MessageHandler handler() {
        return message -> {
            System.out.println(message.getPayload());
        };
    }

    /**
     * allow files only document or photo
     */
    class DocPhotoFileListFilter extends AbstractFileListFilter<File> {

        private Set<String> allowExtensions = null;

        @Override
        protected boolean accept(File file) {
            if(allowExtensions == null) {
                // create immutable set
                allowExtensions = Collections.unmodifiableSet(new HashSet<>(config.getAllowExtensions()));
            }
            return allowExtensions.contains(FilenameUtils.getExtension(file.getName()));
        }
    }
}
