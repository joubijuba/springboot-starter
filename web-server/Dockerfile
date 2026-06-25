FROM eclipse-temurin:21-jre 
WORKDIR /app
COPY web-server/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

# FROM : quelle image doit on pull ? ici : un Java Runtime environment
# WORKDIR : définir le nom / path du dossier de travail qui contient les JAR dans le conteneur créé
# COPY : on copie le JAR depuis notre target vers /app du conteneur et on le nomme app.jar
# ENTRYPOINT : la commande qu'on instructe à Docker d'executer pour démarrer le conteneur 