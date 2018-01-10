# Selenium Grid With Docker Container
Docker selenium project
This project designed as an experment project to run tests in parallel in a docker container in GCP.
maven failsafe plugin used to for parallel execution and For Reporting used the serenity maven plugin
```
                   	<plugin>
				<artifactId>maven-failsafe-plugin</artifactId>
				<version>2.20</version>
				<configuration>
					<forkCount>15</forkCount>
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
				<version>${serenity.version}</version>
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
```
			
<h1>Creating Docker Network</h1>
Finding out the IP address of the host is a key part in to avoid any conflict with other grids running on same machine. So in order to have a fixed set of IPs for both host and containers you can set a docker network.

```docker network create -d bridge --subnet 192.168.0.0/24 --gateway 192.168.0.1 docnet```

Now each container can connect to the host under the fixed IP 192.168.0.1

Now Each conntainer will use 'docnet' network as you can see on the docker-compose.yml file
      
<h1>Set up docker-compose.yml</h1>

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
