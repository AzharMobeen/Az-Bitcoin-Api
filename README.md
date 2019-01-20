# Az-Bitcoin-Api
It's a simple rest api in which I will consume CoinDesk Api and provide some features. 
    
    https://www.coindesk.com/api

**How to run**
* mvn test (for test)
* mvn clean install spring-boot:run OR java -jar target/Az-BitCoin-Api.jar

**How to use**
* Enter any currency code e.g AED, USD, PKR
* Api will display result with respect to entered currency:
    * BitCoin current rate.
    * BitCoin lowest rate in the last 30 days.
    * BitCoin highest rate in the last 30 days.
