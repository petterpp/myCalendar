/* 
 * Read Schedule data from MongoDB.
 */
alert("123");
// Retrieve
var MongoClient = require('../lib/mongodb').MongoClient;

// Connect to the db
MongoClient.connect("mongodb://localhost:27017/Calender", function(err, db) {
  if(!err) {
    console.log("We are connected");
  }
});