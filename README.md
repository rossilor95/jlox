# JLox
This is a Java interpreter for the Lox language, as described in the book 
[Crafting Interpreters](http://craftinginterpreters.com/) by Bob Nystrom.

## Usage
You need to have Java 23 installed on your system to use jlox. 

Clone the repository and navigate to the `jlox` directory. You first need to build the project using Gradle:
```bash
./gradlew build
```
From now on, you can run the interpreter using the following command:
```bash
java -jar build/libs/jlox.jar
```