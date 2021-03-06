package ch.frostnova.mimic.service.io;

import ch.frostnova.mimic.api.MimicMapping;
import ch.frostnova.mimic.api.type.RequestMethod;
import ch.frostnova.util.check.Check;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Mimic scripts loader, loads all scripts from a predefined path
 *
 * @author pwalser
 * @since 25.01.2018.
 */
@Component
public class ScriptsLoader {

    private final static String ALLOWED_METHODS_PATTERN = Stream.of(RequestMethod.values()).map(RequestMethod::name).collect(Collectors.joining("|"));
    private final static Pattern PATH_MAPPING = Pattern.compile("//\\s*(" + ALLOWED_METHODS_PATTERN + ")\\s+(.+)", Pattern.CASE_INSENSITIVE);

    public MimicMapping load(Path path) throws IOException {
        Check.required(path, "path", Files::exists);
        if (!Files.isRegularFile(path) || !Files.isReadable(path)) {
            throw new IOException(path.getFileName() + " cannot be read");
        }

        // read mapping: first line is the mapping, the rest of the lines are the script (ECMAScript 5.1)

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            MimicMapping mapping = load(reader);
            mapping.setDisplayName(path.toString());
            BasicFileAttributes fileAttributes = Files.readAttributes(path, BasicFileAttributes.class);
            mapping.setCreatedAt(toLocalDateTime(fileAttributes.creationTime()));
            mapping.setLastModifiedAt(toLocalDateTime(fileAttributes.lastModifiedTime()));
            return mapping;
        } catch (IOException ex) {
            throw new IOException(path.getFileName() + " does not contain a mimic mapping");
        }
    }

    private static LocalDateTime toLocalDateTime(FileTime fileTime) {
        if (fileTime == null) {
            return null;
        }
        return LocalDateTime.ofInstant(fileTime.toInstant(), ZoneId.systemDefault());
    }

    public MimicMapping load(Reader reader) throws IOException {
        Check.required(reader, "reader");
        return load(new BufferedReader(reader));
    }

    public MimicMapping load(BufferedReader reader) throws IOException {
        Check.required(reader, "reader");

        // read mapping: first line is the mapping, the rest of the lines are the script (ECMAScript 5.1)

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
                    requestMethod = RequestMethod.checked(matcher.group(1));
                    pathMapping = matcher.group(2);
                }
            } else {
                script.append(line).append("\n");
            }
        }
        if (requestMethod != null) {
            MimicMapping mapping = new MimicMapping();
            mapping.setMethod(requestMethod);
            mapping.setPath(pathMapping);
            mapping.setScript(script.toString());
            return mapping;
        }
        throw new IOException("No valid request method found in mapping");
    }
}
