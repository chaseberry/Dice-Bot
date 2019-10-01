package edu.csh.chase.dice

import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

fun main() {

    val jda = JDABuilder(String(Resources.load("key.txt").readBytes())).build()

    jda.awaitReady()

    val dieRegex = Regex("^roll (\\d+)?d(\\d+)\$")

    jda.addEventListener(object : ListenerAdapter() {

        override fun onMessageReceived(event: MessageReceivedEvent) {
            val raw = event.message.contentRaw

            dieRegex.matchEntire(raw)?.let {

                val rolls = it.groupValues.getOrNull(1)?.toIntOrNull() ?: 1

                val dieValue = it.groupValues.getOrNull(2)?.toIntOrNull() ?: 6

                when {
                    rolls == 0 -> {
                        event.reply("You get nothing. You lose. Good day sir.")
                        return
                    }
                    rolls > 100 -> {
                        event.reply("You can't roll more than 100")
                        return
                    }
                    dieValue == 0 -> {
                        event.reply("d0? Heck off.")
                        return
                    }
                    dieValue == 1 -> {
                        event.reply("What even is a d1?")
                        return
                    }
                    dieValue > 50 -> {
                        event.reply("I'm not rolling higher than a d50")
                        return
                    }
                    else -> Unit
                }

                val die = Die(dieValue)

                val rs = (1..rolls).map {
                    die.roll()
                }

                if (rolls == 1) {
                    event.reply("Rolled ${rs.first()}")
                } else {
                    event.reply("Rolled ${rs.joinToString(",", "[", "]")}=${rs.sum()}")
                }

            }
        }
    })

}

fun MessageReceivedEvent.reply(msg: String) = this.channel.sendMessage(msg).queue()