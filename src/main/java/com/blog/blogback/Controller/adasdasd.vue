Content-Type : 
{
    "data":9,
    "status":200,
    "statusText":"OK",

"headers":{
    "access-control-allow-credentials":"true",
    "access-control-allow-origin":"http://localhost:3000",
    "connection":"close",
    "content-encoding":"gzip",
    "content-type":"application/json",
    "date":"Thu, 08 Feb 2024 09:18:04 GMT","transfer-encoding":"chunked",
    "vary":"Origin, 
    Access-Control-Request-Method, 
    Access-Control-Request-Headers, 
    Accept-Encoding","x-powered-by":"Express"
},

"config":{
    "transitional":{
        "silentJSONParsing":true,
        "forcedJSONParsing":true,
        "clarifyTimeoutError":false
        },
        "adapter":["xhr","http"],
        "transformRequest":[null],
        "transformResponse":[null],
        "timeout":0,
        "xsrfCookieName":"XSRF-TOKEN",
        "xsrfHeaderName":"X-XSRF-TOKEN",
        "maxContentLength":-1,
        "maxBodyLength":-1,
        "env":{},
        "headers":{
            "Accept":"application/json, 
            text/plain, */*",
            "Content-Type":"application/json"
        },
        "method":"post",
        "url":"/api/user/login",
        "data":"{
            \"userId\":\"kim\",
            \"userPw\":\"1234\"
        }"
    },
    "request":{}
}