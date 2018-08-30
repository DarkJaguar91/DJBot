FROM openjdk:11
ADD ./DJBot-0.0.1.tar /code
WORKDIR /code
RUN tar xf DJBot-0.0.1.tar
CMD ["sh", "DJBot-0.0.1/bin/DJBot", "$TOKEN$"]