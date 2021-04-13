# Money Transfer Api

A banking API allows you to transfer money between two accounts. 

#### Technologies
- Java
- Dropwizard
- JDBI
- H2

#### How to run
```sh
mvn package

java -jar target/banking_api-1.0.0-SNAPSHOT.jar server banking.yml
```
Application starts on localhost port 8080 An H2 in memory database initialized with some sample transfer and account data

- [http://localhost:8080/accounts]
- [http://localhost:8080/transfers]

#### Available Services
| Http Method | Path | Usage |
| ------ | ------ | ------ |
| GET | /accounts | get all accounts |
| POST | /accounts | create an account |
| GET | /transfers | get all transfers |
| POST | /transfers | create a transfer |

#### Http Status
```sh
200 OK - Everything worked as expected
204 No Content - Everything worked as expected and not additional content was sent back
404 Not Found - The requested resource does not exist
422 Unprocessable Entity - The request might have missing / incorrect parameters or failed certain conditions
```
## Account Resources
### GET /accounts
#### Request

```sh
GET /accounts
```
#### Response

```json
[
    {
        "isError": false,
        "referenceNumber": 1,
        "accountNumber": 123,
        "currencyCode": "TRY",
        "balance": 100.0
    },
    {
        "isError": false,
        "referenceNumber": 2,
        "accountNumber": 156,
        "currencyCode": "TRY",
        "balance": 23.45
    }
]
```
### POST /accounts
#### Request

```sh
POST /accounts
```
#### Body
```json
{
  "accountNumber": 185,
  "currencyCode":"USD",
  "balance":99.99
}
```
#### Response

```json
{
    "referenceNumber": 3,
    "isError": false
}
```
## Transfer Resources
### GET /transfers
#### Request

```sh
GET /transfers
```
#### Response

```json
[
    {
        "isError": false,
        "referenceNumber": 250,
        "senderAccountNumber": 156,
        "receiverAccountNumber": 123,
        "amount": 10.0,
        "currencyCode": "TRY"
    },
    {
        "isError": false,
        "referenceNumber": 251,
        "senderAccountNumber": 123,
        "receiverAccountNumber": 156,
        "amount": 10.0,
        "currencyCode": "TRY"
    }
]
```
### POST /transfers
#### Request

```sh
POST /transfers
```
#### Body
```json
{
  "senderAccountNumber":123,
  "receiverAccountNumber":156,
  "amount":23.45,
  "currencyCode":"TRY"
}
```
#### Response

```json
{
    "referenceNumber": 252,
    "isError": false
}
```
