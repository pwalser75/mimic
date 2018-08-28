package ch.frostnova.mimic.api.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Random utility functions.
 *
 * @author pwalser
 * @since 02.06.2017
 */
public final class RandomUtil {

    private final static String[] LOREM_IPSUM = ("a accumsan accusam ad adipiscing aliquam aliquip aliquyam amet assum at augue autem blandit " +
            "clita commodo congue consectetuer consequat consetetur cum delenit diam dignissim dolor dolore dolores doming duis duo " +
            "e eirmod eleifend elit elitr enim eos erat eros esse est et eu euismod eum ex exerci facer facilisi facilisis feugait " +
            "feugiat gubergren hendrerit id illum imperdiet i in invidunt ipsum iriure iusto justo kasd labore laoreet liber lobortis " +
            "lorem luptatum magna mazim minim molestie nam nibh nihil nisl no nobis nonummy nonumy nostrud nulla odio option placerat " +
            "possim praesent qui quis quod rebum sadipscing sanctus sea sed sit soluta stet suscipit takimata tation te tempor " +
            "tincidunt ullamcorper ut vel velit veniam vero voluptua volutpat vulputate wisi zzril").split("\\s");

    private RandomUtil() {
    }

    /**
     * Provides the {@link Random} class for the functions.
     *
     * @return random
     */
    private static Random getRandom() {
        return ThreadLocalRandom.current();
    }

    /**
     * Returns a random number in the range min(inclusive)..max(inclusive!).
     *
     * @param min minimum
     * @param max maximum
     * @return random number
     */
    public static int rnd(int min, int max) {
        if (min == max) {
            return min;
        }
        if (min > max) {
            return rnd(max, min);
        }
        return min + getRandom().nextInt(max - min + (max < Integer.MAX_VALUE ? 1 : 0));
    }

    /**
     * Returns a random number in the range min(inclusive)..max(exclusive).
     *
     * @param min minimum
     * @param max maximum
     * @return random number
     */
    public static float rnd(float min, float max) {
        return min + getRandom().nextFloat() * (max - min);
    }

    /**
     * Returns a random number in the range min(inclusive)..max(exclusive).
     *
     * @param min minimum
     * @param max maximum
     * @return random number
     */
    public static double rnd(double min, double max) {
        return min + getRandom().nextFloat() * (max - min);
    }

    /**
     * Returns a random value from the given enum type
     *
     * @param e   enum type
     * @param <E> enum type
     * @return random enum value
     */
    public static <E extends Enum<E>> E rnd(Class<E> e) {
        return rnd(e.getEnumConstants());
    }

    /**
     * Returns a random value from the given list of items
     *
     * @param items items
     * @param <T>   type
     * @return random value
     */
    public static <T> T rnd(List<T> items) {
        return items == null || items.isEmpty() ? null : items.get(rnd(0, items.size() - 1));
    }

    /**
     * Returns a random value from the given set of items
     *
     * @param items items
     * @param <T>   type
     * @return random value
     */
    public static <T> T rnd(Set<T> items) {
        return items == null || items.isEmpty() ? null : rnd(new ArrayList<>(items));
    }

    /**
     * Returns a random subset of the given list of items
     *
     * @param items items
     * @param <T>   type
     * @return random value
     */
    public static <T> Set<T> rndSubset(Collection<T> items) {
        return items == null || items.isEmpty() ? new HashSet<>() : items.stream().filter(x -> rndBoolean()).collect(Collectors.toSet());
    }

    /**
     * Returns a random subset of the given array of items
     *
     * @param items items
     * @param <T>   type
     * @return random value
     */
    public static <T> Set<T> rndSubset(T... items) {
        return items == null || items.length == 0 ? new HashSet<>() : Stream.of(items).filter(x -> rndBoolean()).collect(Collectors.toSet());
    }

    /**
     * Returns a random value from the given array of items
     *
     * @param items items
     * @param <T>   type of items and result
     * @return random value
     */
    @SafeVarargs
    public static <T> T rnd(T... items) {
        return items == null || items.length == 0 ? null : items[rnd(0, items.length - 1)];
    }

    /**
     * Creates a random boolean
     *
     * @return random boolean
     */
    public static boolean rndBoolean() {
        return getRandom().nextBoolean();
    }

    /**
     * Creates randomized data as a byte array of a given length.
     *
     * @param length length of array to generate
     * @return array with randomized data
     */
    public static byte[] rndData(int length) {
        if (length < 0) {
            throw new IllegalArgumentException("length must be positive");
        }
        byte[] result = new byte[length];
        getRandom().nextBytes(result);
        return result;
    }

