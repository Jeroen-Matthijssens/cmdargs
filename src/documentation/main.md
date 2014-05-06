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
Either the command (either the main command or a subcommand) always uses subcommands, or
it never uses any.

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

The first example is correct, the second and third example should give an error.

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

### Value lists

If an option can have a value, it can have multiple values. There are two ways to give
mulitple values. The values can be given one at a time by repeating the option and then
the next value in the list. This should work with both long and short options.

```bash
$ command -l first -l second -l third
$ command --long-option=first --long-option=second
```

An other way is to specify the values in one go, this only works with short options. The
values are seperated by a comma. No spaces are allowed in this list.

```bash
$ command --value-option=first,second,third
```

### Boolean Options

Allow for boolean options. As a convention allow the `not-` prefix to negate the option
when it is a boolean value. Do not allow `--not-option=false` or `--not-option=true`.

```bash
$ command --enabled
$ command --not-enabled
$ command --enabled=true
$ command --enabled=false
```

## ordering

The only order that is preserved is that of multiple values being assigned to a single
option. There is no way to infer which option of a pair came before the other. This means
that constructions such as:

```bash
$ command --include=path/to/file --not --include=path/to/file/sub
```

can not be given the meaning "include everthing under `path/to/file` but not what is
under `path/to/file/sub`". The above example has the `--not` option set and the
`--include` option, furthermore the `--include` option has two value assigned:
`path/to/file` and `path/to/file/sub`. But there is no way to know where in the list of
values for `--include` the `--not` option would have appeared.


# Constructing a scheme

Keeping around references to the option that are valid minimized the risk of mistyping
representations of the options.

