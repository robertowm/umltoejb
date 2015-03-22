# Introduction #

In this page we describe how to run our tool, the transformer, and documents for a better understanding.

# Test our transformer #

We develop 3 applications that use our tool.

**1) SimpleCommandLine:**
  * [Download here](http://umltoejb.googlecode.com/files/SimpleCommandLine-1.0-SNAPSHOT-jar-with-dependencies.jar).
  * Run to transform our diagram's example ([see here](http://code.google.com/p/umltoejb/wiki/ViewAnExample)) hardcoded in this app.
  * To run it, just double click on the .jar or use the command line 'java -jar jar\_name'. All the necessary dependencies is already included in the .jar.

**2) XMICommandLine:**
  * [Download here](http://umltoejb.googlecode.com/files/XMICommandLine-1.0-SNAPSHOT-jar-with-dependencies.jar).
  * Transform XMI files in your Terminal using 'java -jar XMICommandLine.jar path\_to\_xmi'.
  * To run it, use the command line 'java -jar jar\_name'. All the necessary dependencies is already included in the .jar.

**3) XMIGui:**
  * [Download here](http://umltoejb.googlecode.com/files/XMIGui-1.0-SNAPSHOT-jar-with-dependencies.jar).
  * Using our GUI, you can load XMI files to transform them and query the result using OCL queries.
  * To run it, just double click on the .jar or use the command line 'java -jar jar\_name'. All the necessary dependencies is already included in the .jar.

## This app's source code is in out repository. If you want, it's in the folder 'apps' ##
== If you need a XMI to test, [download here](http://umltoejb.googlecode.com/files/BlogExample.xmi).

# Download our project compiled #

First, you have to download the dependencies:

**[Simple Logging Facade for Java or (SLF4J)](http://www.slf4j.org/)
  * slf4j-api-1.5.11.jar - [Download](http://umltoejb.googlecode.com/files/slf4j-nop-1.5.11.jar)
  * slf4j-nop-1.5.11.jar - [Download](http://umltoejb.googlecode.com/files/slf4j-api-1.5.11.jar)**

**[XEOS (eXtended Eye OCL Software)](http://secumltoaac.sourceforge.net/)
  * XEOS.jar - [Download](http://umltoejb.googlecode.com/files/XEOS-1.0.jar)
> Obs.: This version is customized for this application. Please use this one.**

**Transformer-base
  * transformer-base-1.0.jar - [Download](http://umltoejb.googlecode.com/files/transformer-base-1.0.jar)**

**Transformer
  * transformer-1.0.jar - [Download](http://umltoejb.googlecode.com/files/transformer-1.0.jar)**

`*``*``*`
_**If you want the source-code, download the version 1.0 [here](http://umltoejb.googlecode.com/files/umltoejb-src-1.0.jar) or [checkout it](http://code.google.com/p/umltoejb/source/checkout). We use [Netbeans](http://netbeans.org/) to develop this tool.**_
`*``*``*`

After this, you can use these files and test the transformer by yourself, in your own application :)

# Download and Compile #

If you want to compile this project:

**1) Get the source code:**
  * Download it [here](http://umltoejb.googlecode.com/files/umltoejb-src-1.0.jar)
  * Checkout it [here](http://code.google.com/p/umltoejb/source/checkout)

**2) Compile using:**
  * [Netbeans](http://netbeans.org/). We use this IDE to develop this tool so we recommend it. If you use an older version, please install the Maven's plugin.
  * [Maven](http://maven.apache.org/). This project uses Maven so just run this command in the console: mvn compile
  * javac. Use the following commands to compile (run it in the 'umltoejb' folder) (In this case, YOU MUST DOWNLOAD ALL SLF4J DEPENDENCIES and put these jar in the 'umltoejb' folder):
```
            Under construction.
```

# Javadoc #

In development but you can download what we have documented in the release 1.0 [here](http://umltoejb.googlecode.com/files/javadocs-1.0.zip).