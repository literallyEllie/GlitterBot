package de.elliepotato.glitterbot.command.staff

import de.elliepotato.glitterbot.GlitterBot
import de.elliepotato.glitterbot.command.GlitterCmd
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent
import java.util.logging.Level
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager
import javax.script.ScriptException

/**
 * Created by Ellie on 27.7.17 for Discord.
 *
 *    Copyright 2017 Ellie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
class CmdEval: GlitterCmd("eval", Permission.MANAGE_EMOTES, "Eval engine - nerds only.",
        "<code>", 2) {

    private val scriptEngine: ScriptEngine = ScriptEngineManager().getEngineByName("nashorn")

    init {
        try {
            scriptEngine.eval("var imports = new JavaImporter(java.io, java.lang, java.staff, Packages.net.dv8tion.jda.core, "
                    + "Packages.net.dv8tion.jda.core.entities, Packages.net.dv8tion.jda.core.managers, " +
                    "Packages.de.elliepotato.glitterbot.staff);")
            scriptEngine.put("gb", GlitterBot.instance)
        } catch (e: ScriptException) {
            e.printStackTrace()
            GlitterBot.instance.log("Failed to setup Eval", Level.WARNING)
        }
    }

    override fun abstractExec(e: GuildMessageReceivedEvent, args: Array<String>) {
        scriptEngine.put("jda", e.jda)
        scriptEngine.put("e", e)
        val output: Any?
        var input = e.message.rawContent.substring(GlitterBot.instance.conf.botPrefix.length)
        input = input.substring(input.indexOf(" "))
        try {
            output = scriptEngine.eval("(function() { with (imports) {\n$input\n} })();")
        } catch (ex: ScriptException) {
            return message(e.channel, "An error occurred: " + ex.message)
        }

        val outputString: String
        if (output == null) {
            outputString = "Executed successfully with no output."
        } else {
            outputString = output.toString()
            if (outputString.length >= 2048) {
                return message(e.channel, "The output exceeds the Discord limit.")
            }
        }
        e.channel.sendMessage(outputString).queue()

    }

}