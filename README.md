# Mapping Users

This REST API consumes the API at https://bpdts-test-app.herokuapp.com

It returns users who are listed as either living in London, or whose current coordinates are within 50 miles of London

To get all users who are living in London  `https://bpdts-test-app.herokuapp.com/city/London/users`

For the users that are within 50 miles of London all users are requested through `https://bpdts-test-app.herokuapp.com/users` then using Haversine formula a subset of users is found where their latitude and longitude are within 50 miles of London.

Both sets of users are then combined for the given response.

## Software

The API is created using Grails which uses the Groovy programming language.

App and tests can be run from an IDE that supports Grails such as Intellij 

Alternatively you can install and run through the command line. 

Grails install

`$ curl -s https://get.sdkman.io | bash`

Then install the latest stable Grails:

`$ sdk install grails`

Confirm Grails install

`$ grails -version`

To run app:

`./gradlew bootrun`

To run tests:

`./gradlew test`