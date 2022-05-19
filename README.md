# Excavate

Red Ducks

Search the Central Repository from the command line.

# Building

## Requirements

The following are needed to build `excavate`

```
* Java 7+
* Groovy 2+
* Maven 3+
```

## Create the jar

**SKIP TESTS - sorry..tests are not really functional at the moment**

```
$ mvn -Dmaven.test.skip=true package
```

This should create the necessary jar. (eg: `target/excavate-1.0-SNAPSHOT.jar`)

# Running

In order to run you need `java 7+`.

```
$ java -jar target/excavate-1.0-SNAPSHOT.jar

usage: excavate [option]
 -a,--artifact <artifact>        artifact id match
 -b,--basic <basic>              matches group and artifact ids (returns
                                 latest)
 -c,--classname <classname>      by classname
 -fc,--fullclass <fullclass>     by full classname
 -g,--group <group>              group id match
 -ga,--ga <groupId,artifactId>   matches group and artifact id (returns
                                 all)
 -h,--help                       Show usage information and quit
 -l,--local                      local .m2 artifacts (returns all)
 -s,--sha1 <sha1>                artifacts that match the SHA1
 -t,--tag <tag>                  artifacts with the matching tag
 -v,--version <version>          artifacts with this version

```

# Sample output

```
$ java -jar target/excavate-1.0-SNAPSHOT.jar -a commons-cli
HTTP/1.1 200 OK
Found 2 possibilities

*** CENTRAL ARTIFACTS ***

org.apache.mahout.commons:commons-cli [2.0-mahout]
commons-cli:commons-cli [20040117.000000]
```

And

```
$ java -jar target/excavate-1.0-SNAPSHOT.jar -ga commons-cli,commons-cli
HTTP/1.1 200 OK
Found 6 possibilities

*** CENTRAL ARTIFACTS ***

commons-cli:commons-cli [1.2, 1.1, 20040117.000000, 1.0, 1.0-beta-2, 1.0-beta-1]
```
