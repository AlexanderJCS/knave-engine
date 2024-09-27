# Knave Engine

Knave Engine computationally solves Raymond Smullyan's 1978 [Knights and Knaves puzzle](https://en.wikipedia.org/wiki/Knights_and_Knaves). It is a logic puzzle where you are given a group of people, some of whom always tell the truth (knights) and some of whom always lie (knaves). The goal is to determine who is who based on a series of statements made by the people.

## Installation

To install Knave Engine, download the jar from the [releases page](https://github.com/AlexanderJCS/knave-engine/releases/tag/1.0.0) and run it with:

```bash
$ java -jar KnaveEngine-1.0.0.jar
```

## Usage

The Knave Engine is a command line tool that takes a series of statements as input and outputs the solution to the puzzle. Below is an example:

```bash
$ java -jar KnaveEngine-1.0.0.jar
Enter a claim for character a (or 'end' to finish): (a = T) & (b = T)
Enter a claim for character b (or 'end' to finish): a = F
Enter a claim for character c (or 'end' to finish): end
a is a Knave, b is a Knight,
```

Another example, this time when we do not define b's statement:
```bash
$ java -jar KnaveEngine-1.0.0.jar

Enter a claim for character a (or 'end' to finish): (a = F) & (b = F)
Enter a claim for character b (or 'end' to finish): end
a is a Knave, b is a Knight,
```

Below you can see a list of the available operators and their meanings:

| Operator              | Meaning |
|-----------------------|---------|
| `&`, `^`, or `∧`      | and     |
| `\|`, `∨`, `v` or `V` | or      |
| `>` or `→`            | implies |
| `=`                   | equals  |
