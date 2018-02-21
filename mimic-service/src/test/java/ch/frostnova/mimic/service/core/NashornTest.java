package ch.frostnova.mimic.service.core;

import ch.frostnova.mimic.service.testdata.Movie;
import ch.frostnova.mimic.service.util.JsonUtil;
import org.junit.Assert;
import org.junit.Test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Tests for interacting with the Nashorn Javascript engine.
 *
 * @author pwalser
 * @since 25.01.2018.
 */
public class NashornTest {

    private ScriptEngine createEngine() {
        return new ScriptEngineManager().getEngineByName("nashorn");
    }

    private Movie createMovie() {

        Movie movie = new Movie();
        movie.setTitle("Blade Runner");
        movie.setYear(1982);
        movie.setGenres(Arrays.asList("Sci-Fi", "Thriller"));
        movie.getRatings().put("IMDB", 8.2);
        movie.getRatings().put("Metacritic", 89d);
        return movie;
    }

    @Test
    public void testReadPOJO() throws Exception {

        ScriptEngine engine = createEngine();
        Movie movie = createMovie();

        engine.put("movie", movie);
        engine.put("data", JsonUtil.stringify(movie));

        engine.eval("var title = movie.title;" +
                "var genreCount = movie.genres.length;" +
                "var imdbRating = movie.ratings.IMDB;");


        System.out.println(engine.get("title"));
        System.out.println(engine.get("genreCount"));
        System.out.println(engine.get("imdbRating"));
    }

    @Test
    public void testModifyPOJO() throws Exception {

        ScriptEngine engine = createEngine();
        Movie movie = createMovie();

        engine.put("movie", movie);
        engine.put("data", JsonUtil.stringify(movie));

        engine.eval("movie.title='Blade Runner 2049';" +
                "movie.year += 34;" +
                // "movie.genres.push('Drama');" + // DOES NOT WORK
                // "movie.genres.add('Drama');" + // DOESN'T WORK EITHER
                "movie.ratings.Metacritic=81;" +
                "movie.ratings.RottenTomatoes=81.5;");


        Assert.assertEquals("Blade Runner 2049", movie.getTitle());
        Assert.assertEquals(2016, movie.getYear());
        Assert.assertEquals(81.0, movie.getRatings().get("Metacritic").doubleValue(), Double.MIN_VALUE);
        Assert.assertEquals(81.5, movie.getRatings().get("RottenTomatoes").doubleValue(), Double.MIN_VALUE);
    }

    @Test
    public void testModifyJSON() throws Exception {

        ScriptEngine engine = createEngine();
        Movie movie = createMovie();

        engine.eval("var movie = JSON.parse('" + JsonUtil.stringify(movie) + "');");

        engine.eval("movie.title='Blade Runner 2049';" +
                "movie.year += 34;" +
                "movie.genres.push('Drama');" +
                "movie.ratings.Metacritic=81;" +
                "movie.ratings.RottenTomatoes=81.5;" +
                "movie.note='Added unmapped property in Javascript';");

        String json = (String) engine.eval("JSON.stringify(movie);");
        movie = JsonUtil.parse(Movie.class, json);

        Assert.assertEquals("Blade Runner 2049", movie.getTitle());
        Assert.assertEquals(2016, movie.getYear());
        Assert.assertEquals(81.0, movie.getRatings().get("Metacritic").doubleValue(), Double.MIN_VALUE);
        Assert.assertEquals(81.5, movie.getRatings().get("RottenTomatoes").doubleValue(), Double.MIN_VALUE);
    }

    @Test
    public void testCreateJSON() throws Exception {

        ScriptEngine engine = createEngine();

        engine.eval("var result = { \n" +
                "\ttitle: 'Ghost Busters', \n" +
                "\tyear: 1984,\n" +
                "\tgenres: ['Action', 'Comedy', 'Fantasy'],\n" +
                "\tratings: {\n" +
                "\t\tIMDB: 7.8,\n" +
                "\t\tMetacritic: 71\n" +
                "\t}\n" +
                "};");

        String json = (String) engine.eval("JSON.stringify(result);");
        System.out.println(json);

        Movie movie = JsonUtil.parse(Movie.class, json);
        Assert.assertEquals("Ghost Busters", movie.getTitle());
        Assert.assertEquals(1984, movie.getYear());
        Assert.assertNotNull(movie.getGenres());
        Assert.assertEquals(3, movie.getGenres().size());
        Assert.assertTrue(movie.getGenres().containsAll(Arrays.asList("Action", "Comedy", "Fantasy")));
        Assert.assertNotNull(movie.getRatings());
        Assert.assertEquals(2, movie.getRatings().size());
        Assert.assertEquals(7.8, movie.getRatings().get("IMDB").doubleValue(), Double.MIN_VALUE);
        Assert.assertEquals(71, movie.getRatings().get("Metacritic").doubleValue(), Double.MIN_VALUE);
    }

    @Test
    public void testPutGetBinaryData() throws Exception {

        byte[] data = new byte[12345];
        ThreadLocalRandom.current().nextBytes(data);

        ScriptEngine engine = createEngine();
        engine.put("data", data);
        engine.eval("var a=JSON.stringify(data);\n" +
                "var len=data.length;\n" +
                "var result={" +
                "bin: data, len: data.length" +
                "}");

        Assert.assertEquals(12345, engine.get("len"));
        Assert.assertArrayEquals(data, (byte[]) engine.eval("result.bin"));
        Assert.assertEquals(12345, engine.eval("result.len"));
    }
}
