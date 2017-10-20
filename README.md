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

### Describe how testable your solution is.

Due to high decoupling our system is rather easily testable. We were able to manually test each service on its own even though the service before or after it were not operational at the time. If we were to create a test suite for the system then that would be easy in the perspective of high decoupling and as each service only handles a small part of the overall system. The down side of testing out System would be that we created the Loan Broker in many different projects. This would make it so that you would not be able to have one central tet suite but would have to have test suites for each project. That would be a major downside for testing as you would trouble doing a full System Integration test. But if time allowed we would have merged all of our seperate Loan Broker components into one project and also made certain parts of the code more reusable. All in all each component of the project is highly testable on its own due to low coupling but the overall System testing would be hard to accomplish with our current structure.

### Provide a description of the loan broker web service you created.

The Loan Broker service we created is split up into many independent projects. Each project is able to run on its own and is completely independent from any other service within the Loan Broker Service. Each service is started up on its own and communicates with each other using RabbitMq. The project is developed using Java. The below diagram shows exactly the flow that our web service follows.

### Describe potential bottlenecks in your solution and possible enhancements to improve performance.

Our System contains a couple bottlenecks. The first one is that our loanbroker system is currently only running locally. This would make it so that you would not be able to access our service unless you run it locally on your machine. That was a bottleneck met due to time contraints when creating the project.

The major bottleneck that makes our system not as performant as we would have liked it to be as well as not making it usable at a large scale, is that we do not have a way of returning the response to the original requestor. This is because we do not have a way of relating who sent the request and who needs to receive the response. The only way we have tries to handle this was by splitting up the reponses by SSN in the Aggregator and creating an object for each request message but then we are unable to find exactly to which end user to respond to.

We could improve performance by aggregating all of our services which fall under the Loan Broker system and put them into one project. This would make the project have less redundant code as well as have more controlled variables. for example we would have a class that would create a sending and replying connection, instead of writing it out every time. That would also make the code more readable. But due to time constaints and not being aware of the benefits of having all the services in one project. 

Otherwise our project is highly decoupled and we have strong type checking as we have used a language that allows for it. We believe our project does have flaws that are caused by being new to messaging as well as being rusty with Java but overall it works well for a singular persons use. Changing the design of the flow could also help make it better. Such as having a gateway whcih takes the request and also returns it to the user. If a gateway to the Loan Broker system was created then we could also use RPC to manage which user gets which request back in a centralized manner. As with any program within a time limit we can say that it is usable but it could still be worked on, for a long long time to reach a more stable state.
