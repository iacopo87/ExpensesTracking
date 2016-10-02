# Expenses Tracking
This is a sample Android application for tracking expenses.
The user can create an account and once logged in can execute CRUD operation on his expenses.

## Goals
I create this app to learn how to deal with a REST backend.

## Backend API

|URL			|Method		|Parameters								|Description|
| ------------- | ----------|-------------------------------------- | -----------------------|
|/register		| POST		|name, email, password					|User registration       |
|/login			| POST		|email, password						|User login              |
|/expenses		| GET		|										|Fetch all expenses      |
|/expenses		| POST		|description, date, amount, category	|Create a new expense    |
|/expenses/:id	| GET		|										|Fetch a single expense  |
|/expenses/:id	| PUT		|description, date, amount, category	|Update a single expense |
|/expenses/:id	| DELETE	|										|Delete single expense   |