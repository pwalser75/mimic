package ch.frostnova.mimic.service.strategy;

import ch.frostnova.mimic.api.MappingProvider;
import ch.frostnova.mimic.api.MimicMapping;
import ch.frostnova.mimic.api.type.RequestMethod;
import ch.frostnova.mimic.api.type.TemplateExpression;
import ch.frostnova.mimic.service.io.ScriptsLoader;
import org.slf4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Mapping provider reading mappings for a configured scripts dir.
 *
 * @author pwalser
 * @since 31.01.2018.
 */
@Component
public class FileSystemMappingProvider implements MappingProvider, InitializingBean, DisposableBean {

    @Autowired
    private Logger logger;

    @Autowired
    private ScriptsLoader scriptsLoader;

    private final Set<MimicMapping> mappings = new HashSet<>();

    private final Path path;

    private WatchKey watch;

    @Autowired
    public FileSystemMappingProvider(@Value("${mimic.scripts.dir:./scripts}") String scriptsDir) {
        path = scriptsDir != null ? Paths.get(scriptsDir).toAbsolutePath().normalize() : null;
    }

    @Override
    public Set<MimicMapping> getMappings(RequestMethod method, String path) {
        return mappings.stream()
                .filter(m -> method == m.getMethod())
                .filter(m -> new TemplateExpression(m.getPath()).matches(path))
                .collect(Collectors.toSet());
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        if (path == null) {
            return;
        }
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        if (!Files.isDirectory(path)) {
            logger.error("Cannot load scripts, path is not a directory: " + path);
        } else if (!Files.isReadable(path)) {
            logger.error("Cannot load scripts, path is not readable: " + path);
        } else {
            logger.info("Using scripts path: " + path);
            loadScripts();
            WatchService watchService = FileSystems.getDefault().newWatchService();
            watch = path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);

            Thread fileSystemObserver = new Thread(() -> {
                logger.info("File system observer daemon started");
                while (watch != null) {
                    try {
                        WatchKey key = watchService.poll(1, TimeUnit.SECONDS);
                        if (key != null && !key.pollEvents().isEmpty()) {
                            logger.info("File system change detected, reloading scripts");
                            try {
                                loadScripts();
                            } catch (IOException ex) {
                                logger.error("error reloading scripts", ex);
                            } finally {
                                key.reset();
                            }
                        }
                    } catch (InterruptedException ignored) {
                    }
                }
                logger.info("File system observer daemon terminated");
            }, "File observer");
            fileSystemObserver.setDaemon(true);
            fileSystemObserver.start();
        }
    }

    private void loadScripts() throws IOException {

        if (path != null || Files.exists(path) && Files.isDirectory(path) && Files.isReadable(path)) {
            logger.debug("Loading scripts from: " + path);

            mappings.clear();
            Files.list(path).filter(Files::isRegularFile).filter(f -> f.getFileName().toString().endsWith(".mimic.js")).forEach(file -> {
                try {
                    MimicMapping mapping = scriptsLoader.load(file);
                    logger.debug("Loaded mapping | " + mapping.getMethod() + " " + mapping.getPath() + " [" + file.getFileName() + "]");
                    mappings.add(mapping);
                } catch (IOException ex) {
                    logger.error("Could not load mapping file " + file.getFileName() + ": " + ex.getMessage());
                }
            });
        }
    }

    @Override
    public void destroy() {
        if (watch != null) {
            watch.cancel();
            watch = null;
        }
        mappings.clear();
    }
}
