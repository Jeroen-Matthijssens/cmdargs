# Main Goal

Give a consistent way of parsing command line options. This means describing what is and
is not allowed by the conventions we well provide here. A lot of inspiration is drawn from
the git command line interface.

## General Overview

### General Options Layout

All options should have at least a long version. Meaning there should be a way to specify
an option by typing out whole words. If possible a short version (a single character)
should be provided as well. Short options can be combined with each other by writing
one after the other.

Short options are case sensitive, long options are not. The words in long options are
separated with a dash (`-`).

When necessary, parsing of options can be stopped by providing a `--` on the command line.
Everything after a `--` will not be parsed as options. This can be useful for instance
when a path name starts with a dash..

```bash
$ command -s
$ command -stv
$ command --long-option --other-long-option
$ command --long-option -s -- -path/starts/with/dash
```

### Sub Commands

Sub commands can be used. Sub commands can be nested if necessary `$ command sub deepsub`.
The command (either the main command or a subcommand) has to always uses subcommands or
never use any. Which subcommands themselves use subcommands can vary depending on the
subcommand, there is no restriction or correlation between subcommands with respect to the
use of subcommands.

A subcommand is a single word and is case insensitive.

The first non option, non option value string that is found is treated as the subcommand.
Commands can not occur after a `--`.

Option can be given to both the subcommand and the command itself

```bash
$ command --command-option sub --sub-command-option -- notsub
```

### Assigning Values To Options.

As a convention short options allow values directly after the option. When more than one
short option is used, you can not group a short option with a value together with other
short options.

The first example is correct, the third example gives an error. The second example is
interpreted as having the options that correspond to `s`, `t`, and `v` and `value` as a
trailing argument or a subcommand.

```bash
$ command -st -v value -w other
$ command -stv value
$ command -s=value
```

Long options can be assigned values by adding an equals sign and the value.

```bash
$ command --long-option=value --other-long-option=other
```

When there is no equals sign the value will be interpreted as either the subcommand or the
first non option argument.

### Value Lists

If an option can have a value, it can have multiple values. There are two ways to give
mulitple values. The values can be given one at a time by repeating the option and then
the next value in the list. This works with both long and short options.

```bash
$ command -l first -l second -l third
$ command --long-option=first --long-option=second
```

An other way is to specify the values in one go, this only works for long options. The
values are seperated by a comma. No spaces are allowed in this list.

```bash
$ command --long-option=first,second,third
```

### Boolean Options

Allow for boolean options. As a convention allow the `not-` or `no-` prefix to negate the
option when it is a boolean value. Do not allow `--not-option=false` or
`--not-option=true`.

```bash
$ command --enabled
$ command --not-enabled
$ command --enabled=true
$ command --enabled=false
```

## Ordering

The only order that is preserved is that of multiple values being assigned to a single
option. There is no way to infer which option of a pair came before the other. This means
that constructions such as:

```bash
$ command --include=path/to/file --not --include=path/to/file/sub
```

can not be given the meaning "include everything under `path/to/file` but not what is
under `path/to/file/sub`". The above example has the `--not` option set and the
`--include` option, furthermore the `--include` option has two value assigned:
`path/to/file` and `path/to/file/sub`. But there is no way to know where in the list of
values for `--include` the `--not` option would have appeared.


# Constructing a scheme

For the layout of the interfaces used here refer to the classes section.

We keep around the references to the options used to create the `CommandScheme`. We can
reuse these afterwards when looking up values, or checking for its presence. This
minimizes the risk of mistyping the representation of the option.

```java
import static org.tutske.cmdargs.Option.Requirement.*;
import org.tutske.cmdargs.*;

public class Options {
	public static final ValueOption<String> msg = new StringOption ("message", "m", RequireValue);
	public static final ValueOption<Boolean> print = new BooleanOption ("print", "p");
	public static final Option printAll = new BasicOption ("print all", "P");

	// Uses above values, so this has to go after the options definitions
	public static final CommandScheme scheme = Options.createScheme ();

	private static CommandScheme createScheme () {
		return CommandSchemeBuilderFactory.newInstance ()
			.addOption (Options.printAll)
			.addOption (Options.print)
			.addOption (Options.msg)
			.buildScheme ();
	}
}
```

A `RuntimeException` will be thrown if the parsing of the arguments has failed. The parser
can print a usefull message to `System.out` when something goes wrong. Other
`OutputStream`s can be given to the method call.

