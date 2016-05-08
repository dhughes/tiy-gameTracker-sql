import org.h2.tools.Server;
import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

import static spark.Spark.halt;

/**
 * Created by doug on 5/3/16.
 */
public class Main {

    public static void main(String[] args) throws SQLException {
        Server.createWebServer().start();
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        initializeDatabase(conn);

        Spark.get(
                "/",
                (request, response) -> {
                    HashMap m = new HashMap();

                    m.put("games", selectGames(conn));

                    return new ModelAndView(m, "home.mustache");

                },
                new MustacheTemplateEngine()
        );

        Spark.get(
                "/create-game",
                (request, response) -> {
                    HashMap<String, Object> m = new HashMap<String, Object>();
                    m.put("action", "/create-game");
                    // show game form
                    return new ModelAndView(m, "gameForm.mustache");
                },
                new MustacheTemplateEngine()
        );

        Spark.post(
                "/create-game",
                (request, response) -> {

                    // create the game and add to the user
                    try {
                        Game game = new Game(
                                0,
                                request.queryParams("gameName"),
                                request.queryParams("gameGenre"),
                                request.queryParams("gamePlatform"),
                                Integer.valueOf(request.queryParams("gameYear"))
                        );

                        insertGame(conn, game);

                        response.redirect("/");
                        halt();

                    } catch (NumberFormatException e) {
                        // show game form
                        HashMap m = new HashMap();
                        m.put("error", "There was a problem with your release year.");

                        m.put("gameId", request.queryParams("gameId"));
                        m.put("gameName", request.queryParams("gameName"));
                        m.put("gameGenre", request.queryParams("gameGenre"));
                        m.put("gamePlatform", request.queryParams("gamePlatform"));
                        m.put("gameYear", request.queryParams("gameYear"));

                        m.put("action", "/create-game");

                        return new ModelAndView(m, "gameForm.mustache");

                    }

                    return null;
                },
                new MustacheTemplateEngine()
        );

        Spark.get(
                "/edit-game",
                (request, response) -> {
                    HashMap<String, Object> m = new HashMap<String, Object>();

                    Game game = readGame(conn, Integer.parseInt(request.queryParams("id")));

                    m.put("gameId", game.id);
                    m.put("gameName", game.name);
                    m.put("gameGenre", game.genre);
                    m.put("gamePlatform", game.platform);
                    m.put("gameYear", game.releaseYear);

                    m.put("action", "/edit-game");

                    // show game form
                    return new ModelAndView(m, "gameForm.mustache");
                },
                new MustacheTemplateEngine()
        );

        Spark.post(
                "/edit-game",
                (request, response) -> {
                    // create the game and add to the user
                    try {
                        Game game = new Game(
                                Integer.parseInt(request.queryParams("gameId")),
                                request.queryParams("gameName"),
                                request.queryParams("gameGenre"),
                                request.queryParams("gamePlatform"),
                                Integer.valueOf(request.queryParams("gameYear"))
                        );

                        updateGame(conn, game);

                        response.redirect("/");
                        halt();

                    } catch (NumberFormatException e) {
                        // show game form
                        HashMap m = new HashMap();
                        m.put("error", "There was a problem with your release year.");

                        m.put("gameId", request.queryParams("gameId"));
                        m.put("gameName", request.queryParams("gameName"));
                        m.put("gameGenre", request.queryParams("gameGenre"));
                        m.put("gamePlatform", request.queryParams("gamePlatform"));
                        m.put("gameYear", request.queryParams("gameYear"));

                        m.put("action", "/edit-game");

                        return new ModelAndView(m, "gameForm.mustache");

                    }

                    return null;
                },
                new MustacheTemplateEngine()
        );

        Spark.get(
                "/delete-game",
                (request, response) -> {
                    deleteGame(conn, Integer.parseInt(request.queryParams("id")));

                    response.redirect("/");
                    halt();

                    return null;
                }
        );

    }

    private static void deleteGame(Connection connection, int id) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("DELETE FROM game WHERE id = ?");
        stmt.setInt(1, id);
        stmt.execute();
    }

    private static void updateGame(Connection connection, Game game) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("UPDATE GAME SET name = ?, genre = ?, platform = ?, releaseYear = ? WHERE id = ?");
        stmt.setString(1, game.name);
        stmt.setString(2, game.genre);
        stmt.setString(3, game.platform);
        stmt.setInt(4, game.releaseYear);
        stmt.setInt(5, game.id);
        stmt.execute();
    }

    private static Game readGame(Connection connection, int id) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM game WHERE id = ?");
        stmt.setInt(1, id);
        ResultSet resultSet = stmt.executeQuery();
        resultSet.next();

        Game game = new Game(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("genre"),
                resultSet.getString("platform"),
                resultSet.getInt("releaseYear")
        );

        return game;
    }

    private static ArrayList<Game> selectGames(Connection connection) throws SQLException {
        ArrayList<Game> games = new ArrayList<>();
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM game");

        while (resultSet.next()) {
            Game game = new Game(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("genre"),
                    resultSet.getString("platform"),
                    resultSet.getInt("releaseYear")
            );

            games.add(game);
        }

        return games;
    }

    private static void insertGame(Connection connection, Game game) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement("INSERT INTO game VALUES (NULL, ?, ?, ?, ?)");
        stmt.setString(1, game.name);
        stmt.setString(2, game.genre);
        stmt.setString(3, game.platform);
        stmt.setInt(4, game.releaseYear);
        stmt.execute();
    }

    private static void initializeDatabase(Connection connection) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS game (id IDENTITY, name VARCHAR, genre VARCHAR, platform VARCHAR, releaseYear INT)");
    }

}
