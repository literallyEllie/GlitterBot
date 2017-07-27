package de.elliepotato.glitterbot.command.staff

import de.elliepotato.glitterbot.GlitterBot
import de.elliepotato.glitterbot.backend.RaffleListener
import de.elliepotato.glitterbot.command.GlitterCmd
import de.elliepotato.glitterbot.util.DateUtil
import de.elliepotato.glitterbot.util.DiscordRaffle
import net.dv8tion.jda.core.EmbedBuilder
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.TextChannel
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent

/**
 * Created by Ellie on 26.7.17 for Discord.
 * Affiliated with www.elliepotato.de
 *
 */
class CmdRaffle: GlitterCmd("raffle", Permission.MANAGE_EMOTES, "Raffle management.",
        "<start <keyword> | stop | pause | info | draw <amount>>", 2) {

    val raffleList: RaffleListener = GlitterBot.instance.raffleListener
    var infoEmbed: EmbedBuilder = setupEmbedBuilder("Information about the current raffle:", "There is no current raffle.")
    var drawEmbed: EmbedBuilder? = null


    override fun abstractExec(e: GuildMessageReceivedEvent, args: Array<String>) {
        val c: TextChannel = e.channel


        when (args[1]) {
            "start" -> {
                if (args.size != 3) {
                    return message(c, correctUsage())
                }
                if (raffleList.isEnabled()) {
                    return message(c, "There is already a raffle going on. Do `!raffle stop` to end the madness.")
                }
                message(c, "Starting a raffle with the keyword `${args[2]}`.")
                raffleList.start(args[2], e.member.user.name + "#" + e.member.user.discriminator)
                infoEmbed.setDescription("It is open to join with the keyword **${args[2]}**.")
            }
            "stop" -> {
                if (!raffleList.isEnabled()) {
                    return message(c, "There be no current raffle, do `!raffle start` to be begin one. :raised_hands:")
                }
                message(c, "Killed the raffle.. :sleeping:")
                raffleList.stop()
                infoEmbed.clearFields()
                infoEmbed.setDescription("There is no current raffle.")
            }
            "pause" -> {
                if (!raffleList.isEnabled()) {
                    return message(c, "There be no current raffle, do `!raffle start` to be begin one. :raised_hands: ")
                }
                val closed = raffleList.current!!.closed
                message(c, (if (closed) ":ok_hand: Resumed raffle." else ":raised_hand: Paused the raffle. Do `!raffle pause` to undo."))
                raffleList.current!!.closed = !closed
                infoEmbed.setDescription(if (closed) "The raffle is currently paused." else "It is open to join with the keyword **${raffleList.
                        current!!.keyWord}**.")
            }
            "info" -> {
                if (raffleList.isEnabled()) {
                    infoEmbed.clearFields()
                    val r: DiscordRaffle = raffleList.current!!
                    infoEmbed.addField("Paused?", if (r.closed) "Yes." else "No.", true)
                    infoEmbed.addField("Participants:", "" + r.participants.size, true)
                    infoEmbed.addField("Started by:", r.startedBy, true)
                    infoEmbed.addField("Started:", DateUtil.instance.formatDateDiff(r.start) + " ago", true)
                }
                c.sendMessage(infoEmbed.build()).queue()
            }
            "draw" -> {
                if (!raffleList.isEnabled()) {
                    return message(c, "There be no current raffle, do `!raffle start` to be begin one. :raised_hands: ")
                }

                if (args.size != 3) {
                    return message(c, correctUsage())
                }

                val toDraw: Int
                try {
                    toDraw = args[2].toInt()
                    if (toDraw < 1) throw NumberFormatException("0 is too low.")
                } catch(e: NumberFormatException) {
                    return message(c, ":x: Invalid number! (${e.message})")
                }

                drawEmbed = setupEmbedBuilder("Drawing $toDraw ${if (toDraw == 1) "person" else "people"} from the raffle...", "The raffle is now paused.\n", "To draw again do: !raffle draw x")

                val drawn = raffleList.current!!.draw(toDraw)
                val b: StringBuilder = StringBuilder()
                drawn.map { e.jda.getUserById(it) }.forEach { b.append(it.name + "#" + it.discriminator + "\n") }

                drawEmbed!!.addField("Congratulations to the following!", b.toString(), false)
                c.sendMessage(drawEmbed!!.build()).queue()
            }


        }


    }

}