```java
public class Main {
	private ParsedCommand parsed;

	private Main (ParsedCommand parsed) {
		this.parsed = parsed;
	}

	public static void main (String [] args) {
		Parser parser = ParserFactory.newInstance (Options.scheme);

		try { new Main (parser.parse (args)).run (); }
		catch (CommandLineException exception) { parser.printError (); }
	}

	...
}
```

As an example we just print out what was given to the `--message` option, but only if the
`--print` or `--print-all` option are given. If the value of `--print` is false we don't
print anything either. With the `--print-all` option every value in the list of
`--message` is printed on its own line.

```java
public void run () {
	if ( parsed.hasOption (Options.printAll) ) { printAll (); }
	else if ( ! parsed.hasOption (Options.print) ) { return; }
	else if ( parsed.getOptionValue (Options.print) ) { printFirst (); }
}

public void printAll () {
	List<String> messages = parsed.getOptionValues (Options.msg);
	for ( String message : messages ) { printSingle (message); }
}

public void printFirst () {
	String message = parsed.getOptionValue (Options.msg);
	printSingle (message);
}

public void printSingle (String msg) {
	System.out.println (msg);
}
```

With the above running the application produces the following.

```bash
$ java -jar command.jar --message='Hello World!' --print
Hello World!
$ java -jar command.jar --message='Hello World!' --no-print
$ java -jar command.jar --message='Hello World!'
$ java -jar command.jar --print --message='Hello World!'
Hello World!
$ java -jar command.jar --message=Hello,World --print
Hello
$ java -jar command.jar --message=Hello,World --print-all
Hello
World
```

The corisponding input with short options gives the same result

```bash
$ java -jar command.jar -m 'Hello World!' -p
Hello World!
$ java -jar command.jar -m 'Hello World!' -p false
$ java -jar command.jar -m 'Hello World!'
$ java -jar command.jar -p -m 'Hello World!'
Hello World!
$ java -jar command.jar -m Hello -m World -p
Hello
$ java -jar command.jar -m Hello -m World -P
Hello
World
```

Using the options wrongly will produce useful outputs.

```java
$ java -jar command.jar --print
Error: missing required option `--message`!
$ java -jar command.jar --message
Error: missing value for `--message`!
```


# API

Adding functionality to the option and what they can accept is as simple as implementing
the right interfaces. There is also a `CommandScheme` interface in case you do not wish to
go through the builders. There are only four main once `Option`, `ValueOption<T>`,
`Argument<T>` and `Commad`.

There should generally not be any need to implement the `Parser`, `CommandScheme` or
`ParsedCommand`, but this too is possible.

## CommandSchemeBuilder

This builder is used to create sub`Command`s and the `CommandScheme`s. The implementation
provided by the factory will keep an internal state and will not flush it. As such it can
not be reused unless one scheme extends the previous one.

```java
public interface CommandSchemeBuilder {
	public CommandScheme buildScheme ();
	public Command buildCommand (String representation);

	public CommandSchemeBuilder addOption (Option option);
	public CommandSchemeBuilder addArgument (Argument<?> argument);
	public CommandSchemeBuilder addCommand (Command command);
}
```

## Option

An Interface describing a valid option on the commandline.

```java
public interface Option {
	public static enum Requirement {
		RequireNone, RequirePresence, RequireValue;
	}

	public String getRepresentation ();
	public String getDescription ();
	public boolean hasShortRepresentation ();
	public String getShortRepresentation ();

	public boolean matches (String representation);
	public boolean hasRequirement (Requirement requirement);
}
```

`getRepresentation ()` should return the option in its dashed form with leading dashed.
(`long-option` instead of `long option`). If the option accepts a value it should
implement the following interface.

```java
public interface ValueOption<T> extends Option {
	public Validator<T> getValidator (String representation);
}
```

The validator is used to check if a value on the commandline is acceptable for the option.
It is also used to parse the value (as a string) into the actual representation as an
object of type `T`.

```java
public interface Validator<T> {
	public boolean isValid (String value);
	public T parse (String value);
	public boolean hasDefault ();
	public T defaultValue ();
}
```

A couple of implementations are provided. `BasicOption` implements only the `Option`
interface. `BooleanOption`. `DecimalOption`, `NumberOption`, `PathOption`
and `StringOption` implement the `ValueOption` interface.

