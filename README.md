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
- Loan broker contacts banks with distribution strategy (Rule based recipient list based on credit score).
- Make up banking rules (Bum bank, Boring bank, Iron bank, Gringotts bank)
- Credit score (0 - 800)inve
- Make design class diagram and sequence diagram documenting out solution
- Describe potential bottlenecks and ways to increase performance
- Describe testability
- Provide description of loan broker web service
- Use RabbitMQ

### Questions:
- ~~Do we need to deploy it?~~
- Where can we find banks that are already created? → *RabbitMQ management*
- Where is the credit bureau? → *pdf file link*
- What kind of rules should we create? (amount of money on the account??)
- Two validations for SSN part of the description → *meaning it's all random*
- ~~Can we use JS?~~
- Where should we start? → *from a begining* 
- What should we focus on? →

### Learning objectives:
Design integration solution with messaging and SOAP web services. 📩 📬