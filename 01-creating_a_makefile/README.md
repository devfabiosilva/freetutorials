# Creating a Makefile for beginners

This tutorial is easy and very clear if you are a beginner in programming language or building your project

_Makefile_ is a text file containing _target_, _dependencies_, _commands_ and _variables_ used in _make_ to build, automate, organize software/program.


## Simple structure

```sh

VARIABLE="ANY VALUE"

target: dependency
    command

```

## Example

```sh
VARIABLE=program

program: main.c
    gcc -o $(VARIABLE) main.c

```

You can view these tutorial videos (in Portuguese) in my youtube channel [O curioso](https://www.youtube.com/@umpoucosobreconhecimentos)

Thank you :)