```java
public class Options {
	public static final ValueOption<String> msg = new StringOption ("message", "m", true);
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

A RuntimeException will be thrown if the parsing of the arguments has failed. The parser
can print a usefull message to `System.out` when something goes wrong.

```java
public static void main (String [] args) {
	Parser parser = ParserFactory.newInstance (Options.scheme);

	try { ParsedCommand parsed = parser.parse (args); }
	catch (CommandLineException exception) { parser.printError (); }

	new Main (parsed).run ();
}
```

As an example we just print out what was given to the `--message` option, but only if the
`--print` option is given

```java
public void run () {
	if ( parsed.hasOption (Options.printAll) ) { printAll (); }
	else if ( ! parsed.hasOption (Options.print) ) { return; }
	else if ( parsed.getOptionValue (Options.print)) { printFirst (); }
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

```java
$ java -jar command.jar --print
Error: missing required argument `--message`!
$ java -jar command.jar --message
Error: missing value for `--message`!
```


# Classes

## Option

An Interface describing the options a valid option on the commandline.

```java
public interface Option {
	public String getRepresentation ();
	public String getDescription ();
	public boolean hasShortRepresentation ();
	public String getShortRepresentation ();

	public boolean matches (String representation);
	public boolean isRequired ();
}
```

When calling `option.getRepresentation ()` the string that is returned is always the
representation with dashes (`long-option` instead of `long option`).

```java
public interface ValueOption<T> extends Option {
	public T validateValue (String value) throws WrongValueException;
}
```

A couple of implementations are provided. `BasicOption` implements only the `Option`
interface. `BooleanOption`. `DecimalValueOption`, `NumberValueOption`, `PathValueOption`
and `StringValueOption` implement the `ValueOption` interface.

They all have constructors with a long representation and a constructor with both a long
and short representation. You can provide an extra boolean indicating whether the option
is required or not. By default options are not required.

```java
Option basic = new BasicOption ("basic option");
ValueOption<Boolean> bool = new BooleanOption ("required boolean", true);
ValueOption<Long> long = new NumberValueOption ("number", "n");
ValueOption<String> string = new StringValueOption ("required string", "s", true);
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

## SubCommand

The command should return a `CommandScheme` that represents the options, commands or
arguments that can follow this command.

```java
public interface Command {
	public boolean matches (String representation);
	public String getRepresentation ();
	public CommandScheme getCommandScheme ();
}
```

One implementation is provided (`SubCommandImpl`). It can be instantiated with a string
representing the subcommand, or with a string and an options scheme.

```java
SubCommand command = new SubCommandImpl ("sub");

OptionScheme scheme = new CustomOptionsScheme ();
SubCommand command = new SubCommandImpl ("sub", scheme);
```

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


## ParsedCommand

```java
public interface ParsedCommand {
	public boolean hasCommand ();
	public Command getCommand ();
	public ParsedCommand getParsedCommand ();

	public boolean isOptionPresent (Option option);
	public <T> T getOptionValue (ValueOption<T> option);
	public <T> List<T> getOptionValues (ValueOption<T> option);

	public boolean isArgumentPresent (Argument<?> argument);
	public <T> T getArgumentValue (Argument<T> argument);

	public String [] getArgumentValues ();
}
```

The `getParsedCommand ()` only returns something sensible if `hasCommand ()` is true. It
returns a new instance of `ParsedCommand` that represents the options and arguments or
commands that were parsed for the command returned by `getCommand ()`.

When using the `getOptionValue ()` method, the option (or representation of the option) has te
refer to an option that supports that type of value. If this is not the case the call will
result in a `ClassCastException`.

## Parser

An Interface for parsers.

```java
public interface Parser {
	public void parse (String [] args);
	public boolean hasSubCommand ();
	public String getSubCommand ();
	public ParsedOptions getOptions ();
	public ParsedOptions getGlobalOoptions ();
}
```

One parser implementation is provided (`ParserImpl`).

It can be instantiated with a list of subcommands. In this case there are no global
options allowed. Recall that when a subcommand can be used, a subcommand has to be used
for all valid `String [] args` sequences.

```java
List<SubCommand> commands = buildSubCommandList ();
Parser parser = new ParserImpl (commands);
```

It can be instantiated with a single options scheme. In this case no
subcommands are allowed and the options scheme will be treathed as the global options
scheme.

```java
OptionScheme scheme = new CustomOptionScheme ();
Parser parser = new ParserImpl (scheme);
```

It can be instantiated with an options scheme and a list op subcommands. This
is the same as a list of subcommands except that global options are now allowed, and are
specified by the options scheme given to `ParserImpl`.

```java
List<SubCommand> commands = buildSubCommandList ();
OptionScheme scheme = new CustomOptionScheme ();
Parser parser = new ParserImpl (scheme, commands);
```




# Example

Suppose we want the following command to be valid.

```bash
$ command -st -u short sub --long-option=long path/to/file
```

We first need to specify that these options are valid. For this we build an
`OptionScheme`. This will contain all the information needed to validate and parse the
command line arguments.

```java
SchemeBuilder schemeBuilder = new SchemeBuilder ();

schemeBuilder.add (new BasicOption ("long option"));
schemeBuilder.add (new BasicOption ("short option", "s"));
schemeBuilder.add (new ValueOption ("with value"));
schemeBuilder.add (new BooleanOption ("enable");
schemeBuilder.add (new Argument ("path", 0));

OptionScheme scheme = schemeBuilder.buildScheme ();
```

When Using this in a program it could look something like this.


```java
public static main (String [] args) {
	OptionScheme scheme = buildScheme ();
	Parser parser = new ParserImpl (scheme);

	parser.parse (args);
	ParsedOptions options = parser.getOptions ();

	assertThat (options.isPresent ("long option"), is (true));
	assertThat (options.getValue ("long option"), is ("long"));

	assertThat (options.isPresent ("s"), is (true));
	assertThat (options.getValue ("s"), is ("short"));

	assertThat (options.getArg (0), is ("path/to/file"));
	assertThat (options.getArg ("path"), is ("path/to/file"));
}

private static OptionScheme buildScheme () {
	SchemeBuilder schemeBuilder = new SchemeBuilder ();
	...
	return scheme;
}

```

When parsing the following command.

```bash
$ command --long-option sub --sub-option
```

```java
public static main (String [] args) {
	OptionScheme scheme = buildScheme ();
	Parser parser = new ParserImpl (scheme);

	parser.parse (args);

	assertThat (parser.hasCommand (), is (true));
	assertThat (parser.getCommand (), is ("sub"));

	ParsedOptions options = parser.getOptions ();
	assertThat (options.isPresent ("long option"), is (false));
	assertThat (options.isPresent ("sub option"), is (true));

	ParsedOptions subOptions = parser.getGlobalOptions ();
	assertThat (subOptions.isPresent ("long option"), is (true));
	assertThat (subOptions.isPresent ("sub option"), is (false));
}

public static OptionScheme buildScheme () {

}
```

# Possible extras?

- A way to indicate that one of a group of options is mandatory, for instance such as the
  tar command. It needs either an x, c, ... .
