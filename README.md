# Jess – A Java Terminal Chess Game

### !!! WORK IN PROGRESS

The game consists of 5 parts:

Events, Exceptions, Figurines, Models and the ChessApp class which handles the interface in Jexer.

Events are not yet implemented, due to time constraints.
All the work on this project is subject to continuation in the future.

The game cannot detect checking properly yet, but the fixes are fairly simple
and can be applied in the future, when I have more time.

Providing a working demo only on moving and capturing pieces, as well as the UI.

# Libraries used

- Jexer : user interface
- JavaTuples : for Pair class

# How to install and run

The project uses Maven, so you can simply use
```bash
mvn compile -f pom.xml
```
and then run this to start the app
```bash
java target/classes/dev/elotonsotilas/ChessApp
```

# Events (TODO)

## Interfaces

`GenericEvent` - core interface for all events in the game.

`FinaliseGameEvent` - interface for events that conclude the game.

## Classes

`Check : GenericEvent` - the event of being checked by the other player. Currently handled outside.

`Checkmate : FinaliseGameEvent` - the event of being checkmated, which ends the game.
Currently handled outside.

`Draw : FinaliseGameEvent` - the event of a draw in the game.
Not yet implemented, so the game would just stall.

`Resignation : FinaliseGameEvent` - the event of resignation.
Not yet implemented; just close the game.

# Exceptions

`IllegalMoveException` - raised when a move cannot occur due to game-breaking reasons.

`InvalidMoveException` - raised when a move notation is invalid.

# Figurines

`GenericPiece` - master class of all other figurines. Used mainly for generics and as an abstract class.

`Queen` - the Queen piece class.

`King` - the King piece class.

`Rook` - the Rook piece class.

`Bishop` - the Bishop piece class.

`Knight` - the Knight piece class.


# Models

`Board` - contains all the essential game methods and the board itself.

`Game` - a game instance.

`Move` - a move constructor that parses the notation and invokes the appropriate commands.

`Player` - a player class that handles white and black team players.


# UNIMPLEMENTED FEATURES

- Proper castling
- En passant
- Input box event handling 
  (work in progress, will try to make it work for the demo, so I can move pieces)
- Notification bar
  (that is coming as soon as possible)

# Final thoughts

Developing this project has been fun,
although I could not finish it in due time, but I would love to continue making it in the future.