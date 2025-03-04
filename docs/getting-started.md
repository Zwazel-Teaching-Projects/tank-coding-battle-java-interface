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
    public void start() {
        GameWorld.startGame(this, LightTank.class);
    }

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

