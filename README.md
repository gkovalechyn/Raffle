#FIRST OF ALL
You can set the base command for the plugin as well as the sub commands. For that, see the configuration, it should
be self-explanatory. With the base command being the string after the "/".


##Commands
For this I will be using "Raffle" as the base command; "Create" as the create subcommand; "Buy" as the buy subcommand,
etc.

FOR ALL THE COMMANDS:  
<> - Means that it is necessary to have that argument.  
[] - Meant that that argument is optional.
	
* /raffle <<buy|cancel|list|create>>
Main command, no permission needed.

* /raffle buy <<Player>> <<Amount>>
Buys the tickets from that player's raffle.
Permission: Raffle.Cmd.Buy
	
* /raffle cancel [Player]
Cancels a raffle or another player's raffle.
Permission: Raffle.Cmd.Cancel
	
For cancelling another player's raffle, you will need to have the permission: Raffle.Cmds.Cancel.Others
	
* /raffle list
Lists all the raffles that are currently active.
Permission: Raffle.Cmd.List
	
* /raffle create <<Tickets>> <<Price>> <<Duration>>
Creates a raffle by using the item in your hand.

Permission: Raffle.Cmd.Create
Arguments:
* Tickets - How many tickets the raffle will have.
* Price - The price for each ticket.
* Duration - For how long will the raffle last. If it exceeds this limit and not all tickets are bought,
* the raffle gets cancelled automatically. SEE THE TOPIC BELOW
	
##About the duration
The format for the duration string of the raffle command is numbers followed by specific characters, each one representing
a timespan. (All characters are case-sensitive)
Allowed characters:
* s - Seconds
* m - Minutes
* h - Hours
* d - Days
* w - Weeks
* M - Months
* y - Years

Examples:
* 1d3d30m = 1 day, 3 hours and 30 minutes.
* 30m = 30 minutes
* 1y3M7w13d20h1m = 1 year,, 3 months, 7 weeks, etc.
		
		
	
	