
all:
	mvn clean test package

skipTests:
	mvn clean package -Dmaven.test.skip=true

docker: all
	docker build -t pkleindienst/jscheme:cont-passing .

docker-skipTests: skipTests
	docker build -t pkleindienst/jscheme:cont-passing .