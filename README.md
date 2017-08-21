# dockerSerenityProject
Docker selenium project
This project designed as an experment project to run tests in parallel in a docker container in google cloud.
I have used the maven failsafe plugin to run the test along with the serenity maven plugin, here the three plugins used

<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-surefire-plugin</artifactId>
				<version>2.20</version>
				<configuration>
					<skip>true</skip>
				</configuration>

			</plugin>
			<plugin>
				<artifactId>maven-failsafe-plugin</artifactId>
				<version>2.20</version>
				<configuration>
					<forkCount>4</forkCount>
					<reuseForks>true</reuseForks>
					<argLine>-Xmx1024m -XX:MaxPermSize=256m</argLine>
					<includes>
						<include>**/*.java</include>
					</includes>
					<argLine>-Xmx512m</argLine>
				</configuration>
				<executions>
					<execution>
						<goals>
							<goal>integration-test</goal>
							<goal>verify</goal>
						</goals>
					</execution>
				</executions>
			</plugin>


			<plugin>
				<groupId>net.serenity-bdd.maven.plugins</groupId>
				<artifactId>serenity-maven-plugin</artifactId>
				<version>1.5.3</version>
				<executions>
					<execution>
						<id>serenity-reports</id>
						<phase>post-integration-test</phase>
						<goals>
							<goal>aggregate</goal>
						</goals>
					</execution>
				</executions>
			</plugin>
      
<h1>This the My docker compose setup</h1>

``` 
selenium-hub:
    image: capitalt/selenium-hub-google-cloud:v1.0
    ports:
      - "4444:4444"

chrome:
    image: capitalt/selenium-chrome-node-debug-google-cloud:v1.0
    links:
      - selenium-hub:hub
    volumes:
      - /dev/shm:/dev/shm # Mitigates the Chromium issue described at https://code.google.com/p/chromium/issues/detail?id=519952
    ports:
      - 5900
firefox:
    image: capitalt/selenium-firefox-node-debug-google-cloud:v1.0
    links:
      - selenium-hub:hub
    ports:
      - 5900
```
you can run docker compose using 
>docker-compose up --scale firefox=5 chrome=5
