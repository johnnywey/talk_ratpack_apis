<pre>
   ____   _       _                                          
  / __ \ (_) ___ | |__  _ __  _ __  _   ___      _____ _   _ 
 / / _` || |/ _ \| '_ \| '_ \| '_ \| | | \ \ /\ / / _ \ | | |
| | (_| || | (_) | | | | | | | | | | |_| |\ V  V /  __/ |_| |
 \ \__,_|/ |\___/|_| |_|_| |_|_| |_|\__, | \_/\_/ \___|\__, |
  \____/__/                         |___/              |___/ 

</pre>
# Building Awesome APIs Using Ratpack and Java 8
## Intro
---
Hi, I'm Johnny Wey.

* [Twitter](https://twitter.com/johnnywey)
* [Github](https://github.com/johnnywey)
* johnnywey@gmail.com

## Outline
---
* What is Ratpack?
* Project organization
	* Lazybones
* Persistance
	* GMongo
* Testing
* Test coverage using Jacoco
* Debugging
* Adding web stuff
* Authentication / Authorization
* Putting it all together
  * UI?

## Ratpack?
---
* Initially built to be a [Sinatra](http://www.sinatrarb.com) like framework for Groovy.
	* Grails was to Rails as Ratpack was to Sinatra
	* Taken over by Tim Berglund in November of 2011
	* Taken over by Luke Daley in March of 2012
* Luke completely re-wrote the framework to use Netty
* High-performance, asynchronous API with a Groovy DSL on top of Java 8
* Perfect for micro-services and full web applications alike
* Active development community
* Support for Spring applications with a Spring bridge

## Project Organization
---
So, let's start with a brand new Ratpack application. For this, we'll use `lazybones` to create a template.






