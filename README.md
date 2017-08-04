# MitL-problems 

This library describes some general problem classes, for instance:
- BitVector problems (i.e. the Checkerboard Probem, the Royal Road problem),
- TSP,
- TTP,
- SAT

//TODO

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

To use this library you need Java 8 JDK, [Scala](https://www.scala-lang.org/download/) and [sbt](http://www.scala-sbt.org/download.html/) installed.

If you want to use an IDE rather than running everything from the command line, the below mentioned IDEs have been checked to work:
- [Scala IDE for eclipse](http://scala-ide.org/download/sdk.html)
- [IntelliJ Idea](https://www.jetbrains.com/idea/) with the Scala plugin [downloaded and enabled](https://www.jetbrains.com/help/idea/enabling-and-disabling-plugins.html) 

(Where IntelliJ is probably slightly simpler to set up.)

### Installing

You can get a copy this repository by downloading the zip folder, or by typing:

```
git clone https://github.com/drdrwhite/mitlproblems.git
```

in the command line.

Once that's done, you can either work directly from the terminal, or using and an IDE. These instructions present how to run the library using IntelliJ and Eclipse.

### IntelliJ

Before , make sure you have the Scala plugin [downloaded and enabled](https://www.jetbrains.com/help/idea/enabling-and-disabling-plugins.html).

Select **Create New Project** on the Welcome screen, or select **File | New | Project**. 

Choose **Scala** -> **SBT** as the project type and click **Next**.

![alt text][img1.png]
![alt text][https://github.com/drdrwhite/mitlproblems/img/img1.png]

Specify project and module location settings.
Depending on your operating system, a **Use auto-import** opion might apper. If it does, check the box to allow auto-import. Otherwise, you'll have to refresh the IDE every time you edit sbt build - related files.

//TODO image

You can alternatively import an existing sbt project, if you already ran sbt on the source files.

### Scala IDE for Eclipse

Once you download [Scala IDE for eclipse](http://scala-ide.org/download/sdk.html) and have sbt installed, you should be able to run ```sbt eclipse``` in the command line, in the project's root directory (.../mitlproblems). The output of the command should look similar to this:

```
[info] Loading project definition from /Users/username/MitL/mitlproblems/project
[info] Set current project to MitLware-problems (in build file:/Users/username/MitL/mitlproblems/)
[info] About to create Eclipse project files for your project(s).
[info] Successfully created Eclipse project files for project(s):
[info] MitLware-problems
```

The command creates .classpath and .project files, which eclipse recognises as its own project files. Now you can fire up eclipse, go to **File** -> **Open Projects from File System...** and enter the path to the project's root directory (/Users/username/MitL/mitlproblems/). Eclipse should see the files created by sbt. Click finish and you're done! 

If you are unable to run ```sbt eclipse```, see <https://github.com/typesafehub/sbteclipse>.

## Running the tests

Tests can be found in the [mitlproblems/src/test/java/org/mitlware/problem](https://github.com/drdrwhite/mitlproblems/tree/master/src/test/java/org/mitlware/problem) directory.

You can run all the tests at once by running ```sbt`` in the terminal, and then typing ```test```, or a particular test with ```sbt``` > ```testOnly *NameOfTestClass```.

## Deployment

//TODO Add additional notes about how to deploy this on a live system

## Built With

* [sbt](http://www.scala-sbt.org/) - Scala Build Tool
* [Maven](https://maven.apache.org/) - Dependency Management

//TODO

## Contributing

Please read [CONTRIBUTING.md](https://github.com/drdrwhite/mitlproblems/CONTRIBUTING.md) for details on our code of conduct, and the process for submitting pull requests to us.

//TODO add text to CONTRIBUTING.md

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/drdrwhite/mitlproblems/tags). 

## Authors

* **Jerry Swan** - *Initial work* - []()
* **David White** - - []()

See <http://www.mitlware.org/> for more infromation on the project.

Related repositories:
1. <https://github.com/MitLware>

See also the list of [collaborators](https://github.com/drdrwhite/mitlproblems/COLLABORATORS) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

//TODO:
* Hat tip to anyone who's code was used
* Inspiration
* etc

[img1]: [[linkToRepo]/img/img1.] "Choose project type"
[linkToRepo]: https://github.com/drdrwhite/mitlproblems
