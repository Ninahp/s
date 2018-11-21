## Food Odering Application

Should be able to:

Receive HTTP requests from the client
Send these requests to the broker
Process the response from the broker

## Running

Run fgRun on sbt shell to fire up
use ctrl + C to stop

## URls

Fetch available food
http://localhost:8080/food/fetch GET

Place Order
http://localhost:8080/food/order POST
request body {
	"name":"Ugali",
	"quantity":2
}