    /**
     * Creates a random date/time between 1900-01-01 00:00:00.000 and 2099-12-31 23:59:59:999.
     *
     * @return random date
     */
    public static LocalDateTime rndDateTime() {

        LocalDateTime min = LocalDateTime.of(1900, 1, 1, 0, 0);
        LocalDateTime max = LocalDateTime.of(2099, 12, 31, 23, 59, 59, 999);

        return rndDateTime(min, max);
    }

    /**
     * Creates a random date/time between the two supplied dates
     *
     * @return random date
     */
    public static LocalDateTime rndDateTime(LocalDateTime min, LocalDateTime max) {

        long start = min.toEpochSecond(ZoneOffset.UTC);
        long end = max.toEpochSecond(ZoneOffset.UTC);
        long time = start + (long) (getRandom().nextDouble() * (end - start + 1));

        Instant instant = Instant.ofEpochSecond(time);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    /**
     * Creates a random date/time from now plus/minus the given range/unit
     *
     * @return random date
     */
    public static LocalDateTime rndDateTime(int range, TemporalUnit unit) {

        LocalDateTime now = LocalDateTime.now();
        return rndDateTime(now.minus(range, unit), now.plus(range, unit));
    }

    /**
     * Generates a random alphanumeric character (A-Z, a-z, 0-9).
     *
     * @return random char
     */
    public static char rndChar() {
        int rnd = getRandom().nextInt(52);
        char base = rnd < 26 ? 'A' : 'a';
        return (char) (base + rnd % 26);

    }

    /**
     * Generate a random IP address
     *
     * @return random IP
     */
    public static String randomIP() {
        return rnd(0, 0xFF) + "." + rnd(0, 0xFF) + "." + rnd(0, 0xFF) + "." + rnd(0, 0xFF);
    }

    /**
     * Generates a random package name
     *
     * @param numSubPackages number of sub packages that compose the package
     * @param packageLength  length of each sub-package name
     * @param separator      separator to use between the sub-packages
     * @return an empty string (which represents the default package) if the separator is blank or one of the 2 int values is less or equal to 0
     */
    public static String rndPackage(int numSubPackages, int packageLength, String separator) {
        if (separator == null || separator.trim().isEmpty() || numSubPackages <= 0 || packageLength <= 0) { //default package
            return "";
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < numSubPackages; i++) {
            for (int j = 0; j < packageLength; j++) {
                builder.append(rndChar());
            }

            if ((i + 1) < numSubPackages) {
                builder.append(separator);
            }
        }

        return builder.toString();
    }

    public static String rndText(int maxLength, boolean withInterpunctation) {
        if (maxLength < 0) {
            throw new IllegalArgumentException("Max length must be positive");
        }
        if (maxLength == 0) {
            return "";
        }
        if (maxLength == 1) {
            return Character.toString((char) rnd('A', 'Z'));
        }

        int totalLength = 0;
        List<String> words = new LinkedList<>();
        int spacingReserve = withInterpunctation ? 2 : 1;
        while (true) {

            String nextWord = rnd(LOREM_IPSUM);
            if (totalLength + nextWord.length() + 1 <= maxLength) {
                words.add(nextWord);
                totalLength += nextWord.length() + spacingReserve;
            } else if (!words.isEmpty()) {
                if (withInterpunctation) {
                    return createSentence(words);
                }
                return firstCharUpperCase(words.stream().collect(Collectors.joining(" ")));
            }
        }
    }

    private static String createSentence(List<String> words) {
        if (words.isEmpty()) {
            return "";
        }
        if (words.size() > 4) {
            int split = words.size() / 2 + rnd(-1, 1);
            List<String> first = new LinkedList<>();
            List<String> second = new LinkedList<>();
            int i = 0;
            for (String word : words) {
                if (i++ < split) {
                    first.add(word);
                } else {
                    second.add(word);
                }
            }
            if (words.size() > 7) {
                return createSentence(first) + " " + createSentence(second);
            }
            if (rndBoolean()) {
                return firstCharUpperCase(words.stream().collect(Collectors.joining(" "))) + ".";
            }
            return firstCharUpperCase(first.stream().collect(Collectors.joining(" "))) + ", "
                    + second.stream().collect(Collectors.joining(" ")) + ".";

        }
        return firstCharUpperCase(words.stream().collect(Collectors.joining(" "))) + ".";
    }

    private static String firstCharUpperCase(String s) {
        if (s.length() == 0) {
            return s;
        }
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}
