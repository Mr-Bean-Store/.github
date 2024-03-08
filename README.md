# Mr Bean Project Requirements
## About
Mr Bean has fans all over the world. He revolutionized the world of comedy as we know it. Especially comedy movies. 
We want to create a Mr. Bean Merch Online store. We recognized a need in the market, and we have decided that we want 
to show some appreciation to Mr. Beans’ fans for supporting him through thick and thin. 
 
## Deployment requirements:
* Deployed on AWS cloud
* You can use any free tier relational database, or SQL Server on AWS
* No serverless databases
* AWS infrastructure deployment scripted (i.e. terraform, AWS cloud formation, AWS CDK)
* Automated deployment of database and database changes
* Automated deployment of Java server
* Automated deployment of Java cli client to allow users to download it
* Build, Test and Deployment automated (CI/CD)
 
## System Requirements:
* Java based server with a stateless API (REST or other, does not have to HTTP based), does not have to be Spring.
* Java based CLI to interact with the stateless API
 
 
## Deliverables
* Deployed database
* Deployed Java Server
* Downloadable and runnable Java CLI client (not just a downloadable JAR, you need to include a tailored JDK in the download/install)
* Full unit and integration test packs (you could, for-example have an integration test pack in Postman if your service is HTTP based)
* Unit and integration tests automated (CI/CD)
* Source code in a git repo which ATC has access to
* Database design documentation (ERD)  in source control
* Infrastructure as code, source controlled
* Database change management code, source controlled
* CI/CD source controlled
* Complete readme on how to set up the working database from scratch, source controlled
* JIRA board tracking project progress
* Confluence pages for documentation
 
## Limitations:
* Java 21 or newer only
* Only libraries that make up part of Spring are allowed (i.e. https://start.spring.io/)
For any libraries you do use, you need to explain what the library does and why you decided to use it.
