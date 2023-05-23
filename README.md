# text-analyzer

1 - Build the project by running mvn install

2 - Run the server: 

  mvn org.codehaus.mojo:exec-maven-plugin:exec -Dexec.executable=java -Dexec.args="-cp %classpath io.vertx.core.Launcher run test.project1.Server"
  
3 - Test it passing some words:

    curl http://localhost:8080/analyze -d '{"text":"dog"}'
    
    curl http://localhost:8080/analyze -d '{"text":"cat"}'
    
    curl http://localhost:8080/analyze -d '{"text":"house"}'
