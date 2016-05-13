package com.top.sdk.http.respone.parser;

public  interface ParserInterface<T> {
   public T parserJson(String json);
}
