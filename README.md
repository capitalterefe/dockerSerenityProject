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
      
<h1>Docker compose setup</h1>

``` 
version: '2'
networks:
 docnet:
    external: true

services:
 selenium-hub:
        image: capitalt/selenium-hub:3.6.0
        environment:
         - GRID_BROWSER_TIMEOUT=60000
         - GRID_TIMEOUT=60000
         - GRID_MAX_SESSION=50
         - GRID_MAX_INSTANCES=3
         - GRID_CLEAN_UP_CYCLE=60000
         - GRID_UNREGISTER_IF_STILL_DOWN_AFTER=180000
         - GRID_NEW_SESSION_WAIT_TIMEOUT=60000
        ports:
          - 4444
        networks:
         - docnet
 chrome:
        image: capitalt/selenium-node-chrome-debug:3.6.0
        depends_on:
         - selenium-hub
        environment:
         - HUB_PORT_4444_TCP_ADDR=selenium-hub
         - HUB_PORT_4444_TCP_PORT=4444
         - NODE_MAX_SESSION=1
        ports:
         - 5900
        networks:
         - docnet

 firefox:
        image: capitalt/selenium-node-firefox-debug:3.6.0
        depends_on:
         - selenium-hub
        environment:
         - HUB_PORT_4444_TCP_ADDR=selenium-hub
         - HUB_PORT_4444_TCP_PORT=4444
         - NODE_MAX_SESSION=1
        ports:
         - 5900
        networks:
         - docnet

```
you can run docker compose using 
>docker-compose up --scale firefox=5 chrome=5
