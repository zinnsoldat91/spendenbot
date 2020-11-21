# spendenbot
A bot that sends Discord messages on donations

# environment variables

|Name| Description |
|---|---|
|`DISCORD_TOKEN` | Token for the Discord bot|
|`DISCORD_GUILD_ID` | Guild ID in which the bot should post to |
|`DISCORD_CHANNEL_IDS` | Comma separated list of channel ids to which the bot should write to |


# Run it

`docker run -d --name donationbot --restart always --env-file=/opt/donationbot.env zinnsoldat/donationbot:2020-11-21_3`