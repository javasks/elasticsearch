# elasticsearch

Open source, search and analytics engine, written in java built on Apache Lucene. Scale to thousands of node. CRUD monitoring and other

Elastic search server will be running on port 9200 by default. Go to Elastic search website and follow the steps to download and setup in your repective environment. 

This is a sample project which will help in connecting local elastic search server. 
Please find below the rest End point:
GET: http://localhost:8080/elasticSearch/users/1 -- To get users from employee index
POST: http://localhost:8080/elasticSearch/users/2 --- To post a document in employee index
DELETE: http://localhost:8080/elasticSearch/users/2 --- To delete documents in employee index
PUT: http://localhost:8080/elasticSearch/users/2 --To update any specific document

To check the index:

localhost:9200/employee

The above employee index is been created by client code, which has been checked in.

======================================================================================

To create any index ->

PUT :    http://localhost:9200/schools

Response: 
{
    "acknowledged": true,
    "shards_acknowledged": true,
    "index": "schools"
}

======================================================================================

Create Mapping and Add data:

Elasticsearch will auto-create the mapping according to the data provided in request body, we can use its bulk functionality to add more than one JSON object in this index.

Note: There should be end line after the end of requestBody.

POST http://localhost:9200/schools/_bulk

Request Body
{   "index":{ "_index":"schools", "_type":"school", "_id":"1"  }}
{   "name":"MG School", "description":"STATE Affiliation", "street":"ARAYANAGR"}
{   "index":{ "_index":"schools", "_type":"school", "_id":"2"  }}
{   "name":"Saint Paul School", "description":"ICSE Afiliation", "street":"Vijay Chowk"}


Response
{
    "took": 26,
    "errors": false,
    "items": [
        {
            "index": {
                "_index": "schools",
                "_type": "school",
                "_id": "1",
                "_version": 2,
                "result": "updated",
                "_shards": {
                    "total": 2,
                    "successful": 1,
                    "failed": 0
                },
                "_seq_no": 1,
                "_primary_term": 1,
                "status": 200
            }
        },
        {
            "index": {
                "_index": "schools",
                "_type": "school",
                "_id": "2",
                "_version": 1,
                "result": "created",
                "_shards": {
                    "total": 2,
                    "successful": 1,
                    "failed": 0
                },
                "_seq_no": 0,
                "_primary_term": 1,
                "status": 201
            }
        }
    ]
}

======================================================================================

To list all index in server:

GET: localhost:9200/_cat/indices?v


health status index    uuid                   pri rep docs.count docs.deleted store.size pri.store.size
yellow open   test     WzZvEFWqSAaTmWGzVi8VAA   3   2          0            0       783b           783b
yellow open   schools  cYW3mxlaTQu6wk-cgbvCSA   5   1          2            0     10.3kb         10.3kb
yellow open   employee gva67GidTq-uPENuo_Kuhg   5   1          3            0     15.1kb         15.1kb


======================================================================================

Lets create another index with data using bulk api:

POST: http://localhost:9200/test/_bulk

Request Body:

{   "index":{ "_index":"test", "_type":"school", "_id":"1"  }}
{   "name":"APJ School", "description":"STATE Affiliation", "street":"ARAYANAGR"}
{   "index":{ "_index":"test", "_type":"school", "_id":"2"  }}
{   "name":" LFS School", "description":"ICSE Afiliation", "street":"Vijay Chowk"}


======================================================================================

Now we have to search the text "STATE" in multiple index (test and schools)

POST: http://localhost:9200/school*,test/_search

Request Body:
{
   "query":{
      "query_string":{
         "query":"STATE"
      }
   }
}

Response:
{
    "took": 24,
    "timed_out": false,
    "_shards": {
        "total": 8,
        "successful": 8,
        "skipped": 0,
        "failed": 0
    },
    "hits": {
        "total": 2,
        "max_score": 0.2876821,
        "hits": [
            {
                "_index": "test",
                "_type": "school",
                "_id": "1",
                "_score": 0.2876821,
                "_source": {
                    "name": "APJ School",
                    "description": "STATE Affiliation",
                    "street": "ARAYANAGR"
                }
            },
            {
                "_index": "schools",
                "_type": "school",
                "_id": "1",
                "_score": 0.2876821,
                "_source": {
                    "name": "MG School",
                    "description": "STATE Affiliation",
                    "street": "ARAYANAGR"
                }
            }
        ]
    }
}

======================================================================================

See all index:
GET: localhost:9200/_cat/indices?v

======================================================================================
Search using query_string in given index:
POST:  http://localhost:9200/employee/_search

{
  "query": {
    "query_string": {
      "query": "javasks"
    }
  }
}

======================================================================================
Search All documnets in given index:
GET: http://localhost:9200/employee/_search?pretty=true&q=*:*

======================================================================================
 Cluster health API:
 Get: localhost:9200/_cluster/health
 
======================================================================================
 
 Create Index without mapping: 
 PUT: localhost:9200/movies
 
 {
	"settings": {
		"index":{
			"number_of_shards": 5,
			"number_of_replicas": 2
			
		}
	}
}

======================================================================================

Search Using Match ALl:
POST: http://localhost:9200/employee/_search

{
  "query": {
    "match_all": {}
  }
}

======================================================================================

Search using match query
Post: http://localhost:9200/employee/_search

{
  "query": {
    "match": {
      "gender": "male"
    }
  }
}

======================================================================================

Search using match_phrase_prefix
POST: http://localhost:9200/employee/_search

{
  "query": {
    "match_phrase_prefix": {
      "gender": "male"
    }
  }
}

======================================================================================
Search using wildcard:
POST: http://localhost:9200/employee/_search

{
  "query": {
    "wildcard": {
      "gender": "*al*"
    }
  }
}

======================================================================================

Search with multi match query
POST: http://localhost:9200/employee/_search

{
  "query": {
    "multi_match": {
      "query": "javasks",
      "fields": [
        "gitRepo",
        "gender"
      ]
    }
  }
}

======================================================================================

Find All mappings:
GET: http://localhost:9200/_mapping
