package ch.frostnova.mimic.io;

import ch.frostnova.mimic.api.MimicMapping;
import ch.frostnova.mimic.api.type.RequestMethod;
import ch.frostnova.util.check.Check;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Mimic scripts loader, loads all scripts from a predefined path
 *
 * @author pwalser
 * @since 25.01.2018.
 */
@Component
public class ScriptsLoader {

    private final static Pattern PATH_MAPPING = Pattern.compile("//\\s*(GET|HEAD|PUT|POST|DELETE|OPTIONS|TRACE)\\s+(.+)");

    @Autowired
    private Logger logger;

    @Value("${mimic.scripts.dir}")
    private String scriptsDir;

    public Set<MimicMapping> loadMappings() throws IOException {

        Path path = Paths.get(scriptsDir);

        if (!Files.exists(path)) {
            logger.warn("scriptsDir: " + scriptsDir + " does not exist");
            return Collections.emptySet();
        } else if (!Files.isDirectory(path)) {
            throw new IOException("scriptsDir: " + scriptsDir + " is not a directory");
        } else if (!Files.isReadable(path)) {
            throw new IOException("scriptsDir: " + scriptsDir + " is not readable");
        } else {
            logger.info("loading scripts from: " + scriptsDir + " -> " + path);

            Set<MimicMapping> result = new HashSet<>();
            Files.list(path).filter(Files::isRegularFile).filter(f -> f.getFileName().toString().endsWith(".mimic.js")).forEach(file -> {
                try {
                    MimicMapping mapping = load(file);
                    logger.info("loaded mapping | " + mapping.getMethod() + " " + mapping.getPath() + " [" + file.getFileName() + "]");
                    result.add(mapping);
                } catch (IOException ex) {
                    logger.error("Could not load mapping file " + file.getFileName() + ": " + ex.getMessage());
                }
            });
            return result;
        }
    }

    public MimicMapping load(Path path) throws IOException {
        Check.required(path, "path", Files::exists);
        if (!Files.isRegularFile(path) || !Files.isReadable(path)) {
            throw new IOException(path.getFileName() + " cannot be read");
        }

        // read mapping: first line is the mapping, the rest of the lines are the script (ECMAScript 5.1)


        try (BufferedReader reader = Files.newBufferedReader(path)) {
            RequestMethod requestMethod = null;
            String pathMapping = null;
            StringBuilder script = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                if (requestMethod == null) {
                    line = line.trim();
                    if (line.length() > 0) {
                        Matcher matcher = PATH_MAPPING.matcher(line);
                        if (!matcher.matches()) {
                            throw new IOException("Invalid path mapping: " + line);
                        }
                        requestMethod = RequestMethod.resolve(matcher.group(1));
                        pathMapping = matcher.group(2);
                    }
                } else {
                    script.append(line).append("\n");
                }
            }
            if (requestMethod != null) {
                return new MimicMapping(requestMethod, pathMapping, script.toString());
            }
        }
        throw new IOException(path.getFileName() + " does not contain a mimic mapping");
    }
}
