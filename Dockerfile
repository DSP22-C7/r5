# Build with:
# docker build . --build-arg r5version=$(gradle -q printVersion | head -n1)
# or
# docker build . --build-arg r5version=$(cat build/version.txt)
# We could instead run the Gradle build and/or fetch version information 
# using run actions within the Dockerfile
FROM openjdk:11
ENV JVM_HEAP_GB=2
WORKDIR /r5
COPY . .

EXPOSE 7070
ENTRYPOINT ["java", "-Xmx2g", "-cp", "build/libs/r5-all.jar"]
CMD ["com.conveyal.analysis.BackendMain"]
