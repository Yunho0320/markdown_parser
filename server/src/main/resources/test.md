# Java Streams
This is about Java streams.

## 1. What is Java Streams
In Java, a Stream is a sequence of data that you can process in a declarative and functional style. It allows you to perform operations such as filtering, mapping, and reducing on a collection of data. Streams can be used with various data sources, including arrays, collections, and even I/O channels.
```java
public class HelloWorld {

    // Your program begins with a call to main().
    // Prints "Hello, World" to the terminal window.
    public static void main(String[] args)
    {
        System.out.println("Hello, World");
    }
}
```

## 2. This is sample heading 2
### 2.1 This is sample heading 3
This is sample text 2

```mermaid
sequenceDiagram
Alice ->> Bob: Hello Bob, how are you?
Bob-->>John: How about you John?
Bob--x Alice: I am good thanks!
Bob-x John: I am good thanks!
Note right of John: Bob thinks a long<br/>long time, so long<br/>that the text does<br/>not fit on a row.

        Bob-->Alice: Checking with John...
Alice->John: Yes... John, how are you?
```


```mermaid
sequenceDiagram
    participant User
    participant Browser
    participant VueRouter as Vue Router (index.js)
    participant MdDisplay as MdDisplay.vue
    participant DataProvider as data_provider.js
    participant Proxy as vue.config.js (proxy)
    participant Jetty as Jetty Server
    participant UploadHandler as UploadHandler
    participant Parser as MarkdownParser

    User->>Browser: enters http://localhost:8080/#/test
    Browser->>Browser: GET / → load index.html
    Browser->>Browser: run main.js → mount Vue app
    Browser->>VueRouter: match route /:filename (test)
    VueRouter->>MdDisplay: load component MdDisplay.vue
    MdDisplay->>DataProvider: fetch(`/api/test`)
    DataProvider->>Proxy: proxy /api/test to Jetty
    Proxy->>Jetty: HTTP GET /api/test
    Jetty->>UploadHandler: call doGet("/api/test")
    UploadHandler->>Parser: parse "test.md"
    Parser-->>UploadHandler: return List<MarkdownModel>
    UploadHandler-->>Jetty: send JSON
    Jetty-->>Browser: HTTP 200 + JSON
    Browser->>MdDisplay: markdownData = parsed JSON
    MdDisplay->>Browser: render blocks using <component :is="...">

```