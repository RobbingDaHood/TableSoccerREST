import Model.PointsPrPlayerPlayer
import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimaps
import groovy.json.JsonSlurper
import ratpack.groovy.handling.GroovyChainAction

import java.sql.Timestamp
import java.text.DateFormat
import java.text.SimpleDateFormat

import static ratpack.jackson.Jackson.json

/**
 * Created by super on 04/10/2016.
 */
class PointsPrPlayer extends GroovyChainAction {

    @Override
    void execute() {
        all {
            byMethod {

                options {
                    response.headers.set('Access-Control-Allow-Methods:', 'GET')
                    response.headers.set('Access-Control-Allow-Origin', '*')
                    render "OK"
                }

                get {
                    def url = 'http://localhost:5050/games' //TODO replace with configs un the future
                    def response = new JsonSlurper().parseText(url.toURL().text)

                    Map<String, Integer> scores = new HashMap<>()
                    Map<String, Integer> numberOfGames = new HashMap<>()
                    response.each {game ->
                        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                        Date d = formatter.parse(game.getAt("lastUpdated"));
                        Timestamp ts = new Timestamp(d.getTime());

                        def addOne = { String name, Integer score -> score + 1 }
                        numberOfGames.computeIfPresent(game.getAt("player_blue_1"), addOne)
                        numberOfGames.computeIfPresent(game.getAt("player_blue_2"), addOne)
                        numberOfGames.computeIfPresent(game.getAt("player_red_1"), addOne)
                        numberOfGames.computeIfPresent(game.getAt("player_red_2"), addOne)

                        numberOfGames.putIfAbsent(game.getAt("player_blue_1"), 1)
                        numberOfGames.putIfAbsent(game.getAt("player_blue_2"), 1)
                        numberOfGames.putIfAbsent(game.getAt("player_red_1"), 1)
                        numberOfGames.putIfAbsent(game.getAt("player_red_2"), 1)

                        scores.putIfAbsent(game.getAt("player_blue_1"), 1)
                        scores.putIfAbsent(game.getAt("player_blue_2"), 2)
                        scores.putIfAbsent(game.getAt("player_red_1"), 3)
                        scores.putIfAbsent(game.getAt("player_red_2"), 4)
                    }

//                    HashMultimap<Integer, String> playersPrValue =
//                            Multimaps.invertFrom(Multimaps.forMap(scores),
//                                    HashMultimap.<Integer, String> create());


                    List<PointsPrPlayerPlayer> result = new LinkedList<>()
                    scores.keySet().each {playerName ->
                        result.add(new PointsPrPlayerPlayer(1, scores.get(playerName), numberOfGames.get(playerName), playerName))
                    }

                    render json(result)
                }
            }
        }
    }


}
