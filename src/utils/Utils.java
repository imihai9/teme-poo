package utils;

import actor.ActorsAwards;
import common.Constants;
import entertainment.Genre;
import entertainment.Show;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import user.SubscriptionType;
import user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The class contains static methods that helps with parsing.
 * <p>
 * We suggest you add your static methods here or in a similar class.
 */
public final class Utils {
    /**
     * for coding style
     */
    private Utils() {
    }

    /**
     * Transforms a string into an enum
     *
     * @param genre of video
     * @return an Genre Enum
     */
    public static Genre stringToGenre(final String genre) {
        return switch (genre.toLowerCase()) {
            case "action" -> Genre.ACTION;
            case "adventure" -> Genre.ADVENTURE;
            case "drama" -> Genre.DRAMA;
            case "comedy" -> Genre.COMEDY;
            case "crime" -> Genre.CRIME;
            case "romance" -> Genre.ROMANCE;
            case "war" -> Genre.WAR;
            case "history" -> Genre.HISTORY;
            case "thriller" -> Genre.THRILLER;
            case "mystery" -> Genre.MYSTERY;
            case "family" -> Genre.FAMILY;
            case "horror" -> Genre.HORROR;
            case "fantasy" -> Genre.FANTASY;
            case "science fiction" -> Genre.SCIENCE_FICTION;
            case "action & adventure" -> Genre.ACTION_ADVENTURE;
            case "sci-fi & fantasy" -> Genre.SCI_FI_FANTASY;
            case "animation" -> Genre.ANIMATION;
            case "kids" -> Genre.KIDS;
            case "western" -> Genre.WESTERN;
            case "tv movie" -> Genre.TV_MOVIE;
            default -> null;
        };
    }

    /**
     * Transforms a string into an enum
     *
     * @param award for actors
     * @return an ActorsAwards Enum
     */
    public static ActorsAwards stringToAwards(final String award) {
        return switch (award) {
            case "BEST_SCREENPLAY" -> ActorsAwards.BEST_SCREENPLAY;
            case "BEST_SUPPORTING_ACTOR" -> ActorsAwards.BEST_SUPPORTING_ACTOR;
            case "BEST_DIRECTOR" -> ActorsAwards.BEST_DIRECTOR;
            case "BEST_PERFORMANCE" -> ActorsAwards.BEST_PERFORMANCE;
            case "PEOPLE_CHOICE_AWARD" -> ActorsAwards.PEOPLE_CHOICE_AWARD;
            default -> null;
        };
    }

    /**
     * Transforms a string into an enum
     *
     * @param subscription type of user
     * @return a SubscriptionType enum
     */
    public static SubscriptionType stringToSubType(final String subscription) {
        return switch (subscription.toLowerCase()) {
            case "basic" -> SubscriptionType.BASIC;
            case "premium" -> SubscriptionType.PREMIUM;
            default -> null;
        };
    }

    /**
     * Transforms an array of JSON's into an array of strings
     *
     * @param array of JSONs
     * @return a list of strings
     */
    public static ArrayList<String> convertJSONArray(final JSONArray array) {
        if (array != null) {
            ArrayList<String> finalArray = new ArrayList<>();
            for (Object object : array) {
                finalArray.add((String) object);
            }
            return finalArray;
        } else {
            return null;
        }
    }

    /**
     * Transforms an array of JSON's into a map
     *
     * @param jsonActors array of JSONs
     * @return a map with Actors Awards as key and Integer as value
     */
    public static Map<ActorsAwards, Integer> convertAwards(final JSONArray jsonActors) {
        Map<ActorsAwards, Integer> awards = new LinkedHashMap<>();

        for (Object iterator : jsonActors) {
            awards.put(stringToAwards((String) ((JSONObject) iterator).get(Constants.AWARD_TYPE)),
                    Integer.parseInt(((JSONObject) iterator).get(Constants.NUMBER_OF_AWARDS)
                            .toString()));
        }

        return awards;
    }

    /**
     * Transforms an array of JSON's into a map
     *
     * @param movies array of JSONs
     * @return a map with String as key and Integer as value
     */
    public static Map<String, Integer> watchedMovie(final JSONArray movies) {
        Map<String, Integer> mapVideos = new LinkedHashMap<>();

        if (movies != null) {
            for (Object movie : movies) {
                mapVideos.put((String) ((JSONObject) movie).get(Constants.NAME),
                        Integer.parseInt(((JSONObject) movie).get(Constants.NUMBER_VIEWS)
                                .toString()));
            }
        } else {
            System.out.println("NU ESTE VIZIONAT NICIUN FILM");
        }

        return mapVideos;
    }

    /**
     * Performs a case insensitive search of all strings from 'words' in 'string' as separate words
     *
     * @param stringIn      - haystack string
     * @param description - needles (the strings which must be contained in 'string')
     * @return - boolean value: true if 'string' contains all 'words'
     */
    public static boolean containsAllWords(final String stringIn, final List<String> description) {
        String string = stringIn.toLowerCase();
        String[] separateWords = string.split("[^a-zA-Z0-9]");

        for (String w1 : description) {
            boolean foundWord = false;
            for (String w2 : separateWords) {
                if (w1.equals(w2)) {
                    foundWord = true;
                    break;
                }
            }

            if (!foundWord) {
                return false;
            }
        }

        return true;
    }

    /**
     *  Calculates the number of users that have the show in one of their
     *  collections:
     * @param users    - the list of users
     * @param show
     * @param criteria - Criteria = CRITERIA_FAVOURITE   => Collection = favouriteMovies
     *                 Criteria = CRITERIA_MOST_VIEWED => Collection = history
     * @return - the requested number of users
     */
    public static Integer getUserStats(final List<User> users, final Show show,
                                       final String criteria) {
        String title = show.getTitle();

        Integer cnt = 0;

        if (criteria.equals(Constants.CRITERIA_FAVORITE)) {
            for (User user : users) {
                if (user.getFavoriteShows().contains(title)) {
                    cnt++;
                }
            }
        } else if (criteria.equals(Constants.CRITERIA_MOST_VIEWED)) {
            for (User user : users) {
                Map<String, Integer> userHistory = user.getHistory();

                if (userHistory.containsKey(title)) {
                    cnt += userHistory.get(title);
                }
            }
        }

        return cnt;
    }

    /**
     * @param users - the list of users
     * @param show  - the show to be searched
     * @return boolean - is the show in any of the users' favourite list?
     */
    public static boolean hasBeenFavorited(final List<User> users, final Show show) {
        String title = show.getTitle();

        for (User user : users) {
            if (user.getFavoriteShows().contains(title)) {
                return true;
            }
        }

        return false;
    }

    /**
     * @param users - the list of users
     * @param shows - the list of shows
     * @return - a map between the genres and the number of apparitions in users' favorite lists
     */
    public static Map<Genre, Integer> getFavoriteGenres(final List<User> users,
                                                        final List<Show> shows) {
        Map<Genre, Integer> genresPopularity = new HashMap<>();

        // initialize map
        for (Genre genre : Genre.values()) {
            genresPopularity.put(genre, 0);
        }

        for (User currUser : users) {
            Map<String, Integer> history = currUser.getHistory();
            for (Map.Entry<String, Integer> entry : history.entrySet()) {
                shows.stream()
                        .filter(show -> show.getTitle().equals(entry.getKey()))
                        .findFirst()
                        .ifPresent(show -> show.getGenres()
                                .forEach(genre -> genresPopularity
                                        .put(genre, genresPopularity.get(genre) + entry.getValue()))
                        );
            }
        }

        return genresPopularity;
    }
}
