# Az-Bitcoin-Api
It's a simple rest api in which I will consume CoinDesk Api and display some results.
    
    https://www.coindesk.com/api

**How to run**
* Api Test:
    * mvn test
* Api Run:
    * mvn clean install spring-boot:run

**How to use**
* Enter any currency code e.g AED, USD, PKR
* Api will display result with respect to entered currency:
    * BitCoin current rate.
    * BitCoin lowest rate in the last 30 days.
    * BitCoin highest rate in the last 30 days.
