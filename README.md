# JWBF - JavaWikiBotFramework.
[![Build Status](https://travis-ci.org/eldur/jwbf.svg)](https://travis-ci.org/eldur/jwbf)
[![Coverage Status](https://img.shields.io/coveralls/eldur/jwbf.svg)](https://coveralls.io/r/eldur/jwbf)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/net.sourceforge/jwbf/badge.svg)](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22net.sourceforge%22%20AND%20a%3A%22jwbf%22)


The Java Wiki Bot Framework is a library to retrieve data from and maintain
[MediaWiki-based wikis](http://www.mediawiki.org) such as Wikipedia. It has
packages that handle basic tasks (login, cookies, encoding, token management)
so that you can write your wiki bot without being a MediaWiki API expert. JWBF
requires JRE 1.7.

## Code sample
```java
  /**
   * Sample bot that retrieves and edits an article.
   */
  public static void main(String[] args) {
    MediaWikiBot wikiBot = new MediaWikiBot("https://en.wikipedia.org/w/");
    Article article = wikiBot.getArticle("42");
    System.out.println(article.getText().substring(5, 42));
    // HITCHHIKER'S GUIDE TO THE GALAXY FANS
    applyChangesTo(article);
    wikiBot.login("user", "***");
    article.save();
  }

  static void applyChangesTo(Article article) {
    // edits the article...
  }
```
## Table of contents
<<<<<<< HEAD
**[Code sample](#code-sample)** | 
**[Developer resources](#developer-resources)** | 
**[Getting started](#getting-started)** | 
**[Dependencies](#dependencies)** | 
**[Working with Wikimedia](#working-with-wikimedia)** | 
=======
**[Code sample](#code-sample)** |
**[Developer resources](#developer-resources)** |
**[Getting started](#getting-started)** |
**[Dependencies](#dependencies)** |
**[Working with Wikimedia](#working-with-wikimedia)** |
>>>>>>> aa1d52bd8f56c47a27ae93c9026eca20636325ce
**[More resources](#see-also)**


## Developer resources
* Repositories
  * RELEASES: [Maven Central](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22net.sourceforge%22%20AND%20a%3A%22jwbf%22)
  * SNAPSHOTS: [oss.sonatype.org](https://oss.sonatype.org/content/groups/public/net/sourceforge/jwbf/)
<<<<<<< HEAD
* JavaDocs: [@sf.net](http://jwbf.sourceforge.net/doc/) 
=======
* JavaDocs: [@sf.net](http://jwbf.sourceforge.net/doc/)
>>>>>>> aa1d52bd8f56c47a27ae93c9026eca20636325ce
(Version specific docs are located [at Maven Central](http://search.maven.org/#search|gav|1|g%3A%22net.sourceforge%22%20AND%20a%3A%22jwbf%22))
* Home Page: [@sf.net](http://jwbf.sourceforge.net/)


## Getting started
<<<<<<< HEAD
JWBF uses Maven to automatically resolve dependencies. To use Maven to start 
a new project, follow the 
[Maven in Five Minutes](http://maven.apache.org/guides/getting-started/maven-in-five-minutes.html)
tutorial. For a more detailed introduction, see Maven's 
[Getting Started](http://maven.apache.org/guides/getting-started/index.html) 
guide.

The Java Wiki Bot Framework is available from two repositories. For a more 
stable version of JWBF, use the most recent version in the 
[RELEASES repository](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22net.sourceforge%22%20AND%20a%3A%22jwbf%22) 
at Maven Central. For the development version, which will be most up-to-date, 
use the most recent version in the 
[SNAPSHOTS repository](https://oss.sonatype.org/content/groups/public/net/sourceforge/jwbf/) 
=======
JWBF uses Maven to automatically resolve dependencies. To use Maven to start
a new project, follow the
[Maven in Five Minutes](http://maven.apache.org/guides/getting-started/maven-in-five-minutes.html)
tutorial. For a more detailed introduction, see Maven's
[Getting Started](http://maven.apache.org/guides/getting-started/index.html)
guide.

The Java Wiki Bot Framework is available from two repositories. For a more
stable version of JWBF, use the most recent version in the
[RELEASES repository](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22net.sourceforge%22%20AND%20a%3A%22jwbf%22)
at Maven Central. For the development version, which will be most up-to-date,
use the most recent version in the
[SNAPSHOTS repository](https://oss.sonatype.org/content/groups/public/net/sourceforge/jwbf/)
>>>>>>> aa1d52bd8f56c47a27ae93c9026eca20636325ce
at oss.sonatype.org.


### Dependencies
<<<<<<< HEAD
Once you have started your project in Maven and have a `pom.xml` file for your 
bot's project, add the appropriate JWBF dependency to the `<dependencies>` 
section. When you build your project, JWBF and its own dependencies will be 
=======
Once you have started your project in Maven and have a `pom.xml` file for your
bot's project, add the appropriate JWBF dependency to the `<dependencies>`
section. When you build your project, JWBF and its own dependencies will be
>>>>>>> aa1d52bd8f56c47a27ae93c9026eca20636325ce
downloaded automatically.

#### From [RELEASES](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22net.sourceforge%22%20AND%20a%3A%22jwbf%22):

```xml
<dependency>
<<<<<<< HEAD
    <groupId>net.sourceforge</groupId>
    <artifactId>jwbf</artifactId>
    <version>2.0.0</version>
</dependency>
```

If you want to use a different release of JWBF, find your desired version in 
[RELEASES](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22net.sourceforge%22%20AND%20a%3A%22jwbf%22) 
=======
  <groupId>net.sourceforge</groupId>
  <artifactId>jwbf</artifactId>
  <version>2.0.0</version>
</dependency>
```

If you want to use a different release of JWBF, find your desired version in
[RELEASES](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22net.sourceforge%22%20AND%20a%3A%22jwbf%22)
>>>>>>> aa1d52bd8f56c47a27ae93c9026eca20636325ce
and change `<version>` to its listed title.


#### From [SNAPSHOTS](https://oss.sonatype.org/content/groups/public/net/sourceforge/jwbf/):

<<<<<<< HEAD
Add this to your `<repositories>` section: 

```xml
    <repository>
        <id>sonatype-nexus-snapshots</id>
        <name>Sonatype Nexus Snapshots</name>
        <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
```

Add this to your `<dependencies>` section: 
=======
Add this to your `<repositories>` section:

```xml
<repository>
  <id>sonatype-nexus-snapshots</id>
  <name>Sonatype Nexus Snapshots</name>
  <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
  <snapshots>
    <enabled>true</enabled>
  </snapshots>
</repository>
```

Add this to your `<dependencies>` section:
>>>>>>> aa1d52bd8f56c47a27ae93c9026eca20636325ce

```xml
<dependency>
  <groupId>net.sourceforge</groupId>
  <artifactId>jwbf</artifactId>
  <version>3.0.0-SNAPSHOT</version>
</dependency>
```

<<<<<<< HEAD
If you want to use a different snapshot of JWBF, find your desired version in 
[SNAPSHOTS](https://oss.sonatype.org/content/groups/public/net/sourceforge/jwbf/) 
=======
If you want to use a different snapshot of JWBF, find your desired version in
[SNAPSHOTS](https://oss.sonatype.org/content/groups/public/net/sourceforge/jwbf/)
>>>>>>> aa1d52bd8f56c47a27ae93c9026eca20636325ce
and change `<version>` to its listed title.


### Examples

<<<<<<< HEAD
Here is [one example](#code-sample) of using JWBF to write a bot that can 
=======
Here is [one example](#code-sample) of using JWBF to write a bot that can
>>>>>>> aa1d52bd8f56c47a27ae93c9026eca20636325ce
retrieve and edit an article on a desired wiki.

More Java examples (e.g. for queries) can be found at
 [unit-](https://github.com/eldur/jwbf/tree/master/src/test/java/net/sourceforge/jwbf) and
 [integration-test packages](https://github.com/eldur/jwbf/tree/master/src/integration-test/java/net/sourceforge/jwbf).


## Working with Wikimedia
If you are working with Wikimedia sites, set an informative User-Agent header,
 because [all Wikimedia sites require a HTTP User-Agent header for all requests](http://meta.wikimedia.org/wiki/User-Agent_policy).

```java
<<<<<<< HEAD
    //Creating a new MediaWikiBot with an informative user agent
    HttpActionClient client = HttpActionClient.builder() //
        .withUrl("https://en.wikipedia.org/w/") //
        .withUserAgent("User name/your email/jwbf/...") //
        .withRequestsPerUnit(10, TimeUnit.MINUTES) //
        .build();
    MediaWikiBot wikiBot = new MediaWikiBot(client);
=======
//Creating a new MediaWikiBot with an informative user agent
HttpActionClient client = HttpActionClient.builder() //
  .withUrl("https://en.wikipedia.org/w/") //
  .withUserAgent("BotName", "1.0", "your Email or Maintainer UserName") //
  .withRequestsPerUnit(10, TimeUnit.MINUTES) //
  .build();
MediaWikiBot wikiBot = new MediaWikiBot(client);
>>>>>>> aa1d52bd8f56c47a27ae93c9026eca20636325ce
```


## See also
* [en.Wikipedia Creating a Bot](https://en.wikipedia.org/wiki/Wikipedia:Creating_a_bot#Java)
* [fr.Wikipedia Créer un bot](https://fr.wikipedia.org/wiki/Wikip%C3%A9dia:Cr%C3%A9er_un_bot#Java)
* [de.Wikipedia Bots](https://de.wikipedia.org/wiki/Wikipedia:Bots#Ressourcen)
* [Mediawiki API Documentation](https://www.mediawiki.org/wiki/API)
* [Evaluation of JWBF](https://www.mediawiki.org/wiki/API:Client_code/Evaluations/Java_Wiki_Bot_Framework_(JWBF))
* Wikidata Integration
 * [Fast forwarded test branch](https://github.com/eldur/jwbf/tree/wikidata)
 * [WikiData-Toolkit](https://github.com/Wikidata/Wikidata-Toolkit/issues/11)
