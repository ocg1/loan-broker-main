# System Integration Loan Broker Project
Authors: **Lukasz Koziarski** & **William Bech**

## Links related to this project:
* [SI Plan 📅](https://datsoftlyngby.github.io/soft2017fall/SI_plan.html)
* [Project description 📋](https://github.com/datsoftlyngby/soft2017fall-system-integration-teaching-material/blob/master/assignments/LoanBrokerProject.pdf)
* [RabbitMQ management → http://datdb.cphbusiness.dk:15672/ 🐰](http://datdb.cphbusiness.dk:15672/)
  - username: **student**
  - password: **cph**
  - server name: **datdb.cphbusiness.dk**
  - port: **5672**

## Loan Broker components
* [RuleBase - repo](https://github.com/loan-broker-SI/rule-base)
* [Our XML bank - repo](https://github.com/loan-broker-SI/bum-bank-xml)
* [Our JSON bank - code](https://github.com/loan-broker-SI/loan-broker-main/blob/master/src/main/java/banks/GringottsBankJSON.java)
* [Get Credit Score enricher - code](https://github.com/loan-broker-SI/loan-broker-main/blob/master/src/main/java/getCreditScore/GetCreditScore.java)
* [Get Banks enricher - code](https://github.com/loan-broker-SI/loan-broker-main/blob/master/src/main/java/getBanks/GetBanks.java)
* [Recipient List - code](https://github.com/loan-broker-SI/loan-broker-main/blob/master/src/main/java/recipList/RecipientList.java)
* Translators
  - [Our JSON - code](https://github.com/loan-broker-SI/loan-broker-main/blob/master/src/main/java/translators/Gringotts_JSON_Translator.java)
  - [Our XML - code](https://github.com/loan-broker-SI/loan-broker-main/blob/master/src/main/java/translators/Bumbank_XML_Translator.java)
  - [School JSON - code](https://github.com/loan-broker-SI/loan-broker-main/blob/master/src/main/java/translators/Cphbusiness_JSON_Translator.java)
  - [School XML - code](https://github.com/loan-broker-SI/loan-broker-main/blob/master/src/main/java/translators/Cphbusiness_XML_Translator.java)
* [Normalizer](https://github.com/loan-broker-SI/loan-broker-main/blob/master/src/main/java/normalizer/Normalizer.java)
* Aggregator - @todo add link

## Recap notes from the Project description:
### Create:
- 2 banks: 1 SOAP, 1 RabbitMQ
- Rule base: SOAP web service
- Loan Broker: SOAP, with messaging components, accessed through simple website

### Requirements:
- Make screen dumps of a process flow scenario (i.e. your running code).
- Make a design class diagram and a sequence diagram that document your solution (including comments). Other types of visual documentation are also welcome ☺
- Describe potential bottlenecks in your solution and possible enhancements to improve performance.
- Describe how testable your solution is.
- Provide a description of the loan broker web service you create.

### Learning objectives:
Design integration solution with messaging and SOAP web services. 📩 📬

### Provide a description of the loan broker web service you created.

The Loan Broker service we created is split up into many independent projects. Each project is able to run on its own and is completely independent from any other service within the Loan Broker Service. Each service is started up on its own and communicates with each other using RabbitMq. The project is developed using Java. The below diagram shows exactly the flow that our web service follows.

We also created a web site for the System but did not have time to hook it up with the Loan Broker system. This was due to challenges we did not expect.

![Alt text](images/diagram.png?raw=true "Diagram")

Here is a sequence diagram that illustrates the data flow within the application.

![Alt text](images/seq.png?raw=true "Sequence Diagram")

### Describe how testable your solution is.

Due to high decoupling our system is rather easily testable. We were able to manually test each service on its own even though the service before or after it were not operational at the time. If we were to create a test suite for the system then that would be easy in the perspective of high decoupling and as each service only handles a small part of the overall system. When we initially created our System the Loan Broker was split up into many different projects which we later noticed was a bad design patter. This was due to the very low testability and reusability of common components used throughout the Loan Broker system. After having merged all of the seperate projects into one we figures that if we were to create tests we would then be able to create one main test suite that could test all of the components within the system with ease. Having had past experience with testing seperate components we knew the complications we could face and are happy of having aggregated the projects into one. Even though the projects are aggregated into one they are all able to run fully independent of each other. This was a "quick-fix" as we did not have the time we wanted to hook-up our front-end and Node.js server to the system.

### Describe potential bottlenecks in your solution and possible enhancements to improve performance.

Our System contains a couple bottlenecks. The first one is that our loanbroker system is currently only running locally. This would make it so that you would not be able to access our service unless you run it locally on your machine. That was a bottleneck met due to time contraints when creating the project.

The major bottleneck that makes our system not as performant as we would have liked it to be as well as not making it usable at a large scale, is that we do not have a way of returning the response to the original requestor. This is because we do not have a way of relating who sent the request and who needs to receive the response. The only way we have tried to handle this was by splitting up the reponses by SSN in the Aggregator and creating an object for each request message but then we are unable to find exactly which end user to respond to.

Otherwise our project is highly decoupled and we have strong type checking as we have used a language that allows for it. We believe our project does have flaws that are caused by being new to messaging as well as being rusty with Java but overall it works well for a singular persons use. Changing the design of the flow could also help make it better. Such as having a gateway whcih takes the request and also returns it to the user. If a gateway to the Loan Broker system was created then we could also use RPC to manage which user gets which request back in a centralized manner. As with any program within a time limit we can say that it is usable but it could still be worked on, for a long long time to reach a more stable state.
