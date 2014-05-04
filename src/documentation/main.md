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

Sub commands can be used. A single subcommand is allowed `$ command sub`. Either the
command always uses subcommands, or it never uses any. The first non option string that is
found is treated as the subcommand. Option can be given to both the subcommand and the
command itself

```bash
$ command --command-option sub --sub-command-option
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

Options can be assigned values multiple times.

```bash
$ command --long-option=first --long-option=second
$ command --long-option=first,second
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

Options should equal each other if the have the same long representation. If the long
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
	public T validateArgument (String arg);
	public boolean isRequired ();
	public String getRepresentation ();
	public int getPosition ();
}
```

## SubCommand

```java
public interface SubCommand {
	public boolean matches (String representation);
	public String getRepresentation ();
	public OptionScheme getOptionScheme ();
}
```

One implementation is provided (`SubCommandImpl`). It can be instantiated with a string
representing the subcommand, or with a string and an options scheme.

```java
SubCommand command = new SubCommandImpl ("sub");

OptionScheme scheme = new CustomOptionsScheme ();
SubCommand command = new SubCommandImpl ("sub", scheme);
```

## OptionScheme

This class is used to build up a scheme that is used to validate commandline arguments
against. It should also provide a nice `toString ()` method, to output a useful
description of the options that are allowed, and what they do.

When a subcommand is used, the paser should handle two schemes, the one used for the
global command, and the scheme for the subcommand. The structure for both is the same.

```java
public interface OptionScheme {
	public boolean hasOption (String representation);
	public boolean hasOption (Option option);

	// The string representation should be useful
	// when printed out to stdout.
	public String toString ();
}
```

One implementation is provided. This options scheme can be instantiated and the build by
adding new options to it.

```java
public class OptionSchemeImpl {
	public void add (Option option);
	public void add (Argument argument);
}
```

When an options scheme is used as a global options scheme it is not allowed to provide
arguments.

```java
public class GlobalOptionScheme {
	public void add (Option option);
}
```

## ParsedOptions

```java
public interface ParsedOptions {
	// See whether an option was provided on the command line or not.
	public boolean isPresent (String representation);
	public boolean isPresent (Option option);

	// get the value assigned to an option.
	public <T> T getValue (String representation);
	public <T> T getValue (ValueOption<T> option);

	// get arguments that did not parse as options.
	public <T> T getArg (int position);
	public <T> T getArg (Argument<T> argument);
}
```

When using the `getValue ()` method, the option (or representation of the option) has te
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
