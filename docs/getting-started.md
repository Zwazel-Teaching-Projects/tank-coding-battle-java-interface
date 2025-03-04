# Get Started

## Installation

To use the Java interface, you need to add the following dependency to your `pom.xml` file:

```xml

<dependency>
    <groupId>dev.zwazel</groupId>
    <artifactId>tank-coding-battle-java-interface</artifactId>
    <version>v0.5</version>
</dependency>
```

And add the following repository to your `settings.xml` or `pom.xml` file:'

```xml

<repository>
    <id>github</id>
    <url>https://maven.pkg.github.com/zwazel-teaching-projects/tank-coding-battle-java-interface</url>
    <snapshots>
        <enabled>true</enabled>
    </snapshots>
</repository>
```

To be able to download the dependency, you need to authenticate with GitHub. You can do this by creating a personal
access token and adding it to your `settings.xml` file or by setting the environment variables `GITHUB_USERNAME` and
`GITHUB_TOKEN`. Read
more [here](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry#authenticating-to-github-packages).

```xml

<server>
    <id>github</id>
    <!--Either set the environment Variable or just replace with your username and GitHub Token-->
    <username>${env.GITHUB_USERNAME}</username>
    <password>${env.GITHUB_TOKEN}</password>
</server>
```

## Usage

To start working on your Bot, you need to create a new class that implements the [
`BotInterface`](https://zwazel-teaching-projects.github.io/tank-coding-battle-java-interface/javadoc/dev/zwazel/bot/BotInterface.html).
This interface provides you with all the methods you need to interact with the game.
There are two methods you need to implement: [
`setup`](https://zwazel-teaching-projects.github.io/tank-coding-battle-java-interface/javadoc/dev/zwazel/bot/BotInterface.html#setup(dev.zwazel.internal.PublicGameWorld))
and [
`processTick`](https://zwazel-teaching-projects.github.io/tank-coding-battle-java-interface/javadoc/dev/zwazel/bot/BotInterface.html#processTick(dev.zwazel.internal.PublicGameWorld)).
The [
`setup`](https://zwazel-teaching-projects.github.io/tank-coding-battle-java-interface/javadoc/dev/zwazel/bot/BotInterface.html#setup(dev.zwazel.internal.PublicGameWorld))
method is called once at the
beginning of the game. In this method, you can initialize your bot. You can for example get the current game
configuration, like how the map is set up and all the connected players.

The [
`processTick`](https://zwazel-teaching-projects.github.io/tank-coding-battle-java-interface/javadoc/dev/zwazel/bot/BotInterface.html#processTick(dev.zwazel.internal.PublicGameWorld))
method is called every tick of the game. In this method, you can decide what your bot should do. You
can for example issue commands to move your bot or shoot.

Here is an example of a simple bot that moves forward every tick:

```java
public class MyBot implements BotInterface {
    @Override
    public void setup(PublicGameWorld world) {
    }

    @Override
    public void processTick(PublicGameWorld world) {
        ClientState myClientState = world.getMyState();

        if (myClientState.state() == ClientState.PlayerState.DEAD) {
            System.out.println("I'm dead!");
            return;
        }

        LightTank tank = (LightTank) world.getTank();
        tank.move(world, Tank.MoveDirection.FORWARD);
    }
}
```

## Running the Game

Before you can run the game, you need to make sure that you have a running game server.
You can download the latest version of the server from
the [releases page](https://github.com/Zwazel-Teaching-Projects/tank-coding-battle/releases).

Once you have the server running, you need to connect your bot to the server.
You can do this by creating a new instance of your bot and getting the instance of
the [GameWorld](https://zwazel-teaching-projects.github.io/tank-coding-battle-java-interface/javadoc/dev/zwazel/GameWorld.html).
The GameWorld uses the Singleton pattern, so you can get the instance of the GameWorld by calling
`GameWorld.getInstance()`.

Then you can start the game by calling `GameWorld.startGame()`. This will start the game and connect your bot to the
server.
You must provide an instance of your bot and the Tank Class you want to use.
Check the [Tank Types](game/tanks/tank-types.md) for more information.

```java
public class Main {
    public static void main(String[] args) {
        MyBot bot = new MyBot();
        GameWorld gameWorld = GameWorld.getInstance();

        gameWorld.startGame(bot, LightTank.class);
    }
}
```

To Successfully connect to the server, you need to provide the server address and port in the `app.properties` file.
For more information, check the [Configuration](config/configuration.md) page.

## Spectating the Game

If you want to spectate the game, you need to join with a Spectator Client.
You can download the latest version of the spectator client from
the [releases page](https://github.com/Zwazel-Teaching-Projects/tank-coding-battle/releases).

To use the client, the game must not have started yet. You'll also start the game from the client by pressing a button.
So make sure the bot only connects to the server, but doesn't start the game.
To do this, call `GameWorld.connectToServer()` instead of `GameWorld.startGame()`.

```java
public class Main {
    public static void main(String[] args) {
        MyBot bot = new MyBot();
        GameWorld gameWorld = GameWorld.getInstance();

        gameWorld.connectToServer(bot, LightTank.class);
    }
}
```