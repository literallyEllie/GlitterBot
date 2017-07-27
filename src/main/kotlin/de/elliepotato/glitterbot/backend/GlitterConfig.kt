package de.elliepotato.glitterbot.backend

import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.BufferedWriter
import java.io.File
import java.io.FileReader
import java.io.IOException

/**
 * Created by Ellie on 25.7.17 for Discord.
 * Affiliated with www.elliepotato.de
 *
 */
class GlitterConfig {

    val file:File = File("glitz.json")
    @Transient val token:String
    val botPrefix:String
    val botGame:String
    val botState:String

    init {
        if(!file.exists()){
            if(!file.createNewFile()){
                throw IOException("Failed to create file ${file.name}!")
            }
            val jsonConfig = "{\n" +
                    "\t\"token\":\"none\",\n" +
                    "\t\"botPrefix\":\"!\",\n" +
                    "\t\"botGame\":\"with glitter\",\n" +
                    "\t\"botState\":\"online\"\n" +
                    "}"
            val writer:BufferedWriter = file.bufferedWriter()
            writer.write(jsonConfig)
            writer.close()
        }

        val parser:JSONParser = JSONParser()
        val reader = parser.parse(FileReader(file)) as JSONObject
        token = reader["token"] as String
        botPrefix = reader["botPrefix"] as String
        botGame = reader["botGame"] as String
        botState = reader["botState"] as String
        reader.clear()
    }

}