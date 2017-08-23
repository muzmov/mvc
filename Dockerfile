FROM tomcat:8.0.20-jre8
EXPOSE 80
RUN rm -r /usr/local/tomcat/webapps/ROOT
RUN rm /usr/local/tomcat/conf/server.xml
COPY server.xml /usr/local/tomcat/conf/server.xml
#RUN mkdir /tmp/data
#COPY /src/main/resources/data/data.zip /tmp/data/data.zip
COPY /target/mvc.war /usr/local/tomcat/webapps/ROOT.war