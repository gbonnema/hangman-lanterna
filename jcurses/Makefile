CURSES=-lncurses
JAVAHOME=/usr
JAVAC=$(JAVAHOME)/bin/javac
JAR=$(JAVAHOME)/bin/jar
JAVAH=$(JAVAHOME)/bin/javah
JAVA=$(JAVAHOME)/bin/java
JAVADOC=$(JAVAHOME)/bin/javadoc
GCC=gcc
GCCFLAGS=-Wall -shared -fPIC -I$(JAVAHOME)/include -I$(JAVAHOME)/include/xkbcommon 
CLASSPATH=./classes

default: native
all: jar native docs
java: ;$(JAVAC) -classpath $(CLASSPATH) -d  ./classes `find ./src/jcurses -name *.java`
docs: ;$(JAVADOC) -classpath $(CLASSPATH) -overview ./overview.html -sourcepath ./src -d ./doc jcurses.dialogs jcurses.event jcurses.system jcurses.util jcurses.widgets
native: java include
include: java;$(JAVAH) -classpath $(CLASSPATH) -d ./src/native/include jcurses.system.Toolkit
clean: ;rm -rf ./classes/jcurses ./lib/libjcurses.so ./lib/jcurses.jar ./src/native/include/*.h
native:java include;$(GCC) $(GCCFLAGS) -o lib/libjcurses.so $(CURSES) src/native/Toolkit.c   
jar: java;cd classes/ && $(JAR) -cvf ../lib/jcurses.jar *
test: ;$(JAVA) -classpath ./lib/jcurses.jar -Djcurses.protocol.filename=jcurses.log jcurses.tests.Test
