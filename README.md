# Search Service

### Requirements
* Install Docker and Docker compose

* Install Java 11 or 12

* Configure your gradle credentials by modifying the gradle.properties file in `~/.gradle/gradle.properties` with the 
nexus credentials, if you don't know the credentials ask you PM or a friend.
    
```
mahiNexusUsername=USER
mahiNexusPassword=PASSWORD
sharedMavenUrl=https://nexus.mahisoft.com/repository/maven-public/
```

#### IMPORTANT! create the file `init.gradle` in ~/.gradle/

```
initscript {
    gradle.settingsEvaluated { settings ->
        settings.pluginManagement {
            repositories {
                maven {
                    url 'https://nexus.mahisoft.com/repository/maven-public/'
                    credentials {
                        Properties properties = new Properties()
                        properties.load(new File(getGradleUserHomeDir(), 'gradle.properties').newDataInputStream())
                        username properties.mahiNexusUsername
                        password properties.mahiNexusPassword
                    }
                }
                gradlePluginPortal()
            }
        }
    }
}
```

## Run project for development locally

###### Note: If you only want to test the service locally skip this and follow the steps on the next section.
* Create the database:

```
docker-compose up db
```

* Run the micro service using the following command:

```
SPRING_PROFILES_ACTIVE=local,doc ./gradlew clean compileKotlin ms-search-service-ng:bootRun
```

## Run project with Docker Compose

* To test locally you can run the following commands:

```
./gradlew clean build
docker-compose up
```

* This will create all the necessary services you will need to test ms-search.

## Test with Swagger or Postman

* Test the endpoints using swagger (http://localhost:9000/api/swagger-ui.html) or test in postman importing the collection 
in the directory called `postman/`
