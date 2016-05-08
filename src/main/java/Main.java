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
        // Start a new instance of the H2 database.


        // Create a connection object using DriverManager


        // initialize the database using the initializeDatabase() method. This will create the required table.


        // fill in the spark route definition

                // set the route to "/"

                // add the lambda

                    // create a hashmap for the model


                    // use your selectGames method to select all of the games from the database


                    // put the games arraylist into the model


                    // show the home page template

                // add the mustache template engine



        // create a "get" spark route for the create-game endpoint

                // set the endpoint

                // add your lambda

                    // create a hashmap for your model

                    // add a value named "action" to the model that sets the target that the game form will post to.
                    // for example, if you wanted the action to be "/create-game", then you'd set the "action" property
                    // to "/create-game"

                    // show game form

                // add the mustache template engine


        // create a post route for create-game

                // set the endpoint path

                // add your lambda


                    // use a try/catch block when creating the game. This is used to catch validation
                    // errors on the game year.

                        // create a new instance of game using the data posted from the game form.
                        // set the ID to 0 since this is a new game.
                        // You will need to parse the gameYear parameter into an integer too. This is what might throw an error


                        // use the insertGame method to insert the game into the database


                        // redirect to the webroot

                        // halt this request


                    // here you will need to catch any exceptions thrown if the game year isn't actually a number

                        // create a hashmap for your model

                        // add an error message, set the key in the model to "error"


                        // Add the five fields posted (for id, name, genre, platform, and year) into the model
                        // this should be five lines of code.

                        // set the action property in the model to the create-game path

                        // show the gameForm template.

                    // return null

                // add the mustache template engine


        // create a get route for the edit-game page

                // set the endpoint route

                // add your lambda

                    // create a hashmap for your model


                    // You should receive a query parameter named id. This will be an integer in string form.
                    // parse the value as an integer and put this into a variable


                    // use the readGame method to read your game using the id you just parsed


                    // put the game's data into five fields in the model for id, name, genre, platform and year.
                    // you can read these values directly from the game.
                    // you should write five lines of code below.


                    // set a property in the model named "action" and set it to the endpoint for edit-game


                    // show game form

                // add the mustache tempalte engine


        // create a spark post endpoint for edit-game

                // add the endpoint for edit-game

                // add your lambda

                    // try create the game and add to the user
                    // you'll need to handle any exceptions related to the game release year

                        // create a new instance of Game and set its properties to the submitted data.
                        // take note that when editing a game the id of the game will be submitted and you'll
                        // need to put that into your instance. Also, use the constructor to provide these five values


                        // use the updateGame method to update the game record in the database


                        // redirect to the homepage

                        // halt this request


                    // now, be sure to catch any error related to parsing the game year.

                        // create a hashmap for the model

                        // add a key for error and set an error message


                        // add all five submitted values (id, name, genre, platform, and year) into the model
                        // there should be five lines of code below.


                        // set the post action to the endpoint for edit-game in the model

                        // show game form


                    // return null

                // add the mustache template engine


        // add a get endpoint for delete-game

                // set the endpoint

                // add your lambda

                    // call the deleteGame method. You'll need to parse the id in the query params
                    // I'm not handling any number parsing errors here because in the perfect would
                    // we're controlling this value when generating the form. However, in the real
                    // world we would need to.


                    // redirect to the homepage


                    // halt the request


                    // return null

    }

    private static void deleteGame(Connection connection, int id) throws SQLException {
        // create a prepared statement to delete the game that has the provided id.

        // set the parameter for the id to the provided id.

        // execute the statement

    }

    private static void updateGame(Connection connection, Game game) throws SQLException {
        // create a prepared statement to update the game record in the DB for the provided Game instance


        // set the give properties in the update statement (name, genre, platform, year, and id)
        // there should be give lines of code


        // execute the statement.

    }

    private static Game readGame(Connection connection, int id) throws SQLException {
        // create a prepared statement to select the game matching the provided ID

        // set the parameter for the game id

        // execute the query and set this into a ResultSet variable

        // read the first line of the result set using the next() method on ResultSet


        // create a new game. Pass the give fields you read from the database into the constructor


        // return the game

    }

    private static ArrayList<Game> selectGames(Connection connection) throws SQLException {
        // create an arraylist to hold all the games in our database

        // create a new statement

        // use the statement to execute a query to select all rows from the game table


        // iterate over the result set while we have records to read.

            // create a new instance of game using the data in the query.


            // add the game to the games arraylist


        // return the arraylist of games

    }

    private static void insertGame(Connection connection, Game game) throws SQLException {
        // create a prepared statement to insert a new game into the game table

        // set the four fields (not ID!) for the prepared statement
        // you should have four lines of code here


        // execute the statement

    }

    private static void initializeDatabase(Connection connection) throws SQLException {
        // Create a new SQL statement


        // execute a statement to create the game table if it doesn't exist already.
        // the id field should be an IDENTITY

    }

}
