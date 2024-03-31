# Courier Tracker API

[![Java CI with Maven](https://github.com/ErgunKargun/CourierTrackerApi/actions/workflows/maven.yml/badge.svg)](https://github.com/ErgunKargun/CourierTrackerApi/actions/workflows/maven.yml)

## What is this?

A restful web api that mainly takes streaming geolocations of couriers *(time, courier, lat, lng)* as input and tracks its entrance into stores' circumference within specified radius of meters and duration interval.

> *Observer, Builder & Repository design patterns* are implemented. Also the circumreference and interval parameters can be managed by configuration.

## Guide

* After successfully build the application on your local;
    * Firstly send http post request to [**/auth/register**](http://localhost:8080/auth/register)
    * Then get your token over [**/auth/sign-in**](http://localhost:8080/auth/sign-in) endpoint.
> You can skip register part by using *h2 in-memory db username&password* defined at config file and directly use **/sign-in** endpoint to get your token. *Because when app start, an admin user created by this informations*.  
* You can reach API documentation either on localhost at **/swagger** endpoint or on [**swaggerhub.com**](https://app.swaggerhub.com/apis-docs/ErgunKargun/CourierTrackerApi/v1).
* To load api endpoints automatically into your postman you can reach openapi specification definition yaml file either
    * On localhost at **/openapi** endpoint to browse and **/openapi.yaml** endpoint to download or
    * Over [**swaggerhub.com**](https://app.swaggerhub.com/apis/ErgunKargun/CourierTrackerApi/v1).

> Tokens expiration duration is also *can managed by configuration* and it is **1 hour as default**.  
> You should create your own ***secret.yaml*** configuration file after clone the project into your *local machine*.  
> You can use
   > **/log/courier** endpoint to log courier geolocation and use
   > **/entrances** endpoint to browse the entrances to the stores.  

## Prerequisites

Make sure you have installed the following softwares on your machine.

* Java *17*
* Apache Maven *3.9.x*
* An IDE *(Intellij Idea or STS suggested)*
* An API Tool *(Postman suggested)*
* A Browser

## Build 

Clone the source codes from Github.

```bash
git clone https://github.com/ErgunKargun/CourierTrackerApi
```

Open a terminal, and switch to the root folder of the project, and run the following command to build the whole project.

```bash
mvn clean install
```

Run the application.

```bash
mvn spring-boot:run
```


## Contribution

Any suggestions are welcome, filing an issue or submitting a pull request is also highly recommended.  
