aReports
============

The code in this repository has two targets:

1) A Small Java Desktop Program for testing and generating the configuration files for Archivesspace Reports.

2) A JAR file to be included in archivesspace/common/lib to support running reports in ArchivesSpace

## Desktop 

Since the program works by connecting directly to the Archivesspace MySQL backend database, MySQL access is need from the computer it runs on.  

####Using

1. Download and Unpack aReports.zip in local directory.
2. Change into the aReports directory.
3. Using a MySQL client, connect to the Archivesspace database, and run the sql/sp.sql script. This scripts creates all the needed stored functions used by the reports
4. From the command line, type "java -jar aReports.jar". This brings up the main program window.
5. Enter the MYSQL connection information for the Archivesspace MySQL database.
6. Press the Connect button to open a connection to the database.
7. Select a report from the drop down list and press the Preview button to run it.

The program also has the ability to generate the needed configuration files and copy them to the Archivesspace server using ssh.  See instructions below to do this.

1. Enter the sFTP information, including the remote directory where Archivesspace is installed.
2. Press the Push button to generate the configuration file, and copy all compiled reports/subreports to the remote Archivesspace reports directory. 

#####Searching
As of version 0.4.0 there is now a simple dialog to allow searching of the connected Archivesspace database using SQL queries. The results of these queries can then be sent to a Jasper Report, or saved in various formats (*.tsv, *.html, *.xls).  By default he queries from the Jasper reports are made available via a drop-down, but additional queries can be entered directly, or loaded from an xml file. See https://github.com/archivesspace/aReports/blob/master/sql/sampleQueries.xml for the format of this file. 

To use search functionality:

1. Connect to the desired Archivesspace database from the main program window.
2. Press the "Search" button to bring up the search window.
3. Enter or sql query, or use the drop down menu to select one from one of the Jasper reports or one loaded from xml file. Please note that many of the queries from the Jasper reports need to be modified inorder to run without errors.
4. Press the "Search" button to retrive the results. The results can then be saved in the desired format using the appropriate button. 


####Notes

1. On Windows 7/8 there is a bug in Java which prevents program information from being stored. Follow the instructions at http://stackoverflow.com/questions/16428098/groovy-shell-warning-could-not-open-create-prefs-root-node
2. Given the reports have a dependency on stored functions, only the MySQL backend is supported at this time.
3. By default, the JDBC connection information for a tracer database is provided for testing purposes.
4. For MySQL you may need to set the `thread_stack` value to 256k in `my.cnf` (requires restart).
5. On Ubuntu (Debian) a non-headless jre is required, as well as [additional fonts](http://www.perfectabstractions.com/blog/how-to-install-windows-fonts-in-java-on-linux).

## ASpace JAR

Run this ant task:

    ant build-aspace-jar

Copy the file to archivesspace and check it in:

    cp dist/areports-aspace.jar {ASpace Project Directory}/common/lib

---