They all have constructors with a long representation and a constructor with both a long
and short representation. You can provide an extra `Requirement` argument indicating
whether the option is required or needs a value. By default options are not required, this
is equivalent to passing `RequireNone`. `RequireValue` also implies `RequirePresence`.

```java
Option basic = new BasicOption ("basic option");
ValueOption<Boolean> bool = new BooleanOption ("required boolean", RequirePresence);
ValueOption<Long> long = new NumberValueOption ("number", "n");
ValueOption<String> string = new StringValueOption ("required string", "s", RequireValue);
```

Options should equal each other if they have the same long representation. If the long
representation is the same, the short representations should also be the same. Long
representations can be written either with spaces or with dashes. Two leading dashes are
also stipped.

```java
Option spaces = new BasicOption ("long option");
Option dashes = new BasicOption ("long-option");
Option leading = new BasicOption ("--long-option");

assertThat (spaces.equals (dashes), is (true));
assertThat (spaces.equals (leading), is (true));
```

## Argument

```java
public interface Argument<T> {
	public String getRepresentation ();
	public int getPosition ();
	public boolean isRequired ();
	public boolean matches (String represenation);
	public Validator<T> getValidator ();
}
```

## Command

The command should return a `CommandScheme` that represents the options, commands or
arguments that can follow this command.

```java
public interface Command {
	public boolean matches (String representation);
	public String getRepresentation ();
	public CommandScheme getCommandScheme ();
}
```


## ParsedCommand

```java
public interface ParsedCommand {
	public boolean hasCommand ();
	public Command getCommand ();
	public ParsedCommand getParsedCommand ();

	public boolean hasOption (Option option);
	public <T> T getOptionValue (ValueOption<T> option);
	public <T> List<T> getOptionValues (ValueOption<T> option);

	public boolean hasArgument (Argument<?> argument);
	public <T> T getArgumentValue (Argument<T> argument);

	public String [] getArgumentValues ();
}
```

The `getParsedCommand ()` only returns something sensible if `hasCommand ()` is true. It
returns a new instance of `ParsedCommand` that represents the options and arguments or
commands that were parsed for the command returned by `getCommand ()`.

Say we have options: 

```java
Option longOpt = new BasicOption ("long", "l");
Option otherOpt= new BasicOption ("other", "o");
...
ParsedCommand parsed = parser.parse (args);
```

and was called with 

```bash
$ command --long sub --other
```

`otherOpt` is only available under the sub command. While only `longOpt` is available
directly.

```java
assert (parsed.hasOption (longOpt));
PaserdCommand subparsed = parser.getParsedCommad ();
assert (subparsed.hasOption (otherOpt));
```

When using the `getOptionValue ()` method, the option (or representation of the option) has te
refer to an option that supports that type of value. If this is not the case the call will
result in a `ClassCastException`.


## CommandScheme

This class is used to build up a scheme that is used to validate commandline arguments
against. It should also provide a nice `toString ()` method, to output a useful
description of the options that are allowed, and what they do.

When a subcommand is used, the paser should handle two schemes, the one used for the
global command, and the scheme for the subcommand. The structure for both is the same.

```java
public interface CommandScheme {
	public boolean hasCommands ();
	public boolean hasArguments ();

	public boolean hasOption (Option option);
	public boolean hasOption (String representation);
	public Option getOption (String representation);

	public boolean hasCommand (Command command);
	public boolean hasCommand (String command);
	public Command getCommand (String command);

	public boolean hasArgument (Argument<?> argument);
	public boolean hasArgument (String argument);
	public Argument<?> getArgument (String representation);
	public List<Argument<?>> getArguments ();
}
```


## Parser

An Interface for parsers.

```java
public interface Parser {
	public ParsedCommand parse (String [] args);
}
```


# Planed features

## Bash completion support.

```java
public class Options {
	public static final Option first = new BasicOption ("first", "f");
	public static final Option second = new BasicOption ("second", "s");
	public static final Option third = new BasicOption ("third", "t");

    public static final CommandScheme scheme = Options.createScheme ();

	private static CommandScheme createScheme () {
		return CommandSchemeBuilderFactory.newInstance ()
			.addOption (first)
			.addOption (second)
			.addOption (third)
			.buildScheme ();
	}
}
```

```bash
$ command --<tab><tab>
first    second    third
$ command --se<tab>
$ command --second
```


# Possible features

- A way to indicate that one of a group of options is mandatory, for instance such as the
  tar command. It needs either an x, c, ... .
