Filteration:
This solution solving how a coding language can select/filter payload request (JSON/XML) base of standard logic that can provide by user/consumer. Any request coming to application can filter by this standard which compose of series of advance rules which are connected by logical operators AND '&&' - OR '|| and parentheses. Any payload which does not pass rules expression defined by user/consumer, will be dropped from application.
Developers always have problem to implement complicated filter to select desire payload (JSON/XML). With this standardization and its implementation which is provided as Java library, they can easily match given rules with payload.

Solution:
Each rules in the below structure can look at the below payload(search all 'People' array) to match with it.
•	name: is name of the rule which is used by rulesExpression.
•	path: is payload path which your desired field is located
•	key: The field which we want to check its value for rule/filter
•	value: desired value
•	condition: relation between key and value. Can be: 'eq', 'Contains','lt' (Less Than), 'gt' (greater than), 'empty', 'null', 'exist', 'true', 'false', 'regex', 'in'.
In the below example rules, A and C return True but rule B return False. So, in ruleExpression we have True && (False || True) which is totally return True. Therefore Payload pass filter and can be consumed by application.

Rules Structure:
{
"rules": [
{
"name": "A",
"path": "people.[*].Address.[?]",
"key": "city",
"value": "Chicago",
"condition": "eq"
},
{
"name": "B",
"path": "people.[*].Address.[?]",
"key": "state",
"value": "TX",
"condition": "eq"
},
{
"name": "C",
"path": "people.[*].Address.[?]",
"key": "state",
"value": "VA",
"condition": "eq"
}
],
"rulesExpression": "A && ( B || C )"
}

Payload:
{
"people": [
{
"name": "Ali",
"address": {
"street": "1211 W Roosevelt Rd",
"city": "Chicago",
"state": "IL",
"zipcode": "60608",
"counrty": "US"
}
},
{
"name": "David",
"address": {
"street": "1211 Lee Highway",
"city": "Fairfax",
"state": "VA",
"zipcode": "50608",
"counrty": "US"
}
},
{
"name": "Jack",
"address": {
"street": "1011 E Main Dr",
"city": "Newyork",
"state": "NY",
"zipcode": "60208",
"counrty": "US"
}
}
]
}

