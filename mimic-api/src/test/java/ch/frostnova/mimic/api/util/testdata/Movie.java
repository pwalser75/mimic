package ch.frostnova.mimic.api.util.testdata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Test bean with JAXB mappings.
 *
 * @author pwalser
 * @since 25.01.2018.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Movie {

    private String title;
    private int year;
    private List<String> genres = new LinkedList<>();
    private Map<String, Number> ratings = new HashMap<>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        title = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public Map<String, Number> getRatings() {
        return ratings;
    }

    public void setRatings(Map<String, Number> ratings) {
        this.ratings = ratings;
    }
}