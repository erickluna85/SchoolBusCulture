School Bus Culture
==================

An implementation of a Cultureal Genetic Algorithm to solve the School Bus problem.

Requirements
------------

  * [JArgs](https://github.com/purcell/jargs) (or [my fork](https://github.com/white-gecko/jargs))
    * [jargs-2.0-SNAPSHOT.jar](https://github.com/downloads/white-gecko/jargs/jargs-2.0-SNAPSHOT.jar) (my build)
    * and the official [download section](http://sourceforge.net/projects/jargs/files/jargs/)
  * [Jenes](http://jenes.intelligentia.it/) (we are using 2.1.0 from the [download section](http://sourceforge.net/projects/jenes/files/Jenes/))
  * [JFreeChart](www.jfree.org/jfreechart/) (we are using 1.0.14 from the [download section](http://sourceforge.net/projects/jfreechart/files/), it includes JCommon which you will also need in the Build Path)

Run
---

Running the generated JAR with the `-h` option will show you the possibilities to run the programm:

    $ java -jar SchoolBus.jar -h

You always have to specify an Area Map file with the `-a` option.

    $ java -jar SchoolBus.jar -a ./TestSets/2-secondSchool.txt

The programm will have some grafical output for some statistics and a map and will show the solution in the terminal window. So keep an eye on both outputs.

To start the algorithm with our cultural genetic algorithm variation, the guru algorithm you have to specify the `-g` option.

    $ java -jar SchoolBus.jar -g -a ./TestSets/2-secondSchool.txt
