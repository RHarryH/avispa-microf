package com.avispa.microf;

import com.avispa.cms.model.filestore.FileStore;
import com.avispa.cms.model.filestore.FileStoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
@EnableAsync
@ComponentScan("com.avispa")
@EntityScan("com.avispa") // required to use CMS entities
@EnableJpaRepositories("com.avispa") // required to use CMS repositories
@PropertySource("classpath:application.properties")
@PropertySource("file:./microf.properties")
@Slf4j
public class MicroFApplication {
	/*@Bean
	@Description("Spring Message Resolver")
	public ResourceBundleMessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("application");
		return messageSource;
	}*/
	@Autowired
	private FileStoreRepository fileStoreRepository;

	@Bean
	public FileStore getFileStore(@Value("${microf.fileStoreName}") String fileStoreName,
								  @Value("${microf.defaultFileStoreName}") String defaultFileStoreName,
								  @Value("${microf.defaultFileStorePath}") String defaultFileStorePath) {
		FileStore fileStore = fileStoreRepository.findByName(fileStoreName);
		if(null == fileStore) {
			fileStore = fileStoreRepository.findByName(defaultFileStoreName);
			if(null == fileStore) {
				fileStore = createDefaultFileStore(defaultFileStoreName, Path.of(System.getProperty("user.home"), defaultFileStorePath).toString());
			}
		}

		createFileStorePath(fileStore);

		return fileStore;
	}

	private FileStore createDefaultFileStore(String defaultFileStoreName, String defaultFileStorePath) {
		FileStore fileStore;

		fileStore = new FileStore();
		fileStore.setName(defaultFileStoreName);
		fileStore.setRootPath(defaultFileStorePath);
		fileStoreRepository.save(fileStore);

		return fileStore;
	}

	private void createFileStorePath(FileStore fileStore) {
		Path fp = Paths.get(fileStore.getRootPath());
		try {
			Files.createDirectories(fp);
		} catch (IOException e) {
			log.error("Can't create file store folders", e);
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(MicroFApplication.class, args);
	}
}
