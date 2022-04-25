<h1 align-"center">Team Java Beans</h1>
<h2>API:Category-Partition testing method</h2>

<p>The purpose of this project is to implement a functional API that takes the name of an input file as its parameter and return the name of an output file as its only result. The input file will specify the functionality of software under test using the category-partition method. The API will apply the category-partition method to the formatted contents of the input file, generating test cases based on the specification, and writing the results for them to an output file in the Cucumber "given-when-then" notation. The test cases will specify the selection of equivalence classes for the parameter and the expected test results when the test has been running.</p>

<h3>Client</h3>
Mr.Jim Wood

### Link

[A Link to the repository](https://github.com/MarshMaster11/Team_Java_Beans)

# How To Install Our Project
1. Clone the master branch of this repository to a location of your choosing
    1. For HTTPS clone
    ```
    git clone https://github.com/MarshMaster11/Team_Java_Beans.git
    ```
    2. For SSH
    ```
    git clone git@github.com:MarshMaster11/Team_Java_Beans.git 
    ```

### Requires
# What To Install
1. [Simple JSON JAR File](http://www.java2s.com/Code/Jar/j/Downloadjsonsimple111jar.htm)
    1. Once downloaded, extract the JAR file to a location of your choosing.
    2. Once extracted, right click the project and click on build path -> configure build path.
    3. Once the window opens, click on libraries -> Add external JARs -> select the extracted JAR file
2. Spring Tool Suite 4
    1. Launch Eclipse and go to the help tab at the top
    2. Click on Eclipse Marketplace and search for Spring Tools 4 (aka Spring Tool Suite 4)
    3. Install Spring Tools 4
# How To Use Testing Packages
1. For the selenium tests, you can follow either of the guides below to install the chrome driver and the selenium JAR files 
    1. [First link](https://www.guru99.com/installing-selenium-webdriver.html)
    2. [Second link](https://www.browserstack.com/guide/how-to-setup-selenium-in-eclipse)
2. You may need to install JUnit 5 in order to run the JUnit tests. Link to this is down below. **NOTE:** The tests made inside of the com.example.uploadingfiles and com.example.uploadingfiles.storage packages were not made by us and were imported using Spring.
    1. [Link to the Spring Guide](https://spring.io/guides/gs/uploading-files/)
    2. [Link to JUnit 5 download](https://junit.org/junit5/)
### To Run
right click on gs-uploading-files-initial -> run as -> Spring Boot App
Once the service is started, type localhost:8080 into your browser to pull up the page.
### Authors
- **Christopher Wilder** (cwilder@ggc.edu)
  - Testing & Documentation Lead
- **David Marshall** (dmarshall5@ggc.edu)
  - Lead Programmer & Client Liaison 🦀
- **Ephrem Engida** (eengida@ggc.edu)
  - Team Manager & UI/UX Design 🔥
- **Matteo Kitic** (mkitic@ggc.edu)
  - Data Modeler 🐯
 ### Technologies
  Java Spring
 ### Communication Platforms
 * MS Teams
 * Discord
 ### Project Managment
 [Jira link](https://jira.ggc.edu/projects/TJB/issues/TJB-4?filter=allopenissues)
 
 ### License
 
 Copyright (c) 2012-2022 Team Java Beans

Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
"Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
